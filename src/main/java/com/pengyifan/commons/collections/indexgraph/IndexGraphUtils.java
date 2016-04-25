package com.pengyifan.commons.collections.indexgraph;

import edu.stanford.nlp.ling.HasIndex;
import java.util.List;
import java.util.stream.Collectors;

public class IndexGraphUtils {

  private IndexGraphUtils() {
  }

  public static List<Integer> getIndexList(List<? extends HasIndex> objects) {
    return objects.stream().map(e -> e.index()).collect(Collectors.toList());
  }

}
