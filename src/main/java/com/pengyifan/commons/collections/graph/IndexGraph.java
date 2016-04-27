package com.pengyifan.commons.collections.graph;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.pengyifan.commons.collections.graph.DirectedLoopGraph;
import edu.stanford.nlp.ling.HasIndex;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class IndexGraph<V extends HasIndex, E extends HasIndex>
    extends DirectedLoopGraph<V, E> {

  private int maxIndex;

  public IndexGraph(Class<E> edgeClass) {
    super(edgeClass);
    maxIndex = 0;
  }

  public boolean containsVertex(int index) {
    return vertexSet().stream()
        .filter(v -> v.index() == index)
        .findFirst()
        .isPresent();
  }

  @Override
  public boolean addEdge(V source, V target, E e) {
    boolean b = super.addEdge(source, target, e);
    if (b) {
      updateIndex(e.index());
    }
    return b;
  }

  @Override
  public boolean addVertex(V v) {
    boolean b = super.addVertex(v);
    if (b) {
      updateIndex(v.index());
    }
    return b;
  }

  @Override
  public boolean addSelfLoop(V v, E e) {
    boolean b = super.addSelfLoop(v, e);
    if (b) {
      updateIndex(e.index());
    }
    return b;
  }

  public int getMaxIndex() {
    return maxIndex;
  }

  public V getVertex(int index) {
    return Iterables.getOnlyElement(vertexSet()
        .stream()
        .filter(v -> v.index() == index)
        .collect(Collectors.toList()));
  }

  private void updateIndex(int index) {
    if (index > this.maxIndex) {
      this.maxIndex = index;
    }
  }

  public E getEdge(int index) {
    return Iterables.getOnlyElement(edgeSet()
        .stream()
        .filter(e -> e.index() == index)
        .collect(Collectors.toList()));
  }

  public void checkHashCode() {
    for (E e : edgeSet()) {
      V s1 = getEdgeSource(e);
      V s2 = getVertex(s1.index());
      checkArgument(
          s1 == s2,
          "edge source has different ends[%s]\nhashCode=%s,%s\nhashCode=%s,%s",
          e,
          System.identityHashCode(s1),
          s1,
          System.identityHashCode(s2),
          s2);
      V t1 = getEdgeTarget(e);
      V t2 = getVertex(t1.index());
      checkArgument(t1 == t2, "edge source has different ends[%s]\n%s\n%s", e,
          t1, t2);
    }
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("\n");
    // vertex
    sj.add("Vertices:");
    vertexSet().stream()
        .sorted((v1, v2) -> Integer.compare(v1.index(), v2.index()))
        .forEach(v -> sj.add(v.toString()));
    // edge
    sj.add("");
    sj.add("Edges:");
    edgeSet().stream()
        .sorted((e1, e2) -> Integer.compare(e1.index(), e2.index()))
        .forEach(e -> sj.add(
            String.format("src=%d,dst=%d,edge=%s",
                getEdgeSource(e).index(),
                getEdgeTarget(e).index(),
                e)));
    return sj.toString();
  }

  @Override
  public boolean containsVertex(V v) {
    return containsVertex(v.index());
  }

  @Override
  public boolean containsEdge(E e) {
    return edgeSet().stream()
        .filter(edge -> e.index() == edge.index())
        .findFirst()
        .isPresent();
  }

  public void validateIndex() {
    Set<Integer> indices = Sets.newHashSet();
    for (V v : vertexSet()) {
      checkArgument(!indices.contains(v.index()),
          "contains duplicate index: %s", v);
      indices.add(v.index());
    }
    for (E e : edgeSet()) {
      checkArgument(!indices.contains(e.index()),
          "contains duplicate index: %s", e);
      indices.add(e.index());
    }
  }
}
