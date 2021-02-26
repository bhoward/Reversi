package edu.depauw.csc232.reversi.move;

import edu.depauw.csc232.reversi.model.Board;
import edu.depauw.csc232.reversi.model.Cell;
import edu.depauw.csc232.reversi.model.Player;

public abstract class BaseLineMoveChecker extends BaseMoveChecker {

	public BaseLineMoveChecker(Board board) {
		super(board);
	}

	@Override
	protected int getNeighbourIndex(final Cell cell, final Player player,
			final boolean isNegativeDirection, final boolean isStoppingSearch) {
		int cellIndex = cell.getIndex();
		for (int i = 1; i < getEndIndex(cell, isNegativeDirection); ++i) {
			cellIndex = incrementIndex(cellIndex, isNegativeDirection);
			final Cell currentCell = board.get(cellIndex);
			if (isClosestNeighbour(player, i, currentCell)) {
				return cellIndex;
			} else if (isStoppingSearch && isStoppingSearch(player, i, currentCell)) {
				return -1;
			}
		}
		return -1;
	}

	protected abstract int getEndIndex(final Cell startCell, final boolean isNegativeDirection);

}
