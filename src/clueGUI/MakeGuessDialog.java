package clueGUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import clueGame.ClueGame;

public class MakeGuessDialog extends JDialog {
	// NAME is both the name of the window and the title
	static public final String NAME = "Make a Guess";
	// Expected dimensions of 300 x 200.
	static public final int WIDTH = 300;
	static public final int HEIGHT = 200;
	private JButton submitBtn;
	private JButton cancelBtn;
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
		this.setName(NAME);
		this.setTitle(NAME);
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new GridLayout(4, 2));
		// first row: "Your room" label, then static text field
		this.add(new JLabel("Your room"));
		yourRoom.setEditable(false);
		this.add(yourRoom);
		// second row: "Person" label, then popDownMenu
		this.add(new JLabel("Person"));
		this.add(new PopDownPanel("Person Guess", people).getPopup());
		// third row: "Weapon" label, then popDownMenu
		this.add(new JLabel("Weapon"));
		this.add(new PopDownPanel("Weapon Guess", weapons).getPopup());
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
				System.out.println("Submit"); //TODO: Hook up to ClueGame function
			ClueGame.toggleMakeGuessDlg();
		}
	}
	
	public void setYourRoom(String text) {
		this.yourRoom.setText(text);
	}
	
}
