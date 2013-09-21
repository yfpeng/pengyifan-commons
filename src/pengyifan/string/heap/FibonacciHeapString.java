package pengyifan.string.heap;

import pengyifan.heap.FibonacciHeap;
import pengyifan.heap.FibonacciHeapNode;
import pengyifan.string.StringUtils;

public class FibonacciHeapString {

  public static StringBuilder toStringPreOrder(FibonacciHeap heap,
      StringBuilder sb) {
    if (heap.getMin() == null) {
      return sb;
    }
    return toStringPreOrder(heap.getMin(), new StringBuilder());
  }

  private static StringBuilder toStringPreOrder(FibonacciHeapNode node,
      StringBuilder sb) {
    for (FibonacciHeapNode cur : node.nodelist()) {
      String s = new String();
      for (FibonacciHeapNode p = cur.getParent(); p != null; p = p.getParent()) {
        s = StringUtils.BAR + " " + s;
      }
      if (cur.getRight() != node) {
        s += StringUtils.MIDDLE + " ";
      } else {
        s += StringUtils.END + " ";
      }
      sb.append(s + cur + "\n");
      toStringPreOrder(cur.getChild(), sb);
    }
    return sb;
  }
}
