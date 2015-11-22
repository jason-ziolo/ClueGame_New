package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoom;
	
	public ComputerPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}
	
	public void doTurn(Board board, int roll) {
		// For part II, WIP
		if(willAccuse(board.getSeenCards(), board.getCards())) {
			Solution acc = makeAccusation(board.getSeenCards(), board.getCards());
			ClueGame.playerAccusation(this.getPlayerName(), acc);
			ClueGame.endPlayerTurn();
		} 
		board.calcTargets(column, row, roll); // TODO: Figure out why arguments do not align
		BoardCell location = pickLocation(board.getTargets());
		if (location.isRoom())
			lastRoom = location.getInitial();
		move(location);
		board.repaint();
		// Does a suggestion if the location is a room
		if(location.isRoom()) {
			Solution sugg = makeSuggestion(board, location);
			ClueGame.playerSuggestion(this.getPlayerName(), sugg, location);
		}
		
		ClueGame.endPlayerTurn();
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		//check against recently visited.  
		//if room matches recently visited, run random choice instead of automatic room choice
		//unless there is another room. Then go to that room
		int rooms = 0;
		for (BoardCell i : targets){
			if (i.isRoom())
				rooms++;
		}
		if (rooms == 0) { // no rooms in range, pick a random square
			int size = targets.size(); //get size of targets
			int picked = new Random().nextInt(size); //get random number between 0 and size -1
			int i = 0;
			for (BoardCell cell: targets){
				if (i == picked) { //if i = the random number, return the cell
					return cell;
				}
				i++;
			}
		}
		else if (rooms == 1) {
			for (BoardCell cell: targets){
				if (cell.isRoom()){
					if (cell.getInitial() == lastRoom){ //its previously visited, random
						int size = targets.size(); //get size of targets
						int picked = new Random().nextInt(size); //get random number between 0 and size -1
						int j = 0;
						for (BoardCell bcell: targets){
							if (j == picked) //if i = the random number, return the cell
								return bcell;
							j++;
						}
					} 
					else { //its not a previously visited cell, go to it.
						return cell;
					}
				}
			}
		}
		else if (rooms >= 2) {
			ArrayList<BoardCell> removeFromTargets = new ArrayList<BoardCell>();
			for (BoardCell k: targets){
				if (k.getInitial() == lastRoom){
					removeFromTargets.add(k);
				}
			}
			targets.removeAll(removeFromTargets);
			for (BoardCell p: targets){
				if (p.isRoom()){
					return p;
				}
			}
			int picked = new Random().nextInt(targets.size());
			int i = 0;
			for (BoardCell bcell: targets){
				if (i == picked) //if i = the random number, return the cell
					return bcell;
				i++;
			}
		}
		return null;
	}
	
	public boolean willAccuse(Set<Card> seenCards, ArrayList<Card> allCards) {
		int roomNum = 0;
		int personNum = 0;
		int weaponNum = 0;
		Set<Card> allSeenCards = seenCards;	// The actual number of cards seen is the cards seen by all players
		allSeenCards.addAll(myCards);	// added to the cards held by the computer player
		for(Card i : allCards) {
			if (i.getCardType() == CardType.ROOM)
				roomNum++;
			else if (i.getCardType() == CardType.PERSON)
				personNum++;
			else if (i.getCardType() == CardType.WEAPON)
				weaponNum++;
		}
		for (Card i : allSeenCards) {
			if (i.getCardType() == CardType.ROOM)
				roomNum--;
			else if (i.getCardType() == CardType.PERSON)
				personNum--;
			else if (i.getCardType() == CardType.WEAPON)
				weaponNum--;
		}
		// If there is only 1 unknown person, weapon, and room; make an accusation
		if (roomNum == 1 && personNum == 1 && weaponNum == 1) {
			return true;
		}
		return false;
	}
	
	// This function is only called when we are sure that we can make an accusation
	public Solution makeAccusation(Set<Card> seenCards, ArrayList<Card> allCards) {
		String person = " ";
		String room = " ";
		String weapon = " ";
		Set<Card> allSeenCards = seenCards;	// The actual number of cards seen is the cards seen by all players
		allSeenCards.addAll(myCards);	// added to the cards held by the computer player
		
		Set<Card> guessCards = new HashSet<Card>(); 
		guessCards.addAll(allCards);
		
		// We know by calling this function that we can eliminate all possibilities except one for
		// each category of person, weapon, room.
		guessCards.removeAll(seenCards);
		guessCards.removeAll(myCards);
		for(Card i : guessCards) {
			if(i.getCardType() == CardType.PERSON)
				person = i.getCardName();
			else if(i.getCardType() == CardType.ROOM)
				room = i.getCardName();
			else if(i.getCardType() == CardType.WEAPON)
				weapon = i.getCardName();
		}
		Solution accusation = new Solution(person, room, weapon);
		return accusation;
		
	}
	
	public Solution makeSuggestion(Board board, BoardCell location) {
		// Every suggestion is made up of a room, a person, and a weapon
		String room, person, weapon = "";
		// The computer player will only guess a person card and weapon card
		// that they haven't seen.
		// The only possible room card is the one at the passed in location.
		room = Board.getRooms().get(location.getInitial());
		// Splitting up all possible cards into the person and weapon card lists
		ArrayList<Card> personCards = new ArrayList<Card>();
		ArrayList<Card> weaponCards = new ArrayList<Card>();
		
		// Make an ArrayList of all cards the computer has seen
		ArrayList<Card> computerSeenCards = (ArrayList<Card>) board.getCards().clone();
		computerSeenCards.addAll(myCards);
		
		for(Card i : computerSeenCards) {
			if(board.getSeenCards().contains(i)) {
				continue; // We are only interested in guessing cards that we have not seen
			}
			
			if(i.getCardType() == CardType.PERSON) {
				personCards.add(i);
			}
			else if(i.getCardType() == CardType.WEAPON) {
				weaponCards.add(i);
			}
		}
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		Card randomPersonCard, randomWeaponCard;
		if(personCards.size() > 1) {
			randomPersonCard = personCards.get(rand.nextInt(personCards.size()));
		}
		else {
			randomPersonCard = personCards.get(0);
		}
		if(weaponCards.size() > 1) {
			randomWeaponCard = weaponCards.get(rand.nextInt(weaponCards.size()));
		}
		else {
			randomWeaponCard = weaponCards.get(0);
		}
		person = randomPersonCard.getCardName();
		weapon = randomWeaponCard.getCardName();
		return new Solution(person, room, weapon);
	}
	
	public char getLastRoom() {
		return lastRoom;
	}
	
	public void setLastRoom(char lastRoom) {
		this.lastRoom = lastRoom;
	}

}
