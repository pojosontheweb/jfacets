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

import java.io.Serializable;
import java.util.*;

import org.hibernate.Session;

import com.mongus.util.Log;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.controller.StripesFilter;

/**
 * <p>HibernateTypeConverter provides an easy way for Stripes to hydrate
 * Hibernate managed objects. When parameter binding is performed
 * HibernateTypeConverter attempts to convert the parameter to the same
 * type as the primary key for the entity. It then calls Session.get()
 * to retrieve the entity through Hibernate.</p>
 * 
 * <p>In order to use HibernateTypeConverter web.xml must be modified to
 * use {@link HibernateTypeConverterFactory}.</p>
 * 
 * @author Aaron Porter
 *
 */
public class HibernateTypeConverter implements TypeConverter<Object>
{
	private static Log log = Log.getInstance(HibernateTypeConverter.class);
	
	private Locale locale;
	
	@SuppressWarnings("unchecked")
	public Object convert(String id, Class<? extends Object> targetType, Collection<ValidationError> errors)
	{
		Class pkType = PrimaryKeyUtil.getPrimaryKeyType(targetType);
		
		if (pkType == null)
		{
			log.warn("Could not determine primary key for ", targetType.getName());
			return null;
		}
		
		// Might as well make use of the Stripes type converters!
		Object o;
		try
		{
			o = StripesFilter.getConfiguration().getTypeConverterFactory().getTypeConverter(pkType, locale).convert(id, pkType, errors);
		}
		catch (Exception e)
		{
			log.error(e);
			return null;
		}
		
		if (o == null)
		{
			log.warn("TypeConverter returned null for ", targetType.getName());
			return null;
		}
		
		log.trace("Primary key converted to ", o.getClass().getName());
		
		if (!(o instanceof Serializable))
		{
			log.warn("Primary key for ", targetType.getName(), " does not implement Serializable");
			return null;
		}
		
		Serializable pkValue = (Serializable) o;
		
		if (pkValue == null)
		{
			log.warn("Could not convert ", id, " into a primary key for ", targetType.getName());
			return null;
		}

		Session session = HibernateProvider.getInstance().getSession(targetType);

		try
		{
			Object value = session.get(targetType, pkValue);

			if (value != null)
				return value;
		}
		catch (Exception e)
		{
			// ignored
		}

		return null;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

}
