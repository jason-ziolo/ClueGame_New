package experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicBorders.FieldBorder;
import javax.swing.plaf.metal.MetalBorders.TextFieldBorder;

public class ControlGUI_Example extends JFrame {
	
	// Window constants
	public final static String WINDOW_TITLE = ""; // For now, leave blank
	public final static int WINDOW_WIDTH = 780;
	public final static int WINDOW_HEIGHT = 220;
	
	private JLabel lastValRef;
	public enum Values { TURN, ROLL, GUESS, RESPONSE };
	private JLabel turnVal;
	private JLabel rollVal;
	private JLabel guessVal;
	private JLabel responseVal;
	
	public ControlGUI_Example()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(WINDOW_TITLE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		// the layout will contain 6 components
		setLayout(new GridLayout(2, 3));
		// Whose turn?
		add(createDisplay("", "Whose turn?", "N/A", true));
		turnVal = lastValRef;
		// Next player btn
		add(createButton("Next player"));
		// Accusation btn
		add(createButton("Make an accusation"));
		// Die/Roll panel
		add(createDisplay("Die", "Roll:  ", "N/A", false));
		rollVal = lastValRef;
		// Guess panel
		add(createDisplay("Guess", "Guess:  ", "N/A", false));
		guessVal = lastValRef;
		// Guess Result/Response panel
		add(createDisplay("Guess Result", "Response:  ", "N/A", false));
		responseVal = lastValRef;
	}

	private JPanel createDisplay(String title, String label, String initialValue, boolean staggered) {
		// The title of the panel, ingrained in the etched border
	 	TitledBorder panelTitle = new TitledBorder(new EtchedBorder(), title);
	 	panelTitle.setTitleJustification(TitledBorder.CENTER);
	 	// The label before the text field
	 	JLabel panelLabel = new JLabel(label);
	 	panelLabel.setAlignmentX(CENTER_ALIGNMENT);
	 	// The text field itself (not editable by user)
	 	JLabel val = new JLabel(initialValue);
	 	lastValRef = val;
	 	val.setHorizontalAlignment(JLabel.LEADING);
	 	Color cb = Color.decode("#BDD2E7");
	 	FieldBorder valBorder = new FieldBorder(cb, cb, cb, cb);
	 	val.setBorder(valBorder);
		val.setAlignmentX(CENTER_ALIGNMENT);
		
	 	JPanel panel = new JPanel();
	 	if(staggered) {
	 		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	 	}
	 	else {
	 		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
	 	}

		panel.add(panelLabel);
		panel.add(val);
		panel.setBorder(panelTitle);
		return panel;
	}
	
	private JButton createButton(String title) {
		JButton btn = new JButton(title);
		return btn;
	}
	
	public void setValue(Values ref, String in) {
		switch(ref) {
		case TURN:
			turnVal.setText(in);
			break;
		case ROLL:
			rollVal.setText(in);
			break;
		case GUESS:
			guessVal.setText(in);
			break;
		case RESPONSE:
			responseVal.setText(in);
			break;
		}
	}
	
	/* TODO: Remove
	public static void main(String[] args) {
		ControlGUI_Example gui = new ControlGUI_Example();	
		gui.setVisible(true);
		gui.setValue(Values.TURN, "Miss Scarlet");
		gui.setValue(Values.ROLL, "6");
		gui.setValue(Values.GUESS, "Mrs. White, Knife, Study");
		gui.setValue(Values.RESPONSE, "Knife");
	}
	*/
}