package com.pengyifan.commons.math;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.Validate;

public class RangeUtils {

  /**
   * Returns true if range1 &le; range2.
   */
  public static <E extends Comparable<E>> boolean isBefore(
      org.apache.commons.lang3.Range<E> range1,
      org.apache.commons.lang3.Range<E> range2) {
    return range1.getMinimum().compareTo(range2.getMinimum()) <= 0;
  }

  /**
   * Returns true if range1 &le; range2.
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
  
  // [xxx..xxx]
  public static final Pattern RANGE_PATTERN = Pattern
      .compile("\\[(.+)([.]{2})(.+)\\]");

  public static Range<Integer> parseRangeInt(String str) {
    Matcher m = RANGE_PATTERN.matcher(str);
    Validate.isTrue(m.find(), "Illegal range str: %s", str);
    return Range.between(
        Integer.parseInt(m.group(1)),
        Integer.parseInt(m.group(3)));
  }

  public static Range<Double> parseRangeDouble(String str) {
    Matcher m = RANGE_PATTERN.matcher(str);
    Validate.isTrue(m.find(), "Illegal range str: %s", str);
    return Range.between(
        Double.parseDouble(m.group(1)),
        Double.parseDouble(m.group(3)));
  }
}
