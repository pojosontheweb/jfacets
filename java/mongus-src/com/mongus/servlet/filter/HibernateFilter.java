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
package com.mongus.servlet.filter;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.*;

import javax.servlet.*;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;

import com.mongus.servlet.util.ClassLocator;
import com.mongus.stripes.HibernateProvider;
import com.mongus.util.Log;

/**
 * <p>HibernateFilter wraps HTTP requests to provide access to a Hibernate Session.
 * HibernateFilter.getSession() returns a thread specific instance of Session
 * associated with the class specified. If there is not already a Session, one is
 * opened and a transaction is started. HibernateFilter.commit() and
 * HibernateFilter.rollback() may be called at any time during the request to perform
 * an immediate commit or rollback and begin another transaction. Please note that
 * <b>you must call HibernateFilter.commit() in order to save any changes</b>.
 * HibernateFilter will not commit transactions by itself. This is a security measure
 * to help prevent unauthorized changes to your database.</p>
 * 
 * <p>When this filter is initialized it loads Hibernate properties from web.xml.
 * Then it scans the classes available through the ServletContext looking for classes
 * annotated with {@literal @Entity} and lets Hibernate know about them by calling
 * AnnotationConfiguration.addAnnotatedClasses(). Using this technique makes it
 * possible to use Hibernate Annotations without creating any mapping or property files.<p>
 * 
 * <p>web.xml must be modified in order to use HibernateFilter. The following example
 * sets up the Hibernate filter to use a PostgreSQL DB, scan for classes annotated 
 * with {@literal @Entity} in com.mypackage.model and com.yourpackage.model packages,
 * and automatically create or update the schema based on the matching classes.</p>
 * 
 * <pre>
     
     &lt;filter&gt;
            &lt;filter-name&gt;HibernateFilter&lt;/filter-name&gt;
            &lt;filter-class&gt;com.mongus.servlet.filter.HibernateFilter&lt;/filter-class&gt;
            &lt;init-param&gt;
                &lt;param-name&gt;packages&lt;/param-name&gt;
                &lt;param-value&gt;
                    com.mypackage.model,
                    com.yourpackage.model
                &lt;/param-value&gt;
            &lt;/init-param&gt;
            &lt;init-param&gt;
                &lt;param-name&gt;properties&lt;/param-name&gt;
                &lt;param-value&gt;
                    hibernate.hbm2ddl.auto update

                    hibernate.dialect org.hibernate.dialect.PostgreSQLDialect
                    hibernate.connection.driver_class org.postgresql.Driver
                    hibernate.connection.datasource java:/comp/env/jdbc/mydb
                &lt;/param-value&gt;
            &lt;/init-param&gt;
            &lt;init-param&gt;
                &lt;param-name&gt;hibernateInterceptorClass&lt;/param-name&gt;
                &lt;param-value&gt;
                   com.xyz.app.util.MyHibernateInterceptor
                &lt;/param-value&gt;
            &lt;/init-param&gt;
    &lt;/filter&gt;
    
 * </pre>
 * 
 * <p>Make sure the HibernateFilter appears int the web.xml file before any other
 * filters which will use it such as the StripesFilter.</p>
 * 
 * <p>Although the filter's packages parameter is optional its use is highly encouraged to
 * decrease load time and prevent accidental inclusion of classes from other packages.</p>
 * 
 * <p>Note: The property line "hibernate.hbm2ddl.auto update" instructs hibernate to
 * automatically create or modify the database schema. Use with caution.</p> 
 *
 * <p>You can register an hibernate interceptor by specifying its class in web.xml for  
 * the optional init-param named <code>hibernateInterceptorClass</code> (must have a 
 * default constructor)</p>
 * 
 * @author Aaron Porter
 *
 */
public class HibernateFilter extends HibernateProvider implements Filter
{
	private static final Log log = Log.getInstance(HibernateFilter.class);
	protected FilterConfig config = null;
	
	private final Map<Class, SessionFactory> sessionFactories = new HashMap<Class, SessionFactory>(); 
	private final Map<Class, Configuration> configurations = new HashMap<Class, Configuration>();
	
