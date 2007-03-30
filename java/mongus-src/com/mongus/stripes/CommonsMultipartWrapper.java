/* Copyright 2005-2006 Tim Fennell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mongus.stripes;


import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.controller.FileUploadLimitExceededException;
import net.sourceforge.stripes.controller.multipart.MultipartWrapper;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * An implementation of MultipartWrapper that uses Jakarta Commons FileUpload (from apache)
 * to parse the request parts. This implementation requires that both commons-fileupload and
 * commons-io be present in the classpath.  While this implementation does introduce additional
 * dependencies, it's licensing (ASL 2.0) is significantly less restrictive than the licesing
 * for COS - the other alternative provided by Stripes.
 *
 * @author Tim Fennell
 * @since Stripes 1.4
 */
public class CommonsMultipartWrapper implements MultipartWrapper {
    private Map<String,FileItem> files = new HashMap<String,FileItem>();
    private Map<String,String[]> parameters = new HashMap<String, String[]>();
    
    public static final String SESSION_ATTRIBUTE = "List<UploadProgress>";

    /**
     * Pseudo-constructor that allows the class to perform any initialization necessary.
     *
     * @param request     an HttpServletRequest that has a content-type of multipart.
     * @param tempDir a File representing the temporary directory that can be used to store
     *        file parts as they are uploaded if this is desirable
     * @param maxPostSize the size in bytes beyond which the request should not be read, and a
     *                    FileUploadLimitExceeded exception should be thrown
     * @throws IOException if a problem occurrs processing the request of storing temporary
     *                    files
     * @throws FileUploadLimitExceededException if the POST content is longer than the
     *                     maxPostSize supplied.
     */
    @SuppressWarnings("unchecked")
	public void build(HttpServletRequest request, File tempDir, long maxPostSize)
            throws IOException, FileUploadLimitExceededException {
    	UploadProgress uploadProgress = null;
    	List<UploadProgress> uploads = null;
    	
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(tempDir);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(maxPostSize);
            
            uploadProgress = new UploadProgress();
            upload.setProgressListener(uploadProgress);
            uploads = (List<UploadProgress>) request.getSession().getAttribute(SESSION_ATTRIBUTE);
            if (uploads == null)
            	request.getSession().setAttribute(SESSION_ATTRIBUTE, uploads = new Vector<UploadProgress>());
            synchronized(uploads) {
            	uploads.add(uploadProgress);
            }
            
            List<FileItem> items = upload.parseRequest(request);
            Map<String,List<String>> params = new HashMap<String, List<String>>();

            for (FileItem item : items) {
                // If it's a form field, add the string value to the list
                if (item.isFormField()) {
                    List<String> values = params.get(item.getFieldName());
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(item.getFieldName(), values);
                    }
                    values.add(item.getString());
                }
                // Else store the file param
                else {
                    files.put(item.getFieldName(), item);
                    uploadProgress.getFilenames().add(item.getName());
                }
            }

            // Now convert them down into the usual map of String->String[]
            for (Map.Entry<String,List<String>> entry : params.entrySet()) {
                List<String> values = entry.getValue();
                this.parameters.put(entry.getKey(), values.toArray(new String[values.size()]));
            }
        }
        catch (FileUploadBase.SizeLimitExceededException slee) {
        	uploadProgress.setError("File upload size limit exceeded.");
            throw new FileUploadLimitExceededException(maxPostSize, slee.getActualSize());
        }
        catch (FileUploadException fue) {
        	String error = "Could not parse and cache file upload data.";
        	uploadProgress.setError(error);
            IOException ioe = new IOException(error);
            ioe.initCause(fue);
            throw ioe;
        }
        finally
        {
        	uploadProgress.setComplete(true);
        }
    }

    /**
     * Fetches the names of all non-file parameters in the request. Directly analogous to the
     * method of the same name in HttpServletRequest when the request is non-multipart.
     *
     * @return an Enumeration of all non-file parameter names in the rqequest
     */
    public Enumeration<String> getParameterNames() {
        return new IteratorEnumeration(this.parameters.keySet().iterator());
    }

    /**
     * Fetches all values of a specifical parameter in the request. To simulate the HTTP request
     * style, the array should be null for non-present parameters, and values in the array should
     * never be null - the empty String should be used when there is value.
     *
     * @param name the name of the request parameter
     * @return an array of non-null parameters or null
     */
    public String[] getParameterValues(String name) {
        return this.parameters.get(name);
    }

    /**
     * Fetches the names of all file parameters in the request. Note that these are not the file
     * names, but the names given to the form fields in which the files are specified.
     *
     * @return the names of all file parameters in the request.
     */
    public Enumeration<String> getFileParameterNames() {
        return new IteratorEnumeration(this.files.keySet().iterator());
    }

    /**
     * Responsible for contructing a FileBean object for the named file parameter. If there is no
     * file parameter with the specified name this method should return null.
     *
     * @param name the name of the file parameter
     * @return a FileBean object wrapping the uploaded file
     */
    public FileBean getFileParameterValue(String name) {
        final FileItem item = this.files.get(name);
        if (item == null) {
            return null;
        }
        else {
            // Use an anonymous inner subclass of FileBean that overrides all the
            // methods that rely on having a File present, to use the FileItem
            // created by commons upload instead.
            return new FileBean(null, item.getContentType(), item.getName()) {
                @Override public long getSize() { return item.getSize(); }

                @Override public InputStream getInputStream() throws IOException {
                    return item.getInputStream();
                }

                @Override public void save(File toFile) throws IOException {
                    try {
                        item.write(toFile);
                        delete();
                    }
                    catch (Exception e) {
                        if (e instanceof IOException) throw (IOException) e;
                        else {
                            IOException ioe = new IOException("Problem saving uploaded file.");
                            ioe.initCause(e);
                            throw ioe;
                        }
                    }
                }

                @Override
                public void delete() throws IOException { item.delete(); }
            };
        }
    }

    /** Little helper class to create an enumeration as per the interface. */
    private static class IteratorEnumeration implements Enumeration<String> {
        Iterator<String> iterator;

        /** Constructs an enumeration that consumes from the underlying iterator. */
        IteratorEnumeration(Iterator<String> iterator) { this.iterator = iterator; }

        /** Returns true if more elements can be consumed, false otherwise. */
        public boolean hasMoreElements() { return this.iterator.hasNext(); }

        /** Gets the next element out of the iterator. */
        public String nextElement() { return this.iterator.next(); }
    }
}