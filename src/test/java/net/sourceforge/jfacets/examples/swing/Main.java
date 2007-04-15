package net.sourceforge.jfacets.examples.swing;


import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.apache.log4j.BasicConfigurator;

import net.sourceforge.jfacets.JFacets;

public class Main {
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();

		// get hold of a JFacets instance
		JFacets jFacets = JFacets.get("/net/sourceforge/jfacets/examples/swing/swingDemoAppCtx.xml");

		// our target object 
		Date currentDate = new Date();

		// iterate on user names (those are defined in the SimpleProfileRepository as 
		// childs of standard_role and asmin_role), and create a JFrame using the 
		// facetized panel builders
		String[] profileIds = {"john", "ivar"};
		for (int i = 0; i < profileIds.length; i++) {
		
			JFrame frame = new JFrame("JFacets Swing demo, profile " + profileIds[i]);
						
			// retrieve a panel builder facet for the 
			// supplied profile ID and target object, 
			// and build the profiled panel...
			PanelBuilderFacet builder = 
				(PanelBuilderFacet)jFacets.getFacet("panelBuilder", profileIds[i], currentDate);
			JPanel panel = builder.buildPanel();
			
			// show frame
			showFrame(frame, panel);
		}		
	}
	
	private static void showFrame(JFrame frame, JPanel panel) {
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);		
	}

}
