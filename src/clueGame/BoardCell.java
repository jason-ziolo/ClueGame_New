package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JTextArea;

public class BoardCell {
	public static Color C_WALKWAY_FILL = Color.YELLOW;
	public static Color C_WALKWAY_BORDER = Color.BLACK;
	public static Color C_ROOM = Color.DARK_GRAY;

	private int row;
	private int column;
	private char initial;
	private boolean nameOutput = false;

	private int prow;
	private int pcol;

	DoorDirection doorDirection;

	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	public boolean isNameOutput() {
		return nameOutput;
	}

	public void setNameOutput(boolean nameOutput) {
		this.nameOutput = nameOutput;
	}

	public void draw(Graphics g){

		pcol = (this.getRow()) *ClueGame.REC_SIZE;  
		prow = (this.getCol()) *ClueGame.REC_SIZE;
		if (this.getInitial() == 'W') {		
			g.setColor(C_WALKWAY_FILL);										// walkway fill
			g.fillRect(prow, pcol, ClueGame.REC_SIZE, ClueGame.REC_SIZE);	// draws and fills the box
			g.setColor(C_WALKWAY_BORDER);  									// walkway border
			g.drawRect(prow, pcol, ClueGame.REC_SIZE, ClueGame.REC_SIZE);	// walkway border
		}
		else {
			g.setColor(C_ROOM); 									// room color
			g.fillRect(prow, pcol, ClueGame.REC_SIZE, ClueGame.REC_SIZE);	//draws and fills the box
		}
	}

	public void drawOver(Graphics g){
		pcol = (this.getRow()) *ClueGame.REC_SIZE;
		prow = (this.getCol()) *ClueGame.REC_SIZE;
		if (nameOutput){
			g.setFont(new Font("Times New Roman", Font.BOLD, 15));
			g.setColor(Color.white);
			g.drawString(Board.getRoom(this.getInitial()), prow, pcol);
		}
		if(this.isDoorway()){
			g.setColor(Color.black);
			if(this.getDoorDirection() == DoorDirection.DOWN)
				g.fillRect(prow, pcol + ClueGame.REC_SIZE *3/4 + 1, ClueGame.REC_SIZE, ClueGame.REC_SIZE/4);
			else if (this.getDoorDirection() == DoorDirection.UP)
				g.fillRect(prow, pcol - ClueGame.REC_SIZE * (3/4) + 1, ClueGame.REC_SIZE, ClueGame.REC_SIZE/4);
			else if (this.getDoorDirection() == DoorDirection.RIGHT){
				prow = prow+ClueGame.REC_SIZE*3/4+1; //java was being rude about me putting it in the next line for some reason
				g.fillRect(prow, pcol, ClueGame.REC_SIZE/4, ClueGame.REC_SIZE);
			}
			else if (this.getDoorDirection() == DoorDirection.LEFT)
				g.fillRect(prow, pcol + ClueGame.REC_SIZE * (3/4) + 1, ClueGame.REC_SIZE/4, ClueGame.REC_SIZE);
		}
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return column;
	}

	public boolean isWalkway() {
		return (initial == 'W');
	}

	public boolean isRoom() {
		return (initial != 'W' && initial != 'X');
	}

	public boolean isDoorway() {
		return (doorDirection != DoorDirection.NONE);
	}

	public char getInitial() {
		return initial;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int col) {
		column = col;
	}

	public void setDoorDirection(char direction) {
		if (direction == 'L') doorDirection = DoorDirection.LEFT;
		else if (direction == 'R') doorDirection = DoorDirection.RIGHT;
		else if (direction == 'U') doorDirection = DoorDirection.UP;
		else if (direction == 'N') doorDirection = DoorDirection.NONE;
		else doorDirection = DoorDirection.DOWN;
	}

	//for texting purposes only
	@Override
	public String toString() {
		return "BoardCell [row=" + row + ", column=" + column + ", initial=" + initial + ", doorDirection="
				+ doorDirection + "]\n";
	}

}
