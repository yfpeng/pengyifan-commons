package pengyifan.heap;

import pengyifan.string.heap.FibonacciHeapString;


/**
 * This test should only be used in the package
 * 
 * @author Yifan Peng
 * @version 09/21/2013
 */
public class FibonacciHeapTest {

  public static void main(String args[]) {

    FibonacciHeap heap = new FibonacciHeap();

    Node x;
    Node subx;
    Node subsubx;
    Node x26, x18, x39, x46, x35;

    x = new Node(23, 0);
    heap.insert(x);

    x = new Node(7, 0);
    heap.insert(x);

    x = new Node(21, 0);
    heap.insert(x);

    // 3
    x = new Node(3, 0);
    heap.insert(x);
    x18 = subx = new Node(18, 0);
    addsubNode(heap, x, subx);
    x39 = subsubx = new Node(39, 0);
    addsubNode(heap, subx, subsubx);

    subx = new Node(52, 0);
    addsubNode(heap, x, subx);

    subx = new Node(38, 0);
    addsubNode(heap, x, subx);
    subsubx = new Node(41, 0);
    addsubNode(heap, subx, subsubx);

    // 17
    x = new Node(17, 0);
    heap.insert(x);
    subx = new Node(30, 0);
    addsubNode(heap, x, subx);

    // 24
    x = new Node(24, 0);
    heap.insert(x);
    x26 = subx = new Node(26, 0);
    addsubNode(heap, x, subx);

    x35 = subsubx = new Node(35, 0);
    addsubNode(heap, subx, subsubx);

    x46 = subx = new Node(46, 0);
    addsubNode(heap, x, subx);

    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    // action of extract-min
    heap.extractMin();
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    x26.mark = true;
    x18.mark = true;
    x39.mark = true;

    // The node with key 46 has its key decreased to 15.
    heap.decreaseKey(x46, 15);
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    // The node with key 35 has its key decreased to 5.
    heap.decreaseKey(x35, 5);
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();
  }

  private static void addsubNode(FibonacciHeap heap, Node x, Node subx) {
    heap.insert(subx);
    heap.link(subx, x);
  }
}

class Node extends FibonacciHeapNode {

  int val;

  Node(int key, int val) {
    super(key);
    this.val = val;
  }
}