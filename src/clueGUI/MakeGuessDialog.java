package clueGUI;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JDialog;

public class MakeGuessDialog extends JDialog {
	// NAME is both the name of the window and the title
	static public final String NAME = "Make a Guess";
	// Expected dimensions of 300 x 200.
	static public final int WIDTH = 300;
	static public final int HEIGHT = 200;
	private String yourRoom = "";
	
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
		this.setLayout(new GridLayout(4, 2));
	}
	
	public void setYourRoom(String yourRoom) {
		this.yourRoom = yourRoom;
	}
	
}
