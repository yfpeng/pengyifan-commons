package com.pengyifan.commons.collections.heap;

import com.pengyifan.commons.collections.heap.FibonacciHeap.Entry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test should only be used in the package
 *
 * @author Yifan Peng
 * @version 09/21/2013
 */
public class FibonacciHeapTest {

  FibonacciHeap<Integer> heap;
  Entry<Integer> x46 = new Entry<>(46, 0);
  Entry<Integer> x35 = new Entry<>(35, 0);

  @Before
  public void init() {
    heap = new FibonacciHeap<>();

    int array[] = {23, 7, 21, 3, 18, 39, 52, 38, 41, 17, 30, 24, 26};

    for (int i : array) {
      Entry<Integer> x = new Entry<>(i, 0);
      heap.insert(x);
    }

    heap.insert(x46);
    heap.insert(x35);

    // System.out.println(heap);
  }

  @Test
  public void testExtractMin() {
    Entry<Integer> e = heap.extractMin();
    assertEquals(3, e.key);

    heap.decreaseKey(x46, 15);
    e = heap.extractMin();
    assertEquals(7, e.key);

    heap.decreaseKey(x35, 5);
    e = heap.extractMin();
    assertEquals(5, e.key);
  }
}