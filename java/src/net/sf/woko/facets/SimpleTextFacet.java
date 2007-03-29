package net.sf.woko.facets;

import net.sourceforge.jfacets.IExecutable;

/**
 * Base facet class, to be used by facets that simply display text.
 */
public abstract class SimpleTextFacet extends BaseFacet implements IExecutable {

	/**
	 * Return the path to the JSP fragment to be used : 
	 * <code>/WEB-INF/woko/fragments/simpleTextFacetRender.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/fragments/simpleTextFacetRender.jsp";
	}
	
	/**
	 * Return the text to be displayed
	 */
	public abstract String getText();
	
}
