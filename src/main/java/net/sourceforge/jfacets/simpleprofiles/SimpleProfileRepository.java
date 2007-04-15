package net.sourceforge.jfacets.simpleprofiles;

import java.util.Collection;
import java.util.HashMap;

import net.sourceforge.jfacets.IProfile;
import net.sourceforge.jfacets.IProfileRepository;

/**
 * The SimpleProfileRepository, a very basic PR implementation that allows to 
 * quickly get started with JFacets. It can be used directly in your application 
 * as a "mock" of the final real graph.<br/>
 * 
 * At the moment, it creates a tree-like graph that's used for JFacets tests :
 * <ul>
 * 		<li>root_profile</li>
 * 		<ul>
 * 			<li>admin_role</li>
 * 			<ul>
 * 				<li>ivar</li>
 * 			</ul>
 * 			<li>standard_role</li>
 * 			<ul>
 * 				<li>john</li>
 * 			</ul>
 * 		</ul>
 * </ul>
 * <br/>
 * This graph is created in the default constructor, you just have to override it in order 
 * to get another graph.
 */
public class SimpleProfileRepository implements IProfileRepository {

	private HashMap profilesByIds = new HashMap();
	
	public void destroy() {
	}

	public SimpleProfileRepository() {		
		feed();
	}
	
	/**
	 * To be overridden to instantiate the graph using the <code>createProfile()</code> method.
	 */
	protected void feed() {
		// create the graph (erf, here it's a tree)
		SimpleProfile root_profile = createProfile("root_profile");
		SimpleProfile admin_role = createProfile("admin_role");
		root_profile.addChild(admin_role);
		SimpleProfile ivar = createProfile("ivar");
		admin_role.addChild(ivar);
		SimpleProfile standard_role = createProfile("standard_role");
		root_profile.addChild(standard_role);
		SimpleProfile john = createProfile("john");
		standard_role.addChild(john);		
	}
	
	@SuppressWarnings("unchecked")
	public SimpleProfile createProfile(String id) {
		if (getProfileById(id)!=null)
			return null;
		else {
			SimpleProfile sp = new SimpleProfile(id);
			profilesByIds.put(id, sp);
			return sp;
		}
	}
	
	public IProfile getProfileById(String profileId) {
		return (IProfile)profilesByIds.get(profileId);
	}

	@SuppressWarnings("unchecked")
	public IProfile[] getSuperProfiles(IProfile profile) {
		SimpleProfile sp = (SimpleProfile)profile;
		Collection parents = sp.getParents();
		IProfile[] res = new IProfile[parents.size()];
		res = (IProfile[])parents.toArray(res);
		return res;
	}

}
