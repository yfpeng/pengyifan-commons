package com.pengyifan.commons.collections.tree2;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.pengyifan.commons.lang.StringUtils;
import edu.stanford.nlp.util.ErasureUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A <code>Tree</code> is a general-purpose node in a tree data structure.
 * <p>
 * A tree node may have at most one parent and 0 or more children. <code>Tree</code> provides
 * operations for examining and modifying a node's parent and children and also operations for
 * examining the tree that the node is a part of. A node's tree is the set of all nodes that can be
 * reached by starting at the node and following all the possible links to parents and children. A
 * node with no parent is the root of its tree; a node with no children is a leaf. A tree may
 * consist of many subtrees, each node acting as the root for its own subtree.
 * <p>
 * This class provides iterator for efficiently traversing a tree or subtree in various orders or
 * for following the path between two nodes. A <code>Tree</code> may also hold a reference to a
 * user object, the use of which is left to the user. Asking a <code>Tree</code> for its string
 * representation with <code>toString()</code> returns the string representation of its user
 * object.
 * <p>
 * <b>This is not a thread safe class.</b>If you intend to use a Tree (or a tree of TreeNodes) in
 * more than one thread, you need to do your own synchronizing. A good convention to adopt is
 * synchronizing on the root node of a tree.
 * <p>
 * Most source code is copied from <code>DefaultMutableTreeNode</code> by Rob Davis.
 *
 * @author Yifan Peng
 */
public class Tree<E, T extends Tree<E, T>> implements Iterable<T> {

  private E obj;

  private T parent;

  private List<T> children;

  /**
   * Creates a tree node that has no parent and no children.
   */
  public Tree() {
    this(null);
  }

  /**
   * Creates a tree node with no parent, no children, and initializes it with the specified user
   * object.
   *
   * @param obj an Object provided by the user that constitutes the node's data
   */
  public Tree(E obj) {
    this.obj = obj;
    parent = null;
  }

  /**
   * Insert the child at the specified position in this node's child array. Shifts the child
   * currently at that position (if any) and any subsequent children to the right (adds one to
   * their indices). Sets the child's parent to this node.
   *
   * @param index the index in this node's child array where this node is to be inserted
   * @param child the child to be inserted under this node
   * @throws ArrayIndexOutOfBoundsException if the index is out of range
   * @throws NullPointerException           if the child is null
   * @throws IllegalArgumentException       if the child is an ancestor of this node
   */
  public void add(int index, T child) {
    checkNotNull(child, "The child is null");
    checkArgument(!isNodeAncestor(child), "The child is an ancestor of this node");
    child.setParent(ErasureUtils.uncheckedCast(this));
    if (children == null) {
      children = Lists.newLinkedList();
    }
    children.add(index, child);
  }

  /**
   * Appends the child to the end of this node's child array. Sets the child's parent to this node.
   *
   * @param child the child node to be appended under this node
   * @throws NullPointerException     if the child is null
   * @throws IllegalArgumentException if the child is an ancestor of this node
   */
  public void add(T child) {
    if (child != null && child.getParent() == this) {
      add(getChildCount() - 1, child);
    } else {
      add(getChildCount(), child);
    }
  }

  /**
   * Creates and returns an iterator that traverses the subtree rooted at this node in
   * breadth-first order. The first node returned by the iterator's <code>next()</code> method is
   * this node.
   * <p>
   * Modifying the tree by inserting, removing, or moving a node invalidates any iterators created
   * before the modification.
   *
   * @return an iterator for traversing the tree in breadth-first order
   * @see #depthFirstIterator()
   * @see #postorderIterator()
   * @see #preorderIterator()
   */
  public Iterator<T> breadthFirstIterator() {
    return new BreadthFirstIterator<>(ErasureUtils.uncheckedCast(this));
  }

  public List<T> breadthFirstList() {
    return Lists.newArrayList(breadthFirstIterator());
  }

  /**
   * Gets the list of this node's children.
   *
   * @return a list of this node's children, or empty list if it is a leaf
   */
  public List<T> children() {
    if (children == null) {
      return Collections.emptyList();
    } else {
      return children;
    }
  }

