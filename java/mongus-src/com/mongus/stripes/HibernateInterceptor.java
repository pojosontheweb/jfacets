package com.mongus.stripes;

import com.mongus.servlet.filter.HibernateFilter;
import com.mongus.util.Log;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.*;

@Intercepts({LifecycleStage.ActionBeanResolution})
public class HibernateInterceptor implements Interceptor
{
	private static Log log = Log.getInstance(HibernateInterceptor.class);
	
	private boolean initializedHibernateProvider = false;
	
	public Resolution intercept(ExecutionContext context) throws Exception
	{
		log.debug("HibernateInterceptor called");
		
		if (!initializedHibernateProvider && (HibernateProvider.getInstance() == null) && (HibernateFilter.getCurrentInstance() != null))
		{
			log.info("Initializing HibernateProvider");
			HibernateProvider.setInstance(HibernateFilter.getCurrentInstance());
			initializedHibernateProvider = true;
		}

		return context.proceed();
	}
}
