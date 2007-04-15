package net.sourceforge.jfacets.simpleprofiles;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.jfacets.IProfile;

/**
 * Implements a simple profile, with childs and parents.
 */
public class SimpleProfile implements IProfile {

	private String id;
	
	private ArrayList<SimpleProfile> childs = new ArrayList<SimpleProfile>();
	private ArrayList<SimpleProfile> parents = new ArrayList<SimpleProfile>();
	
	public SimpleProfile(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
		
	public Collection<SimpleProfile> getChildren() {
		return childs;
	}
	
	public void addChild(SimpleProfile child) {
		if (!childs.contains(child)) { 
			childs.add(child);
			child.parents.add(this);
		}
	}
	
	public Collection<SimpleProfile> getParents() {
		return parents;
	}
		
	public boolean equals(Object other) {
		if (other instanceof IProfile) {
			IProfile p = (IProfile)other;
			return p.getId().equals(id);
		} else 
			return false;
	}
	
}
