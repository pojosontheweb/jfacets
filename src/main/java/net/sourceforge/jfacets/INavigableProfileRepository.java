package net.sourceforge.jfacets;

/**
 * An extension of the standard {@link IProfileRepository} that supports 
 * full navigation (not only bottom-up) in the Profiles Graph.
 * <br/>
 * <b>NOTE</b> : JFacets doesn't need the Profiles Graph to be fully navigable 
 * in order to operate. The Navigable PR is used only for IDE purposes where 
 * you want to be able to get all informations about the Profiles Graph.
 * 
 * @author RŽmi VANKEISBELCK - remi 'at' rvkb.com
 */
public interface INavigableProfileRepository extends IProfileRepository {

	/**
	 * Return root profiles (profiles that have no parents).
	 */
	public IProfile[] getRootProfiles();

	/**
	 * Return the children of passed profile.
	 * @param parentProfile The profile to get the children for
	 * @return an array of child profiles
	 */
	public IProfile[] getSubProfiles(IProfile parentProfile);
	
}
