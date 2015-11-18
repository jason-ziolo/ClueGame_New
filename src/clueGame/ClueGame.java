package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthSeparatorUI;

import java.awt.Graphics;

import clueGUI.DetectiveNotesDialog;
import clueGUI.DisplayPanel;
import clueGUI.MyCards;
import clueGame.Player;

@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	// NAME is both the name of the window and the title
	static public final String NAME = "Clue";
	// Expected dimensions of 700 x 700.
	static public final int SIZE = 600;
	static public final int WINDOW_PADDING = 200;
	private static int recSize; // This will be equal to the size divided by the number of rows
	private static Board board;
	private static boolean waitingForTurn = true;
	private static ArrayList<Player> players = null;
	private static Player currPlayer = null;
	private static int currPlayerIndex = 0;
	private static int currDiceRoll = 0;
	private static DisplayPanel display;
	
	private JDialog notesDialog;
	
	public ClueGame() {
		this.setJMenuBar(mainJMenuBar());
		this.setName(NAME);
		this.setTitle(NAME);
		this.setSize(SIZE + WINDOW_PADDING, SIZE + WINDOW_PADDING);
		//this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeNotesDialog(Board board) {
		// One of the easiest ways to get the required info
		// is from the cards created by the Board class.
		ArrayList<Card> allInfo = board.getCards();
		ArrayList<String> peopleInfo = new ArrayList<String>();
		ArrayList<String> roomsInfo = new ArrayList<String>();
		ArrayList<String> weaponsInfo = new ArrayList<String>();

		for(Card i : allInfo) {
			if(i.getCardType() == CardType.PERSON) {
				peopleInfo.add(i.getCardName());
			}
			if(i.getCardType() == CardType.ROOM) {
				roomsInfo.add(i.getCardName());
			}
			if(i.getCardType() == CardType.WEAPON) {
				weaponsInfo.add(i.getCardName());
			}
		}
		notesDialog = new DetectiveNotesDialog(peopleInfo, roomsInfo, weaponsInfo);
	}
	
	private JMenuBar mainJMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.add(createFileNotesItem());
		menu.add(createFileExitItem());
		bar.add(menu);
		return bar;
	}
	
	private JMenuItem createFileNotesItem() {
		JMenuItem item = new JMenuItem("Show detective notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				notesDialog.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	public static int getRecSize() {
		return recSize;
	}

	public static void main(String[] args) {
		board = new Board();
		display = new DisplayPanel();		
		board.initialize();
		board.selectAnswer();
		players = board.getPotentialPlayers(); // for now
		board.dealCards(players);
		Player humanPlayer = players.get(0);
		MyCards cards = new MyCards(humanPlayer);
		recSize = SIZE / Board.getNumRows();
		ClueGame cgWindow = new ClueGame();
		cgWindow.add(board, BorderLayout.CENTER);
		cgWindow.add(display, BorderLayout.SOUTH);
		cgWindow.add(cards, BorderLayout.EAST);
		
		// Splash screen
		JOptionPane firstDisplay = new JOptionPane("You are " + humanPlayer.getPlayerName() + ". Press OK to continue.");
		JDialog information = firstDisplay.createDialog(cgWindow, "Welcome to Clue");
		information.setVisible(true);
		cgWindow.setVisible(true);  //setting visible after makes it populate much quicker
		cgWindow.initializeNotesDialog(board);
		
		// Main Board Logic
		currPlayerIndex = players.size() - 1;
		currPlayer = players.get(currPlayerIndex);
		
		// Enable interaction
		waitingForTurn = false;
	}
	
	private static int rollDie() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}
	
	public static void nextPlayerBtnPress() {
		if(waitingForTurn) {
			String message = "You need to finish your turn!";
			JOptionPane.showMessageDialog(null, message);
			return;
		}
		else {
			waitingForTurn = true;
			int nextPlayerIndex = (currPlayerIndex + 1) % players.size();
			currPlayer = players.get(nextPlayerIndex);
			currDiceRoll = rollDie();
			display.updateDisplay(currPlayer.getPlayerName(), currDiceRoll);
			currPlayer.doTurn(board, currDiceRoll);
		}
	}
	
	public static void playerAccusation(String playerName, Solution accusation) {
		boolean gameWon = board.checkAccusation(accusation);
		if(gameWon) {
			String message = playerName + " has found the solution! The game is over.";
			JOptionPane.showMessageDialog(null, message);
		}
		else {
			waitingForTurn = false;
		}
	}

	public static void playerSuggestion(String sPlayer, Solution suggestion, BoardCell clicked) {
		Card result = board.handleSuggestion(players, suggestion, sPlayer, clicked);
		display.updateGuess(suggestion.toString(), result.getCardName());
		waitingForTurn = false;
	}
	
	public static Player getCurrentPlayer() {
		return currPlayer;	// For when the human player clicks the board
	}
}