  /**
   * Creates and returns a forward-order iterator of this node's children. Modifying this node's
   * child array invalidates any child iterators created before the modification.
   *
   * @return an iterator of this node's children
   */
  public Iterator<T> childrenIterator() {
    if (children == null) {
      return Collections.emptyIterator();
    } else {
      return children.iterator();
    }
  }

  /**
   * Makes a deep copy of not only the Tree structure but of the user object as well.
   *
   * @param treeFactory the tree factory of the new tree.
   * @return A deep copy of the tree structure and its user object.
   */
  public T deepCopy(TreeFactory<E, T> treeFactory) {
    T dst = treeFactory.newTree(obj);
    for (T child : children()) {
      dst.add(child.deepCopy(treeFactory));
    }
    return dst;
  }

  /**
   * Creates and returns an iterator that traverses the subtree rooted at this node in depth-first
   * order. The first node returned by the iterator's <code>next()</code> method is the leftmost
   * leaf. This is the same as a postorder traversal.
   * <p>
   * Modifying the tree by inserting, removing, or moving a node invalidates any iterators created
   * before the modification.
   *
   * @return an iterator for traversing the tree in depth-first order
   * @see #breadthFirstIterator()
   * @see #leavesIterator()
   * @see #postorderIterator()
   * @see #preorderIterator()
   */
  public Iterator<T> depthFirstIterator() {
    return postorderIterator();
  }

  /**
   * Returns the child at the specified index in this node's child array.
   *
   * @param index an index into this node's child array
   * @return the Tree in this node's child array at the specified index
   * @throws ArrayIndexOutOfBoundsException if <code>index</code> is out of bounds
   */
  public T getChild(int index) {
    if (children == null) {
      throw new ArrayIndexOutOfBoundsException("node has no children");
    }
    return children.get(index);
  }

  /**
   * Returns the child in this node's child array that immediately follows the specified child,
   * which must be a child of this node. If the specified child is the last child, returns null.
   * This method performs a linear search of this node's children for the specified child and is
   * O(n) where n is the number of children; to traverse the entire array of children, use an
   * enumeration instead.
   *
   * @return the child of this node that immediately follows the child
   * @throws NullPointerException     if the child is null
   * @throws IllegalArgumentException if the child is not a child of this node
   * @see #children()
   */
  public T getChildAfter(T child) {
    checkNotNull(child, "The child is null");

    int index = indexOf(child); // linear search
    checkArgument(index != -1, "The child is not a child of this node");

    if (index < getChildCount() - 1) {
      return getChild(index + 1);
    } else {
      return null;
    }
  }

  /**
   * Returns the child in this node's child array that immediately precedes the specified child,
   * which must be a child of this node. If the specified child is the first child, returns null.
   * This method performs a linear search of this node's children for the specified child and is
   * O(n) where n is the number of children.
   *
   * @return the child of this node that immediately precedes the child
   * @throws NullPointerException     if the child is null
   * @throws IllegalArgumentException if the child is not a child of this node
   */
  public T getChildBefore(T child) {
    checkNotNull(child, "The child is null");

    int index = indexOf(child); // linear search
    checkArgument(index != -1, "The child is not a child of this node");

    if (index > 0) {
      return getChild(index - 1);
    } else {
      return null;
    }
  }

  /**
   * Returns the number of children of this node.
   *
   * @return the number of children of this node
   */
  public int getChildCount() {
    if (children == null) {
      return 0;
    } else {
      return children.size();
    }
  }

  /**
   * Returns the depth of the tree rooted at this node -- the longest distance from this node to a
   * leaf. If this node has no children, returns 0. This operation is much more expensive than
   * {@link #getLevel()} because it must effectively traverse the entire tree rooted at this
   * node.
   *
   * @return the depth of the tree rooted at this node
   * @see #getLevel()
   */
  public int getDepth() {
    T last = Iterators.getLast(breadthFirstIterator());
    return last.getLevel() - getLevel();
  }

