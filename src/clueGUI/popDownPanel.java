package clueGUI;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class popDownPanel extends JPanel {
	private TitledBorder panelTitle;
	// TODO: Don't forget to add ? entry for detective notes
	public popDownPanel(String title, ArrayList<String> options) {
		panelTitle = new TitledBorder(new EtchedBorder(), title);
		this.setBorder(panelTitle);
		JComboBox<String> popup = new JComboBox<String>();
		for(int i = 0; i < options.size(); i++) {
			popup.addItem(options.get(i));
		}
		this.add(popup);
	}
}
