package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameActionTests {

	/*
	 * GameActionTests
	 * Includes tests for:
	 * Checking an accusation
	 * Selecting a target location
	 * Disproving a suggestion
	 * Making a suggestion
	 */
	
	private static Board board;
	
	@BeforeClass  
	public static void setUp() throws FileNotFoundException {
		board = new Board("clueFiles/clueLayout.csv", "clueFiles/clueLayoutLegend.txt");
		board.initialize();
		board.loadMiscConfigFiles("clueFiles/PeopleCards.txt", "clueFiles/WeaponsCards.txt");
	}
	
	
	@Test
	public void testAccusation() {
		//tests validating an accusation
		//correct accusation is setup and set to theAnswer variable
		//incorrect person, weapon, and room are created for testing
		//correct solution and 4 incorrect solutions are tested
		//setup for the test
		String person = "Colonel Mustard";
		String room = " Bathroom";
		String weapon = "Knife";
		Solution correct = new Solution(person, room, weapon);
		board.setTheAnswer(correct);//sets the answer to the correct solution
		String wrongPerson = "Miss Scarlet";
		String wrongRoom = "Study";
		String wrongWeapon = "Lead Pipe";
		//sets up incorrect solutions for testing
		Solution wrongPersonTest = new Solution (wrongPerson, room, weapon);
		Solution wrongRoomTest = new Solution (person, wrongRoom, weapon);
		Solution wrongWeaponTest = new Solution (person, room, wrongWeapon);
		Solution allWrongTest = new Solution (wrongPerson, wrongRoom, wrongWeapon);
		//tests the correct solution, different pieces of the solution being wrong
		//and tests all the answers being wrong
		assertTrue(board.checkAccusation(correct));
		assertFalse(board.checkAccusation(wrongPersonTest));
		assertFalse(board.checkAccusation(wrongRoomTest));
		assertFalse(board.checkAccusation(wrongWeaponTest));
		assertFalse(board.checkAccusation(allWrongTest));
	}
	
	@Test
	public void testRoomPickLocation() {
		//if list of targets includes room that was NOT recently visited, go to room
		//else pick randomly between choices (including just visited room)

		ComputerPlayer colonel = new ComputerPlayer("Colonel Mustard", 6, 16, "#FCDD14"); //location with 1 possible room with a roll of 1-4
		
		//test if room is in targets it picks the room every time
		board.doCalcTargets(colonel.getRow(), colonel.getColumn(), 2); 
		for (int i = 0; i < 100; i ++){ //run 100 times to ensure it picks the door every time
			BoardCell newloc = colonel.pickLocation(board.getTargets()); //pass in the targets calculated and pick location
			assertEquals(board.getCellAt(5, 15), newloc);
		}
		
		//test choice when there are 2 doors and 1 of them has been recently visited
		//should return second door every time
		ComputerPlayer professor = new ComputerPlayer("Professor Plumb", 6, 15, "#BF1D73"); //location with 2 possible rooms with a roll of 4
		board.doCalcTargets(professor.getRow(), professor.getColumn(), 5);
		professor.setLastRoom('L');
		for (int i = 0; i < 100; i ++){ //run 100 times to ensure it picks the second door every time
			BoardCell newloc = professor.pickLocation(board.getTargets()); //pick location and assert that is it always the door that was NOT recently visited
			assertEquals(board.getCellAt(9, 17), newloc);
		}
	}
	
	@Test
	public void testPreviousVisitPickLocation() {
		ComputerPlayer professor = new ComputerPlayer("Professor Plumb", 6, 15, "#BF1D73"); //location with 2 possible rooms with a roll of 4
		
		//test choice when there are 2 doors and 1 of them has been recently visited
		board.doCalcTargets(professor.getRow(), professor.getColumn(), 4);
		professor.setLastRoom('L');
		for (int i = 0; i < 100; i ++){ //run 100 times to ensure it picks the second door every time
			BoardCell newloc = professor.pickLocation(board.getTargets()); //pick location and assert that is it always the door that was NOT recently visited
			assertEquals(newloc, board.getCellAt(9, 17));
		}
	}
	
	@Test
	public void testRandomPickLocation() {
		ComputerPlayer white = new ComputerPlayer("Miss White", 6, 7, "#FFFFFF"); //location with 2 possible rooms with a roll of 4
		//test choice when the only door in the possibilities list is recently visited
		board.doCalcTargets(white.getRow(), white.getColumn(), 1);
		white.setLastRoom('L');
		int loc1 = 0;
		int loc2 = 0;
		int loc3 = 0;
		int loc4 = 0;
		for (int i = 0; i < 100; i ++){ //run 100 times to ensure it picks the door every time
			BoardCell loc = white.pickLocation(board.getTargets()); //pass in the targets calculated and pick location
			if (loc == board.getCellAt(5, 7))
				loc1++;
			else if (loc == board.getCellAt(6, 8))
				loc2++;
			else if (loc == board.getCellAt(6, 6))
				loc3++;
			else if (loc == board.getCellAt(7, 7))
				loc4++;
			else {
				fail("Invalid target selected");
			}
		}
		assertTrue(loc1 > 10);
		assertTrue(loc2 > 10);
		assertTrue(loc3 > 10);
		assertTrue(loc4 > 10);
	}

	// All disproveSuggestion tests will run twice, once involving a HumanPlayer
	@Test
	public void testDisproveSuggestion_1() {
		// Test that one player returns the only possible card
		Player player = new ComputerPlayer("Colonel Mustard", 14, 21, "#FCDD14");
		player.initializeCards();
		
		Card scarlet = new Card("Miss Scarlet", CardType.PERSON);
		Card knife = new Card("Knife", CardType.WEAPON);
		Card lib = new Card("Bathroom", CardType.ROOM);

		Solution suggestion = new Solution("Miss Scarlet", "Office", "Lead Pipe");
		
		player.addDealtCard(scarlet);
		player.addDealtCard(knife);
		player.addDealtCard(lib);

		Card out = player.disproveSuggestion(suggestion);
		assertNotNull(scarlet);
		assertEquals(scarlet, out);
		// Human version
		player = new HumanPlayer("Colonel Mustard", 14, 21, "#FCDD14");
		player.initializeCards();
		
		player.addDealtCard(scarlet);
		player.addDealtCard(knife);
		player.addDealtCard(lib);

		out = player.disproveSuggestion(suggestion);
		assertNotNull(scarlet);
		assertEquals(scarlet, out);
	}
	
	@Test
	public void testDisproveSuggestion_2() {
		// Test that one player randomly chooses between two possible cards
		Player player = new ComputerPlayer("Colonel Mustard", 14, 21, "#FCDD14");
		player.initializeCards();
		
		Card scarlet = new Card("Miss Scarlet", CardType.PERSON);
		Card knife = new Card("Knife", CardType.WEAPON);
		Card lib = new Card("Bathroom", CardType.ROOM);

		Solution suggestion = new Solution("Miss Scarlet", "Office", "Knife");
		
		player.addDealtCard(scarlet);
		player.addDealtCard(knife);
		player.addDealtCard(lib);

		int counterScarlet = 0;
		int counterKnife = 0;
		for(int i = 0; i < 200; i++) {
			Card out = player.disproveSuggestion(suggestion);
			if(out == scarlet) {
				counterScarlet++;
			}
			else if(out == knife) {
				counterKnife++;
			}
			else {
				fail("Unexpected card returned");
			}
		}
		assertTrue(counterScarlet > 0);
		assertTrue(counterKnife > 0);
		
		// Human version
		player = new HumanPlayer("Colonel Mustard", 14, 21, "#FCDD14");
		player.initializeCards();
		
		player.addDealtCard(scarlet);
		player.addDealtCard(knife);
		player.addDealtCard(lib);
		
		counterScarlet = 0;
		counterKnife = 0;
		for(int i = 0; i < 200; i++) {
			Card out = player.disproveSuggestion(suggestion);
			if(out == scarlet) {
				counterScarlet++;
			}
			else if(out == knife) {
				counterKnife++;
			}
			else {
				fail("Unexpected card returned");
			}
		}
		assertTrue(counterScarlet > 0);
		assertTrue(counterKnife > 0);
	}
	
	@Test
	public void testDisproveSuggestion_3() {
		// Test that players are queried in order
		// Starting by initializing the full list of players
		ArrayList<Player> players = board.getPotentialPlayers();
		// Each player on either 'side' will have a card to show,
		// we will test to see that only the next player's card
		// is shown. We also put a card in everyone else's hand
		// that will double check to make sure we don't test
		// anyone else's hand.
		Player pA = players.get(0);
		Player pB = players.get(1);	// will suggest
		Player pC = players.get(2);
		
		Card scarlet = board.getCardByName("Miss Scarlet");
		Card knife = board.getCardByName("Knife");
		Card lib = board.getCardByName("Bathroom");
		
		for(Player i : players) {
			i.initializeCards();
		}
		
		pA.addDealtCard(scarlet);
		pC.addDealtCard(lib);
		
		for(Player i : players) {
			if(i == pA || i == pC) {
				continue;
			}
			i.addDealtCard(knife);
		}
		
		Solution suggestion = new Solution("Miss Scarlet", "Bathroom", "Knife");
		
		BoardCell dummyCell = board.getCellAt(0, 0);
		Card testCard = board.handleSuggestion(players, suggestion, pB.getPlayerName(), dummyCell);
		if(testCard == null) {
			fail("Null card returned from handleSuggestion");
		}
		String cardName = testCard.getCardName();
		if(cardName == "Knife") {
			fail("Players are not queried in order");
		}
		if(cardName == "Miss Scarlet") {
			fail("Players are queried in reverse order, or in off-order");
		}
		assertEquals("Bathroom", cardName);
		// This test already includes a HumanPlayer by default
	}
	
	@Test
	public void testDisproveSuggestion_4() {
		// Test that the player whose turn it is does not return a card
		// Starting by initializing the full list of players
		ArrayList<Player> players = board.getPotentialPlayers();
		Player pA = players.get(4); // This will be our suggesting player
		
		Card scarlet = board.getCardByName("Miss Scarlet");
		
		for(Player i : players) {
			i.initializeCards();
		}
		
		pA.addDealtCard(scarlet);
		Solution suggestion = new Solution("Miss Scarlet", "Bathroom", "Knife");
		// Only the suggesting player has a card, and it is "Miss Scarlet".
		
		BoardCell dummyCell = board.getCellAt(0, 0);
		Card testCard = board.handleSuggestion(players, suggestion, pA.getPlayerName(), dummyCell);
		assertNull(testCard);
		// This test already includes a HumanPlayer by default
	}
	
	@Test
	public void testMakeSuggestion() {
		// Test cases:
		// Only one suggestion is possible, based on the seen cards
		// Random possibilities, testing multiple times to see that all are covered
		
		// Test case 1: Only one suggestion is possible, based on the seen cards
		ArrayList<Player> players = board.getPotentialPlayers();
		ComputerPlayer computer = (ComputerPlayer) players.get(1); // The second player is a ComputerPlayer
		computer.initializeCards();
		for(Card i : board.getCards()) {
			computer.addSeenCard(i);
		}
		computer.removeSeenCard(board.getCards().get(10));
		computer.removeSeenCard(board.getCards().get(18));
		BoardCell location = board.getCellAt(0, 22); // in the office, so the player must suggest the office
		assertEquals('O', location.getInitial());	// Make sure that the location is indeed the office
		Solution suggestion = computer.makeSuggestion(board, location);
		assertEquals("Office", suggestion.room);
		assertEquals("Colonel Mustard", suggestion.person);
		assertEquals("Revolver", suggestion.weapon);
		
		// Test case 2: Random possibilities, testing multiple times to see that all are covered
		for(Card i : board.getCards()) {
			computer.removeSeenCard(i);	// The computer has no seen cards, so they will guess
		}								// all possible weapon/person combinations
		HashMap<String, Boolean> possibilities = new HashMap<String, Boolean>();
		for(int i = 0; i < 4000; i++) {	// 36 iterations would be enough if the randomness was perfect
			Solution next = computer.makeSuggestion(board, location);
			assertEquals("Office", next.room);	// Testing the current room as only possibility
			possibilities.put(next.person, true);
			possibilities.put(next.weapon, true);
		}
		boolean containsAll = true;	// Will stay true if all suggestions are accounted for
		String failures = ""; // Updated for diagnosing failing tests
		for(Card i : board.getCards()) {
			if(i.getCardType() == CardType.ROOM) {
				continue;	// Ignore any room cards, the suggestion will
			}				// always be the office.
			if(!possibilities.containsKey(i.getCardName())) {
				containsAll = false;
				failures += " - " + i.getCardName();
			}
		}
		if(!containsAll) {
			fail("Computer does not suggest all possibilities. Failed at" + failures
					+ " Note: False positives are possible, test again.");
		}
	}
	
}
