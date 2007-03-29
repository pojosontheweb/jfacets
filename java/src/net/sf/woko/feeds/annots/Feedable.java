package net.sf.woko.feeds.annots;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be owned by "feedable" objects (an RSS 
 * feed will automatically be produced and maintained 
 * for objects of thoses classes).
 * Classes with this annotation MUST be entities as well (they 
 * must have the Entity annot).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Feedable {

	/**
	 * The max number of items in the feed
	 */
	int maxItems();
	
}