  /**
   * Returns the first child of this node.
   *
   * @return the first child of this node
   * @throws NoSuchElementException if this node has no child
   */
  public T getFirstChild() {
    if (getChildCount() == 0) {
      throw new NoSuchElementException("This node has no child");
    }
    return getChild(0);
  }

  /**
   * Finds and returns the first leaf that is a descendant of this node -- either this node or its
   * first child's first leaf. Returns this node if it is a leaf.
   *
   * @return the first leaf that is a descendant of this node
   * @see #isLeaf()
   */
  public T getFirstLeaf() {
    T node = ErasureUtils.uncheckedCast(this);
    while (!node.isLeaf()) {
      node = node.getFirstChild();
    }
    return node;
  }

  /**
   * Returns this node's last child. If this node has no children, throws {@link
   * java.util.NoSuchElementException}.
   *
   * @return the last child of this node
   * @throws NoSuchElementException if this node has no child
   */
  public T getLastChild() {
    if (getChildCount() == 0) {
      throw new NoSuchElementException("This node has no child");
    }
    return getChild(getChildCount() - 1);
  }

  /**
   * Returns the last leaf that is a descendant of this node -- either this node or its last child's
   * last leaf. Returns this node if it is a leaf.
   *
   * @return the last leaf in the subtree rooted at this node
   * @see #isLeaf()
   */
  public T getLastLeaf() {
    T node = ErasureUtils.uncheckedCast(this);
    while (!node.isLeaf()) {
      node = node.getLastChild();
    }
    return node;
  }

  /**
   * Returns the leaves under this node in the order by the natural left to right.
   *
   * @return the leaves under this node in the order by the natural left to right
   */
  public List<T> getLeaves() {
    return Lists.newArrayList(leavesIterator());
  }

  /**
   * Returns a list of the data in the tree's leaves. The object of all leaf nodes is returned as a
   * list ordered by the natural left to right order of the leaves. Null values, if any, are
   * inserted into the list like any other value.
   *
   * @return a List of the data in the tree's leaves.
   */
  public List<E> getLeafObjects() {
    final Iterable<T> iterable = () -> leavesIterator();
    return StreamSupport.stream(iterable.spliterator(), false)
        .map(T::getObject)
        .collect(Collectors.toList());
  }

  /**
   * Returns the number of levels above this node -- the distance from the root to this node. If
   * this node is the root, returns 0.
   *
   * @return the number of levels above this node
   * @see #getDepth()
   */
  public int getLevel() {
    T ancestor = ErasureUtils.uncheckedCast(this);
    int levels = 0;

    while ((ancestor = ancestor.getParent()) != null) {
      levels++;
    }

    return levels;
  }

  /**
   * Returns the next sibling of this node in the parent's children array. Returns null if this
   * node has no parent or is the parent's last child. This method performs a linear search that is
   * O(n) where n is the number of children; to traverse the entire array, use the parent's child
   * enumeration instead.
   *
   * @return the sibling of this node that immediately follows this node
   * @see #children()
   */
  public T getNextSibling() {
    T myParent = getParent();

    T retval;
    if (myParent == null) {
      retval = null;
    } else {
      retval = myParent.getChildAfter(ErasureUtils.uncheckedCast(this)); // linear search
    }

    if (retval != null && !isNodeSibling(retval)) {
      throw new Error("child of parent is not a sibling");
    }

    return retval;
  }

  /**
   * Returns this node's user object.
   *
   * @return the Object stored at this node by the user
   * @see #setObject(Object)
   * @see #toString()
   */
  public E getObject() {
    return obj;
  }

  /**
   * Returns this node's parent or null if this node has no parent.
   *
   * @return this node's parent Tree, or null if this node has no parent
   */
  public T getParent() {
    return parent;
  }

  /**
   * Returns the path from this node to the root. The first element in the path is this node.
   *
   * @return a list of Tree objects giving the path, where the first element in the path is this
   * node and the last element is the root.
   */
  public List<T> getPathToRoot() {
    List<T> elderList = Lists.newLinkedList();
    for (T p = ErasureUtils.uncheckedCast(this); p != null; p = p.getParent()) {
      elderList.add(p);
    }
    return elderList;
  }

