package clueGUI;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class PopDownPanel extends JPanel {
	private TitledBorder panelTitle;
	private JComboBox<String> popup;
	
	public PopDownPanel(String title, ArrayList<String> options) {
		panelTitle = new TitledBorder(new EtchedBorder(), title);
		this.setBorder(panelTitle);
		popup = new JComboBox<String>();
		for(int i = 0; i < options.size(); i++) {
			popup.addItem(options.get(i));
		}
		this.add(popup);
	}
	
	public JComboBox<String> getPopup() {
		return popup;
	}
}
