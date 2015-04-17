package com.pengyifan.commons.collections;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.AbstractList;

/**
 * This class contains static utility methods that operate on or return objects
 * of type Iterable.
 */
public class MyIterables {
  /**
   * Creates an iterable combining the first element and an array
   * 
   * @param first first element
   * @param rest rest element in an array
   * @return combined iterable
   */
  public static <E> Iterable<E> iterable(final E first, final E[] rest) {
    checkNotNull(rest);
    return new AbstractList<E>() {

      @Override
      public int size() {
        return rest.length + 1;
      }

      @Override
      public E get(int index) {
        switch (index) {
        case 0:
          return first;
        default:
          return rest[index - 1];
        }
      }
    };
  }
}
