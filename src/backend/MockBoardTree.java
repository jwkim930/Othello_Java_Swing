package backend;

import java.util.Arrays;
import java.util.List;

/**
 * A tree structure for MockBoard.
 * The parent should be a game state immediately before the children.
 */
public class MockBoardTree extends GenericTree<MockBoard> {
    /**
     * The [row, col] move that sent the parent to this state.
     * {@code null} if root or the previous turn was passed.
     */
    private int[] previousMove;


    /**
     * Initialize the tree by specifying a root.
     *
     * @param root The root node to be used.
     */
    public MockBoardTree(MockBoard root) {
        super(root.copy());
    }

    /**
     * Initialize the tree by copying the current board state.
     */
    public MockBoardTree() {
        this(new MockBoard());
    }

    /**
     * Helper constructor to relabel the instance to {@code MockBoardTree}.
     * The attributes will be copied by references.
     */
    private MockBoardTree(GenericTree<MockBoard> genTree) {
        super(genTree.data, genTree.parent, genTree.children);
        if (genTree instanceof MockBoardTree mTree) {
            this.previousMove = mTree.previousMove;
        }
    }

    /**
     * Alias for getData().
     *
     * @return The {@code MockBoard} instance stored at this node.
     */
    public MockBoard getMockBoard() {
        return this.getData();
    }

    @Override
    public MockBoardTree getParent() {
        GenericTree<MockBoard> superParent = super.getParent();
        if (superParent == null) {
            return null;
        }
        if (superParent instanceof MockBoardTree mbParent) {
            return mbParent;
        }
        return new MockBoardTree(superParent);
    }

    @Override
    public MockBoardTree getChild(int i) throws IndexOutOfBoundsException {
        GenericTree<MockBoard> child = super.getChild(i);
        if (child instanceof MockBoardTree mbChild) {
            return mbChild;
        }
        return new MockBoardTree(child);
    }

    /**
     * Do not call this as you shouldn't manually add a child to MockBoardTree.
     */
    @Override
    @Deprecated
    public void addChild(MockBoard elem) {
        throw new UnsupportedOperationException("MockBoardTree should not directly call addChild");
    }

    /**
     * @return The move that sent the parent board to this board as [row, col].
     * {@code null} if there is no parent or the previous turn was passed.
     */
    public int[] getPreviousMove() {
        if (this.previousMove == null) {
            return null;
        }
        return Arrays.copyOf(this.previousMove, 2);
    }

    /**
     * Generates all possible states that come after this given state
     * and add them as children. A duplicate child will not be added.
     * If there's no possible move at any point, the state after passing
     * will be added, guaranteeing that at least one child is added
     * unless there's a duplicate child.
     *
     * @return The number of children added.
     */
    public int generateAllOutcomes() {
        int numAdded = 0;
        MockBoard current = this.data;
        List<int[]> validMoves = current.getValidMoves();
        for (int[] move : validMoves) {
            MockBoard next = current.copy();
            next.placeStone(move[0], move[1]);
            boolean dup = false;
            for (int i = 0; i < this.size(); i++) {
                MockBoardTree cNode = this.getChild(i);
                if (cNode.data.equals(next)) {
                    dup = true;
                    break;
                }
            }
            if (!dup) {
                MockBoardTree nextNode = new MockBoardTree(next);
                nextNode.parent = this;
                nextNode.previousMove = move;
                this.children.add(nextNode);
                numAdded++;
            }
        }
        if (validMoves.isEmpty()) {
            // pass the turn and add as a child
            MockBoard passed = current.copy();
            passed.nextTurn();
            MockBoardTree child = new MockBoardTree(passed);
            child.parent = this;
            this.children.add(child);

        }
        return numAdded;
    }

    /**
     * Generates all possible states that come after every leaf node.
     *
     * @return The total number of children nodes generated.
     */
    public int deepGenerateAllOutcomes() {
        return dGAORecurse(this);
    }

    /**
     * The recursive helper method implemented for {@code deepGenerateAllOutcomes}.
     *
     * @param root The root node to start searching from.
     * @return The total number of children nodes generated.
     */
    private static int dGAORecurse(MockBoardTree root) {
        if (root.size() == 0) {
            // this node is a leaf
            return root.generateAllOutcomes();
        }
        // otherwise, recurse into all children
        int numAdded = 0;
        for (int i = 0; i < root.size(); i++) {
            MockBoardTree child = root.getChild(i);
            numAdded += dGAORecurse(child);
        }
        return numAdded;
    }
}
