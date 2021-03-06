package com.pengyifan.commons.collections.counter;

import java.util.HashMap;

/**
 * @deprecated Use {@link com.google.common.collect.HashMultiset} instead.
 */
@Deprecated
public class HashCounter<K> extends Counter<K> {

  /**
   * Constructs a new (empty) Counter.
   */
  public HashCounter() {
    map = new HashMap<>();
    totalCount = 0;
  }

  /**
   * Constructs a new Counter with the contents of the given Counter.
   * 
   * @param counter
   */
  public HashCounter(Counter<K> counter) {
    this();
    addAll(counter);
  }

  @Override
  public Object clone() {
    return new HashCounter<>(this);
  }
}
