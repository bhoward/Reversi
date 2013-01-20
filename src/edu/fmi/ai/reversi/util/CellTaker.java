package edu.fmi.ai.reversi.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.fmi.ai.reversi.Game;
import edu.fmi.ai.reversi.model.Board;
import edu.fmi.ai.reversi.model.Cell;
import edu.fmi.ai.reversi.model.Player;
import edu.fmi.ai.reversi.move.MoveChecker;

public class CellTaker {

	private final MoveChecker checker;

	private final Board board;

	public CellTaker(final MoveChecker checker, final Board board) {
		this.checker = checker;
		this.board = board;
	}

	private Collection<Cell> takeSurroundedCells(final Cell cell, final Player player) {
		final Set<Cell> takenCells = new LinkedHashSet<Cell>();

		takenCells.add(cell);
		takenCells.addAll(takeHorizontalCells(cell, player));
		takenCells.addAll(takeVerticalCells(cell, player));
		takenCells.addAll(takeDiagonalCells(cell, player));

		for (final Cell takenCell : takenCells) {
			takenCell.take(player);
		}

		return takenCells;
	}

	public Collection<Cell> takeCell(final int cellIndex, final Player player) {
		return takeSurroundedCells(board.get(cellIndex), player);
	}

	private Collection<Cell> takeVerticalCells(final Cell cell, final Player player) {
		final Set<Cell> result = new HashSet<Cell>();
		result.addAll(takeTopCells(cell, player));
		result.addAll(takeBottomCells(cell, player));
		return result;
	}

	private Collection<Cell> takeHorizontalCells(final Cell cell, final Player player) {
		final Set<Cell> result = new HashSet<Cell>();
		result.addAll(takeLeftCells(cell, player));
		result.addAll(takeRightCells(cell, player));
		return result;
	}

	private Collection<Cell> takeDiagonalCells(final Cell cell, final Player player) {
		final Set<Cell> result = new HashSet<Cell>();
		result.addAll(takeMainTopCells(cell, player));
		result.addAll(takeMainBottomCells(cell, player));
		result.addAll(takeSecondaryTopCells(cell, player));
		result.addAll(takeSecondaryBottomCells(cell, player));
		return result;
	}

	private Collection<Cell> takeSecondaryTopCells(final Cell cell, final Player player) {
		int secondaryTopIndex = checker.getNeighbourIndex(Direction.SECONDARY_DIAGONAL_TOP, cell,
				player);
		return takeCells(secondaryTopIndex, secondaryTopIndex, cell.getIndex(),
				Game.BOARD_COLUMN_COUNT - 1, player);
	}

	private Collection<Cell> takeSecondaryBottomCells(final Cell cell, final Player player) {
		int secondaryBottomIndex = checker.getNeighbourIndex(Direction.SECONDARY_DIAGONAL_BOTTOM,
				cell, player);
		return takeCells(secondaryBottomIndex, cell.getIndex(), secondaryBottomIndex,
				Game.BOARD_COLUMN_COUNT - 1, player);
	}

	private Collection<Cell> takeMainTopCells(final Cell cell, final Player player) {
		int mainTopIndex = checker.getNeighbourIndex(Direction.MAIN_DIAGONAL_TOP, cell, player);
		return takeCells(mainTopIndex, mainTopIndex, cell.getIndex(), Game.BOARD_COLUMN_COUNT + 1,
				player);
	}

	private Collection<Cell> takeMainBottomCells(final Cell cell, final Player player) {
		int mainBottomIndex = checker.getNeighbourIndex(Direction.MAIN_DIAGONAL_BOTTOM, cell,
				player);
		return takeCells(mainBottomIndex, cell.getIndex(), mainBottomIndex,
				Game.BOARD_COLUMN_COUNT + 1, player);
	}

	private Collection<Cell> takeBottomCells(final Cell cell, final Player player) {
		int bottomNeigbhourIndex = checker.getNeighbourIndex(Direction.BOTTOM, cell, player);
		return takeCells(bottomNeigbhourIndex, cell.getIndex(), bottomNeigbhourIndex,
				Game.BOARD_COLUMN_COUNT, player);
	}

	private Collection<Cell> takeTopCells(final Cell cell, final Player player) {
		int topNeighbourIndex = checker.getNeighbourIndex(Direction.TOP, cell, player);
		return takeCells(topNeighbourIndex, topNeighbourIndex, cell.getIndex(),
				Game.BOARD_COLUMN_COUNT, player);
	}

	private Collection<Cell> takeRightCells(final Cell cell, final Player player) {
		int rightNeighbourIndex = checker.getNeighbourIndex(Direction.RIGHT, cell, player);
		return takeCells(rightNeighbourIndex, cell.getIndex(), rightNeighbourIndex, 1, player);
	}

	private Collection<Cell> takeLeftCells(final Cell cell, final Player player) {
		int leftNeighbourindex = checker.getNeighbourIndex(Direction.LEFT, cell, player);
		return takeCells(leftNeighbourindex, leftNeighbourindex, cell.getIndex(), 1, player);
	}

	private Collection<Cell> takeCells(final int fromIndex, final int toIndex, final int step,
			final Player player) {
		final Collection<Cell> result = new HashSet<Cell>();
		for (int i = fromIndex; i <= toIndex; i += step) {
			result.add(board.get(i));
		}
		return result;
	}

	private Collection<Cell> takeCells(final int neighbourIndex, final int start, final int end,
			final int step, final Player player) {
		return neighbourIndex > 0 ? takeCells(start, end, step, player) : Collections
				.<Cell> emptySet();
	}

}