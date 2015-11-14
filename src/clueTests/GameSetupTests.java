package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class GameSetupTests {
	
	private static Board board;
	
	@BeforeClass  
	public static void setUp() throws FileNotFoundException {
		board = new Board("clueFiles/clueLayout.csv", "clueFiles/clueLayoutLegend.txt");
		board.initialize();
	}
	

	@Test
	public void testLoadingPlayers() {
		//tests loading of players.  
		//checks first and last computer players and human player (Miss Scarlet)
		//checks name, color, and location (row and column)
	
		// The ArrayList stores Player objects, so that the
		// HumanPlayer and ComputerPlayer objects can all be stored together
		ArrayList<Player> potentialPlayers = new ArrayList<Player>(); 
		potentialPlayers = board.getPotentialPlayers();
		assertEquals(6, potentialPlayers.size()); //6 potential players, 5 computer and 1 human
		
		//create correct local player objects to test against ones read in from file
		ComputerPlayer colonel = new ComputerPlayer("Colonel Mustard", 14, 21, "#FF9900");
		ComputerPlayer professor = new ComputerPlayer("Professor Plum", 0, 12, "#BF1D73");
		HumanPlayer scarlet = new HumanPlayer("Miss Scarlet", 5, 1, "#EB0505");
		
		//all 3 blocks of tests are the same, just for different players
		//Colonel Mustard tests
		assertEquals(colonel.getPlayerName(), potentialPlayers.get(1).getPlayerName()); //tests name
		assertEquals(colonel.getRow(), potentialPlayers.get(1).getRow()); 				//tests row
		assertEquals(colonel.getColumn(), potentialPlayers.get(1).getColumn()); 		//tests column
		assertEquals(colonel.getColor(), potentialPlayers.get(1).getColor()); 			//tests color
		//Professor Plumb tests
		assertEquals(professor.getPlayerName(), potentialPlayers.get(5).getPlayerName());
		assertEquals(professor.getRow(), potentialPlayers.get(5).getRow());
		assertEquals(professor.getColumn(), potentialPlayers.get(5).getColumn());
		assertEquals(professor.getColor(), potentialPlayers.get(5).getColor());
		//Miss Scarlet tests
		assertEquals(scarlet.getPlayerName(), potentialPlayers.get(0).getPlayerName());
		assertEquals(scarlet.getRow(), potentialPlayers.get(0).getRow());
		assertEquals(scarlet.getColumn(), potentialPlayers.get(0).getColumn());
		assertEquals(scarlet.getColor(), potentialPlayers.get(0).getColor());
	}
	
	@Test
	public void testAllCardsLoaded() {
		// There are three card types:
		// PERSON: There are 6 of these
		// WEAPON: There are 6 of these
		// ROOM: There are 9 of these (that are cards)
		// So there should be a total of 21 cards
		
		ArrayList<Card> cards = board.getCards();
		
		int pCount = 0; // PERSON card counter
		int wCount = 0; // WEAPON card counter
		int rCount = 0; // ROOM card counter
		for(Card card : cards) {
			if(card.getCardType() == CardType.PERSON) {
				pCount++;
			}
			else if(card.getCardType() == CardType.WEAPON) {
				wCount++;
			}
			else if(card.getCardType() == CardType.ROOM) {
				rCount++;
			}
			else {	// This will only occur if more CardTypes are added
					// without appropriate changes
				fail("Unexpected CardType");
			}
		}
		// Individual card counts:
		assertEquals(6, pCount);
		assertEquals(6, wCount);
		assertEquals(9, rCount);
		// Total count should be 21:
		assertEquals(21, pCount + wCount + rCount);
		assertEquals(21, cards.size());
	}
	
	@Test
	public void testDeal() {
		board.selectAnswer(); // This will prevent 3 cards from being dealt out
		// We will assume that all players are being used (the players list will
		// include all potential players)
		ArrayList<Player> players = board.getPotentialPlayers();
		board.dealCards(players);
		ArrayList<Card> cards = board.getCards();
		// Test that all (non-answer) cards have been dealt
		// Meanwhile, test that no card has been dealt to two different players
		// To start, remove all cards that will not be dealt from the set of all
		// of the cards that we will test.
		Solution theAnswer = board.getTheAnswer();
		Set<String> forbidden = new HashSet<String>();
		forbidden.add(theAnswer.person);
		forbidden.add(theAnswer.weapon);
		forbidden.add(theAnswer.room);
		for(Card i : cards) {
			if(forbidden.contains(i.getCardName())) {
				continue;	// Go on to the next card if the card is part of the answer
			}
			boolean cardDealt = false;
			for(Player j : players) {
				HashSet<Card> playerCards = (HashSet<Card>) j.getCards();
				if(playerCards.contains(i)) {
					if(cardDealt) {	// The card must not be redealt
						fail("Card dealt to multiple players");
					}
					else {
						cardDealt = true;
					}
				}
			}
			assertTrue(cardDealt);
		}
		// Test that all players have roughly the same number of cards
		// A 1 card difference should be the maximum acceptable difference
		// between the minimum and maximum number of cards
		int minCount = 0;	// The minimum and maximum card counts will be
		int maxCount = 0;	// tracked and compared later in this test
		boolean getInitialMin = true;
		for(Player i : players) {
			int cardCount = i.getCards().size();
			if(getInitialMin) {
				minCount = cardCount;
				getInitialMin = false;
			}
			if(minCount > cardCount) {
				minCount = cardCount;
			}
			if(maxCount < cardCount) {
				maxCount = cardCount;
			}
		}
		assertTrue(1 >= Math.abs(maxCount - minCount));
	}
	
	
}
