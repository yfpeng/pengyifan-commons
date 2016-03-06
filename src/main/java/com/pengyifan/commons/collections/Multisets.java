package com.pengyifan.commons.collections;

import com.google.common.collect.Multiset;

/**
 * Provides static utility methods for creating and working with {@link
 * com.google.common.collect.Multiset} instances.
 *
 * @author "Yifan Peng"
 * @since 0.3.0
 */
public abstract class Multisets {
  /**
   * Returns the element in this multiset with the largest count. If there
   * are several max counts, random value is returned. Returns null if this
   * multiset is empty.
   *
   * @return the key in this Counter with the largest count
   */
  public static <E> E argmax(Multiset<E> multiset) {
    int max = Integer.MIN_VALUE;
    E argmax = null;
    for (E e : multiset.elementSet()) {
      int count = multiset.count(e);
      if (argmax == null || count > max) {
        max = count;
        argmax = e;
      }
    }
    return argmax;
  }
}
