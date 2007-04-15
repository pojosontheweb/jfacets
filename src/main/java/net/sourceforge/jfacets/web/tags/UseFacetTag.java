package net.sourceforge.jfacets.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.web.WebFacets;
import net.sourceforge.jfacets.web.WebFacetsFilter;

/**
 * A tag that allows to retrieve and use a facet from inside a JSP, 
 * a la <jsp:useBean>. 
 * 
 * @author VANKEISBELCK R�mi
 */
public class UseFacetTag extends FacetTagSupport {

	private static final long serialVersionUID = 1L;
	
	private String var;	

	public UseFacetTag() {
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	protected Object getFacet() throws JspException {
		checkAttributes();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		WebFacets wf = WebFacets.get(request);
		if (wf==null)
			throw new JspException("Unable to obtain WebFacets bean from request ! is the filter OK ?");
		String pflId = getProfileId();
		if (pflId==null) {
			return wf.getFacet(getName(), getTargetObject(), getTargetObjectClass(), request);
		} else 
			return wf.getFacet(getName(), pflId, getTargetObject(), getTargetObjectClass());
	}

	@Override
	public int doStartTag() throws JspException {
		// retrieve the facet...
		Object facet = getFacet();		
		// set as page ctx attribute if found
		if (var!=null && facet!=null)
			pageContext.setAttribute(var, facet);		
		resetAttributes();
		return SKIP_BODY;
	}

	@Override
	protected void checkAttributes() throws JspException {		
		super.checkAttributes();
		// check if "var" attr is supplied
		if (var==null)
			throw new JspException("var attribute can't be null !");
	}
	
}
