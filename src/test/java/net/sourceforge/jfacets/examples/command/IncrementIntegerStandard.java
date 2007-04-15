package net.sourceforge.jfacets.examples.command;

public class IncrementIntegerStandard extends BaseCommandFacet {

	public Object execute() {
		// we increment of 1
		Integer i = (Integer)getContext().getTargetObject();
		return new Integer(i.intValue() + 1);
	}

}
