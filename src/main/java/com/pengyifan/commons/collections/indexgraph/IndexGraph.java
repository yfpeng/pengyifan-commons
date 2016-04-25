package com.pengyifan.commons.collections.indexgraph;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.pengyifan.commons.collections.ImmutableCollectors;
import edu.stanford.nlp.ling.HasIndex;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class IndexGraph<V extends HasIndex, E extends HasIndex>
    implements DirectedGraph<V, E> {

  private static class DirectedMultiLoopGraph<V, E> extends
      AbstractBaseGraph<V, E>
      implements DirectedGraph<V, E> {
    public DirectedMultiLoopGraph(Class<E> edgeClass) {
      super(new ClassBasedEdgeFactory<>(edgeClass), true, true);
    }
  }

  private final AbstractBaseGraph<V, E> graph;
  private int maxIndex;

  public IndexGraph(Class<E> edgeClass) {
    graph = new DirectedMultiLoopGraph<>(edgeClass);
    maxIndex = 0;
  }

  public boolean addEdge(V source, V target, E e) {
    checkArgument(!source.equals(target),
        "self-loops not allowed: Edge[index=%s,src=%s,dst=%s]\n%s",
        e.index(), source.index(), target.index(), this);
    boolean isSuccess = graph.addEdge(source, target, e);
    checkArgument(
        isSuccess,
        "multiple edges between any two vertices are not permitted: "
            + "Edge[index=%s,src=%s,dst=%s]\n%s\n%s\n%s\n",
        e.index(),
        source.index(),
        target.index(),
        e,
        source,
        target);
    updateIndex(e.index());
    return true;
  }

  public void addSelfLoop(V v, E e) {
    boolean isSuccess = graph.addEdge(v, v, e);
    checkArgument(
        isSuccess,
        "cannot add self loop: Edge[index=%s,src=%s]",
        e.index(),
        v.index());
    updateIndex(e.index());
  }

  public boolean addVertex(V v) {
    boolean isSuccess = graph.addVertex(v);
    checkState(isSuccess,
        "the vertex is present: Vertex[index=%s]",
        v.index());
    updateIndex(v.index());
    return true;
  }

  public boolean containsEdge(V source, V target) {
    return graph.containsEdge(source, target);
  }

  public void checkHashCode() {
    for (E e : graph.edgeSet()) {
      V s1 = graph.getEdgeSource(e);
      V s2 = getVertex(s1.index());
      checkArgument(
          s1 == s2,
          "edge source has different ends[%s]\nhashCode=%s,%s\nhashCode=%s,%s",
          e,
          System.identityHashCode(s1),
          s1,
          System.identityHashCode(s2),
          s2);
      V t1 = graph.getEdgeTarget(e);
      V t2 = getVertex(t1.index());
      checkArgument(t1 == t2, "edge source has different ends[%s]\n%s\n%s", e,
          t1, t2);
    }
  }

  public boolean containsVertex(int index) {
    return graph.vertexSet().stream()
        .filter(v -> v.index() == index)
        .findFirst()
        .isPresent();
  }

  public ImmutableSet<E> edgeSet() {
    return ImmutableSet.copyOf(graph.edgeSet());
  }

  public ImmutableSet<E> edgeSet(Predicate<E> predicate) {
    return graph.edgeSet().stream()
        .filter(predicate)
        .collect(ImmutableCollectors.toSet());
  }

  public ImmutableSet<E> getAllEdges(V sourceVertex, V targetVertex) {
    return ImmutableSet.copyOf(graph.getAllEdges(sourceVertex, targetVertex));
  }

  public E getEdge(int index) {
    return Iterables.getOnlyElement(graph.edgeSet()
        .stream()
        .filter(e -> e.index() == index)
        .collect(Collectors.toList()));
  }

  public V getEdgeSource(E e) {
    return graph.getEdgeSource(e);
  }

  public V getEdgeTarget(E e) {
    return graph.getEdgeTarget(e);
  }

  public int getMaxIndex() {
    return maxIndex;
  }

  public V getVertex(int index) {
    return Iterables.getOnlyElement(graph.vertexSet()
        .stream()
        .filter(v -> v.index() == index)
        .collect(Collectors.toList()));
  }

  public ImmutableSet<E> incomingEdgesOf(V v) {
    return ImmutableSet.copyOf(graph.incomingEdgesOf(v));
  }

  public ImmutableSet<E> outgoingEdgesOf(V v) {
    return ImmutableSet.copyOf(graph.outgoingEdgesOf(v));
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("\n");
    // vertex
    graph.vertexSet()
        .stream()
        .sorted((v1, v2) -> Integer.compare(v1.index(), v2.index()))
        .forEach(v -> sj.add(v.toString()));
    // edge
    graph.edgeSet()
        .stream()
        .sorted((e1, e2) -> Integer.compare(e1.index(), e2.index()))
        .forEach(e -> sj.add(toString(e)));
    return sj.toString();
  }

  private String toString(E e) {
    return String.format("%s,[src=%d,dst=%d]",
        e,
        graph.getEdgeSource(e).index(),
        graph.getEdgeTarget(e).index());
  }

  public void validateIndex() {
    Set<Integer> indices = Sets.newHashSet();
    for (V v : graph.vertexSet()) {
      checkArgument(!indices.contains(v.index()),
          "contains duplicate index: %s", v);
      indices.add(v.index());
    }
    for (E e : graph.edgeSet()) {
      checkArgument(!indices.contains(e.index()),
          "contains duplicate index: %s", e);
      indices.add(e.index());
    }
  }

  public ImmutableSet<V> vertexSet() {
    return ImmutableSet.copyOf(graph.vertexSet());
  }

  public ImmutableSet<V> vertexSet(Predicate<V> predicate) {
    return graph.vertexSet().stream()
        .filter(predicate)
        .collect(ImmutableCollectors.toSet());
  }

  public boolean removeEdge(E e) {
    checkArgument(graph.containsEdge(e),
        "The graph does not contain edge: %s", e);
    return graph.removeEdge(e);
  }

  public boolean removeVertex(V v) {
    checkArgument(graph.containsVertex(v),
        "The graph does not contain vertex: %s", v);
    return graph.removeVertex(v);
  }

  public ImmutableSet<V> incomingEdgeSourcesOf(V vertex) {
    return incomingEdgesOf(vertex).stream()
        .map(e -> getEdgeSource(e))
        .collect(ImmutableCollectors.toSet());
  }

  public ImmutableSet<V> outgoingEdgeTargetsOf(V vertex) {
    return outgoingEdgesOf(vertex).stream()
        .map(e -> getEdgeTarget(e))
        .collect(ImmutableCollectors.toSet());
  }

  private void updateIndex(int index) {
    if (index > this.maxIndex) {
      this.maxIndex = index;
    }
  }

  @Override
  public E getEdge(V sourceVertex, V targetVertex) {
    return Iterables.getOnlyElement(getAllEdges(sourceVertex, targetVertex));
  }

  @Override
  public EdgeFactory<V, E> getEdgeFactory() {
    return graph.getEdgeFactory();
  }

  @Override
  public E addEdge(V sourceVertex, V targetVertex) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public boolean containsEdge(E e) {
    return graph.edgeSet().stream()
        .filter(edge -> e.index() == edge.index())
        .findFirst()
        .isPresent();
  }

  @Override
  public boolean containsVertex(V v) {
    return containsVertex(v.index());
  }

  @Override
  public Set<E> edgesOf(V vertex) {
    Set<E> incomings = incomingEdgesOf(vertex);
    Set<E> outgoings = outgoingEdgesOf(vertex);
    return ImmutableSet.copyOf(Sets.union(incomings, outgoings));
  }

  @Override
  public boolean removeAllEdges(Collection<? extends E> edges) {
    boolean flag = true;
    for(E e: edges) {
      flag &= removeEdge(e);
    }
    return flag;
  }

  @Override
  public Set<E> removeAllEdges(V sourceVertex, V targetVertex) {
    Set<E> edges = getAllEdges(sourceVertex, targetVertex);
    removeAllEdges(edges);
    return edges;
  }

  @Override
  public boolean removeAllVertices(Collection<? extends V> vertices) {
    boolean flag = true;
    for(V v: vertices) {
      flag &= removeVertex(v);
    }
    return flag;
  }

  @Override
  public E removeEdge(V sourceVertex, V targetVertex) {
    E e = getEdge(sourceVertex, targetVertex);
    if (e != null) {
      removeEdge(e);
    }
    return e;
  }

  @Override
  public double getEdgeWeight(E e) {
    throw new UnsupportedOperationException("Unsupported");
  }

  @Override
  public int inDegreeOf(V vertex) {
    return incomingEdgesOf(vertex).size();
  }

  @Override
  public int outDegreeOf(V vertex) {
    return outgoingEdgesOf(vertex).size();
  }
}
