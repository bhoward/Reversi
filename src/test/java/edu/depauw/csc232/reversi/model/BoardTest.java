package edu.depauw.csc232.reversi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;

import edu.depauw.csc232.reversi.Game;

/**
 * Demonstration of some tests of the Board model class. This tests the pieces
 * and available moves immediately before and after the first move of a game.
 * 
 * @author bhoward
 */
class BoardTest {

	@Test
	void testSize() {
		Board board = new Board();
		assertEquals(Game.BOARD_ROW_COUNT * Game.BOARD_COLUMN_COUNT, board.size());
	}

	private int computeIndex(int x, int y) {
		return y * Game.BOARD_COLUMN_COUNT + x;
	}

	@Test
	void testGet() {
		Board board = new Board();
		// All cells in an empty board should be unclaimed, and
		// Cell index should be y * BOARD_COLUMN_COUNT + x
		for (int x = 0; x < Game.BOARD_COLUMN_COUNT; x++) {
			for (int y = 0; y < Game.BOARD_ROW_COUNT; y++) {
				Cell cell = board.get(x, y);
				assertEquals(Player.UNKNOWN, cell.getOwner());
				assertEquals(computeIndex(x, y), cell.getIndex());
			}
		}
	}

	@Test
	void testStartGame() {
		int xMid = Game.BOARD_COLUMN_COUNT / 2;
		int yMid = Game.BOARD_ROW_COUNT / 2;

		Board board = new Board();
		board.startGame();

		for (int x = 0; x < Game.BOARD_COLUMN_COUNT; x++) {
			for (int y = 0; y < Game.BOARD_ROW_COUNT; y++) {
				Cell cell = board.get(x, y);
				if ((x == xMid && y == yMid) || (x == xMid - 1 && y == yMid - 1)) {
					assertEquals(Player.WHITE, cell.getOwner());
				} else if ((x == xMid && y == yMid - 1) || (x == xMid - 1 && y == yMid)) {
					assertEquals(Player.BLACK, cell.getOwner());
				} else {
					assertEquals(Player.UNKNOWN, cell.getOwner());
				}
			}
		}
	}

	@Test
	void testIsMovePermitted() {
		int xMid = Game.BOARD_COLUMN_COUNT / 2;
		int yMid = Game.BOARD_ROW_COUNT / 2;
		int UL = computeIndex(yMid - 1, xMid - 1);
		int UR = computeIndex(yMid - 1, xMid);
		int LL = computeIndex(yMid, xMid - 1);
		int LR = computeIndex(yMid, xMid);

		Board board = new Board();
		board.startGame();

		assertFalse(board.isMovePermitted(UL, Player.BLACK));
		assertFalse(board.isMovePermitted(UR, Player.BLACK));
		assertFalse(board.isMovePermitted(LL, Player.BLACK));
		assertFalse(board.isMovePermitted(LR, Player.BLACK));
		assertFalse(board.isMovePermitted(UL, Player.WHITE));
		assertFalse(board.isMovePermitted(UR, Player.WHITE));
		assertFalse(board.isMovePermitted(LL, Player.WHITE));
		assertFalse(board.isMovePermitted(LR, Player.WHITE));

		assertTrue(board.isMovePermitted(computeIndex(yMid - 2, xMid - 1), Player.BLACK));
		assertTrue(board.isMovePermitted(computeIndex(yMid - 1, xMid - 2), Player.BLACK));
		assertTrue(board.isMovePermitted(computeIndex(yMid, xMid + 1), Player.BLACK));
		assertTrue(board.isMovePermitted(computeIndex(yMid + 1, xMid), Player.BLACK));

		assertTrue(board.isMovePermitted(computeIndex(yMid - 2, xMid), Player.WHITE));
		assertTrue(board.isMovePermitted(computeIndex(yMid - 1, xMid + 1), Player.WHITE));
		assertTrue(board.isMovePermitted(computeIndex(yMid, xMid - 2), Player.WHITE));
		assertTrue(board.isMovePermitted(computeIndex(yMid + 1, xMid - 1), Player.WHITE));
	}

	@Test
	void testGetNextMoves() {
		int xMid = Game.BOARD_COLUMN_COUNT / 2;
		int yMid = Game.BOARD_ROW_COUNT / 2;

		Board board = new Board();
		board.startGame();

		Collection<Cell> moves = board.getNextMoves(Player.BLACK);
		assertEquals(4, moves.size());

		for (Cell cell : moves) {
			int x = cell.getIndex() % Game.BOARD_COLUMN_COUNT;
			int y = cell.getIndex() / Game.BOARD_COLUMN_COUNT;

			if (x == xMid - 2) {
				assertEquals(yMid - 1, y);
			} else if (x == xMid - 1) {
				assertEquals(yMid - 2, y);
			} else if (x == xMid) {
				assertEquals(yMid + 1, y);
			} else if (x == xMid + 1) {
				assertEquals(yMid, y);
			} else {
				fail("Unexpected move");
			}
		}
	}

	@Test
	void testTakeCell() {
		int xMid = Game.BOARD_COLUMN_COUNT / 2;
		int yMid = Game.BOARD_ROW_COUNT / 2;

		Board board = new Board();
		board.startGame();

		board.takeCell(computeIndex(xMid, yMid + 1), Player.BLACK);

		for (int x = 0; x < Game.BOARD_COLUMN_COUNT; x++) {
			for (int y = 0; y < Game.BOARD_ROW_COUNT; y++) {
				Cell cell = board.get(x, y);
				if (x == xMid - 1 && y == yMid - 1) {
					assertEquals(Player.WHITE, cell.getOwner());
				} else if ((x == xMid && y == yMid - 1) || (x == xMid - 1 && y == yMid)
						|| (x == xMid && y == yMid) || (x == xMid && y == yMid + 1)) {
					assertEquals(Player.BLACK, cell.getOwner());
				} else {
					assertEquals(Player.UNKNOWN, cell.getOwner());
				}
			}
		}
	}

}
