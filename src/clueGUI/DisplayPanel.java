package clueGUI;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.ClueGame;
import clueGame.Solution;

@SuppressWarnings("serial")
public class DisplayPanel extends JPanel {
	public JButton nextPlayerButton;
	public JButton accusationButton;
	private DiePanel die;
	private WhoseTurnPanel playerDisplay;
	private GuessPanel guess;
	private GuessResultPanel guessResult;
	public static boolean nextTurnPressed;
	
	public DisplayPanel() {
		setLayout(new GridLayout(0,1));
		die = new DiePanel();
		playerDisplay = new WhoseTurnPanel();
		guessResult = new GuessResultPanel();
		guess = new GuessPanel();
		TopPanel top = new TopPanel(playerDisplay);
		BottomPanel bottom = new BottomPanel(die, guess, guessResult);
		add(top);
		add(bottom);
	}
	
	public void updateDisplay(String player, int roll) {
		die.setDie(roll);
		playerDisplay.setPlayer(player);
	}
	
	public void updateGuess(Solution theGuess, String theResult) {
		guess.setGuess(theGuess);
		guessResult.setGuessResult(theResult);
	}
	
	public class TopPanel extends JPanel{
		public TopPanel(WhoseTurnPanel playerDisplay){
			setLayout(new GridLayout(0,3));
			add(playerDisplay);
			// Adding buttons
			nextPlayerButton = new JButton("Next Player");
			nextPlayerButton.addActionListener(new ButtonListener());
			add(nextPlayerButton);
			accusationButton = new JButton("Make an Accusation");
			accusationButton.addActionListener(new ButtonListener());
			add(accusationButton);
		}
	}
	
	public class BottomPanel extends JPanel{
		public BottomPanel(DiePanel die, GuessPanel guess, GuessResultPanel result){
			setLayout(new FlowLayout());
			// Adding the Die Panel
			add(die);
			// Adding the guess Panel
			add(guess);
			// Adding the guess Result Panel
			add(result);
		}
	}
		
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == accusationButton)
				System.out.println("Accused!");
			else {
				ClueGame.nextPlayerBtnPress();
			}
		}
	}
	
	public class WhoseTurnPanel extends JPanel {
		private JTextField WhoseTurn;
		
		public WhoseTurnPanel() {
			WhoseTurn = new JTextField(10);
			JLabel title = new JLabel("Whose Turn?");
			WhoseTurn.addActionListener(new WhoseTurnListener());
			WhoseTurn.setEditable(true);
			add(title);
			add(WhoseTurn);
		}
		
		private void setPlayer(String player){
			WhoseTurn.setText(player);
		}
		
		private class WhoseTurnListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String message = "It is " + WhoseTurn.getText() + "'s turn";
				JOptionPane.showMessageDialog(null, message);
			}
		}
	}
	

	public class DiePanel extends JPanel {
		private JTextField Die;
		
		public DiePanel() {
			Die = new JTextField(1);
			Die.setEditable(false);
			JLabel title = new JLabel("Roll");
			add(title);
			add(Die);
			setBorder(new TitledBorder (new EtchedBorder(), "Die"));
		}
		
		public void setDie(int num){
			Die.setText(Integer.toString(num));
		}
	}
	
	public class GuessPanel extends JPanel {
		private JTextField Guess;
		
		public GuessPanel() {
			Guess = new JTextField(35);
			Guess.setEditable(false);
			JLabel title = new JLabel("Guess");
			add(title);
			add(Guess);
			setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		}
		
		public void setGuess(Solution suggestion){
			Guess.setText(suggestion.person + " in the " + suggestion.room + " with the " + suggestion.weapon + ".");
		}
	}
	
	public class GuessResultPanel extends JPanel {
		private JTextField GuessResult;
		
		public GuessResultPanel() {
			GuessResult = new JTextField(15);
			GuessResult.setEditable(false);
			JLabel title = new JLabel("Response");
			add(title);
			add(GuessResult);
			setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		}
		
		public void setGuessResult(String in){
				GuessResult.setText(in);
		}
	}
}
