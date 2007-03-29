/* Copyright (C) 2005-2006 Aaron Porter
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
 * 
 */

package com.mongus.beans.validation;

import java.lang.reflect.Method;
import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class contains various methods copied from other
 * <code>com.mongus</code> classes to get rid of dependencies. It extends
 * <code>SecurityManager</code> to allow us to call <code>getClassContext()</code>
 * 
 * @author Aaron Porter
 */
public class Util extends SecurityManager
{
    private final static Util singleton = new Util();
    private Util() { }
    
    /**
     * Returns an instance of the <code>Class</code> which
     * called the caller of <code>getCallingClass()</code>.
     *
     * @return caller's caller's <code>Class</code>
     */
    static public Class getCallingClass()
    {
        return singleton.getClassContext()[2];
    }
    
    /**
     * Attempts to return the <code>Method</code> that called the
     * method that called <code>getCallingMethod()</code>.
     *  
     * @return caller's caller's <code>Method</code>
     */
    static public Method getCallingMethod()
    {
        Method[] methods = getPossibleCallingMethods();
        
        if ((methods != null) && (methods.length == 1))
            return methods[0];
        
        throw new RuntimeException("Unable to determine calling method! " + methods.length);
    }
    
    /**
     * Retrieves the methods that match the name of the
     * caller's calling method.
     * 
     * @return possible matches for caller's caller's method
     */
    static private Method[] getPossibleCallingMethods()
    {
        String methodName = Thread.currentThread().getStackTrace()[5].getMethodName();
        
        Class caller = singleton.getClassContext()[3];
        
        Method[] methods = caller.getDeclaredMethods();
        
        Vector<Method> vector = new Vector<Method>();
        
        for (Method method : methods)
            if (method.getName().equals(methodName))
                vector.add(method);
                
        return vector.toArray(new Method[0]);
    }

    /**
     * Attempts to translate a bean's getter or setter
     * method name into a more human friendly form.
     * 
     * @param method getter or setter method
     * @return a more readable property name
     */
    public static String getDescriptiveName(Method method)
    {
        String methodName = method.getName();
        
        if (!methodName.startsWith("get") && !methodName.startsWith("set"))
            throw new IllegalArgumentException("Method must be a getter or a setter.");

        return getDescriptiveName(methodName.substring(3));
    }
    
    /**
     * Attempts to translate a bean's field's name into
     * a more human friendly form.
     * 
     * @param field field to translate
     * @return a more readable property name
     */
    public static String getDescriptiveName(String field)
    {
        StringBuffer name = new StringBuffer();
        
        name.append(field.substring(0, 1).toUpperCase());
        
        for (int i = 1; i < field.length(); i++)
        {
            char c = field.charAt(i);
            
            if (Character.isUpperCase(c) && !(Character.isUpperCase(field.charAt(i - 1))))
                name.append(' ');
                
            name.append(c);
        }
        
        return name.toString();
    }    
    
    final static private SimpleDateFormat[] dateFormats = {
        new SimpleDateFormat("M/d/yy"),
        new SimpleDateFormat("MM/d/yy"),
        new SimpleDateFormat("M/dd/yy"),
        new SimpleDateFormat("MM/dd/yy"),
        
        new SimpleDateFormat("M/d/yyyy"),
        new SimpleDateFormat("MM/d/yyyy"),
        new SimpleDateFormat("M/dd/yyyy"),
        new SimpleDateFormat("MM/dd/yyyy"),
        
        new SimpleDateFormat("M-d-yy"),
        new SimpleDateFormat("MM-d-yy"),
        new SimpleDateFormat("M-dd-yy"),
        new SimpleDateFormat("MM-dd-yy"),
        
        new SimpleDateFormat("M-d-yyyy"),
        new SimpleDateFormat("MM-d-yyyy"),
        new SimpleDateFormat("M-dd-yyyy"),
        new SimpleDateFormat("MM-dd-yyyy"),
        
        new SimpleDateFormat("d MMM yy"),
        new SimpleDateFormat("dd MMM yy"),
        new SimpleDateFormat("d MMM yyyy"),
        new SimpleDateFormat("dd MMM yyyy"),
        
        new SimpleDateFormat("d MMM, yy"),
        new SimpleDateFormat("dd MMM, yy"),
        new SimpleDateFormat("d MMM, yyyy"),
        new SimpleDateFormat("dd MMM, yyyy"),
        
        new SimpleDateFormat("yyyy-M-d"),
        new SimpleDateFormat("yyyy-MM-d"),
        new SimpleDateFormat("yyyy-M-dd"),
        new SimpleDateFormat("yyyy-MM-dd"),

        new SimpleDateFormat("yyyy/M/d"),
        new SimpleDateFormat("yyyy/MM/d"),
        new SimpleDateFormat("yyyy/M/dd"),
        new SimpleDateFormat("yyyy/MM/dd") };

    /**
     * Attempts to parse a date <code>String</code> from
     * many common date formats.
     * 
     * @param string the date <code>String</code> to parse
     * @return the parsed <code>Date</code>
     * @throws ParseException if none of the standard formats matched
     */
    public static java.util.Date parseDate(String string) throws ParseException
    {
        if (string == null)
            return null;
        
        if (string.equalsIgnoreCase("now"))
            return new java.util.Date();

        for (SimpleDateFormat format : dateFormats)
        {
            try
            {
                String pattern = format.toPattern();
                pattern = pattern.replaceAll("M{3,}", "\\\\w+");
                pattern = pattern.replaceAll("[dMy]", "\\\\d");
                if (string.matches(pattern))
                    return format.parse(string);
            }
            catch (ParseException e) { }
        }
    
        throw new ParseException("Couldn't parse date (" + string + ")", 0);
    }
}