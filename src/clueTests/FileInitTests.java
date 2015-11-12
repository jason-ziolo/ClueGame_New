package clueTests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class FileInitTests {
	
	// Constants to test if the file was loaded correctly
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 22;
	public static final int NUM_COLS = 23;
	
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		// Use default filenames in the Board class to load the board
		board = new Board();
		board.initialize();
	}
	
	@Test
	public void testRooms() {
	// rooms is static, see discussion in lab writeup
		Map<Character, String> rooms = Board.getRooms();
		// Ensure we read the correct number of rooms
		assertEquals(NUM_ROOMS, rooms.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Family Room", rooms.get('F'));
		assertEquals("Garage", rooms.get('G'));
		assertEquals("Bathroom", rooms.get('B'));
		assertEquals("Dining Room", rooms.get('D'));
		assertEquals("Walkway", rooms.get('W'));
		assertEquals("Kitchen", rooms.get('K'));
		assertEquals("Closet", rooms.get('X'));
		assertEquals("Office", rooms.get('O'));
		assertEquals("Attic", rooms.get('A'));
		assertEquals("Loft", rooms.get('L'));
		assertEquals("Piano Room", rooms.get('P'));
		
}

	@Test
	public void testBoardDimensions() {
	// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLS, board.getNumColumns());	
	}
	
	@Test
	public void FourDoorDirections() {
	// Test one each RIGHT/LEFT/UP/DOWN
		BoardCell room = board.getCellAt(4, 3);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(4, 8);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(2, 13);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(5, 15);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(4, 20);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(7, 20);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		room = board.getCellAt(9, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(10, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(10, 6);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(11, 6);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(13, 4);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(16, 1);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		room = board.getCellAt(17, 13);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(18, 13);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(19, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(0, 1);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(14, 15);
		assertFalse(cell.isDoorway());		
	}
	
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		assertEquals(506, totalCells);
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		assertEquals(15, numDoors);
	}
	
	@Test
	public void testRoomInitials() {
		assertEquals('F', board.getCellAt(0, 0).getInitial());
		assertEquals('P', board.getCellAt(2, 9).getInitial());
		assertEquals('L', board.getCellAt(3, 16).getInitial());
		assertEquals('O', board.getCellAt(0, 22).getInitial());
		assertEquals('B', board.getCellAt(9, 0).getInitial());
		assertEquals('A', board.getCellAt(10, 20).getInitial());
		assertEquals('K', board.getCellAt(16, 3).getInitial());
		assertEquals('D', board.getCellAt(21, 11).getInitial());
		assertEquals('G', board.getCellAt(21, 22).getInitial());
		
	}
	
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("clueFiles/ClueLayoutBadColumns.csv", "clueFiles/clueLayoutLegend.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("clueFiles/ClueLayoutBadRoom.csv", "clueFiles/clueLayoutLegend.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		Board board = new Board("clueFiles/clueLayout.csv", "clueFiles/ClueLegendBadFormat.txt");
		board.loadRoomConfig();
	}
	
}