  /**
   * Returns the path from the root, to get to this node. The last element in the path is this
   * node.
   *
   * @return a list of Tree objects giving the path, where the first element in the path is the root
   * and the last element is this node.
   */
  public List<T> getPathFromRoot() {
    List<T> elderList = getPathToRoot();
    Collections.reverse(elderList);
    return elderList;
  }

  public boolean hasNextSiblingNode() {
    return getNextSibling() != null;
  }

  /**
   * Returns the index of the specified child in this node's child array. If the specified node is
   * not a child of this node, returns -1. This method performs a linear search and is O(n) where
   * n is the number of children.
   *
   * @param child the Tree to search for among this node's children
   * @return an int giving the index of the node in this node's child array, or <code>-1</code> if
   * the specified node is a not a child of this node
   * @throws NullPointerException if the specified child is null
   */
  public int indexOf(T child) {
    checkNotNull(child, "argument is null");
    if (!isNodeChild(child)) {
      return -1;
    }
    return children.indexOf(child); // linear search
  }

  /**
   * Returns true if this node has no children.
   *
   * @return true if this node has no children
   */
  public boolean isLeaf() {
    return (getChildCount() == 0);
  }

  /**
   * Returns true if <code>anotherNode</code> is an ancestor of this node -- if it is this node,
   * this node's parent, or an ancestor of this node's parent. (Note that a node is considered an
   * ancestor of itself.) If <code>anotherNode</code> is null, this method returns false. This
   * operation is at worst O(h) where h is the distance from the root to this node.
   *
   * @param anotherNode node to test as an ancestor of this node
   * @return true if this node is a descendant of <code>anotherNode</code>
   */
  public boolean isNodeAncestor(T anotherNode) {
    if (anotherNode == null) {
      return false;
    }

    T ancestor = ErasureUtils.uncheckedCast(this);

    do {
      if (ancestor == anotherNode) {
        return true;
      }
    } while ((ancestor = ancestor.getParent()) != null);

    return false;
  }

  /**
   * Returns true if the child is a child of this node. Returns false if the child is null.
   *
   * @param child the specified child
   * @return true if the child is a child of this node
   */
  public boolean isNodeChild(T child) {
    boolean retval = false;

    if (child == null) {
      retval = false;
    } else {
      if (getChildCount() == 0) {
        retval = false;
      } else {
        retval = (child.getParent() == this);
      }
    }

    return retval;
  }

  /**
   * Returns true if the specified node is a sibling of (has the same parent as) this node. A
   * node is its own sibling. Returns false if the node is null.
   *
   * @param t node to test as sibling of this node
   * @return true if the specified node is a sibling of this node
   */
  public boolean isNodeSibling(T t) {
    boolean val;
    if (t == null) {
      val = false;
    } else if (t == this) {
      val = true;
    } else {
      val = (parent != null && parent == t.getParent());
      if (val && !(getParent()).isNodeChild(t)) {
        throw new Error("sibling has different parent");
      }
    }
    return val;
  }

  /**
   * Returns true if this node is the root of the tree. The root is the only node in the tree with
   * a
   * null parent; every tree has exactly one root.
   *
   * @return true if this node is the root of its tree
   */
  public boolean isRoot() {
    return getParent() == null;
  }

  @Override
  public Iterator<T> iterator() {
    return preorderIterator();
  }

  public Iterator<T> leavesIterator() {
    return new LeavesIterator<>(ErasureUtils.uncheckedCast(this));
  }

  /**
   * Creates and returns an iterator that traverses the subtree rooted at this node in postorder.
   * The first node returned by the iterator's <code>next()</code> method is the leftmost leaf.
   * This is the same as a depth-first traversal.
   * <p>
   * Modifying the tree by inserting, removing, or moving a node invalidates any iterators created
   * before the modification.
   *
   * @return an iterator for traversing the tree in postorder
   * @see #breadthFirstIterator()
   * @see #depthFirstIterator
   * @see #preorderIterator
   */
  public Iterator<T> postorderIterator() {
    return new PostorderIterator<>(ErasureUtils.uncheckedCast(this));
  }

