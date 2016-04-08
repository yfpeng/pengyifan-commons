package com.pengyifan.commons.math;

import static com.google.common.base.Preconditions.checkArgument;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/**
 * This implementation assumes that the vector requires sparse backing storage.
 */
public class SparseRealVector {

  private static final double DEFAULT_VALUE = 0;
  private static final double PRECISION = 0.000001;
  private static final DecimalFormat FORMAT = new DecimalFormat("#0.#####");

  private final int dimension;
  private final ConcurrentMap<Integer, Double> map;

  /**
   * Construct a vector of zeroes.
   * 
   * @param dimension Size of the vector.
   */
  public SparseRealVector(int dimension) {
    this.dimension = dimension;
    map = Maps.newConcurrentMap();
  }

  /**
   * Set a single element.
   *
   * @param index element index.
   * @param value new value for the element.
   * @see #getEntry(int)
   */
  public void setEntry(int index, double value) {
    checkArgument(index >= 0 && index < dimension,
        "Index is out of dimension: %s", index);
    if (Precision.equals(value, DEFAULT_VALUE, PRECISION)) {
      map.remove(index);
    } else {
      map.put(index, value);
    }
  }

  /**
   * Return the entry at the specified index.
   * 
   * @param index Index location of entry to be fetched.
   * @return the vector entry at {@code index}.
   */
  public double getEntry(int index) {
    checkArgument(
        index >= 0 && index < dimension,
        "Index is out of dimension: %s",
        index);
    return map.getOrDefault(index, DEFAULT_VALUE);
  }

  /**
   * Returns the size of the vector.
   * 
   * @return the size of the vector.
   */
  public int getDimension() {
    return dimension;
  }

  /**
   * Compute the dot product of this vector with {@code v}.
   *
   * @param v Vector with which dot product should be computed
   * @return the scalar dot product between this instance and {@code v}.
   */
  public double dotProduct(SparseRealVector v) {
    checkArgument(
        dimension == v.dimension,
        "Two vectors have different dimensions (%s != %s)",
        dimension,
        v.dimension);
    return map.keySet().parallelStream().map(index -> {
      double vvalue = v.getEntry(index);
      return Precision.equals(vvalue, DEFAULT_VALUE, PRECISION)
          ?
          0 :
          getEntry(index) * v.getEntry(index);
    }).reduce(0d, Double::sum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, map);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SparseRealVector other = (SparseRealVector) obj;
    if (dimension != other.dimension) {
      return false;
    }
    if (map == null) {
      if (other.map != null) {
        return false;
      }
    } else if (!map.equals(other.map)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner(" ");
    List<Entry<Integer, Double>> entries = map.entrySet().stream()
        .sorted((e1, e2) ->
            Integer.compare(e1.getKey(), e2.getKey())
        ).collect(Collectors.toList());

    for (Entry<Integer, Double> entry : entries) {
      if (Precision.equals(entry.getValue(), DEFAULT_VALUE)) {
        continue;
      }
      sj.add(entry.getKey() + ":" + FORMAT.format(entry.getValue()));
    }
    return sj.toString();
  }

  /**
   * Creates a unit vector pointing in the direction of this vector. The
   * instance is changed by this method.
   * 
   * @throws MathArithmeticException if the norm is zero.
   */
  public void normalize() {
    double norm = getNorm();
    checkArgument(norm != 0, "norm is zero.");

    for (Entry<Integer, Double> entry : map.entrySet()) {
      if (Precision.equals(entry.getValue(), DEFAULT_VALUE)) {
        continue;
      }
      entry.setValue(entry.getValue() / norm);
    }
  }

  /**
   * Returns the L<sub>2</sub> norm of the vector.
   * <p>
   * The L<sub>2</sub> norm is the root of the sum of the squared elements.
   * </p>
   *
   * @return the norm.
   */
  public double getNorm() {
    double sum = 0;
    for (Entry<Integer, Double> entry : map.entrySet()) {
      if (Precision.equals(entry.getValue(), DEFAULT_VALUE)) {
        continue;
      }
      sum += entry.getValue() * entry.getValue();
    }
    return FastMath.sqrt(sum);
  }

  /**
   * Returns true is the vector is empty, or all elements is zero
   */
  public boolean isZero() {
    for (Entry<Integer, Double> entry : map.entrySet()) {
      if (!Precision.equals(entry.getValue(), DEFAULT_VALUE)) {
        return false;
      }
    }
    return true;
  }

  // public int numOfNonZeros() {
  // int count = 0;
  // for (Entry<Integer, Double> entry : map.entrySet()) {
  // if (Precision.equals(entry.getValue(), DEFAULT_VALUE)) {
  // continue;
  // }
  // count++;
  // }
  // return count;
  // }

  public List<Entry<Integer, Double>> toList() {
    return map.entrySet().stream()
        .filter(entry -> !Precision.equals(entry.getValue(), DEFAULT_VALUE))
        .sorted((e1, e2) -> Integer.compare(e1.getKey(), e2.getKey()))
        .collect(Collectors.toList());
  }

  protected Map<Integer, Double> getMap() {
    return map;
  }
}