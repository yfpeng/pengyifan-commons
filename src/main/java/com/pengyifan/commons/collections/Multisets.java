package com.pengyifan.commons.collections;

import com.google.common.collect.Multiset;

import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

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
   * are several max counts, random value is returned.
   *
   * @param multiset multiset
   * @return the element in this multiset with the largest count
   */
  public static <E> Optional<E> argmax(Multiset<E> multiset) {
    OptionalInt maybeMax = max(multiset);
    if (maybeMax.isPresent()) {
      return multiset.elementSet().stream()
          .filter(e -> multiset.count(e) == maybeMax.getAsInt())
          .findAny();
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the elements in this multiset with the largest count. Returns
   * empty set if this multiset is empty.
   *
   * @param multiset multiset
   * @return the set of elements in this multiset with the largest count
   */
  public static <E> Set<E> argmaxSet(Multiset<E> multiset) {
    OptionalInt maybeMax = max(multiset);
    if (maybeMax.isPresent()) {
      return elementsAt(multiset, maybeMax.getAsInt());
    } else {
      return Collections.emptySet();
    }
  }

  /**
   * Finds and returns the key in this multiset with the smallest count. If
   * there are several min counts, random value is returned.
   *
   * @param multiset multiset
   * @return the element in this multiset with the smallest count
   */
  public static <E> Optional<E> argmin(Multiset<E> multiset) {
    OptionalInt maybeMin = min(multiset);
    if (maybeMin.isPresent()) {
      return multiset.elementSet().stream()
          .filter(e -> multiset.count(e) == maybeMin.getAsInt())
          .findAny();
    } else {
      return Optional.empty();
    }
  }

  /**
   * Returns the elements in this multiset with the smallest count. Returns empty set if this
   * multiset is empty.
   *
   * @param multiset multiset
   * @return the set of elements in this multiset with the smallest count
   */
  public static <E> Set<E> argminSet(Multiset<E> multiset) {
    OptionalInt maybeMin = min(multiset);
    if (maybeMin.isPresent()) {
      return elementsAt(multiset, maybeMin.getAsInt());
    } else {
      return Collections.emptySet();
    }
  }

  /**
   * Returns the mean of all the counts (total count/distinct element size).
   */
  public static <E> double averageCount(Multiset<E> multiset) {
    return ((double) multiset.size()) / multiset.elementSet().size();
  }

  /**
   * Returns the set of elements whose counts are at or above the given count.
   * This set may have 0 elements but will not be null.
   *
   * @param multiset multiset
   * @param count    count
   * @return the set of elements whose counts are at or above the given count
   */
  public static <E> Set<E> elementsAbove(Multiset<E> multiset, int count) {
    return multiset.elementSet().stream()
        .filter(e -> multiset.count(e) >= count)
        .collect(Collectors.toSet());
  }

  /**
   * Returns the set of elements that have exactly the given count. This set may
   * have 0 elements but will not be null.
   *
   * @param multiset multiset
   * @param count    count
   * @return the set of elements that have exactly the given count
   */
  public static <E> Set<E> elementsAt(Multiset<E> multiset, int count) {
    return multiset.elementSet().stream()
        .filter(e -> multiset.count(e) == count)
        .collect(Collectors.toSet());
  }

  /**
   * Returns the set of elements whose counts are at or below the given count.
   * This set may have 0 elements but will not be null.
   *
   * @param multiset multiset
   * @param count    count
   * @return the set of elements whose counts are at or below the given count
   */
  public static <E> Set<E> elementsBelow(final Multiset<E> multiset, final int count) {
    return multiset.elementSet().stream()
        .filter(e -> multiset.count(e) <= count)
        .collect(Collectors.toSet());
  }

  /**
   * Returns the largest count in this multiset.
   *
   * @param multiset multiset
   * @return the largest count in this multiset
   */
  public static <E> OptionalInt max(final Multiset<E> multiset) {
    return multiset.elementSet().stream()
        .mapToInt(multiset::count)
        .max();
  }

  /**
   * Returns the smallest count in this multiset.
   *
   * @param multiset multiset
   * @return the smallest count in this multiset
   */
  public static <E> OptionalInt min(final Multiset<E> multiset) {
    return multiset.elementSet().stream()
        .mapToInt(multiset::count)
        .min();
  }
}
