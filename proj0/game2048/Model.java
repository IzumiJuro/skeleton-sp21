package game2048;

import java.util.Formatter;
import java.util.Observable;


/**
 * The state of a game of 2048.
 *
 * @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
	/**
	 * Largest piece value.
	 */
	public static final int MAX_PIECE = 2048;
	/**
	 * Current contents of the board.
	 */
	private Board board;
	/**
	 * Current score.
	 */
	private int score;
	/**
	 * Maximum score so far.  Updated when game ends.
	 */
	private int maxScore;
	
	/* Coordinate System: column C, row R of the board (where row 0,
	 * column 0 is the lower-left corner of the board) will correspond
	 * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
	 */
	/**
	 * True iff game is ended.
	 */
	private boolean gameOver;
	
	/**
	 * A new 2048 game on a board of size SIZE with no pieces
	 * and score 0.
	 */
	public Model(int size) {
		board = new Board(size);
		score = maxScore = 0;
		gameOver = false;
	}
	
	/**
	 * A new 2048 game where RAWVALUES contain the values of the tiles
	 * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
	 * to the bottom-left corner. Used for testing purposes.
	 */
	public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
		int size = rawValues.length;
		board = new Board(rawValues, score);
		this.score = score;
		this.maxScore = maxScore;
		this.gameOver = gameOver;
	}
	
	/**
	 * Determine whether game is over.
	 */
	private static boolean checkGameOver(Board b) {
		return maxTileExists(b) || !atLeastOneMoveExists(b);
	}
	
	/**
	 * Returns true if at least one space on the Board is empty.
	 * Empty spaces are stored as null.
	 */
	public static boolean emptySpaceExists(Board b) {
		// TODO: Fill in this function.
		int boundary = b.size();
		for (int COL = 0; COL < boundary; COL++) {
			for (int ROW = 0; ROW < boundary; ROW++) {
				Tile tile = b.tile(COL, ROW);
				if (tile == null) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns true if any tile is equal to the maximum valid value.
	 * Maximum valid value is given by MAX_PIECE. Note that
	 * given a Tile object t, we get its value with t.value().
	 */
	public static boolean maxTileExists(Board b) {
		// TODO: Fill in this function.
		int boundary = b.size();
		for (int COL = 0; COL < boundary; COL++) {
			for (int ROW = 0; ROW < boundary; ROW++) {
				Tile tile = b.tile(COL, ROW);
				if (tile == null) {
					continue;
				}
				if (tile.value() == MAX_PIECE) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns true if there are any valid moves on the board.
	 * There are two ways that there can be valid moves:
	 * 1. There is at least one empty space on the board.
	 * 2. There are two adjacent tiles with the same value.
	 */
	public static boolean atLeastOneMoveExists(Board b) {
		// TODO: Fill in this function.
//		int boundary = b.size();
//		for (int COL = 0; COL < boundary; COL++) {
//			for (int ROW = 0; ROW < boundary; ROW++) {
//				Tile tile = b.tile(ROW, ROW);
//				if (tile == null) {
//					return true;
//				}
//				if (sameValueAtAdjacentTile(b, tile)) {
//					return true;
//				}
//			}
//		}
//		return false;
		boolean exist = false;
		exist = emptySpaceExists(b) ||
				Model.mergeable(b);
		
		return exist;
	}
	
	public static boolean sameValueAtAdjacentTile(Board b, Tile t) {
		int row = t.row();
		int col = t.col();
		
		if (col + 1 < b.size()) {
			Tile right = b.tile(col + 1, row);
			if (right.value() == t.value()) {
				return true;
			}
		}
		if (col - 1 > 0) {
			Tile left = b.tile(col - 1, row);
			if (left.value() == t.value()) {
				return true;
			}
		}
		if (row + 1 < b.size()) {
			Tile up = b.tile(col, row + 1);
			if (up.value() == t.value()) {
				return true;
			}
		}
		if (row - 1 > 0) {
			Tile down = b.tile(col, row - 1);
			if (down.value() == t.value()) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean validateMerge(Tile checkTile, Tile adjacentTile) {
		return checkTile != null && adjacentTile != null && checkTile.value() == adjacentTile.value();
	}
	
	private static boolean mergeable(Board b) {
		int boundary = b.size();
		Tile checkTile, adjacentTile;
		
		b.setViewingPerspective(Side.NORTH);
		for (int c = 0; c < boundary; ++c) {
			for (int r = 0; r < boundary - 1; ++r) {
				checkTile = b.tile(c, r);
				adjacentTile = b.tile(c, r + 1);
				if (validateMerge(checkTile, adjacentTile)) {
					return true;
				}
			}
		}
		b.setViewingPerspective(Side.EAST);
		for (int c = 0; c < boundary; ++c) {
			for (int r = 0; r < boundary - 1; ++r) {
				checkTile = b.tile(c, r);
				adjacentTile = b.tile(c, r + 1);
				if (validateMerge(checkTile, adjacentTile)) {
					b.setViewingPerspective(Side.NORTH);
					return true;
				}
			}
		}
		b.setViewingPerspective(Side.WEST);
		for (int c = 0; c < boundary; ++c) {
			for (int r = 0; r < boundary - 1; ++r) {
				checkTile = b.tile(c, r);
				adjacentTile = b.tile(c, r + 1);
				if (validateMerge(checkTile, adjacentTile)) {
					b.setViewingPerspective(Side.NORTH);
					return true;
				}
			}
		}
		b.setViewingPerspective(Side.SOUTH);
		for (int c = 0; c < boundary; ++c) {
			for (int r = 0; r < boundary - 1; ++r) {
				checkTile = b.tile(c, r);
				adjacentTile = b.tile(c, r + 1);
				if (validateMerge(checkTile, adjacentTile)) {
					b.setViewingPerspective(Side.NORTH);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
	 * 0 <= COL < size(). Returns null if there is no tile there.
	 * Used for testing. Should be deprecated and removed.
	 */
	public Tile tile(int col, int row) {
		return board.tile(col, row);
	}
	
	/**
	 * Return the number of squares on one side of the board.
	 * Used for testing. Should be deprecated and removed.
	 */
	public int size() {
		return board.size();
	}
	
	/**
	 * Return true iff the game is over (there are no moves, or
	 * there is a tile with value 2048 on the board).
	 */
	public boolean gameOver() {
		checkGameOver();
		if (gameOver) {
			maxScore = Math.max(score, maxScore);
		}
		return gameOver;
	}
	
	/**
	 * Return the current score.
	 */
	public int score() {
		return score;
	}
	
	/**
	 * Return the current maximum game score (updated at end of game).
	 */
	public int maxScore() {
		return maxScore;
	}
	
	/**
	 * Clear the board to empty and reset the score.
	 */
	public void clear() {
		score = 0;
		gameOver = false;
		board.clear();
		setChanged();
	}
	
	/**
	 * Add TILE to the board. There must be no Tile currently at the
	 * same position.
	 */
	public void addTile(Tile tile) {
		board.addTile(tile);
		checkGameOver();
		setChanged();
	}
	
	/**
	 * Tilt the board toward SIDE. Return true iff this changes the board.
	 * <p>
	 * 1. If two Tile objects are adjacent in the direction of motion and have
	 * the same value, they are merged into one Tile of twice the original
	 * value and that new value is added to the score instance variable
	 * 2. A tile that is the result of a merge will not merge again on that
	 * tilt. So each move, every tile will only ever be part of at most one
	 * merge (perhaps zero).
	 * 3. When three adjacent tiles in the direction of motion have the same
	 * value, then the leading two tiles in the direction of motion merge,
	 * and the trailing tile does not.
	 */
	public boolean tilt(Side side) {
		boolean changed;
		changed = false;
		
		// TODO: Modify this.board (and perhaps this.score) to account
		// for the tilt to the Side SIDE. If the board changed, set the
		// changed local variable to true.
		this.board.setViewingPerspective(side);
		changed = tileWithoutMerge() | merge();
		tileWithoutMerge();
		this.board.setViewingPerspective(Side.NORTH);
		checkGameOver();
		if (changed) {
			setChanged();
		}
		return changed;
	}
	
	private boolean tileWithoutMerge() {
		boolean moved = false;
		for (int col = 0; col < this.board.size(); col++) {
			int boundary = this.board.size() - 1;
			for (int row = boundary; row >= 0 ; row--) {
				Tile currentTile = this.board.tile(col, row);
				if (currentTile != null) {
					this.board.move(col, boundary, currentTile);
					if (boundary != row) {
						moved = true;
					}
					boundary --;
				}
			}
		}
		return moved;
	}
	
	private boolean merge() {
		boolean merged = false;
		int boundary = this.board.size() - 1;
		Tile checkTile, adjacentTile;
		
		for (int col = 0; col < boundary; col++) {
			int mergeBoundary = boundary;
			for (int row = boundary - 1; row >= 0; row--) {
				checkTile = this.board.tile(col, row);
				adjacentTile = this.board.tile(col, row + 1);
				if (row + 1 <= mergeBoundary && validateMerge(checkTile, adjacentTile)) {
					this.board.move(col, row + 1, checkTile);
					mergeBoundary -= 2;
					this.score += checkTile.value() * 2;
					merged = true;
				}
			}
		}
		return merged;
	}
	
	/**
	 * Checks if the game is over and sets the gameOver variable
	 * appropriately.
	 */
	private void checkGameOver() {
		gameOver = checkGameOver(board);
	}
	
	@Override
	/** Returns the model as a string, used for debugging. */
	public String toString() {
		Formatter out = new Formatter();
		out.format("%n[%n");
		for (int row = size() - 1; row >= 0; row -= 1) {
			for (int col = 0; col < size(); col += 1) {
				if (tile(col, row) == null) {
					out.format("|    ");
				} else {
					out.format("|%4d", tile(col, row).value());
				}
			}
			out.format("|%n");
		}
		String over = gameOver() ? "over" : "not over";
		out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
		return out.toString();
	}
	
	@Override
	/** Returns whether two models are equal. */
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (getClass() != o.getClass()) {
			return false;
		} else {
			return toString().equals(o.toString());
		}
	}
	
	@Override
	/** Returns hash code of Model’s string. */
	public int hashCode() {
		return toString().hashCode();
	}
}
