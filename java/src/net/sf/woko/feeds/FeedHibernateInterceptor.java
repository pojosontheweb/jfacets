package net.sf.woko.feeds;

import java.io.Serializable;
import java.util.Date;

import net.sf.woko.feeds.annots.Feedable;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.util.Util;

import org.apache.log4j.Logger;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * Hibernate Interceptor that updates object feeds 
 * automatically when persistent instances are updated.
 * @see Feed
 * @see FeedUtil
 * @see Feedable
 */
public class FeedHibernateInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = Logger.getLogger(FeedHibernateInterceptor.class);
	
	/**
	 * Invoked when an instance has been updated.
	 */
	@Override
	public boolean onFlushDirty(Object entity,
            Serializable id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) {
		handleSaveOrUpdate(entity);
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
	
	/**
	 * Refreshes the feed for passed entity.
	 */
	private void handleSaveOrUpdate(Object entity) {
		logger.info("Handling save or update of entity " + entity);
		if (!(entity instanceof IWokoInternal)) {
			// is this entity "Feedable" ?
			if (Util.isFeedable(entity)) {	
				Feed f = new FeedUtil().refreshFeed(entity);	
				f.setLastBuildDate(new Date());
				logger.info("Feed " + f + " updated");
			}
		}
	}

}
