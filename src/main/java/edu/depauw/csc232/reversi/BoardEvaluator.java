package edu.depauw.csc232.reversi;

import edu.depauw.csc232.reversi.model.Board;
import edu.depauw.csc232.reversi.model.Cell;
import edu.depauw.csc232.reversi.model.Player;

/**
 * 
 * A helper class that is used for evaluating the current state of the board.
 * 
 * @author martin
 * 
 */
public class BoardEvaluator {

	/**
	 * {@value}
	 */
	private static final int WEIGHT_LOCATION = 13;

	/**
	 * {@value}
	 */
	private static final int WEIGHT_SKIP_TURN = 500;

	/**
	 * {@value}
	 */
	private static final int WEIGHT_STABLE_DISCS = 8;

	/**
	 * {@value}
	 */
	private static final int WEIGHT_MOBILITY = 5;

	/**
	 * {@value4}
	 */
	private static final int WEIGHT_DISC_COUNT = 2;

	private final int[][] locationValues;

	public BoardEvaluator() {
		locationValues = new int[][] { { 50, -1, 5, 2, 2, 5, -1, 50 },
				{ -1, -10, 1, 1, 1, 1, -10, -1 }, { 5, 1, 1, 1, 1, 1, 1, 5 },
				{ 2, 1, 1, 0, 0, 1, 1, 2 }, { 2, 1, 1, 0, 0, 1, 1, 2 }, { 5, 1, 1, 1, 1, 1, 1, 5 },
				{ -1, -10, 1, 1, 1, 1, -10, -1 }, { 50, -1, 5, 2, 2, 5, -1, 50 } };
	}

	/**
	 * Gets the heuristic value of the board for the given <tt>player</tt>
	 * 
	 * @param board
	 *            the board that is to be evaluated
	 * @param player
	 *            the player for which the board is evaluated
	 * @return the heuristic value of the board for the given player
	 */
	public int getValue(final Board board, final Player player) {
		return getLocationValue(board, player) + getStabilityValue(board, player)
				+ getTurnValue(board, player) + getMobilityValue(board, player)
				+ getDiscCountValue(board, player);
	}

	/**
	 * Returns the value of the <tt>board</tt> given, based on assessing the
	 * location of discs of the given <tt>player</tt>
	 * 
	 * @param board
	 *            the board that is to be evaluated
	 * @param player
	 *            the player for which the evaluation is to be performed
	 * @return the ratio between the location-based value of the board for the
	 *         <tt>player</tt> given and the one for his opponent.
	 */
	private int getLocationValue(final Board board, final Player player) {
		int locationValue = 0;
		int opponentValue = 0;
		final Player opponent = Player.getOpponent(player);
		for (int i = 0; i < Game.BOARD_ROW_COUNT; ++i) {
			for (int j = 0; j < Game.BOARD_COLUMN_COUNT; ++j) {
				final Cell currentCell = board.get(j, i);
				if (currentCell.isOwnedBy(player)) {
					locationValue += player.getSign() * locationValues[i][j];
				} else if (currentCell.isOwnedBy(opponent)) {
					opponentValue += opponent.getSign() * locationValues[i][j];
				}
			}
		}

		return (locationValue + opponentValue) * WEIGHT_LOCATION;
	}

	/**
	 * Returns the value of the <tt>board</tt> given, based on assessing the
	 * stability of the discs that are currently on the board for the
	 * <tt>player</tt> specified
	 * 
	 * @param board
	 *            the board that is to be evaluated
	 * @param player
	 *            the player for whom the board is to be evaluated
	 * @return the difference between the number of stable discs on the board
	 *         for the <tt>player</tt> given and his opponent, multiplied by the
	 *         respective weight
	 */
	private int getStabilityValue(final Board board, final Player player) {
		return player.getSign()
				* (board.getStableDiscsCount(player) - board.getStableDiscsCount(Player
						.getOpponent(player))) * WEIGHT_STABLE_DISCS;
	}

	/**
	 * Returns the value of the <tt>board</tt> given, based on assessing the
	 * chances of the current player having the advantage that the next player
	 * will skip his turn.
	 * 
	 * @param board
	 *            the board that is to be evaluated
	 * @param player
	 *            the player for which the board is to be evaluated
	 * @return the value of the board, computed with respect to the chance that
	 *         the next player will have to skip his turn.
	 */
	private int getTurnValue(final Board board, final Player player) {
		return isOpponentSkippingTurn(board, player) ? player.getSign() * WEIGHT_SKIP_TURN : 0;
	}

	/**
	 * Returns the mobility value of the board. This is the number of possible
	 * moves the next player can make taken with the respective weight.
	 * 
	 * @param board
	 *            the board instance that is to be evaluated
	 * @param player
	 *            the player for which the board is to be evaluated
	 * @return the mobility value of the board
	 */
	private int getMobilityValue(final Board board, final Player player) {
		final Player opponent = Player.getOpponent(player);
		return opponent.getSign() * board.getNextBoards(opponent).size() * WEIGHT_MOBILITY;
	}

	/**
	 * Returns the disc count value of the board for the given player
	 * 
	 * @param board
	 *            the board that is to be evaluated
	 * @param player
	 *            the player for which the board is to be evaluated
	 * @return the disc count value of the board specified for the
	 *         <tt>player</tt> given
	 */
	private int getDiscCountValue(final Board board, final Player player) {
		return player.getSign()
				* (board.getDiscCount(player) - board.getDiscCount(Player.getOpponent(player)))
				* WEIGHT_DISC_COUNT;

	}

	private boolean isOpponentSkippingTurn(final Board board, final Player player) {
		return board.getNextBoards(Player.getOpponent(player)).isEmpty();
	}
}
