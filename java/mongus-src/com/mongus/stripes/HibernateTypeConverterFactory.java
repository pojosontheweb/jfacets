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
package com.mongus.stripes;

import java.util.*;

import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;

/**
 * 
 * <p>HibernateTypeConverterFactory simply extends DefaultTypeConverterFactory
 * to use HibernateTypeConverter when DefaultTypeConverter.getTypeConverter()
 * returns null and the target destination managed by Hibernate.<p/>
 * 
 * <p>Stripes may be configured to use HibernateTypeConverterFactory by adding
 * the following lines to the Stripes filter in web.xml:</p>
 * 
 *     <pre>

    &lt;init-param&gt;
        &lt;param-name&gt;TypeConverterFactory.Class&lt;/param-name&gt;
        &lt;param-value&gt;com.mongus.stripes.HibernateTypeConverterFactory&lt;/param-value&gt;
    &lt;/init-param&gt;

</pre>
 * 
 * @author Aaron Porter
 *
 */
public class HibernateTypeConverterFactory extends DefaultTypeConverterFactory
{
	private final Log log = Log.getInstance(HibernateTypeConverterFactory.class);
	
    @SuppressWarnings("unchecked")
	public TypeConverter getTypeConverter(Class forType, Locale locale) throws Exception
    {
    	TypeConverter converter = super.getTypeConverter(forType, locale);

    	try
    	{
    		if ((converter == null) && (HibernateProvider.getInstance().getConfiguration(forType).getClassMapping(forType.getName()) != null))
    		{
    			// Let DefaultTypeConverterFactory handle it next time
    			add(forType, HibernateTypeConverter.class);
    			converter = super.getTypeConverter(forType, locale);
    		}
    	}
    	catch (Exception e)
    	{
    		log.debug(e);
    	}

    	return converter;
    }
}