	private static final ThreadLocal<Map<SessionFactory, Session>> sessions = new ThreadLocal<Map<SessionFactory, Session>>();
	
	private static final ThreadLocal<HibernateFilter> currentHibernateFilter = new ThreadLocal<HibernateFilter>();
	
	/**
	 * <p>Initializes Hibernate with properties from web.xml and classes annotated
	 *  with {@literal @Entity}.</p>  
	 */
	public void init(final FilterConfig config) throws ServletException
	{
		this.config = config;
		
		AnnotationConfiguration configuration = new AnnotationConfiguration();
		
		loadHibernateProperties(configuration);
		
		loadHibernateInterceptors(configuration);
		
		Set<Class<Object>> entities = locateEntities(configuration);
		
		beforeBuildSessionFactory(configuration);

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		
		for (Class entity : entities)
		{
			configurations.put(entity, configuration);
			sessionFactories.put(entity, sessionFactory);
		}
	}
	
	/**
	 * <p>Called immediately before configuration.buildSessionFactory().
	 * You can extend HibernateFilter and override this method in case 
	 * you want fine-grained control over the configuration,
	 * before SessionFactories are created.</p>
	 * 
	 * @param configuration
	 */
	protected void beforeBuildSessionFactory(Configuration configuration)
	{
	}

	// Hibernate Interceptor code provided by Remi Vankeisbelck
	// Thanks Remi! :)
	private void loadHibernateInterceptors(Configuration configuration) throws ServletException
	{
		// do we register an interceptor?
		String hibernateInterceptorClass = config.getInitParameter("hibernateInterceptorClass");
		
		if (hibernateInterceptorClass == null)
			return;

		// yep, we try...
		log.debug("Trying to register hibernate interceptor : ", hibernateInterceptorClass);

		Class clazz;
		
		try
		{
			clazz = Class.forName(hibernateInterceptorClass);
			Interceptor interceptor = (Interceptor)clazz.newInstance();
			configuration.setInterceptor(interceptor);
			log.debug("Hibernate interceptor '", interceptor, "' created and registered OK");
		}
		catch (ClassNotFoundException e) { throw new ServletException("Unable to load hibernate interceptor class", e); }
		catch (InstantiationException e) { throw new ServletException("Unable to create hibernate interceptor : default constructor throws error", e); }
		catch (IllegalAccessException e) { throw new ServletException("Unable to create hibernate interceptor : default constructor is not accessible", e); }
		catch(ClassCastException e) { throw new ServletException(hibernateInterceptorClass + " does not implement Interceptor !", e); }
	}

	@SuppressWarnings("unchecked")
	private Set<Class<Object>> locateEntities(AnnotationConfiguration configuration)
	{
		ClassLocator<Object> locator = new ClassLocator<Object>(config.getServletContext());
		
		locator.addAnnotations(javax.persistence.Entity.class, org.hibernate.annotations.Entity.class);

		for (String type : new String[] { "classes", "packages" })
		{
			String values = config.getInitParameter(type);
			
			if (values != null)
			{
				// Make sure it is in a format we can read
				values = values.replace(';', ',');
				values = values.replaceAll("\\s*,\\s*", ",");
				values = values.replaceAll("\\s+", ",");

				for (String value : values.split(","))
				{
					value = value.trim();
					
					if (value.length() == 0)
						continue;

					if ("classes".equals(type))
					{
						if (!locator.addIfMatches(value))
							log.warn(value, " is not annotated with @Entity");
					}
					else
					{
						int length = value.length();
						if ((length >= 1) && (value.charAt(length - 1) == '*'))
						{
							value = value.substring(0, value.length() - 1);
							length--;
						}

						if ((length >= 1) && (value.charAt(length - 1) != '.'))
							value += '.';

						locator.addPackage(value);
					}
				}
			}
		}
		
		// Scan classpath if no specific classes were listed or packages were listed
		if ((locator.getClasses().size() == 0) || (locator.getPackages().size() > 0))
			locator.findClasses();
		
		Set<Class<Object>> classes = locator.getClasses();

		for (Class c : classes)
		{
			configuration.addAnnotatedClass(c);
			log.trace("Added ", c.getCanonicalName(), " to Hibernate configuration");
		}
		
		return classes;
	}

