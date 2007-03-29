package net.sf.woko.facets.read;

import net.sourceforge.jfacets.IExecutable;
import net.sourceforge.jfacets.IFacetContext;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.facets.BaseFacet;
import net.sf.woko.util.Util;

/**
 * Facet used for RSS2 feeds. Allows to create and display RSS feeds 
 * for objects !
 * <br/><br/>
 * <b>Assignation details :</b>
 * <ul>
 * <li><b>name</b> : viewFeed</li>
 * <li><b>profileId</b> : ROLE_WOKO_GUEST</li>
 * <li><b>targetObjectType</b> : Object</li>
 * </ul>
 */
@FacetKey(name="viewFeed",profileId="ROLE_WOKO_GUEST")
public class ViewFeed extends BaseFacet implements IExecutable {

	private String title;
	private String type;
	
	@Override
	public void setContext(IFacetContext context) {
		super.setContext(context);
		WebFacets wf = WebFacets.get(getRequest());
		ViewObjectTitle vt = (ViewObjectTitle)wf.getFacet("viewTitle", getTargetObject(), getRequest());
		title = vt.getTitle();
		type = vt.getType();
	}

	/**
	 * Return true if the object has an associated feed. 
	 * This implementation returns all Feedable objects.
	 */
	public Boolean getShowFeed() {
        return Util.isFeedable(getTargetObject());
	}
	
	/**
	 * Helper method used to construct links to feeds
	 */
	public String getFeedObjectId() {
		return Util.getId(getTargetObject());
	}
	
	/**
	 * Helper method used to construct links to feeds
	 */
	public String getFeedObjectClass() {
		return getTargetObject().getClass().getName();
	}

	/**
	 * Return the title of the RSS channel
	 */
	public String getChannelTitle() {
		return "Woko feed - " + title + " (" + type + ")"; 
	}

	/**
	 * Return the target object's title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return the target object's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Return the path to the JSP to be used for creating the feed : 
	 * <code>/WEB-INF/woko/feed.jsp</code>
	 */
	public Object execute() {
		return "/WEB-INF/woko/feed.jsp";
	}
}
