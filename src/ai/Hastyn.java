package ai;

import backend.Board;
import backend.MockBoard;
import entities.Stone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * She will make moves that will immediately flip as many stones as possible.
 */
public class Hastyn extends AIPlayer {
    /**
     * Invite Hastyn to play. She will try to maximize the number of flips for that turn.
     * @param stone The stone Hastyn should use.
     */
    public Hastyn(Stone stone) {
        super(stone);
    }

    @Override
    public int[] findMove() {
        Board board = Board.getInstance();
        int best = 0;
        List<int[]> bestMoves = new ArrayList<>();
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                MockBoard mBoard = new MockBoard();
                int flipped = mBoard.placeStone(row, col);
                int[] move = {row, col};
                if (flipped >= best) {
                    if (flipped > best) {
                        best = flipped;
                        bestMoves.clear();
                    }
                    bestMoves.add(move);
                }
            }
        }
        if (bestMoves.isEmpty()) {
            return null;
        }
        else {
            Random rand = new Random();
            return bestMoves.get(rand.nextInt(bestMoves.size()));
        }
    }
}
