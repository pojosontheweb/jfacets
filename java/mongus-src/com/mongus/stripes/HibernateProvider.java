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

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.mongus.servlet.filter.HibernateFilter;
import com.mongus.util.Log;

/**
 * <p>HibernateProvider is used to retrieve Hibernate Configurations,
 * SessionFactories, and Sessions by specifying the class they will
 * be used with. HibernateFilter is derived from HibernateProvider.
 * Subclass HibernateProvider if you intend to provide your own lookup
 * class. There may only be one HibernateProvider per ClassLoader.</p>
 * 
 * <p>To retrieve a Hibernate Session that is configured to work with
 * MyObject you would use the following code:
 * <pre>Session session = HibernateProvider.getInstance().getSession(MyObject.class);</pre>
 * </p>
 * 
 * @author Aaron Porter
 *
 */
public abstract class HibernateProvider
{
	private static HibernateProvider instance;
	
	private static final Log log = Log.getInstance(HibernateProvider.class);
	
	/**
	 * Hopefully with a Stripes update this will loop through all Hiberate configured
	 * classes and adds them to the TypeConverterFactory and FormatterFactory.
	 * 
	 * If you override init() make sure to call super.init().
	 */
	protected void init()
	{
		// This will have to wait until we can dynamically configure the TypeConverterFactory and FormatterFactory
		/*
		TypeConverterFactory typeConverterFactory = StripesFilter.getConfiguration().getTypeConverterFactory();
		FormatterFactory formatterFactory = StripesFilter.getConfiguration().getFormatterFactory();
		
		for (Configuration configuration : getConfigurations())
		{
			Iterator iterator = configuration.getClassMappings();
			
			while (iterator.hasNext())
			{
				Class clazz = ((PersistentClass) iterator.next()).getMappedClass();
				
				log.debug("Adding HibernateTypeConverter and HibernateFormatter for ", clazz.getName());
				
				typeConverterFactory.addTypeConverter(clazz, HibernateTypeConverter.class);
				formatterFactory.addFormatter(clazz, HibernateFormatter.class);
			}
		}*/
	}

	/**
	 * Gets the SessionFactory that is associated with the class specified.
	 */
	abstract public SessionFactory getSessionFactory(Class forClass);

	/**
	 * Gets the Configuration used to build the SessionFactory associated with the class specified. 
	 */
	abstract public Configuration getConfiguration(Class forClass);
	
	/**
	 * Gets a Session from the SessionFactory associated with the class specified.
	 */
	abstract public Session getSession(Class forClass);

	/**
	 * Sets the instance that will be returned by HibernateProvider.getInstance(). There
	 * should only be one one HibernateProvider configured for your application. init()
	 * is called by setInstance() so the specified HibernateProvider should be ready to
	 * go or init should be overridden.
	 * 
	 * setInstance() must be called after Stripes has been configured because init() attempts
	 * to add Hibernate configured classes to the configured TypeConverterFactory and
	 * FormatterFactory.
	 * 
	 * @param hibernateProvider
	 */
	public static void setInstance(HibernateProvider hibernateProvider)
	{
		if ((hibernateProvider == null) && (instance == null))
			log.warn("Setting HibernateProvider to null");
		if ((hibernateProvider != null) && (instance == hibernateProvider))
			log.warn("The existing instance of HibernateProvider is already set to this exact object.");
		else if (hibernateProvider == null)
			log.warn("Setting HibernateProvider to null when there is already an instance.");
		else if ((instance != null) && (instance != hibernateProvider))
			log.warn("The existing instance of HibernateProvider (",
					instance.getClass().getName(),
					") is being replaced with a new instance (",
					hibernateProvider.getClass().getName(),
					").");
		
		instance = hibernateProvider;

		if (instance != null)
			instance.init();
	}
	
	/**
	 * Get the configured instance of HibernateProvider. There should only be one one
	 * HibernateProvider configured for your application.
	 * @return the configured instance of HibernateProvider
	 */
	public static HibernateProvider getInstance()
	{
		if (instance != null)
			return instance;

		// Default to HibernateFilter if setInstance() was never called
		return HibernateFilter.getCurrentInstance();
	}
	
	/**
	 * @return A Set containing all SessionFactories that this HibernateProvider is configured to use.
	 */
	abstract public Set<SessionFactory> getSessionFactories();
	
	/**
	 * @return A Set containing all Configurations that this HibernateProvider is configured to use.
	 */
	abstract public Set<Configuration> getConfigurations();
	
	/**
	 * If there is only one SessionFactory this method will return it, otherwise it will throw an IndexOutOfBoundsException.
	 */
	public SessionFactory getSessionFactory()
	{
		Set<SessionFactory> sessionFactories = getSessionFactories();
		
		if (sessionFactories.size() != 1)
			throw new IndexOutOfBoundsException("There must be exactly one SessionFactory for this method to work.");
		
		return sessionFactories.iterator().next();
	}
	
	/**
	 * If there is only one Configuration this method will return it, otherwise it will throw an IndexOutOfBoundsException.
	 */
	public Configuration getConfiguration()
	{
		Set<Configuration> configurations = getConfigurations();
		
		if (configurations.size() != 1)
			throw new IndexOutOfBoundsException("There must be exactly one Configuration for this method to work.");
		
		return configurations.iterator().next();
	}
	
	public Session getSession() { throw new UnsupportedOperationException("Not implemented"); }
	
	public void commit() { commit(getSession()); }
	public void commit(Class forClass) { commit(getSession(forClass)); }
	public void commit(Session session) { throw new UnsupportedOperationException("Not implemented"); }
	
	public void rollback() { rollback(getSession()); }
	public void rollback(Class forClass) { rollback(getSession(forClass)); }
	public void rollback(Session session) { throw new UnsupportedOperationException("Not implemented"); }
}