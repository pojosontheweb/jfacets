package net.sourceforge.jfacets.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows to have multiple assignations for a 
 * single facet class. 
 * @author vankeisb
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FacetKeyList {
	
	/**
	 * An array with the facet keys for the annotated class
	 */
	FacetKey[] keys();

}
