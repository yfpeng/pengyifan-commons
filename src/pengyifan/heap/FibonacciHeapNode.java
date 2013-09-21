package pengyifan.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a node of the Fibonacci heap. It holds the information necessary
 * for maintaining the structure of the heap. It also holds the reference to
 * the key value (which is used to determine the heap structure).
 * 
 * @author Yifan Peng
 * @version 09/06/2011
 */
public abstract class FibonacciHeapNode {

  /**
   * Any one of its children
   */
  FibonacciHeapNode child;

  /**
   * Left sibling
   */
  FibonacciHeapNode left;

  /**
   * Its parent
   */
  FibonacciHeapNode parent;

  /**
   * Right sibling
   */
  FibonacciHeapNode right;

  /**
   * Whether this node has lost a child since the last time this node was made
   * the child of another node.
   */
  boolean           mark;

  /**
   * Key value for this node
   */
  int               key;

  /**
   * The number of children in the child list
   */
  int               degree;

  public FibonacciHeapNode(int key) {
    this.key = key;
  }

  public FibonacciHeapNode getLeft() {
    return left;
  }

  public FibonacciHeapNode getRight() {
    return right;
  }

  public FibonacciHeapNode getParent() {
    return parent;
  }

  public FibonacciHeapNode getChild() {
    return child;
  }

  /**
   * 
   * @param node
   * 
   * @return node list of the same level
   */
  public List<FibonacciHeapNode> nodelist() {
    List<FibonacciHeapNode> list = new ArrayList<FibonacciHeapNode>();
    list.add(this);
    FibonacciHeapNode next = right;
    while (next != this) {
      list.add(next);
      next = next.right;
    }
    return list;
  }

  @Override
  public String toString() {
    return String.valueOf(key) + ":" + String.valueOf(mark);
  }
}
