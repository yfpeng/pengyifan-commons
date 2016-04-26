package com.pengyifan.commons.collections.heap;

import com.google.common.base.Function;
import com.pengyifan.commons.lang.StringUtils;

import javax.annotation.Nullable;

public final class FibonacciHeapPrettyFormatter<E> implements Function<FibonacciHeap<E>, String>{

  @Nullable
  @Override
  public String apply(@Nullable FibonacciHeap heap) {
    if (heap.minimum() == null) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    toStringPreOrder(heap.minimum(), sb);
    return sb.toString();
  }

  private <E> StringBuilder toStringPreOrder(FibonacciHeap.Entry<E> node,
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
