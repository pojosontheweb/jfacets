package net.sourceforge.jfacets.web.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import net.sourceforge.jfacets.web.WebFacets;
import net.sourceforge.jfacets.web.WebFacetsFilter;

public class ExecFacetTag extends FacetTagSupport {

	private static final long serialVersionUID = 1L;

	private String var;	

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	@Override
	public int doStartTag() throws JspException {
		// retrieve and execute the facet...
		Object res = execFacet();		
		if (res!=null) {
			if (var!=null) {
				// set result as page ctx attribute if required
				pageContext.setAttribute(var, res);
			} else {
				// write directly to the JSP output if "var" not specified...
				try {
					pageContext.getOut().write(res.toString());
				} catch (IOException e) {
					throw new JspException(e);
				}
			}			
		}		
		resetAttributes();
		return SKIP_BODY;
	}		
	
	protected Object execFacet() throws JspException {
		checkAttributes();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		WebFacets wf = WebFacets.get(request);
		if (wf==null)
			throw new JspException("Unable to obtain WebFacets bean from request ! is the filter OK ?");
		String pflId = getProfileId();
		if (pflId==null) {
			return wf.execFacet(getName(), getTargetObject(), getTargetObjectClass(), request);
		} else 
			return wf.execFacet(getName(), pflId, getTargetObject(), getTargetObjectClass());
	}


}
