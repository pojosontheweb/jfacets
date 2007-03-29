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

import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.*;
import net.sourceforge.stripes.util.bean.EvaluationException;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;

/**
 * <p>MongusValidationPropertyBinder provides an easy way to use the Mongus Validation package
 * with <a href="http://stripes.mc4j.org">Stripes</a>. A Stripes ValidationError will be
 * generated for each ValidationException that is thrown.</p>
 * 
 * <p>Stripes may be configured to use MongusValidationPropertyBinder by adding the following
 * lines to the Stripes filter in web.xml:</p>
 * <pre>
    &lt;init-param&gt;
        &lt;param-name&gt;ActionBeanPropertyBinder.Class&lt;/param-name>
        &lt;param-value&gt;com.mongus.stripes.MongusValidationPropertyBinder&lt;/param-value&gt;
    &lt;/init-param&gt;
 * </pre>
 * 
 * @author Aaron Porter
 */
public class HibernatePropertyBinder extends DefaultActionBeanPropertyBinder
{
	private static final ThreadLocal<Boolean> dontValidate = new ThreadLocal<Boolean>();
	
    public ValidationErrors bind(ActionBean bean, ActionBeanContext context, boolean validate)
    {
    	dontValidate.set(new Boolean(!validate));
    	
    	return super.bind(bean, context, validate);
    }

    /**
     * Invoked whenever an exception is thrown when attempting to bind a property to an
     * ActionBean.  By default logs some information about the occurrence, but could be overridden
     * to do more intelligent things based on the application.
     */
    protected void handlePropertyBindingError(ActionBean bean, ParameterName name, List<Object> values,
                                              Exception e, ValidationErrors errors)
    {
        if (e instanceof EvaluationException)
        {
        	if (dontValidate.get().booleanValue())
        		return;
        	
        	Throwable t = e.getCause();
        	
        	while ((t != null) && !(t instanceof IllegalArgumentException))
        		t = t.getCause();
        	
        	if (t != null)
            {
        		SimpleError error = new SimpleError(t.getMessage());
        		error.setFieldName(name.getName());
        		if ((values != null) && (values.size() > 0))
        			error.setFieldValue(values.get(0).toString());
        		errors.add(name.getName(), error);
                return;
            }
        }
        
        super.handlePropertyBindingError(bean, name, values, e, errors);
    }
    
}
