package clueGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics;

public abstract class Player {
	private String playerName;
	protected int row;
	protected int column;
	private Color color;

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
		int recSize = ClueGame.getRecSize();
		int pcol = (this.getRow()) * recSize;  //if getCol is 1, pcolumn is 20.  2 = 40, 3 = 60
		int prow = (this.getColumn()) * recSize;
		g.setColor(this.getColor());
		g.fillOval(pcol, prow, recSize, recSize);
		g.setColor(Color.BLACK);
		g.drawOval(pcol, prow, recSize, recSize);
	}
	
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> match = new ArrayList<Card>();
		for(Card i : myCards) {
			if(i.getCardName().equals(suggestion.person) ||
					i.getCardName().equals(suggestion.room) ||
					i.getCardName().equals(suggestion.weapon)) {
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
	
	public void move(BoardCell cell){
		row = cell.getCol();
		column = cell.getRow();
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

	public void doTurn(Board board, int roll) {
		// TODO Auto-generated method stub
		
	}
}
