package net.sourceforge.jfacets;


/**
 * The interface of the Profile Repository.
 * 
 * @author Remi VANKEISBELCK - rvkb.com (remi 'at' rvkb.com)
 *  
 */
public interface IProfileRepository {
	
	/**
	 * Return a profile by its ID
	 */
	IProfile getProfileById( String profileId );

	/**
	 * Return passed profile's parents in an array
	 */
	IProfile[] getSuperProfiles( IProfile profile );

}
