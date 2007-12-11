package net.sourceforge.jfacets.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The FacetKey annotation. 
 * Allows to mark classes as facets.
 *
 * @see net.sourceforge.jfacets.annotations.AnnotatedFacetDescriptorManager
 * @author Remi VANKEISBELCK - remi@rvkb.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FacetKey {
	/**
	 * The name of the facet (mandatory)
	 */
	String name();
	
	/**
	 * The profile ID (mandatory)
	 */
	String profileId();
	
	/**
	 * The target object type (optional, defaults to Object.class)
	 */
	Class targetObjectType() default Object.class;	
}
