package net.sourceforge.jfacets.examples.command;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.sourceforge.jfacets.JFacets;

public class Main {

	public static void main(String[] args) {
		// the target object
		Integer i = new Integer(10);
		
		// get the JFacets bean
		ApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext(
					new String[]{
						"net/sourceforge/jfacets/examples/command/jFacetsAppCtx.xml"});
		JFacets jFacets = (JFacets)applicationContext.getBean("jFacets");

		// execute commands
		System.out.println("i = " + i);
		
		System.out.println("Executing command (increment, john, " + i + ")");
		BaseCommandFacet baseCmdFacet = (BaseCommandFacet)jFacets.getFacet("increment", "john", i);
		Integer i1 = (Integer)baseCmdFacet.execute();		
		print(i1);

		System.out.println("Executing command (increment, ivar, " + i + ")");
		baseCmdFacet = (BaseCommandFacet)jFacets.getFacet("increment", "ivar", i);
		Integer i2 = (Integer)baseCmdFacet.execute();		
		print(i2);

		System.out.println("Command example OK : check the logs !");
	}
	
	private static void print(Integer i) {
		System.out.println("Result = " + i);
	}
	
}
