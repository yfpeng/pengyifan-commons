package com.pengyifan.commons.collections.counter;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * Defines a collection that counts the number of times an object appears in
 * the collection.
 * 
 * Suppose you have a Counter that contains {a, a, b, c}. Calling
 * getCount(Object) on "a" would return 2, while calling uniqueSet()/keySet()
 * would return {a, b, c}.
 * 
 * It works like a Map, but with different methods for easily
 * getting/setting/incrementing counts for objects and computing various
 * functions with the counts. The Counter constructor and addAll method can be
 * used to copy another Counter's contents over.
 * 
 * Modified according edu.stanford.nlp.stats.IntCounter and
 * org.apache.commons.collections4.bag.AbstractMapBag
 * 
 * TODO: whether to use AtomicInteger or to create a mutable Integer remains a
 * question
 * 
 * @author Yifan Peng
 * @version 10/12/2013
 * 
 */
public abstract class Counter<K> {

  protected static class MutableInteger {

    private int val;

    MutableInteger(int val) {
      this.val = val;
    }

    void set(int val) {
      this.val = val;
    }

    int get() {
      return val;
    }
  }

  protected Map<K, MutableInteger> map;
  protected int                    totalCount;

  /**
   * Adds the counts in the given Counter to the counts in this Counter. To
   * copy the values from another Counter rather than adding them, use
   * 
   * @param counter
   */
  public void addAll(Counter<K> counter) {
    for (K key : map.keySet()) {
      int count = counter.getCount(key);
      incrementCount(key, count);
    }
  }

  /**
   * Finds and returns the key in this Counter with the largest count. If there
   * are several max counts, random value is returned. Returns null if this
   * Counter is empty.
   * 
   * @return the key in this Counter with the largest count
   */
  public K argmax() {
    int max = Integer.MIN_VALUE;
    K argmax = null;
    for (K key : keySet()) {
      int count = getCount(key);
      if (argmax == null || count > max) {
        max = count;
        argmax = key;
      }
    }
    return argmax;
  }

  /**
   * Finds and returns the keys in this Counter with the largest count. Returns
   * empty set if this Counter is empty.
   * 
   * @return the set of keys in this Counter with the largest count
   */
  public Set<K> argmaxSet() {
    int max = Integer.MIN_VALUE;
    Set<K> argmaxSet = new HashSet<K>();
    for (K key : keySet()) {
      int count = getCount(key);
      if (argmaxSet.isEmpty()) {
        max = count;
        argmaxSet.add(key);
      } else if (count > max) {
        max = count;
        argmaxSet.clear();
        argmaxSet.add(key);
      } else if (count == max) {
        argmaxSet.add(key);
      }
    }
    return argmaxSet;
  }

  /**
   * Finds and returns the key in this Counter with the smallest count. If
   * there are several min counts, random value is returned. Returns null if
   * this Counter is empty.
   * 
   * @return the key in this Counter with the smallest count
   */
  public K argmin() {
    int min = Integer.MAX_VALUE;
    K argmin = null;
    for (K key : keySet()) {
      int count = getCount(key);
      if (argmin == null || count < min) {
        min = count;
        argmin = key;
      }
    }
    return argmin;
  }

  /**
   * Finds and returns the keys in this Counter with the smallest count.
   * Returns empty set if this Counter is empty.
   * 
   * @return the set of keys in this Counter with the smallest count
   */
  public Set<K> argminSet() {
    int min = Integer.MAX_VALUE;
    Set<K> argminSet = new HashSet<K>();
    for (K key : keySet()) {
      int count = getCount(key);
      if (argminSet.isEmpty()) {
        min = count;
        argminSet.add(key);
      } else if (count < min) {
        min = count;
        argminSet.clear();
        argminSet.add(key);
      } else if (count == min) {
        argminSet.add(key);
      }
    }
    return argminSet;
  }

  /**
   * Returns the mean of all the counts (totalCount/size).
   * 
   * @return
   */
  public double averageCount() {
    return ((double) totalCount()) / map.size();
  }

  /**
   * Removes all counts from this Counter.
   */
  public void clear() {
    map.clear();
    totalCount = 0;
  }

  public boolean containsKey(K key) {
    return map.containsKey(key);
  }

  /**
   * Subtracts 1 from the count for the given key. If the key hasn't been seen
   * before, it is assumed to have count 0, and thus this method will set its
   * count to -1.
   * <p/>
   * To decrement the count by a value other than 1, use
   * {@link #decrementCount(Object,int)}.
   * <p/>
   * To set a count to a specific value instead of decrementing it, use
   * {@link #setCount(Object,int)}.
   */
  public double decrementCount(K key) {
    return decrementCount(key, 1);
  }

