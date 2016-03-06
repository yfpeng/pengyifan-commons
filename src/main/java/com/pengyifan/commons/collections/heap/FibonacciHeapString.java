package com.pengyifan.commons.collections.heap;

import com.pengyifan.commons.lang.StringUtils;

public final class FibonacciHeapString {

  private FibonacciHeapString() throws InstantiationException {
    throw new InstantiationException("This class is not for instantiation");
  }

  public static <E> StringBuilder toString(FibonacciHeap<E> heap, StringBuilder sb) {
    if (heap.minimum() == null) {
      return sb;
    }
    return toStringPreOrder(heap.minimum(), new StringBuilder());
  }

  private static <E> StringBuilder toStringPreOrder(FibonacciHeap.Entry<E> node,
      StringBuilder sb) {
    if (node != null) {
      for (FibonacciHeap.Entry<E> cur : node.nodelist()) {
        String s = "";
        for (FibonacciHeap.Entry<E> p = cur.parent; p != null; p = p.parent) {
          s = StringUtils.BAR + " " + s;
        }
        if (cur.right != node) {
          s += StringUtils.MIDDLE + " ";
        } else {
          s += StringUtils.END + " ";
        }
        sb.append(s).append(cur).append('\n');
        toStringPreOrder(cur.child, sb);
      }
    }
    return sb;
  }
}
