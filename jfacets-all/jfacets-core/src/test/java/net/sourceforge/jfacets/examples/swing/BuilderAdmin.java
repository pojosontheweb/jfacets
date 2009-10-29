package net.sourceforge.jfacets.examples.swing;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="panelBuilder", profileId="admin_role",targetObjectType=Date.class)
public class BuilderAdmin extends PanelBuilderFacet {

	public JPanel buildPanel() {
		// get hold of the date (passed as the target object of this facet)
		Date date = (Date) getFacetContext().getTargetObject();
		
		// and build the JPanel to be returned for profile "admin_role"
		JTextPane textPane = new JTextPane();
		textPane.setText("Hello, there !\nAs an admin, you can edit stuff...");
		JFormattedTextField jFormattedTextField = new JFormattedTextField(DateFormat.getDateInstance());
		jFormattedTextField.setValue(date);
		JButton btn1 = new JButton("Do");
		JButton btn2 = new JButton("funky");
		JButton btn3 = new JButton("stuff");
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(btn1);
		buttonsPanel.add(btn2);
		buttonsPanel.add(btn3);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(textPane, BorderLayout.NORTH);
		panel.add(jFormattedTextField, BorderLayout.CENTER);
		panel.add(buttonsPanel, BorderLayout.SOUTH);
		return panel;
	}

}
