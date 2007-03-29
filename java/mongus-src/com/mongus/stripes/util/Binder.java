package com.mongus.stripes.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public class Binder implements ActionBean
{
	private static Log log = Log.getInstance(Binder.class);
	
	private ActionBeanContext context;

	@SuppressWarnings("unchecked")
	public void setContext(ActionBeanContext context)
	{
		this.context = context;
		
		HttpServletRequest request = context.getRequest();

		Enumeration parameters = request.getParameterNames();

		Collection<ValidationError> errors = new Vector<ValidationError>();

		while (parameters.hasMoreElements())
		{
			String name = (String) parameters.nextElement();
			String value = request.getParameter(name);

			Object temp = request.getParameter(name + "Class");

			if (temp == null)
				temp = request.getAttribute(name + "Class");

			try
			{
				Class clazz = null;

				if (temp == null)
					continue;
				else if (temp instanceof Class)
					clazz = (Class) temp;
				else if (temp instanceof String)
					clazz = Thread.currentThread().getContextClassLoader().loadClass((String) temp);

				log.debug("converting ", value, " to instance of ", clazz.getName());

				TypeConverter converter = StripesFilter.getConfiguration().getTypeConverterFactory().getTypeConverter(clazz, context.getLocale());

				Object object = converter.convert(value, clazz, errors);

				if (object != null)
				{
					request.setAttribute(name, object);
					log.debug("set request attribute \"", name, "\" to ", object);
				}
			}
			catch (Exception e) { log.error(e); }
		}
	}

	public Resolution dummy() {	return null; }

	public ActionBeanContext getContext()
	{
		return context;
	}
}