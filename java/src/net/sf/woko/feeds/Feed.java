package net.sf.woko.feeds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.validator.NotNull;

import net.sf.woko.feeds.annots.Feedable;
import net.sf.woko.util.IWokoInternal;
import net.sf.woko.util.Util;

/**
 * A Feed : holds data needed to create RSS feeds.
 * @see FeedHibernateInterceptor
 */
@Entity
public class Feed implements IWokoInternal {
	
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(Feed.class);
	
	@Id
	private String objectKey;	
	@NotNull
	private Date lastBuildDate;
	
	public Feed() { }
	
	@OneToMany(cascade={CascadeType.ALL})
	private List<FeedItem> items;
	
	public Date getLastBuildDate() {
		return lastBuildDate;
	}
	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	public List<FeedItem> getItems() {
		return items;
	}
	public void setItems(List<FeedItem> items) {
		this.items = items;
	}
	
	/**
	 * Updates that feed by creating a new item for passed entity. Invoked after the 
	 * entity is saved or updated. 
	 * We take entity as a param to avoid reloading it : when this method is 
	 * invoked, we have a ref to the object...
	 */
	@SuppressWarnings("unchecked")
	public boolean computeNewItem(Object entity, FeedItemType type) {
		// is passed entity matching this feed ?
		String s = FeedUtil.getFeedKey(entity);
		if (!s.equals(objectKey)) { 
			logger.warn("Trying to compute feed item for wrong entity ! Feed key = " + objectKey + ", entity=" + entity);
			return false;
		} else {
			// create a new item
			logger.info("Creating new feed item for entity " + entity + ", type=" + type);
			FeedItem item = new FeedItem();
			item.setType(type);
			item.setPubDate(new Date());			
			if (items==null)
				items = new ArrayList<FeedItem>();
			this.items.add(item);
			// set username
			item.setUsername(Util.getCurrentUsername());
			
			// delete items if size exceeds limit
			Class clazz = entity.getClass();
			Feedable f = (Feedable)clazz.getAnnotation(Feedable.class);
			int maxItems = f.maxItems();
			if (this.items.size()>maxItems) {
				ArrayList<FeedItem> tmpList = new ArrayList<FeedItem>(this.items);
				Collections.sort(tmpList);
				for(int i = maxItems ; i < tmpList.size() ; i++) 
					this.items.remove(tmpList.get(i));
			}
			
			return true;
		}
	}

	public String toString() {
		int nbItems = 0;
		if (items!=null && items.size()>0)
			nbItems = items.size();
		return objectKey + ", " + nbItems + " item(s)";
	}
	
}
