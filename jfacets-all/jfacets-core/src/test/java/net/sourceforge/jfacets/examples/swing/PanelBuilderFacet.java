package net.sourceforge.jfacets.examples.swing;

import javax.swing.JPanel;

import net.sourceforge.jfacets.IFacet;
import net.sourceforge.jfacets.IFacetContext;

public abstract class PanelBuilderFacet implements IFacet {
	
	private IFacetContext facetContext;
	
	public abstract JPanel buildPanel();

	public IFacetContext getFacetContext() {
		return facetContext;
	}

	public void setFacetContext(IFacetContext ctx) {
		this.facetContext = ctx;
	}

}
