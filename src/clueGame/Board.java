package clueGame;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clueGUI.DisplayPanel;
import clueGame.BoardCell;


@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener {
	private static int numRows;
	private static int numColumns;
	private boolean highlight = false; // Made so the board knows weather or not to highlight squares

	public static final int BOARD_SIZE = 50;

	private BoardCell[][] board;
	static private Map<Character, String> rooms;
	private Map<BoardCell, LinkedList<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private BoardCell clickedCell;

	private String boardConfigFile;
	private String roomConfigFile;
	private String peopleConfigFile = "clueFiles/PeopleCards.txt"; 
	private String weaponsConfigFile = "clueFiles/WeaponsCards.txt";

	private Solution theAnswer;
	private Set<Card> seenCards;

	private ArrayList<Player> potentialPlayers;
	private ArrayList<Card> allCards = new ArrayList<Card>();

	// Constructor with files passed in
	public Board(String boardConfigFile, String roomConfigFile) {
		this.boardConfigFile = boardConfigFile;
		this.roomConfigFile = roomConfigFile;
		visited = new HashSet<BoardCell>();
		
		board = new BoardCell[BOARD_SIZE][BOARD_SIZE];
		
		for (int i=0; i<BOARD_SIZE; i++) {
			for (int j=0; j<BOARD_SIZE; j++) {
				board[i][j] = new BoardCell(i,j);
			}
		}
		
		rooms = new HashMap<Character, String>();
		adjMatrix = new HashMap<BoardCell, LinkedList<BoardCell>>();
		targets = new HashSet<BoardCell>();
		seenCards = new HashSet<Card>();
	}

	// Constructor using default files
	public Board() {
		// Use default filenames
		this("clueFiles/clueLayout.csv", "clueFiles/clueLayoutLegend.txt");
		addMouseListener(this);
	}

	public void initialize() {
		try {
			loadRoomConfig();
			loadBoardConfig();
			loadMiscConfigFiles();	//NOTE* - this will currently only call default file names, to change that, use a setter and call the setter
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}
		calcAdjacencies();
	}
	
	public void loadMiscConfigFiles() {
		try {
			loadPlayers();
			loadWeapons();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public void loadPlayers() throws FileNotFoundException {		
		FileReader reader = new FileReader(peopleConfigFile);
		Scanner in = new Scanner(reader);

		potentialPlayers = new ArrayList<Player>();
		
		int count = 0;
		while (in.hasNextLine()){
			String line = in.nextLine();
			Scanner lineScanner = new Scanner(line);
			lineScanner.useDelimiter(",");
			String name = lineScanner.next();
			name = name.replace(",", "");
			name = name.trim();
			String color = lineScanner.next();
			color = color.replace(",", "");
			color = color.trim();
			int row = Integer.parseInt(lineScanner.next());
			int col = Integer.parseInt(lineScanner.next());
			if (count == 0){
				potentialPlayers.add(new HumanPlayer(name, row, col, color));
				allCards.add(new Card(name, CardType.PERSON));
			}
			else {
				potentialPlayers.add(new ComputerPlayer(name, row, col, color));
				allCards.add(new Card(name, CardType.PERSON));
			}
			lineScanner.close();
			count++;
		}
		in.close();
	}

	public void loadWeapons() throws FileNotFoundException {
		FileReader reader = new FileReader(weaponsConfigFile);
		Scanner scanner = new Scanner(reader);
		while(scanner.hasNextLine()) {
			allCards.add(new Card(scanner.nextLine(), CardType.WEAPON));
		}
		scanner.close();
	}

	public Card handleSuggestion(ArrayList<Player> players, Solution suggestion, String sPlayer, BoardCell clicked){
		int playerNum = 0;
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).getPlayerName().equals(sPlayer))
				playerNum = i;
		}
		
		Card returnedCard = null;
		for (int i = 1; i < players.size(); i++){
			returnedCard = players.get((i + playerNum) % players.size()).disproveSuggestion(suggestion);
			if (returnedCard != null){
				seenCards.add(returnedCard);
				return returnedCard;
			}
		}
		return null;
	}

	public boolean checkAccusation(Solution accusation){
		if (theAnswer.person != accusation.person || theAnswer.room != accusation.room || theAnswer.weapon != accusation.weapon)
			return false;
		return true;
	}

	// Load room using passed in file
	public void loadRoomConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(roomConfigFile);
		Scanner scanner = new Scanner(reader);

		String tempInitial = "";
		String tempName = "";
		String tempCardType = "";

		while (scanner.hasNext()) {
			// Read line from file, throw exception if not enough entries on the line
			tempInitial = scanner.next();
			if (!scanner.hasNext()) {
				scanner.close();
				throw new BadConfigFormatException();
			}
			tempName = scanner.next();
			if (!scanner.hasNext()) {
				scanner.close();
				throw new BadConfigFormatException();
			}
			while (tempName.charAt(tempName.length() - 1) != ',') {
				tempName += " ";
				tempName += scanner.next();
			}
			if (!scanner.hasNext()) {
				scanner.close();
				throw new BadConfigFormatException();
			}
			tempCardType = scanner.next();

			// Throw exception if the input was not formatted correctly
			if (tempInitial.length() != 2 || tempInitial.charAt(1) != ',' ||
					tempName.charAt(tempName.length() - 1) != ',' ||
					!(tempCardType.equals("Other") || tempCardType.equals("Card"))){
				scanner.close();	
				throw new BadConfigFormatException();
			}
			tempName = tempName.substring(0, tempName.length() - 1);
			rooms.put(tempInitial.charAt(0), tempName);
			
			// Generating cards
			if(tempCardType.equals("Card")) {
				allCards.add(new Card(tempName, CardType.ROOM));
			}
		}
		scanner.close();
	}
	
	// Load board using passed in file
	public void loadBoardConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(boardConfigFile);
		Scanner scanner = new Scanner(reader);

		String tempLine = "";
		int currentLine = 0;
		int numCols = 0;

		Set<Integer> allNumCols = new HashSet<Integer>();

		while (scanner.hasNextLine()) {
			tempLine = scanner.nextLine();

			numCols = 0;
			// Loop through line to add all the cells
			for (int i=0; i<tempLine.length(); i++) {

				if (tempLine.charAt(i) == ',') {
					numCols++;
					continue;
				}

				if (tempLine.charAt(i) == 'N'){
					board[currentLine][numCols].setNameOutput(true);
					continue;
				}

				board[currentLine][numCols].setInitial(tempLine.charAt(i));
				board[currentLine][numCols].setRow(currentLine);
				board[currentLine][numCols].setColumn(numCols);
				board[currentLine][numCols].setDoorDirection('N');

				if (!rooms.containsKey(board[currentLine][numCols].getInitial())) {
					scanner.close();
					throw new BadConfigFormatException();
				}

				if (i != tempLine.length() - 1 && tempLine.charAt(i+1) != ',' && tempLine.charAt(i+1) != 'N') {
					board[currentLine][numCols].setDoorDirection(tempLine.charAt(i+1));
					i++;
				} 
			}

			allNumCols.add(numCols);
			currentLine++;
		}

		if (allNumCols.size() != 1) {
			scanner.close();
			throw new BadConfigFormatException();
		}
		scanner.close();
		numRows = currentLine;
		numColumns = numCols + 1;

	}

	public static int getNumRows() {
		return numRows;
	}

	public static int getNumColumns() {
		return numColumns;
	}

	public void calcAdjacencies() {
		for (int i=0; i < board.length; i++) {
			for (int j=0; j < board[i].length; j++) {
				LinkedList<BoardCell> tempList = new LinkedList<BoardCell>();
				// If we are on a doorway, the only adjacency is the room's exit
				if (!board[i][j].isWalkway()) {
					DoorDirection doorDir = board[i][j].getDoorDirection();
					if (doorDir == DoorDirection.UP)
						tempList.add(board[i - 1][j]);
					else if (doorDir == DoorDirection.DOWN)
						tempList.add(board[i + 1][j]);
					else if (doorDir == DoorDirection.LEFT)
						tempList.add(board[i][j - 1]);
					else if (doorDir == DoorDirection.RIGHT)
						tempList.add(board[i][j + 1]);
					adjMatrix.put(board[i][j], tempList);
					continue;
				}
				// Otherwise there are four potential adjacencies
				// The cells must be walkways or doors in this case
				if (i > 0) {
					if (board[i - 1][j].isWalkway() || (board[i - 1][j].isDoorway() && 
							board[i - 1][j].doorDirection == DoorDirection.DOWN))
						tempList.add(board[i - 1][j]);
				}
				if (j > 0) {
					if (board[i][j - 1].isWalkway() || (board[i][j - 1].isDoorway() &&
							board[i][j - 1].doorDirection == DoorDirection.RIGHT))
						tempList.add(board[i][j - 1]);
				}
				if (i < board.length - 1) {
					if (board[i + 1][j].isWalkway() || (board[i + 1][j].isDoorway() &&
							board[i + 1][j].doorDirection == DoorDirection.UP))
						tempList.add(board[i + 1][j]);
				}
				if (j < board[i].length - 1) {
					if (board[i][j + 1].isWalkway() || (board[i][j + 1].isDoorway() &&
							board[i][j + 1].doorDirection == DoorDirection.LEFT))
						tempList.add(board[i][j + 1]);
				}
				adjMatrix.put(board[i][j], tempList);
			}
		}
	}

	public void calcTargets(int row, int column, int pathLength) {
		// Clear values from last targets calculation
		targets.clear();
		visited.clear();
		visited.add(board[column][row]); // Ensure the starting point is not a target
		doCalcTargets(column, row, pathLength);
	}

	public void doCalcTargets(int row, int column, int pathLength) {
		// Find all adjacent cells that have not been visited
		LinkedList<BoardCell> tempCells = new LinkedList<BoardCell>(getAdjList(row, column));
		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();

		for (BoardCell tempCell : tempCells) {
			if (!visited.contains(tempCell)) {
				// Add the cell if it is a walkway
				if (tempCell.isWalkway()) {
					adjacentCells.push(tempCell);
				}
				// Add the cell to targets if it is a doorway that we can use.
				// The path should stop here (since we can't move within a room), so
				// this cell is not added to visited.
				if (tempCell.isDoorway()) {
					if (tempCell.getRow() == row - 1 && tempCell.getCol() == column && tempCell.getDoorDirection() == DoorDirection.DOWN) {
						targets.add(tempCell);
					}
					if (tempCell.getRow() == row && tempCell.getCol() == column + 1 && tempCell.getDoorDirection() == DoorDirection.LEFT) {
						targets.add(tempCell);
					}
					if (tempCell.getRow() == row + 1 && tempCell.getCol() == column && tempCell.getDoorDirection() == DoorDirection.UP) {
						targets.add(tempCell);
					}
					if (tempCell.getRow() == row && tempCell.getCol() == column - 1 && tempCell.getDoorDirection() == DoorDirection.RIGHT) {
						targets.add(tempCell);
					}
				}
			}
		}

		// Recursive call for each of these cells
		for (BoardCell cell : adjacentCells) {
			visited.add(cell);
			if (pathLength == 1) {
				targets.add(cell);
			} else {
				doCalcTargets(cell.getRow(), cell.getCol(), pathLength - 1);
			}
			visited.remove(cell);
		}
	}

	public void dealCards(ArrayList<Player> players) {
		// Note: There are no requirements for dealing, other than that each player
		// gets a similar number of cards.
		// Only non-answer cards are eligible to be dealt
		ArrayList<Card> cards = new ArrayList<Card>(allCards);
		ArrayList<Card> ineligible = new ArrayList<Card>(3);
		for(Card i : cards) {
			if(	i.getCardName() == theAnswer.person ||
					i.getCardName() == theAnswer.room ||
					i.getCardName() == theAnswer.weapon) {
				ineligible.add(i);
			}
		}
		cards.removeAll(ineligible);
		// Random cards will be dealt in sequence to the players
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		int currPlayerIndex = 0;
		int numPlayers = players.size();
		for(Player i : players) {
			i.initializeCards();
		}
		while(cards.size() > 0) {
			Player next = players.get(currPlayerIndex);
			int nextCardIndex = 0;
			if(cards.size() != 1) {
				nextCardIndex = rand.nextInt(cards.size() - 1);
			}
			next.addDealtCard(cards.get(nextCardIndex));
			cards.remove(nextCardIndex);
			currPlayerIndex = (currPlayerIndex == numPlayers - 1 ? 0 : currPlayerIndex + 1);
		}
	}

	public void selectAnswer() {
		// We will select the answer by getting a random person, weapon, and room
		// we assume that all of the correct cards have been loaded
		ArrayList<Card> personCards = new ArrayList<Card>(6);
		ArrayList<Card> weaponCards = new ArrayList<Card>(6);
		ArrayList<Card> roomCards = new ArrayList<Card>(9);

		for(Card i : allCards) {
			if(i.getCardType() == CardType.PERSON) {
				personCards.add(i);
			}
			if(i.getCardType() == CardType.WEAPON) {
				weaponCards.add(i);
			}
			if(i.getCardType() == CardType.ROOM) {
				roomCards.add(i);
			}
		}

		Random rand = new Random();
		int personIndex = rand.nextInt(personCards.size() - 1);
		int weaponIndex = rand.nextInt(weaponCards.size() - 1);
		int roomIndex = rand.nextInt(roomCards.size() - 1);
		// The answer is the randomly selected person, room, and weapon
		// combination.
		this.theAnswer = new Solution(
				personCards.get(personIndex).getCardName(), 
				roomCards.get(roomIndex).getCardName(),
				weaponCards.get(weaponIndex).getCardName());
	}

	public BoardCell getCellAt(int row, int column) {
		return board[row][column];
	}

	public static Map<Character, String> getRooms() {
		return rooms;
	}

	public static String getRoom(Character c) {
		return rooms.get(c);
	}

	public LinkedList<BoardCell> getAdjList(int row, int col) {
		return adjMatrix.get(board[row][col]);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public ArrayList<Card> getCards() {
		return allCards;
	}

	@Override
	public void paintComponent(Graphics g){  //draws the board
		super.paintComponent(g);
		for (int i=0; i< numRows; i++) {
			for (int j=0; j< numColumns; j++) {
				board[i][j].draw(g);	// Will draw the square itself
			}
		}
		if (highlight){
			for (BoardCell cells : targets){
				cells.draw(g, Color.cyan);
			}
		}
		for (int i=0; i< numRows; i++) {
			for (int j=0; j< numColumns; j++) {
				board[i][j].drawOver(g);	// Will draw labels or doorways
			}
		}
		for (Player i: potentialPlayers){
			i.draw(g);						// Will draw the players on the grid
		}
	}
	
	public void highLightTargets(){
		setHighlight(true);
	}
	
	public void setHighlight(boolean highlight){
		this.highlight = highlight;
		repaint();
	}

	//all methods below intended for testing purposes only	
	public Card getCardByName(String in) {
		for(Card i : allCards) {
			if(i.getCardName().equals(in)) {
				return i;
			}
		}
		return null;
	}

	public Solution getTheAnswer() {	// For testing purposes
		return theAnswer;
	}

	public void setTheAnswer(Solution theAnswer) {	// For testing purposes
		this.theAnswer = theAnswer;
	}

	public ArrayList<Player> getPotentialPlayers() { // For testing purposes
		return potentialPlayers;
	}

	public Set<Card> getSeenCards() {
		return seenCards;
	}
	
	public void addSeenCard(Card card) {
		seenCards.add(card);
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		if(!ClueGame.currPlayerIsHuman() || !ClueGame.playerMayMove()) {
			return;
		}
		BoardCell clickedCell = null;
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numColumns; j++){
				if (board[i][j].containsClick(e.getX(), e.getY())){
					clickedCell = board[i][j];
					break;
				}
			}
		}
		if (clickedCell != null){
			System.out.println(clickedCell.toString());
			if (targets.contains(clickedCell)) {
				this.clickedCell = clickedCell;
				ClueGame.getCurrentPlayer().move(clickedCell);
				this.setHighlight(false);
				repaint(); // After moving, update the board
				ClueGame.endPlayerTurn(); // Besides showing the suggestion prompt, the
				// player's turn is over
			} else {
				String message = "Invalid move! Please select a cyan box";
				JOptionPane.showMessageDialog(null, message);
			}
		}
	}
	
	// For testing only
	public void removeSeenCard(Card card){
		seenCards.remove(card);
	}
	
	// For testing only
	public void clearSeenCards(){
		seenCards.clear();
	}
}
