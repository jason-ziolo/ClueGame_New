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

public class DisplayPanel extends JPanel {
	public JButton nextPlayerButton;
	public JButton accusationButton;
	
	public DisplayPanel() {
		//setTitle("ClueGUI");
		//setSize(500, 200);
		setLayout(new GridLayout(0,1));
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TopPanel top = new TopPanel();
		BottomPanel bottom = new BottomPanel();
		add(top);
		add(bottom);
	}
	
	public class TopPanel extends JPanel{
		public TopPanel(){
			setLayout(new GridLayout(0,3));
			WhoseTurnPanel whoseTurnPanel = new WhoseTurnPanel();
			add(whoseTurnPanel);
			// Adding buttons
			nextPlayerButton = new JButton("Next Player");
			nextPlayerButton.addActionListener(new ButtonListener());
			add(nextPlayerButton);
			accusationButton = new JButton("Make an Accusation");
			accusationButton.addActionListener(new ButtonListener());
			add(accusationButton);
		}
		
		public class ButtonListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == accusationButton)
					System.out.println("Accused!");
				else
					System.out.println("Next Player");
			}
		}
		
		public class WhoseTurnPanel extends JPanel {
			private JTextField WhoseTurn;
			
			public WhoseTurnPanel() {
				WhoseTurn = new JTextField(10);
				JLabel title = new JLabel("Whose Turn?");
				WhoseTurn.addActionListener(new WhoseTurnListener());
				WhoseTurn.setEditable(false);
				add(title);
				add(WhoseTurn);
			}
			
			private class WhoseTurnListener implements ActionListener {
				public void actionPerformed(ActionEvent e) {
					String message = "It is " + WhoseTurn.getText() + "'s turn";
					JOptionPane.showMessageDialog(null, message);
				}
			}
		}
	}
	
	public class BottomPanel extends JPanel{
		public BottomPanel(){
			setLayout(new FlowLayout());
			// Adding the Die Panel
			DiePanel diePanel = new DiePanel();
			add(diePanel);
			// Adding the guess Panel
			GuessPanel guessPanel = new GuessPanel();
			add(guessPanel);
			// Adding the guess Result Panel
			GuessResultPanel guessResultPanel = new GuessResultPanel();
			add(guessResultPanel);
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
		}
		
		public class GuessPanel extends JPanel {
			private JTextField Guess;
			
			public GuessPanel() {
				Guess = new JTextField(25);
				Guess.setEditable(false);
				JLabel title = new JLabel("Guess");
				add(title);
				add(Guess);
				setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
			}
		}
		
		public class GuessResultPanel extends JPanel {
			private JTextField GuessResult;
			
			public GuessResultPanel() {
				GuessResult = new JTextField(20);
				GuessResult.setEditable(false);
				JLabel title = new JLabel("Response");
				add(title);
				add(GuessResult);
				setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
			}
		}
	}
}
