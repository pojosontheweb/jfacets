package net.sourceforge.jfacets.examples.command;

public class IncrementIntegerAdmin extends BaseCommandFacet {

	public Object execute() {
		// we increment of ten !
		Integer i = (Integer)getContext().getTargetObject();
		return new Integer(i.intValue() + 10);
	}

}
