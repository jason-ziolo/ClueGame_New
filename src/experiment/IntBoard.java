package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	
	private Map<BoardCell, LinkedList<BoardCell>> adjMatrix;
	private Set<BoardCell> visited;
	private Set<BoardCell> targets;
	private BoardCell[][] grid;
	
	public IntBoard(int rows, int cols) {
		grid = new BoardCell[rows][cols];
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
				grid[i][j] = new BoardCell(i, j);
			}
		}
		
		adjMatrix = new HashMap<BoardCell, LinkedList<BoardCell>>();
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		calcAdjacencies();
		
	}
	
	public void calcAdjacencies() {
		
		Set tempSet;
		
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[i].length; j++) {
				tempSet = new HashSet<BoardCell>();
				if (i > 0) tempSet.add(grid[i-1][j]);
				if (j > 0) tempSet.add(grid[i][j-1]);
				if (i < grid.length - 1) tempSet.add(grid[i+1][j]);
				if (j < grid[i].length - 1) tempSet.add(grid[i][j+1]);
				adjMatrix.put(grid[i][j], new LinkedList<BoardCell>(tempSet));
				
			}
		}
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		
		// Find all adjacent cells that have not been visited
		LinkedList<BoardCell> tempCells = new LinkedList<BoardCell>(getAdjList(startCell));
		LinkedList<BoardCell> adjacentCells = new LinkedList<BoardCell>();
		for (BoardCell tempCell : tempCells) {
			if (!visited.contains(tempCell)) {
				adjacentCells.push(tempCell);
			}
		}
		
		// Recursive call for each of these cells
		for (BoardCell cell : adjacentCells) {
			visited.add(cell);
			if (pathLength == 1) {
				targets.add(cell);
			} else {
				calcTargets(cell, pathLength - 1);
			}
			visited.remove(cell);
		}
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public LinkedList<BoardCell> getAdjList(BoardCell cell) {
		return adjMatrix.get(cell);
	}
	
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
}