	private void loadHibernateProperties(AnnotationConfiguration configuration)
	{
		configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);

		configuration.setProperty("hibernate.current_session_context_class", "thread");
		configuration.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");

		String propertiesString = config.getInitParameter("properties");

		if (propertiesString != null)
		{
			try
			{
				Properties properties = new Properties();
				properties.load(new ByteArrayInputStream(propertiesString.getBytes()));
				configuration.addProperties(properties);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Calls the next filter in the chain then clean up the session if one was opened.
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException
	{
		currentHibernateFilter.set(this);
		
		try
		{
			sessions.set(new HashMap<SessionFactory, Session>());
			
			chain.doFilter(request, response);
		}
		finally
		{
			if (sessions.get() != null)
			{
				for (Session session : sessions.get().values())
				{
					Transaction transaction = session.getTransaction();
					if (transaction.isActive())
						transaction.rollback();
					session.close();
				}
				
				sessions.get().clear();

				sessions.set(null);
			}
			
			currentHibernateFilter.set(null);
		}
	}

	public void destroy()
	{
		config = null;
		sessionFactories.clear();
		configurations.clear();
	}
	
	/**
	 * Opens a session and begins a transaction if there isn't already a session.
	 * There may be more than one HibernateFactory in use. You must specify a
	 * class that you intend to use with the Session so that the correct
	 * HibernateFactory may be selected to retrieve the Session from.
	 * 
	 * @return The Session associated with the current thread.
	 */
	@Override
	public Session getSession(Class forClass)
	{
		return getSession(getSessionFactory(forClass));
	}
	
	private Session getSession(SessionFactory sessionFactory)
	{
		Session session = sessions.get().get(sessionFactory);
		
		if ((session == null) && (sessionFactory != null))
		{
			session = sessionFactory.openSession();
			sessions.get().put(sessionFactory, session);
			session.beginTransaction();
		}

		return session;
	}
	
	@Override
	public Session getSession()
	{
		return getSession(getSessionFactory());
	}
	
	/**
	 * Convenience method that performs an immediate commit on the
	 * current transaction and begins a new transaction.
	 */
	@Override
	public void commit(Session session)
	{
		Transaction transaction = session.getTransaction();
		
		if ((transaction != null) && transaction.isActive())
		{
			session.flush();
			transaction.commit();
		}
		
		session.beginTransaction();
	}
	
	/**
	 * Convenience method that performs an immediate rollback on the
	 * current transaction and begins a new transaction.
	 */
	@Override
	public void rollback(Session session)
	{
		Transaction transaction = session.getTransaction();
		
		if ((transaction != null) && transaction.isActive())
			transaction.rollback();
		
		session.beginTransaction();
	}

	/**
	 * @return The Configuration used to build the SessionFactory associated with the specified class. 
	 */
	@Override
	public Configuration getConfiguration(Class forClass)
	{
		return configurations.get(forClass);
	}
	
	/**
	 * @return The SessionFactory associated with the specified class.
	 */
	@Override
	public SessionFactory getSessionFactory(Class forClass)
	{
		return sessionFactories.get(forClass);
	}

	@Override
	public Set<Configuration> getConfigurations()
	{
		HashSet<Configuration> configurations = new HashSet<Configuration>();
		
		configurations.addAll(this.configurations.values());
		
		return configurations;
	}

	@Override
	public Set<SessionFactory> getSessionFactories()
	{
		HashSet<SessionFactory> sessionFactories = new HashSet<SessionFactory>();
		
		sessionFactories.addAll(this.sessionFactories.values());
		
		return sessionFactories;
	}
	
	static public HibernateFilter getCurrentInstance()
	{
		return currentHibernateFilter.get();
	}
}
