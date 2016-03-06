package com.pengyifan.commons.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Implementations of {@link Collector} that implement operations to accumulate
 * elements into immutable collections.
 * <p>
 * The following are examples of using the predefined collectors to perform
 * common immutable reduction tasks:
 * <p>
 * <pre>
 * // Accumulate names into a ImmutableList
 * List&lt;String&gt; list = people.stream().map(Person::getName)
 *     .collect(ImmutableCollectors.toList());
 *
 * // Accumulate names into a ImmutableSet
 * Set&lt;String&gt; set = people.stream().map(Person::getName)
 *     .collect(ImmutableCollectors.toSet());
 * </pre>
 */
public final class ImmutableCollectors {

  private ImmutableCollectors() throws InstantiationException {
    throw new InstantiationException("This class is not for instantiation");
  }

  /**
   * Returns a {@link Collector} that accumulates the input elements into a new
   * {@link ImmutableList}.
   *
   * @param <T> the type of the input elements
   * @return a {@link Collector} which collects all the input elements into a
   * {@link ImmutableList}, in encounter order
   */
  public static <T> Collector<T, ?, ImmutableList<T>> toList() {
    return Collector.of(
        ImmutableList.Builder::new,
        ImmutableList.Builder::add,
        (left, right) -> left.addAll(right.build()),
        (Function<ImmutableList.Builder<T>, ImmutableList<T>>) ImmutableList.Builder::build);
  }

  /**
   * Returns a {@link Collector} that accumulates the input elements into a new
   * {@link ImmutableSet}.
   *
   * @param <T> the type of the input elements
   * @return a {@link Collector} which collects all the input elements into a
   * {@link ImmutableSet}
   */
  public static <T> Collector<T, ?, ImmutableSet<T>> toSet() {
    return Collector.of(
        ImmutableSet.Builder::new,
        ImmutableSet.Builder::add,
        (left, right) -> left.addAll(right.build()),
        (Function<ImmutableSet.Builder<T>, ImmutableSet<T>>) ImmutableSet.Builder::build,
        Collector.Characteristics.UNORDERED);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into a
   * {@code ImmutableMap} whose keys and values are the result of applying the
   * provided mapping functions to the input elements.
   * <p>
   * If the mapped keys contains duplicates (according to
   * {@link Object#equals(Object)}), the last element will be stored.
   *
   * @param <T>         the type of the input elements
   * @param <K>         the output type of the key mapping function
   * @param <V>         the output type of the value mapping function
   * @param keyMapper   a mapping function to produce keys
   * @param valueMapper a mapping function to produce values
   * @return a {@link Collector} which collects elements into a
   * {@link ImmutableMap} whose keys and values are the result of
   * applying mapping functions to the input elements
   */
  public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper) {

    BiConsumer<ImmutableMap.Builder<K, V>, T> accumulator =
        (map, element) -> map.put(keyMapper.apply(element),
            valueMapper.apply(element));

    return Collector.of(
        ImmutableMap.Builder::new,
        accumulator,
        (left, right) -> left.putAll(right.build()),
        ImmutableMap.Builder::build);
  }

}
