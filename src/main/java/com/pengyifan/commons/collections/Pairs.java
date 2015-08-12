package com.pengyifan.commons.collections;

import com.google.common.collect.Lists;
import org.javatuples.Pair;

import java.util.Collection;
import java.util.List;

public class Pairs {

  public static <E> Collection<Pair<E, E>> getPairs(Collection<E> set) {
    List<Pair<E, E>> pairs = Lists.newArrayList();

    List<E> list = Lists.newArrayList(set);
    for (int i = 0; i < list.size(); i++) {
      E e1 = list.get(i);
      for (int j = i + 1; j < list.size(); j++) {
        E e2 = list.get(j);
        pairs.add(Pair.with(e1, e2));
      }
    }

    return pairs;
  }
}
