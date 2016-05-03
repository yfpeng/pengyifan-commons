package com.pengyifan.commons.collections.counter;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * @deprecated Use {@link com.google.common.collect.TreeMultiset} instead.
 */
public class TreeCounter<K> extends Counter<K> {

  /**
   * Constructs a new (empty) Counter.
   */
  public TreeCounter() {
    map = new TreeMap<>();
    totalCount = 0;
  }

  /**
   * Constructs a new (empty) Counter.
   */
  public TreeCounter(Comparator<? super K> comparator) {
    map = new TreeMap<>(comparator);
    totalCount = 0;
  }

  /**
   * Constructs a new Counter with the contents of the given Counter.
   * 
   * @param counter
   */
  public TreeCounter(Counter<K> counter) {
    this();
    addAll(counter);
  }

  /**
   * Constructs a new Counter with the contents of the given Counter.
   * 
   * @param counter
   */
  public TreeCounter(Counter<K> counter, Comparator<? super K> comparator) {
    this(comparator);
    addAll(counter);
  }

  @Override
  public Object clone() {
    return new TreeCounter<>(this);
  }
}
