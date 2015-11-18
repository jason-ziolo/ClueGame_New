package clueGame;

import java.awt.Graphics;

public class HumanPlayer extends Player {
	public HumanPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}
	
	@Override
	public void doTurn(Board board, int roll){
		board.calcTargets(row, column, roll);
		board.highLightTargets();
	}

}
