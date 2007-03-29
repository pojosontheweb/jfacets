package net.sf.woko.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.jfacets.web.WebFacets;
import net.sf.woko.feeds.Feed;
import net.sf.woko.feeds.FeedItem;
import net.sf.woko.feeds.FeedUtil;
import net.sf.woko.util.Util;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;

/**
 * Action that returns an RSS feed for a given object
 */
@UrlBinding("/@feed/:className/:id")
public class FeedActionBean extends BaseActionBean {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(FeedActionBean.class);
	
	@Validate(required=true)
	private String className;
	@Validate(required=true)
	private String id;
	
	private Object object;
	private Feed feed;

	private List<FeedItem> items;
	
	@DefaultHandler
	public Resolution outputFeed() {
		// get hold of the object 
		Object[] objectData = Util.getObjectData(id, className, getContext());
		object = objectData[0];		
		if (object==null) {
			String msg = "Unable to convert ID " + id + " for type " + className;
			logger.error(msg);
			return null;
		}		
		// get hold of the feed for this object...
		feed = new FeedUtil().getFeed(object);
		
		if (feed==null) {
			// TODO : error page
			return null;
		}
		
		// exec viewFeed
		WebFacets wf = WebFacets.get(getRequest());
		String s = (String)wf.execFacet("viewFeed", object, getRequest());
		return new ForwardResolution(s);
	}

	public Feed getFeed() {
		return feed;
	}

	public Object getObject() {
		return object;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<FeedItem> getItems() {
		if (items==null) {
			items = new ArrayList<FeedItem>(feed.getItems());
			// sort the collection...
			Collections.sort(items);
		}
		return items;
	}
	
}