  public List<Tree> postorderList() {
    return Lists.newArrayList(postorderIterator());
  }

  /**
   * Creates and returns an iterator that traverses the subtree rooted at this node in preorder.
   * The first node returned by the iterator's <code>next()</code> method is this node.
   * <p>
   * Modifying the tree by inserting, removing, or moving a node invalidates any enumerations
   * created before the modification.
   *
   * @return an iterator for traversing the tree in preorder
   * @see #postorderIterator()
   */
  public Iterator<T> preorderIterator() {
    return new PreorderIterator<>(ErasureUtils.uncheckedCast(this));
  }

  public List<T> preorderList() {
    return Lists.newArrayList(preorderIterator());
  }

  /**
   * Removes the child at the specified index from this node's children. Sets that node's parent
   * to null.
   *
   * @param index the index in this node's child array of the child to be removed
   * @throws IndexOutOfBoundsException if the index is out of bounds
   */
  public void remove(int index) {
    T child = children.remove(index);
    child.setParent(null);
  }

  /**
   * Removes the child from this node's child array, giving it a null parent.
   *
   * @param child a child of this node to remove
   * @throws NullPointerException     if the child is null
   * @throws IllegalArgumentException if the child is not a child of this node
   */
  public void remove(T child) {
    checkNotNull(child, "The child is null");
    checkArgument(!isNodeChild(child), "The child is not a child of this node");
    children.remove(child);
    child.setParent(null);
  }

  public void reversal() {
    if (children != null) {
      Collections.reverse(children);
      children.forEach(T::reversal);
    }
  }

  /**
   * Sets the user object for this node to <code>obj</code>.
   *
   * @param obj the Object that constitutes this node's user-specified data
   * @see #getObject()
   * @see #toString()
   */
  public void setObject(E obj) {
    this.obj = obj;
  }

  /**
   * Sets this node's parent to the specified parent but does not change the parent's child
   * array. This method is called from {@link #add(Tree)} and {@link #remove(Tree)} to reassign
   * a child's parent, it should not be messaged from anywhere else.
   *
   * @param parent this node's new parent
   */
  public void setParent(T parent) {
    this.parent = parent;
  }

  /**
   * Returns true if <code>anotherNode</code> is dominated by this node. Object equality (==)
   * rather
   * than .equals() is used to determine domination. t.dominates(t) returns false.
   */
  public boolean dominates(T anotherNode) {
    return !(getDominationPath(anotherNode).isEmpty());
  }

