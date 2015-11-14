package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class HumanPlayer extends Player {
	private int pcol;
	private int prow;
	
	public HumanPlayer(String playerName, int row, int column, String color) {
		super(playerName, row, column, color);
	}

}
