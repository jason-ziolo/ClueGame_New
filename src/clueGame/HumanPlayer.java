package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class HumanPlayer extends Player {
	private int pcol;
	private int prow;
	
	public HumanPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}
	
	@Override
	public void draw(Graphics g){
		pcol = (this.getRow()) *ClueGame.REC_SIZE;  //if getCol is 1, pcolumn is 20.  2 = 40, 3 = 60
		prow = (this.getColumn()) *ClueGame.REC_SIZE;
		g.setColor(this.getColor());
		g.fillOval(pcol, prow, ClueGame.REC_SIZE, ClueGame.REC_SIZE);
	}
}
