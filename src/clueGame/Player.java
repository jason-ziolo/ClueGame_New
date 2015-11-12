package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Player {
	private String playerName;
	private int row;
	private int column;
	private Color color;
	private int pcol;
	private int prow;

	public Player(String playerName, int row, int column, String stringColor) {
		super();
		this.playerName = playerName;
		this.row = row;
		this.column = column;
		this.color = Color.decode(stringColor);
	}

	protected Set<Card> myCards;
	protected Set<Card> seenCards;
	
	public void initializeCards() {
		myCards = new HashSet<Card>();
		seenCards = new HashSet<Card>();
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> match = new ArrayList<Card>();
		for(Card i : myCards) {
			if(i.getCardName() == suggestion.person || i.getCardName() == suggestion.room || i.getCardName() == suggestion.weapon) {
				match.add(i);
			}
		}
		if(match.size() > 0) {
			Random rand = new Random();
			rand.setSeed(System.nanoTime());
			return match.get(rand.nextInt(match.size()));
		}
		return null;
	}
	
	public void draw(Graphics g){ 
		//make the children do it
	}
	
	public ArrayList<Player> queryPlayersForDisproveSuggestion(){
		return null;
	}
	
	public void addDealtCard(Card in) {
		myCards.add(in);
		seenCards.add(in);
	}
	
	public void addSeenCard(Card in) {
		seenCards.add(in);
	}
	
	//all getters for testing purposes only
	public String getPlayerName() {
		return playerName;
	}

	public Set<Card> getCards() {
		return myCards;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Color getColor() {
		return color;
	}
	
	//testing purposes only
	public void removeSeenCard(Card out) {
		seenCards.remove(out);
	}
	
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", row=" + row + ", column=" + column + ", color=" + color + "]";
	}
}