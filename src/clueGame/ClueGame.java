package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import clueGUI.DetectiveNotesDialog;
import clueGUI.DisplayPanel;
import clueGUI.MyCards;

@SuppressWarnings("serial")
public class ClueGame extends JFrame {
	// NAME is both the name of the window and the title
	static public final String NAME = "Clue";
	// Expected dimensions of 700 x 700.
	static public final int SIZE = 600;
	static public final int WINDOW_PADDING = 200;
	private static int recSize; // This will be equal to the size divided by the number of rows
	
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
		Board board = new Board();
		DisplayPanel display = new DisplayPanel();
		MyCards cards = new MyCards();
		board.initialize();
		recSize = SIZE / Board.getNumRows();
		ClueGame cgWindow = new ClueGame();
		cgWindow.add(board, BorderLayout.CENTER);
		cgWindow.add(display, BorderLayout.SOUTH);
		cgWindow.add(cards, BorderLayout.EAST);
		cgWindow.setVisible(true);  //setting visible after makes it populate much quicker
		cgWindow.initializeNotesDialog(board);
	}
}
