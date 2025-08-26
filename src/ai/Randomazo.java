package ai;

import backend.Board;
import entities.Stone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * He will essentially press every square available until it makes a move.
 * If he does a good job, is it because of the player's luck, or Mr. Randomazo's?
 */
public class Randomazo extends AIPlayer {
    /**
     * Invite Mr. Randomazo to play. He will always make a random move.
     *
     * @param stone The stone Mr. Randomazo should use.
     */
    public Randomazo(Stone stone) {
        super(stone);
    }

    @Override
    public int[] findMove() {
        return findRandomMove(this.stone);
    }

    /**
     * Finds a random valid move for the specified stone.
     *
     * @param stone The stone to find a move for.
     * @return An array [row, col] representing the coordinate
     *         of a valid square to place the stone, or {@code null} if no valid move exists.
     */
    public static int[] findRandomMove(Stone stone) {
        Board board = Board.getInstance();
        int size = board.getSize();

        // Create a list of coordinates, then shuffle order to simulate random choices
        List<int[]> coordinates = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int[] ar = {row, col};
                coordinates.add(ar);
            }
        }
        Collections.shuffle(coordinates);

        // Go through the list and check for valid moves
        for (int[] coor : coordinates) {
            int row = coor[0], col = coor[1];
            // Check if this would be a valid move
            if (board.getSquareAt(row, col).getStone() == null &&
                    board.getFlippingDirections(stone, row, col).length > 0) {
                return new int[]{row, col};
            }
        }

        // No valid move found
        return null;
    }
}
