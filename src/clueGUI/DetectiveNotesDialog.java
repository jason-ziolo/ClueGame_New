package clueGUI;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class DetectiveNotesDialog extends JDialog {
	// NAME is both the name of the window and the title
	static public final String NAME = "Detective Notes";
	// Expected dimensions of 500 x 550.
	static public final int WIDTH = 520;
	static public final int HEIGHT = 550;
	
	public DetectiveNotesDialog(ArrayList<String> people, ArrayList<String> rooms, ArrayList<String> weapons) {
		this.setName(NAME);
		this.setTitle(NAME);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new GridLayout(3, 2));
		// People panel
		JPanel peoplePanel = checkBoxPanel("People", people.size());
		for(int i = 0; i < people.size(); i++) {
			peoplePanel.add(new JCheckBox(people.get(i)));
		}
		this.add(peoplePanel);
		// Person guess 
		ArrayList<String> temp = (ArrayList<String>) people.clone();
		temp.add("?");
		this.add(new PopDownPanel("Person Guess", temp));
		// Rooms panel
		JPanel roomsPanel = checkBoxPanel("Rooms", rooms.size());
		for(int i = 0; i < rooms.size(); i++) {
			roomsPanel.add(new JCheckBox(rooms.get(i)));
		}
		this.add(roomsPanel);
		// Room guess
		temp = (ArrayList<String>) rooms.clone();
		temp.add("?");
		this.add(new PopDownPanel("Room Guess", temp));
		// Weapons panel
		JPanel weaponsPanel = checkBoxPanel("Weapons", weapons.size());
		for(int i = 0; i < weapons.size(); i++) {
			weaponsPanel.add(new JCheckBox(weapons.get(i)));
		}
		this.add(weaponsPanel);
		// Weapons guess
		temp = (ArrayList<String>) weapons.clone();
		temp.add("?");
		this.add(new PopDownPanel("Weapon Guess", temp));
	}
	
	private JPanel checkBoxPanel(String title, int numOptions) {
		int x = (int) Math.ceil((double)numOptions / 2.0);
		JPanel panel = new JPanel(new GridLayout(x, 2));
		TitledBorder panelTitle = new TitledBorder(new EtchedBorder(), title);
		panel.setBorder(panelTitle);
		return panel;
	}
	
}
