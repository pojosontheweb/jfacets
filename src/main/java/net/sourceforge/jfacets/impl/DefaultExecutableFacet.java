package net.sourceforge.jfacets.impl;

import net.sourceforge.jfacets.IExecutable;

public class DefaultExecutableFacet extends DefaultFacet implements IExecutable {

	public Object execute() {
		return getContext();
	}

}
