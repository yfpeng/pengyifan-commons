package com.pengyifan.commons.collections.tree2;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.pengyifan.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkArgument;
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
    child.setParent((T) this);
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
    return new BreadthFirstIterator(this);
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
    Iterator<T> itr = breadthFirstIterator();
    T last = Iterators.getLast(itr, null);
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
    T node = (T) this;

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
    T node = (T) this;

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
        .map(t -> t.getObject())
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
    T ancestor = (T) this;
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
    T retval;
    if (parent == null) {
      retval = null;
    } else {
      retval = parent.getChildAfter((T) this); // linear search
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
    for (T p = (T) this; p != null; p = p.getParent()) {
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
   * not a child of this node, returns <code>-1</code>. This method performs a linear search and is
   * O(n) where n is the number of children.
   *
   * @param child the Tree to search for among this node's children
   * @return an int giving the index of the node in this node's child array, or <code>-1</code> if
   * the specified node is a not a child of this node
   * @throws NullPointerException if <code>child</code> is null
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

    T ancestor = (T) this;

    do {
      if (ancestor == anotherNode) {
        return true;
      }
    } while ((ancestor = ancestor.getParent()) != null);

    return false;
  }

  /**
   * Returns true if <code>t</code> is a child of this node. If <code>t</code> is null,
   * this method returns false.
   *
   * @return true if <code>t</code> is a child of this node; false if <code>t</code> is null
   */
  public boolean isNodeChild(T t) {
    boolean retval = false;

    if (t == null) {
      retval = false;
    } else {
      if (getChildCount() == 0) {
        retval = false;
      } else {
        retval = (t.getParent() == this);
      }
    }

    return retval;
  }

  /**
   * Returns true if <code>anotherNode</code> is a sibling of (has the same parent as) this node. A
   * node is its own sibling. If <code>anotherNode</code> is null, returns false.
   *
   * @param anotherNode node to test as sibling of this node
   * @return true if <code>anotherNode</code> is a sibling of this node
   */
  public boolean isNodeSibling(T anotherNode) {
    boolean retval = false;

    if (anotherNode == null) {
      retval = false;
    } else if (anotherNode == this) {
      retval = true;
    } else {
      Tree myParent = getParent();
      retval = (myParent != null && myParent == anotherNode.getParent());

      if (retval && !(getParent()).isNodeChild(anotherNode)) {
        throw new Error("sibling has different parent");
      }
    }

    return retval;
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
    return new LeavesIterator(this);
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
    return new PostorderIterator(this);
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
    return new PreorderIterator(this);
  }

  public List<T> preorderList() {
    return Lists.newArrayList(preorderIterator());
  }

  /**
   * Removes the child at the specified index from this node's children and
   * sets that node's parent to null. The child node to remove must be a
   * <code>MutableTreeNode</code>.
   *
   * @param childIndex the index in this node's child array of the child to
   *                   remove
   * @throws ArrayIndexOutOfBoundsException if <code>childIndex</code> is
   *                                        out of bounds
   */
  public void remove(int childIndex) {
    Tree child = getChild(childIndex);
    children.remove(childIndex);
    child.setParent(null);
  }

  /**
   * Removes <code>aChild</code> from this node's child array, giving it a null
   * parent.
   *
   * @param aChild a child of this node to remove
   * @throws IllegalArgumentException if <code>aChild</code> is null or is
   *                                  not a child of this node
   */
  public void remove(T aChild) {
    if (aChild == null) {
      throw new IllegalArgumentException("argument is null");
    }

    if (!isNodeChild(aChild)) {
      throw new IllegalArgumentException("argument is not a child");
    }
    remove(indexOf(aChild)); // linear search
  }

  public void reversal() {
    if (children == null) {
      return;
    } else {
      Collections.reverse(this.children);
      for (Tree child : children) {
        child.reversal();
      }
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
   * Sets this node's parent to <code>newParent</code> but does not change the parent's child
   * array. This method is called from <code>insert()</code> and <code>remove()</code> to reassign
   * a child's parent, it should not be messaged from anywhere else.
   *
   * @param newParent this node's new parent
   */
  public void setParent(T newParent) {
    parent = newParent;
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
  public List<Tree> getDominationPath(T t) {
    Tree[] result = getDominationPath(t, 0);
    if (result == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(result);
  }

  T[] getDominationPath(T t, int depth) {
    if (this == t) {
      T[] result = (T[]) Array.newInstance(this.getClass(), depth + 1);
      result[depth] = (T) this;
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
        result[depth] = (T) this;
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
    return toString(SIMPLE_PRINT);
  }

  /**
   * An intermediary "Formatter" interface. An implementation of this interface
   * outputs the data of {@link Tree} formatted as appropriate.
   */
  public String toString(Function<Tree, String> formatter) {
    return formatter.apply(this);
  }


  public static final Function<Tree, String> SIMPLE_PRINT =
      new Function<Tree, String>() {

        @Override
        public String apply(Tree tree) {
          if (tree.obj == null) {
            return null;
          } else {
            return tree.obj.toString();
          }
        }
      };

  /**
   * Given a <code>Tree</code> structure, <code>TreeString</code> will
   * print a string like
   * <p>
   * <pre>
   * └ a
   *   ├ b
   *   ├ c
   *   │ ├ e
   *   │ └ f
   *   └ d
   * </pre>
   *
   * @author Yifan Peng
   */
  public static final Function<Tree, String> PRETTY_PRINT =
      new Function<Tree, String>() {

        @Override
        public String apply(Tree tree) {
          StringBuilder sb = new StringBuilder();
          Iterator<Tree> itr = tree.preorderIterator();
          while (itr.hasNext()) {
            Tree tn = itr.next();
            // add prefix
            for (Object p : tn.getPathFromRoot()) {
              // if parent has sibling node
              if (p == tn) {
                ;
              } else if (((Tree) p).hasNextSiblingNode()) {
                sb.append(StringUtils.BAR + " ");
              } else {
                sb.append("  ");
              }
            }
            // if root has sibling node
            if (tn.hasNextSiblingNode()) {
              sb.append(StringUtils.MIDDLE + " ");
            } else {
              sb.append(StringUtils.END + " ");
            }
            sb.append(tn.getObject() + "\n");

          }
          return sb.toString();
        }
      };

}