  /**
   * Returns the path of nodes leading down to a dominated node, including
   * <code>this</code> and the dominated node itself. Returns null if t is not
   * dominated by <code>this</code>. Object equality (==) is the relevant
   * criterion. t.dominationPath(t) returns emptyList.
   */
  public List<T> getDominationPath(T t) {
    T[] result = getDominationPath(t, 0);
    if (result == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(result);
  }

  T[] getDominationPath(T t, int depth) {
    if (this == t) {
      T[] result = ErasureUtils.uncheckedCast(Array.newInstance(this.getClass(), depth + 1));
      result[depth] = ErasureUtils.uncheckedCast(this);
      return result;
    }
    List<T> kids = children();
    for (int i = kids.size() - 1; i >= 0; i--) {
      T t1 = kids.get(i);
      if (t1 == null) {
        return null;
      }
      T[] result;
      if ((result = t1.getDominationPath(t, depth + 1)) != null) {
        result[depth] = ErasureUtils.uncheckedCast(this);
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the result of sending <code>toString()</code> to this node's user
   * object, or null if this node has no user object.
   *
   * @see #getObject
   */
  @Override
  public String toString() {
    return toString(tree -> tree.obj == null ? null : tree.obj.toString());
  }

  /**
   * An intermediary "Formatter" interface. An implementation of this interface
   * outputs the data of {@link Tree} formatted as appropriate.
   */
  public String toString(Function<Tree, String> formatter) {
    return formatter.apply(this);
  }

  /**
   * Print a string of a tree structure like
   * <p>
   * <pre>
   * └ a
   *   ├ b
   *   ├ c
   *   │ ├ e
   *   │ └ f
   *   └ d
   * </pre>
   */
  public static class PrettyPrint<E, T extends Tree<E, T>> implements Function<T, String> {

    @Override
    public String apply(T tree) {
      StringBuilder sb = new StringBuilder();
      Iterator<T> itr = tree.preorderIterator();
      while (itr.hasNext()) {
        T tn = itr.next();
        // add prefix
        for (T p : tn.getPathFromRoot()) {
          // if parent has sibling node
          if (p == tn) {
            ;
          } else if (p.hasNextSiblingNode()) {
            sb.append(StringUtils.BAR).append(" ");
          } else {
            sb.append("  ");
          }
        }
        // if root has sibling node
        if (tn.hasNextSiblingNode()) {
          sb.append(StringUtils.MIDDLE);
        } else {
          sb.append(StringUtils.END);
        }
        sb.append(" ").append(tn.getObject()).append("\n");

      }
      return sb.toString();
    }
  }

  public static class BreadthFirstIterator<E, T extends Tree<E, T>> implements Iterator<T> {

    private Queue<Iterator<T>> queue;

    public BreadthFirstIterator(T rootNode) {
      queue = Lists.newLinkedList();
      queue.offer(Iterators.singletonIterator(rootNode));
    }

    @Override
    public boolean hasNext() {
      return (!queue.isEmpty() && queue.peek().hasNext());
    }

    @Override
    public T next() {
      Iterator<T> itr = queue.peek();
      T node = itr.next();
      Iterator<T> children = node.childrenIterator();

      if (!itr.hasNext()) {
        queue.poll();
      }
      if (children.hasNext()) {
        queue.offer(children);
      }
      return node;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported.");
    }
  }

  public static class LeavesIterator<E, T extends Tree<E, T>> implements Iterator<T> {

    private Iterator<T> depthFirstItr;
    private T nextLeaf;

    public LeavesIterator(T rootNode) {
      depthFirstItr = rootNode.depthFirstIterator();
    }

    @Override
    public boolean hasNext() {
      nextLeaf = null;
      while (depthFirstItr.hasNext()) {
        T next = depthFirstItr.next();
        if (next.isLeaf()) {
          nextLeaf = next;
          return true;
        }
      }
      return false;
    }

    @Override
    public T next() {
      return nextLeaf;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported.");
    }
  }

  public static class PostorderIterator<E, T extends Tree<E, T>> implements Iterator<T> {

    private T root;
    private Iterator<T> children;
    private Iterator<T> subtree;

    public PostorderIterator(T rootNode) {
      root = rootNode;
      children = root.childrenIterator();
      subtree = Collections.emptyIterator();
    }

    @Override
    public boolean hasNext() {
      return root != null;
    }

    @Override
    public T next() {
      T retval;

      if (subtree.hasNext()) {
        retval = subtree.next();
      } else if (children.hasNext()) {
        subtree = new PostorderIterator<>(children.next());
        retval = subtree.next();
      } else {
        retval = root;
        root = null;
      }

      return retval;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported.");
    }

  }

  public static class PreorderIterator<E, T extends Tree<E, T>> implements Iterator<T> {

    private final Stack<Iterator<T>> stack;

    public PreorderIterator(T t) {
      stack = new Stack<>();
      stack.push(Iterators.singletonIterator(t));
    }

    @Override
    public boolean hasNext() {
      return (!stack.empty() && stack.peek().hasNext());
    }

    @Override
    public T next() {
      Iterator<T> itr = stack.peek();
      T node = itr.next();
      Iterator<T> childrenItr = node.childrenIterator();

      if (!itr.hasNext()) {
        stack.pop();
      }
      if (childrenItr.hasNext()) {
        stack.push(childrenItr);
      }
      return node;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove() is not supported.");
    }

  }
}