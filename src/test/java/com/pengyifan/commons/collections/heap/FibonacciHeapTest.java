package com.pengyifan.commons.collections.heap;

import com.pengyifan.commons.collections.heap.FibonacciHeap.Entry;

/**
 * This test should only be used in the package
 * 
 * @author Yifan Peng
 * @version 09/21/2013
 */
public class FibonacciHeapTest {

  public static void main(String args[]) {

    FibonacciHeap heap = new FibonacciHeap();
    
    int array[] = {23,7,21,3,18,39,52,38,41,17,30,24,26};

    for(int i: array) {
      Entry x = new Entry(i, 0);
      heap.insert(x);
    }
    
    Entry x46 = new Entry(46, 0);
    heap.insert(x46);
    
    Entry x35 = new Entry(35, 0);
    heap.insert(x35);

    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    // action of extract-min
    heap.extractMin();
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    // The node with key 46 has its key decreased to 15.
    heap.decreaseKey(x46, 15);
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();

    // The node with key 35 has its key decreased to 5.
    heap.decreaseKey(x35, 5);
    System.out.println(FibonacciHeapString.toString(heap, new StringBuilder()));
    System.out.println();
  }
}