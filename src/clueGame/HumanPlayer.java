package clueGame;

import java.awt.Graphics;

public class HumanPlayer extends Player {
	private int pcol;
	private int prow;
	
	public HumanPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}
	
	@Override
	public void doTurn(Board board, int roll){
		board.calcTargets(column, row, roll);
		board.highLightTargets();
	}

}
