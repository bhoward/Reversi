package edu.fmi.ai.reversi.move;

import edu.fmi.ai.reversi.Game;
import edu.fmi.ai.reversi.model.Board;
import edu.fmi.ai.reversi.model.Cell;
import edu.fmi.ai.reversi.model.Player;

public class VerticalMoveChecker extends BaseLineMoveChecker {

	public VerticalMoveChecker(final Board board) {
		super(board);
	}

	public int getTopNeighbourIndex(final Cell cell, final Player player) {
		return getNeighbourIndex(cell, player, true);
	}

	public int getBottomNeighbourIndex(final Cell cell, final Player player) {
		return getNeighbourIndex(cell, player, false);
	}

	@Override
	protected int getEndIndex(Cell startCell, boolean isMinusDirection) {
		return isMinusDirection ? startCell.getY() : Game.BOARD_ROW_COUNT - startCell.getY();
	}

	@Override
	protected int incrementIndex(int cellIndex, boolean isMinusDirection) {
		return isMinusDirection ? cellIndex -Game.BOARD_COLUMN_COUNT : cellIndex + Game.BOARD_COLUMN_COUNT;
	}

}
