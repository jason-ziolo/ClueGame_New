package clueGame;

import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ClueGame extends JFrame {
	// NAME is both the name of the window and the title
	static public final String NAME = "Clue";
	// Expected dimensions of 700 x 700.
	static public final int WIDTH = 800;
	static public final int HEIGHT = 850;
	static public final int REC_SIZE = 27; 	//size of board tiles.  
	
	private JDialog notesDialog;
	
	public ClueGame() {
		this.setJMenuBar(mainJMenuBar());
		this.setName(NAME);
		this.setTitle(NAME);
		this.setSize(WIDTH, HEIGHT);
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
	
	public static void main(String[] args) {
		Board board = new Board();
		board.initialize();
		board.loadMiscConfigFiles("clueFiles/PeopleCards.txt", "clueFiles/WeaponsCards.txt");
		ClueGame cgWindow = new ClueGame();
		cgWindow.add(board, BorderLayout.CENTER);
		cgWindow.setVisible(true);  //setting visible after makes it populate much quicker
		cgWindow.initializeNotesDialog(board);
	}
}