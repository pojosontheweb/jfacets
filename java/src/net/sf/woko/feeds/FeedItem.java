package net.sf.woko.feeds;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.sf.woko.util.IWokoInternal;

import org.hibernate.validator.NotNull;

/**
 * A feed item.
 */
@Entity
public class FeedItem implements IWokoInternal, Comparable<FeedItem> {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@NotNull
	private Date pubDate;
	
	@NotNull
	private FeedItemType type;

	private String username;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getPubDate() {
		return pubDate;
	}
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}
	public FeedItemType getType() {
		return type;
	}
	public void setType(FeedItemType type) {
		this.type = type;
	}
	
	public String toString() {
		return  type + " on " + pubDate;
	}
	public int compareTo(FeedItem o) {
		return -getPubDate().compareTo(o.getPubDate());
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String currentUsername) {
		this.username = currentUsername;
	}
		
}
