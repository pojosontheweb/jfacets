package net.sourceforge.jfacets.examples.swing;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import net.sourceforge.jfacets.annotations.FacetKey;

@FacetKey(name="panelBuilder", profileId="standard_role",targetObjectType=Date.class)
public class BuilderStandard extends PanelBuilderFacet {

	public JPanel buildPanel() {
		// get hold of the date (passed as the target object of this facet)
		Date date = (Date) getFacetContext().getTargetObject();
		
		// and build the JPanel to be returned for profile "admin_role"
		JTextPane textPane = new JTextPane();
		textPane.setText("Hello, there !\nAs a standard user, you can't edit anything !");
		textPane.setEditable(false);
		DateFormat df = DateFormat.getDateInstance();
		JLabel label = new JLabel(df.format(date));
		JButton btn = new JButton("Doh !");
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(textPane, BorderLayout.NORTH);
		panel.add(label, BorderLayout.CENTER);
		panel.add(btn, BorderLayout.SOUTH);
		return panel;
	}

}
