package com.mongus.stripes.util;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.mongus.stripes.CommonsMultipartWrapper;
import com.mongus.stripes.UploadProgress;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.ajax.JavaScriptResolution;

public class UploadMonitor implements ActionBean
{
	private ActionBeanContext context;
	
	public ActionBeanContext getContext()
	{
		return context;
	}

	public void setContext(ActionBeanContext context)
	{
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public Resolution monitor()
	{
        List<UploadProgress> uploads = (List<UploadProgress>) context.getRequest().getSession().getAttribute(CommonsMultipartWrapper.SESSION_ATTRIBUTE);
        
		HttpServletResponse response = context.getResponse();
		
		response.setHeader( "Pragma", "no-cache" );
		response.addHeader( "Cache-Control", "must-revalidate" );
		response.addHeader( "Cache-Control", "no-cache" );
		response.addHeader( "Cache-Control", "no-store" );
		response.setDateHeader("Expires", 0);
		
        return new JavaScriptResolution(uploads != null ? uploads : false);
	}
}
