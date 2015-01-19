package com.pengyifan.commons.math;

public class RangeUtils {

  /**
   * Returns true if range1 <= range2.
   */
  public static <E extends Comparable<E>> boolean isBefore(
      org.apache.commons.lang3.Range<E> range1,
      org.apache.commons.lang3.Range<E> range2) {
    return range1.getMinimum().compareTo(range2.getMinimum()) <= 0;
  }

  /**
   * Returns true if range1 <= range2.
   */
  public static <E extends Comparable<E>> boolean isBefore(
      com.google.common.collect.Range<E> range1,
      com.google.common.collect.Range<E> range2) {
    return range1.lowerEndpoint().compareTo(range2.lowerEndpoint()) <= 0;
  }

  /**
   * Checks whether this range is overlapped by the specified range.
   * 
   * Two ranges overlap if there is at least one element in common.
   */
  public static <E extends Comparable<E>> boolean isOverlapped(
      com.google.common.collect.Range<E> range1,
      com.google.common.collect.Range<E> range2
      ) {
    return range1.isConnected(range2) && !range1.intersection(range2).isEmpty();
  }

  public static <E extends Comparable<E>>
      com.google.common.collect.Range<E>
      getGoogleRange(org.apache.commons.lang3.Range<E> range) {
    return com.google.common.collect.Range.closedOpen(
        range.getMinimum(),
        range.getMaximum());
  }
}