  /**
   * Subtracts the given count from the current count for the given key. If the
   * key hasn't been seen before, it is assumed to have count 0, and thus this
   * method will set its count to the negative of the given amount. Negative
   * increments are equivalent to calling <tt>incrementCount</tt>.
   * <p/>
   * To more conveniently decrement the count by 1, use
   * {@link #decrementCount(Object)}.
   * <p/>
   * To set a count to a specific value instead of decrementing it, use
   * {@link #setCount(Object,int)}.
   */
  public int decrementCount(K key, int count) {
    return incrementCount(key, -count);
  }

  /**
   * Returns a view of the doubles in this map. Can be safely modified.
   * 
   * @return
   */
  public Set<Map.Entry<K, Integer>> entrySet() {
    return new AbstractSet<Map.Entry<K, Integer>>() {

      @Override
      public Iterator<Entry<K, Integer>> iterator() {
        return new Iterator<Entry<K, Integer>>() {

          final Iterator<Entry<K, MutableInteger>> inner = map.entrySet()
                                                             .iterator();

          @Override
          public boolean hasNext() {
            return inner.hasNext();
          }

          @Override
          public Entry<K, Integer> next() {
            return new Map.Entry<K, Integer>() {

              final Entry<K, MutableInteger> e = inner.next();

              @Override
              public K getKey() {
                return e.getKey();
              }

              @Override
              public Integer getValue() {
                return e.getValue().get();
              }

              @Override
              public Integer setValue(Integer value) {
                final int old = e.getValue().get();
                e.getValue().set(value.intValue());
                totalCount = totalCount - old + value.intValue();
                return old;
              }
            };
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }

      @Override
      public int size() {
        return map.size();
      }
    };
  }

  /**
   * Returns the current count for the given key, which is 0 if it hasn't been
   * seen before. This is a convenient version of <code>get</code> that casts
   * and extracts the primitive value.
   * 
   * @param the object to search for
   * @return the number of occurrences of the object, zero if not found
   */
  public int getCount(K key) {
    MutableInteger count = map.get(key);
    if (count == null) {
      return 0;
    }
    return count.get();
  }

  /**
   * Adds 1 to the count for the given key. If the key hasn't been seen before,
   * it is assumed to have count 0, and thus this method will set its count to
   * 1.
   * <p/>
   * To increment the count by a value other than 1, use
   * {@link #incrementCount(Object,int)}.
   * <p/>
   * To set a count to a specific value instead of incrementing it, use
   * {@link #setCount(Object,int)}.
   * 
   * @param key the object to search for
   * @return
   */
  public int incrementCount(K key) {
    return incrementCount(key, 1);
  }

  /**
   * Adds the given count to the current count for the given key. If the key
   * hasn't been seen before, it is assumed to have count 0, and thus this
   * method will set its count to the given amount. Negative increments are
   * equivalent to calling <tt>decrementCount</tt>.
   * <p/>
   * To more conveniently increment the count by 1, use
   * {@link #incrementCount(Object)}.
   * <p/>
   * To set a count to a specific value instead of incrementing it, use
   * {@link #setCount(Object,int)}.
   * 
   * @param key the object to search for
   * @param count the number of copies to add
   * @return
   */
  public int incrementCount(K key, int count) {
    MutableInteger tmpCount = new MutableInteger(0);
    MutableInteger oldCount = map.put(key, tmpCount);
    totalCount += count;
    if (oldCount != null) {
      count += oldCount.get();
    }
    tmpCount.set(count);
    return count;
  }

  /**
   * Returns true if the underlying map is empty.
   * 
   * @return true if there are no elements in this counter
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the set of keys whose counts are at or above the given threshold.
   * This set may have 0 elements but will not be null.
   */
  public Set<K> keysAbove(int countThreshold) {
    Set<K> keys = new HashSet<K>();
    for (K key : keySet()) {
      if (getCount(key) >= countThreshold) {
        keys.add(key);
      }
    }
    return keys;
  }

  /**
   * Returns the set of keys that have exactly the given count. This set may
   * have 0 elements but will not be null.
   */
  public Set<K> keysAt(int count) {
    Set<K> keys = new HashSet<K>();
    for (K key : keySet()) {
      if (getCount(key) == count) {
        keys.add(key);
      }
    }
    return keys;
  }

  /**
   * Returns the set of keys whose counts are at or below the given threshold.
   * This set may have 0 elements but will not be null.
   */
  public Set<K> keysBelow(int countThreshold) {
    Set<K> keys = new HashSet<K>();
    for (K key : keySet()) {
      if (getCount(key) <= countThreshold) {
        keys.add(key);
      }
    }
    return keys;
  }

  /**
   * Returns an unmodifiable view of the underlying map's key set.
   * 
   * @return the set of unique elements in this counter
   */
  public Set<K> keySet() {
    return map.keySet();
  }

  /**
   * Returns an unmodifiable view of the underlying map's key set.
   * 
   * @return the set of unique elements in this counter
   */
  public Set<K> uniqueSet() {
    return Collections.unmodifiableSet(keySet());
  }

  /**
   * Finds and returns the largest count in this Counter.
   */
  public int max() {
    int max = Integer.MIN_VALUE;
    for (K key : keySet()) {
      max = Math.max(max, getCount(key));
    }
    return max;
  }

  /**
   * Finds and returns the smallest count in this Counter.
   */
  public int min() {
    int min = Integer.MAX_VALUE;
    for (K key : keySet()) {
      min = Math.min(min, getCount(key));
    }
    return min;
  }

  /**
   * Removes the given key from this Counter. Its count will now be 0 and it
   * will no longer be considered previously seen.
   * 
   * @return
   */
  public int remove(K key) {
    // subtract removed count from total (may be 0)
    totalCount -= getCount(key);
    MutableInteger val = map.remove(key);
    if (val == null) {
      return Integer.MIN_VALUE;
    } else {
      return val.get();
    }
  }

  /**
   * Removes all the given keys from this Counter.
   */
  public void removeAll(Collection<K> c) {
    for (K key : c) {
      remove(key);
    }
  }

  /**
   * Removes all keys whose count is 0. After incrementing and decrementing
   * counts or adding and subtracting Counters, there may be keys left whose
   * count is 0, though normally this is undesirable. This method cleans up the
   * map.
   * 
   * Maybe in the future we should try to do this more on-the-fly, though it's
   * not clear whether a distinction should be made between "never seen" (i.e.
   * null count) and "seen with 0 count". Certainly there's no distinction in
   * getCount() but there is in containsKey().
   */
  public void removeZeroCounts() {
    Iterator<K> itr = keySet().iterator();
    while (itr.hasNext()) {
      K key = itr.next();
      int count = getCount(key);
      if (count == 0) {
        itr.remove();
      }
    }
  }

  /**
   * Sets the current count for the given key. This will wipe out any existing
   * count for that key.
   * 
   * To add to a count instead of replacing it, use
   * {@link #incrementCount(Object,int)}.
   */
  public void setCount(K key, int count) {
    MutableInteger tmpCount = new MutableInteger(count);
    tmpCount = map.put(key, tmpCount);
    totalCount += count;
    if (tmpCount != null) {
      totalCount -= tmpCount.get();
    }
  }

  /**
   * Returns the number of elements in this counter.
   * 
   * @return the number of elements in this counter
   */
  public int size() {
    return map.size();
  }

  /**
   * Subtracts the counts in the given Counter from the counts in this Counter.
   * To copy the values from another Counter rather than subtracting them, use
   * 
   * @param counter
   */
  public void substractAll(Counter<K> counter) {
    for (K key : map.keySet()) {
      int count = counter.getCount(key);
      decrementCount(key, count);
    }
  }

  /**
   * Returns the current total count for all objects in this Counter. All
   * counts are summed each time, so cache it if you need it repeatedly.
   * 
   * @return
   */
  public int totalCount() {
    return totalCount;
  }

  /**
   * Implement a toString() method suitable for debugging.
   * 
   * @return a debugging toString
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("[");
    Iterator<K> i = uniqueSet().iterator();
    while (i.hasNext()) {
      K current = i.next();
      int count = getCount(current);
      buf.append(count);
      buf.append(":");
      buf.append(current);
      if (i.hasNext()) {
        buf.append(",");
      }
    }
    buf.append("]");
    return buf.toString();
  }

  /**
   * Returns true if the given object is not null, has the precise type of this
   * counter, and contains the same number of occurrences of all the same
   * elements.
   * 
   * @param the object to test for equality
   * @return true if that object equals this counter
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof Counter == false) {
      return false;
    }
    @SuppressWarnings("unchecked")
    Counter<K> other = (Counter<K>) obj;
    if (other.size() != size()) {
      return false;
    }
    for (final K key : keySet()) {
      if (other.getCount(key) != getCount(key)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets a hash code for the Bag compatible with the definition of equals. The
   * hash code is defined as the sum total of a hash code for each element. The
   * per element hash code is defined as
   * <code>(e==null ? 0 : e.hashCode()) ^ noOccurances)</code>. This hash code
   * is compatible with the Set interface.
   * 
   * @return the hash code of the underlying map
   */
  @Override
  public int hashCode() {
    int total = 0;
    for (final Entry<K, MutableInteger> entry : map.entrySet()) {
      final K key = entry.getKey();
      final MutableInteger count = entry.getValue();
      total += (key == null ? 0 : key.hashCode()) ^ count.get();
    }
    return total;
  }

}
