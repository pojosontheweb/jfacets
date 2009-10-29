package net.sourceforge.jfacets.examples.command;

public class IncrementIntegerAdmin extends BaseCommandFacet {

	public Object execute() {
		// we increment of ten !
		Integer i = (Integer) getFacetContext().getTargetObject();
		return new Integer(i.intValue() + 10);
	}

}
