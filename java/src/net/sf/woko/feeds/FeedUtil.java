package net.sf.woko.feeds;

import java.util.Date;

import net.sf.woko.util.Util;

/**
 * Feed utilities
 * @see Feed
 * @see FeedHibernateInterceptor
 */
public class FeedUtil {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(FeedUtil.class);
		
	/**
	 * Refreshes the feed for passed entity (creates a 
	 * new item in the feed)
	 */
	public Feed refreshFeed(Object entity) {
		logger.debug("Refreshing feed for object " + entity);
		// get the feed
		FeedItemType type = FeedItemType.UPDATED;
		Feed feed = getFeed(entity);
		if (feed==null) {
			// create the feed
			String feedKey = getFeedKey(entity);
			feed = new Feed();
			feed.setObjectKey(feedKey);
			feed.setLastBuildDate(new Date());
			Util.getSession().save(feed);
			type = FeedItemType.CREATED;
		}
		feed.computeNewItem(entity, type);
		logger.debug("...feed " + feed + " refreshed OK");
		return feed;
	}

	/**
	 * Return the feed for passed entity.
	 */
	public Feed getFeed(Object entity) {
		logger.debug("Retrieving feed for object " + entity);		
		String feedKey = getFeedKey(entity);
		Feed feed = (Feed)Util.getSession().get(Feed.class, feedKey);
		logger.debug("Returning " + feed);
		return feed;
	}
	
	public static String getFeedKey(Object entity) {
		String pk = Util.getId(entity);
		String className = entity.getClass().getName();
		return className + "@" + pk;
	}
		
}
