package ai;

import backend.MockBoardTree;
import entities.Stone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Ms. Oracina can see the future... sort of.
 * She will try to maximize the number of stones she has
 * after a set number of her turns.
 */
public class Oracina extends AIPlayer {
    /** The number of turns to look ahead. Does not include the opponent's turn. */
    private int turns;

    /**
     * Ask Ms. Oracina to join the game.
     * She will try to move towards the best future for her.
     *
     * @param stone The stone she should use.
     * @param turns The number of turns to look ahead.
     */
    public Oracina(Stone stone, int turns) {
        super(stone);
        this.turns = turns;
    }

    @Override
    public int[] findMove() {
        // generate all possibilities as a tree
        MockBoardTree possibilities = new MockBoardTree();
        if (possibilities.getMockBoard().getValidMoves().isEmpty()) {
            // no possible move at the current state
            return null;
        }
        // the number of turns to simulate should include the opponent turns
        int depth = 2 * this.turns - 1;
        for (int i = 0; i < depth; i++) {
            possibilities.deepGenerateAllOutcomes();
        }

        // calculate the score for each immediate move
        double[] scores = this.scoreMoves(possibilities);

        // find the one with the highest score
        double bestScore = -1.0;
        List<int[]> bestMoves = new ArrayList<>();
        for (int i = 0; i < possibilities.size(); i++) {
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                bestMoves.clear();
                bestMoves.add(possibilities.getChild(i).getPreviousMove());
            }
            else if (scores[i] == bestScore) {
                bestMoves.add(possibilities.getChild(i).getPreviousMove());
            }
        }

        // return a random best move
        Collections.shuffle(bestMoves);
        return bestMoves.get(0);
    }

    /**
     * Assign scores to the moves immediately after the root.
     * This computes the average number of stones at the leaf nodes,
     * then recursively carries it up to tone below the root.
     *
     * @param root The tree of possible moves.
     *             Every branch should have the same max depth for this to work properly.
     * @return The score of each move.
     */
    private double[] scoreMoves(MockBoardTree root) {
        if (root == null) {
            return null;
        }
        if (root.size() == 0) {
            // this is a leaf node, so just return the number of stones
            return new double[] {root.getMockBoard().countStones(this.stone)};
        }
        double[] scores = new double[root.size()];
        for (int i = 0; i < root.size(); i++) {
            MockBoardTree next = root.getChild(i);
            // collect score from each child
            double[] nextScores = this.scoreMoves(next);
            // compute the average score
            double mean = 0.0;
            for (double score : nextScores) {
                mean += score;
            }
            mean /= nextScores.length;
            scores[i] = mean;
        }
        return scores;
    }
}
