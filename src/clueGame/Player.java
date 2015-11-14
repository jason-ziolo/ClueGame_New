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
	
	public void initializeCards() {
		myCards = new HashSet<Card>();
	}
	
	public void draw(Graphics g){
		pcol = (this.getRow()) *ClueGame.REC_SIZE;  //if getCol is 1, pcolumn is 20.  2 = 40, 3 = 60
		prow = (this.getColumn()) *ClueGame.REC_SIZE;
		g.setColor(this.getColor());
		g.fillOval(pcol, prow, ClueGame.REC_SIZE, ClueGame.REC_SIZE);
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> match = new ArrayList<Card>();
		for(Card i : myCards) {
			if(i.getCardName().equals(suggestion.person) || i.getCardName().equals(suggestion.room) || i.getCardName().equals(suggestion.weapon)) {
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
	
	public ArrayList<Player> queryPlayersForDisproveSuggestion(){
		return null;
	}
	
	public void addDealtCard(Card in) {
		myCards.add(in);
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
	
	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", row=" + row + ", column=" + column + ", color=" + color + "]";
	}
}
