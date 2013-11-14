package pengyifan.collection;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ListUtils {

  /**
   * Given a sequence of s1,...,sn, find the first subsequence si1 < si2 < ...<
   * sik with i1 < ... < ik so that k is as large as possible.
   * 
   * O(n^2)
   * 
   * @param list
   * @return
   */
  public static <E extends Comparable<E>> List<E> longestIncreasingSubsequence(
      List<E> list) {
    return longestIncreasingSubsequence(list, new Comparator<E>() {

      @Override
      public int compare(E o1, E o2) {
        return o1.compareTo(o2);
      }
    });
  }

  /**
   * Given a sequence of s1,...,sn, find the first subsequence si1 < si2 < ...<
   * sik with i1 < ... < ik so that k is as large as possible.
   * 
   * O(n^2)
   * 
   * @param list
   * @param comp
   * @return
   */
  public static <E> List<E> longestIncreasingSubsequence(List<E> list,
      Comparator<E> comp) {
    /**
     * length of longest increasing subsequence in s1,...,sn that ends in si
     */
    int l[] = new int[list.size()];
    /**
     * by following the p[j] values we can reconstruct the whole sequence in
     * linear time.
     */
    int p[] = new int[list.size()];
    for (int j = 0; j < list.size(); j++) {
      E sj = list.get(j);
      l[j] = 1;
      p[j] = -1;
      for (int i = 0; i < j; i++) {
        E si = list.get(i);
        if (comp.compare(si, sj) < 0 && l[i] + 1 > l[j]) {
          p[j] = i;
          l[j] = l[i] + 1;
        }
      }
    }
    // find j such that L[j] is largest
    int maxIndex = 0;
    int maxLength = l[0];
    for (int i = 1; i < l.length; i++) {
      if (maxLength < l[i]) {
        maxLength = l[i];
        maxIndex = i;
      }
    }
    // walk backwards through P[j] pointers to find the sequence
    LinkedList<E> lis = new LinkedList<E>();
    lis.addFirst(list.get(maxIndex));
    int j = maxIndex;
    while (p[j] != -1) {
      lis.addFirst(list.get(p[j]));
      j = p[j];
    }

    return lis;
  }
}
