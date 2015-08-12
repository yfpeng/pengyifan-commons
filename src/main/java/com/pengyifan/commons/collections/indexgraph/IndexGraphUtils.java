package com.pengyifan.commons.collections.indexgraph;

import java.util.List;
import java.util.stream.Collectors;

public class IndexGraphUtils {

  private IndexGraphUtils() {
  }

  public static List<Integer> getIndexList(List<? extends IndexObject> objects) {
    return objects.stream().map(e -> e.getIndex()).collect(Collectors.toList());
  }

}
