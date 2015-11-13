package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
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
	
	@Override
	public void draw(Graphics g){
		pcol = (this.getRow()) * ClueGame.REC_SIZE;  //if getCol is 1, pcolumn is 20.  2 = 40, 3 = 60
		prow = (this.getColumn()) * ClueGame.REC_SIZE;
		g.setColor(this.getColor());
		g.fillOval(pcol, prow, ClueGame.REC_SIZE - 1, ClueGame.REC_SIZE-1);
	}
	
	public void makeAccusation() {
		// TODO
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
			if(seenCards.contains(i)) {
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
