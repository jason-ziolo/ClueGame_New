package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class BoardCell {
	public static Color C_WALKWAY_FILL = Color.decode("#FFF2E5");
	public static Color C_WALKWAY_BORDER = Color.BLACK;
	public static Color C_ROOM = Color.DARK_GRAY;
	public static Color C_ROOM_TEXT = Color.WHITE;
	public static Color C_DOOR = Color.GRAY;
	public static Font F_ROOM = new Font("Times New Roman", Font.BOLD, 15);
	public static int DOOR_PADDING = 1;
	
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

	public void draw(Graphics g) {
		int recSize = ClueGame.getRecSize();
		pcol = this.getRow() * recSize;  
		prow = this.getCol() * recSize;
		if (this.getInitial() == 'W') {		
			g.setColor(C_WALKWAY_FILL);										// walkway fill
			g.fillRect(prow, pcol, recSize, recSize);	// draws and fills the box
			g.setColor(C_WALKWAY_BORDER);  									// walkway border
			g.drawRect(prow, pcol, recSize, recSize);	// walkway border
		}
		else {
			g.setColor(C_ROOM); 									// room color
			g.fillRect(prow, pcol, recSize, recSize);	//draws and fills the box
		}
	}

	public void drawOver(Graphics g){
		int recSize = ClueGame.getRecSize();
		pcol = (this.getRow()) * recSize;
		prow = (this.getCol()) * recSize;
		if (nameOutput){
			g.setFont(F_ROOM);
			g.setColor(C_ROOM_TEXT);
			g.drawString(Board.getRoom(this.getInitial()), prow, pcol);
		}
		if(this.isDoorway()){
			g.setColor(C_DOOR);
			if(this.getDoorDirection() == DoorDirection.DOWN)
				g.fillRect(prow, pcol + recSize * 3 / 4 + DOOR_PADDING, recSize, recSize / 4);
			else if (this.getDoorDirection() == DoorDirection.UP)
				g.fillRect(prow, pcol + DOOR_PADDING, recSize, recSize / 4);
			else if (this.getDoorDirection() == DoorDirection.RIGHT)
				g.fillRect(prow + recSize * 3 / 4 + DOOR_PADDING, pcol, recSize / 4, recSize);
			else
				g.fillRect(prow + DOOR_PADDING, pcol, recSize / 4, recSize);
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

	//for testing purposes only
	@Override
	public String toString() {
		return "BoardCell [row=" + row + ", column=" + column + ", initial=" + initial + ", doorDirection="
				+ doorDirection + "]\n";
	}

}
