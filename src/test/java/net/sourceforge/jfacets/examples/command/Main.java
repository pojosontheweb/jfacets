package net.sourceforge.jfacets.examples.command;

import net.sourceforge.jfacets.JFacets;

public class Main {

	public static void main(String[] args) {
		// the target object
		Integer i = new Integer(10);
		
		// get the JFacets bean
		JFacets jFacets = JFacets.get("net/sourceforge/jfacets/examples/command/jFacetsAppCtx.xml");

		// execute commands
		System.out.println("i = " + i);
		
		System.out.println("Executing command (increment, john, " + i + ")");
		Integer i1 = (Integer)jFacets.execFacet("increment", "john", i);		
		print(i1);

		System.out.println("Executing command (increment, ivar, " + i + ")");
		Integer i2 = (Integer)jFacets.execFacet("increment", "ivar", i);		
		print(i2);

		System.out.println("Command example OK : check the logs !");
	}
	
	private static void print(Integer i) {
		System.out.println("Result = " + i);
	}
	
}
