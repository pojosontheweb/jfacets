package net.sourceforge.jfacets.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import net.sourceforge.jfacets.FacetDescriptor;
import net.sourceforge.jfacets.IFacetDescriptorManager;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * The FacetDescriptorManager : handles loading of descriptors from an 
 * XML resource, and provides access to them.
 *
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 * 
 */
public class FacetDescriptorManager implements IFacetDescriptorManager {

	private static final Logger logger = Logger.getLogger(FacetDescriptorManager.class);
	
	/** an array of all loaded descriptors */
	private FacetDescriptor[] descriptors = null;

	/** location of the facet descriptors file */
	private String facetsFilePath = null;

	/**
	 * Creates the manager and loads descriptors.
	 * @param facetsFilePath The path of the facets XML descriptor to be used (CLASSPATH resource)
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws FactoryConfigurationError
	 */
	public FacetDescriptorManager(String facetsFilePath) 
		throws IOException, ParserConfigurationException, SAXException, FactoryConfigurationError {
		
		this.facetsFilePath = facetsFilePath;
		if (!this.facetsFilePath.startsWith("/"))
			this.facetsFilePath = "/" + this.facetsFilePath;
		int nbDesc = loadDescriptors();
		if (logger.isInfoEnabled()) logger.info("FacetDescriptorManager created, " + nbDesc + " descriptor(s) loaded");
	}
	
	protected int loadDescriptors() throws IOException, ParserConfigurationException, SAXException, FactoryConfigurationError {
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : getting facets XML file from " + facetsFilePath);
		InputStream is = getClass().getResourceAsStream(facetsFilePath);
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : parsing...");
		FacetsSaxParser parser = new FacetsSaxParser(is);
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : ... parsed");
		descriptors = parser.getDescriptors();
		if (logger.isDebugEnabled()) logger.debug("loadDescriptors() : " + descriptors.length + " descriptors loaded OK");
		return descriptors.length;
	}
		
	/** returns an array of all loaded descriptors */
	public FacetDescriptor[] getDescriptors() {
		return descriptors;
	}

	/** 
	 * returns the descriptor for passed parameters, null if not found.
	 * strict match : does not handle inheritance
	 */
	public FacetDescriptor getDescriptor(String name, String profileId, Class targetObjectType) {
		if (logger.isDebugEnabled()) logger.debug("getDescriptor() : trying to get descriptor for name='" + name + "' profileId='" + profileId + "' " +
				"objType='" + targetObjectType.getName() + "' ...");
		FacetDescriptor d = null;
		for (int i = 0; ((i < descriptors.length) && (d==null)); i++) {
			if ( (name.equals(descriptors[i].getName())) &&
					(profileId.equals(descriptors[i].getProfileId())) &&
					(targetObjectType.getName().equals(descriptors[i].getTargetObjectType().getName())) ) {
				if (logger.isDebugEnabled()) logger.debug("getDescriptor() : ... descriptor found OK");
				d = descriptors[i];
			}
		}
		return d;
	}
	
}
