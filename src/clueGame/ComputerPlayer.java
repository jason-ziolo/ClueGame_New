package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoom;
	private int pcol;
	private int prow;
	
	public ComputerPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		//check against recently visited.  
		//if room matches recently visited, run random choice instead of automatic room choice
		//unless there is another room. Then go to that room
		int rooms = 0;
		for (BoardCell i: targets){
			if (i.isRoom())
				rooms++;
		}
		if (rooms == 0){ //gonna be random
			int size = targets.size(); //get size of targets
			int picked = new Random().nextInt(size); //get random number between 0 and size -1
			int i = 0;
			for (BoardCell cell: targets){
				if (i == picked) //if i = the random number, return the cell
					return cell;
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
		}
		return null;
	}
	
	public Solution makeAccusation(Set<Card> seenCards, ArrayList<Card> allCards){
		String person = " ";
		String room = " ";
		String weapon = " ";
		Set<Card> computerSeenCards = new HashSet<Card>();
		computerSeenCards.equals(seenCards);

		//Add the computers held cards to their seen cards
		computerSeenCards.addAll(myCards);
		
		/* A check to see how many of each type of card has been seen. Not needed here, should be needed to get into the makeAccusation function
		int roomNum = 0;
		int personNum = 0;
		int weaponNum = 0;
		for (Card knownCards : computerSeenCards){
			if (knownCards.getCardType() == CardType.ROOM){
				roomNum++;
			} else if (knownCards.getCardType() == CardType.PERSON){
				personNum++;
			} else if (knownCards.getCardType() == CardType.WEAPON){
				weaponNum++;
			}
		}
		// If there is only 1 unknown person, weapon, and room; make an accusation
		//if (roomNum == rooms.size() - 1 && personNum == people.size() - 1 && weaponNum == weapons.size() - 1){
		*/

		Set<String> possibilitiesRoom = new HashSet<String>();
		Set<String> possibilitiesWeapon = new HashSet<String>();
		Set<String> possibilitiesPerson = new HashSet<String>();

		for (Card cards : allCards){
			if (cards.getCardType().equals(CardType.ROOM))
				possibilitiesRoom.add(cards.getCardName());
			else if (cards.getCardType().equals(CardType.WEAPON))
				possibilitiesWeapon.add(cards.getCardName());
			else if (cards.getCardType().equals(CardType.PERSON))
				possibilitiesPerson.add(cards.getCardName());
		}

		boolean remove = false;	//Boolean set to true if the computer knows the card has been seen
		for (Card knownCards : computerSeenCards){
			if (knownCards.getCardType() == CardType.ROOM){			//If the card type is a room
				for (String roomsH : possibilitiesRoom){			//Check all possible rooms
					if (roomsH.equals(knownCards.getCardName())){	//If the card checked is a possible room...
						remove = true;								//Set this true so the room is removed as a possibility
					}
				}
				if (remove){
					remove = false;
					possibilitiesRoom.remove(knownCards.getCardName());	//Remove the room as a possible accusation room
				}
			} else if (knownCards.getCardType() == CardType.PERSON){	//Same thing as the room check, but for people
				for (String peopleH : possibilitiesPerson){
					if (peopleH.equals(knownCards.getCardName())){
						remove = true;
					}
				}
				if (remove){
					remove = false;
					possibilitiesPerson.remove(knownCards.getCardName());
				}
			} else if (knownCards.getCardType() == CardType.WEAPON){	// Same thing as the two checks above
				for (String weaponsH : possibilitiesWeapon){
					if (weaponsH.equals(knownCards.getCardName())){
						remove = true;
					}
				}
				if (remove){
					remove = false;
					possibilitiesWeapon.remove(knownCards.getCardName());
				}
			}
		}
		
		for (String Person : possibilitiesPerson){
			person = Person;
		}
		for (String Room : possibilitiesRoom){
			room = Room;
		}
		for (String weaponsH : possibilitiesWeapon){
			weapon = weaponsH;
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
		for(Card i : board.getCards()) {
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
