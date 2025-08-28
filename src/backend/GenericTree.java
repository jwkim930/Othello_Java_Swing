package backend;

import java.util.ArrayList;
import java.util.List;

/**
 * A tree structure with a generic content.
 */
public class GenericTree<T> {
    /** The object instance at this node */
    protected T data;
    /** The parent node for this node. {@code null} if it's the root. */
    protected GenericTree<T> parent;
    /** The children nodes for this node, if any. */
    protected List<GenericTree<T>> children;

    /**
     * Initialize the tree by specifying a root.
     * @param root The root node to be used.
     */
    public GenericTree(T root) {
        this.data = root;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    /**
     * Helper constructor to relabel into a subclass.
     * The attributes will be copied by references.
     */
    protected GenericTree(T data, GenericTree<T> parent, List<GenericTree<T>> children) {
        this.data = data;
        this.parent = parent;
        this.children = children;
    }

    /**
     * @return The number of children under this node.
     */
    public int size() {
        return this.children.size();
    }

    /**
     * @return The object instance stored at this node.
     */
    public T getData() {
        return this.data;
    }

    /**
     * @return The parent node if there is one, {@code null} if this node is the root.
     */
    public GenericTree<T> getParent() {
        return this.parent;
    }

    /**
     * @param i The index of the desired child.
     * @return The child node at the given index.
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public GenericTree<T> getChild(int i) throws IndexOutOfBoundsException {
        return this.children.get(i);
    }

    /**
     * Adds a child as a new branch under this node.
     * The added child will be placed at the end of the children list.
     *
     * @param elem The content of the child node.
     */
    public void addChild(T elem) {
        this.addChild(new GenericTree<T>(elem));
    }

    /**
     * Adds a child node under this node.
     * The added child will be placed at the end of the children list.
     * @param node The node to be added as the child. This must not have a parent.
     * @throws IllegalArgumentException if the node already has a parent.
     */
    public void addChild(GenericTree<T> node) throws IllegalArgumentException {
        if (node.parent != null) {
            throw new IllegalArgumentException("the node already has a parent");
        }
        node.parent = this;
        this.children.add(node);
    }

    /**
     * Remove the child node at the index.
     * The child will have no parent after removal.
     *
     * @param i The index of the child to be removed.
     * @return The removed node.
     * @throws IndexOutOfBoundsException if the index is out of range.
     */
    public GenericTree<T> removeChild(int i) throws IndexOutOfBoundsException {
        GenericTree<T> removed = this.children.remove(i);
        removed.parent = null;
        return removed;
    }
}
