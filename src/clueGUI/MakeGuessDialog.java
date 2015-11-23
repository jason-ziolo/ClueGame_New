package clueGUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import clueGame.BoardCell;
import clueGame.ClueGame;
import clueGame.Solution;

public class MakeGuessDialog extends JDialog {
	// Expected dimensions of 300 x 200.
	static public final int WIDTH = 300;
	static public final int HEIGHT = 200;
	private boolean isAccusationMenu;
	private JButton submitBtn;
	private JButton cancelBtn;
	private JComboBox<String> personPopup;
	private JComboBox<String> weaponPopup;
	private JTextField yourRoom = new JTextField("");
	
	public MakeGuessDialog(ArrayList<String> people, ArrayList<String> weapons) {
		/* MakeGuessDialog will use grid layout, with 2 columns and 4 rows
		 * There will be, in order of addition:
		 * 3 text fields (non-editable)
		 * A "person" drop down list
		 * A text field (non-editable)
		 * A "weapon" drop down list
		 * A submit button, which will call ClueGame.playerSuggestion
		 * A cancel button, which will close the dialog (set it non-visible)
		 */
		//if()
		String name = "";
		this.setName(name);
		this.setTitle(name);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new GridLayout(4, 2));
		// first row: "Your room" label, then static text field
		this.add(new JLabel("Your room"));
		yourRoom.setEditable(false);
		this.add(yourRoom);
		// second row: "Person" label, then popDownMenu
		this.add(new JLabel("Person"));
		personPopup = new PopDownPanel("Person Guess", people).getPopup();
		this.add(personPopup);
		// third row: "Weapon" label, then popDownMenu
		this.add(new JLabel("Weapon"));
		weaponPopup = new PopDownPanel("Weapon Guess", weapons).getPopup();
		this.add(weaponPopup);
		// fourth row: "Submit" button, then "Cancel" button
		submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ButtonListener());
		this.add(submitBtn);
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ButtonListener());
		this.add(cancelBtn);
	}
	
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == submitBtn)
				ClueGame.humanPlayerSuggestion(new Solution((String) personPopup.getSelectedItem(), 
						yourRoom.getText(), 
						(String) weaponPopup.getSelectedItem()));
			ClueGame.toggleMakeGuessDlg("");
		}
	}
	
	public void setYourRoom(String text) {
		this.yourRoom.setText(text);
	}
}
