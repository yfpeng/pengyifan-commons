package com.pengyifan.commons.collections.graph;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pengyifan.commons.collections.ImmutableCollectors;
import edu.stanford.nlp.graph.ConnectedComponents;
import edu.stanford.nlp.graph.Graph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.javatuples.Pair;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class DirectedLoopGraph<V, E> extends AbstractBaseGraph<V, E>
    implements DirectedGraph<V, E>, Graph<V, E> {

  public DirectedLoopGraph(Class<E> edgeClass) {
    super(new ClassBasedEdgeFactory<>(edgeClass), true, true);
  }

  @Override
  public void add(V source, V dest, E data) {
    addEdge(source, dest, data);
  }

  @Override
  public boolean addEdge(V source, V target, E e) {
    checkArgument(!source.equals(target), "Self-loops is not allowed.\nSrc=%s\nDst=%s\nedge=%s",
        source, target, e);
    boolean isSuccess = super.addEdge(source, target, e);
    checkState(isSuccess,
        "multiple edges between any two vertices are not permitted.\nSrc=%s\nDst=%s\nedge=%s",
        source, target, e);
    return true;
  }

  @Override
  public E addEdge(V sourceVertex, V targetVertex) {
    throw new UnsupportedOperationException();
  }

  public boolean addSelfLoop(V v, E e) {
    boolean isSuccess = super.addEdge(v, v, e);
    checkState(isSuccess, "Cannot add self loop.\nSrc=Dst=%s\nEdge=%s", v, e);
    return true;
  }

  @Override
  public boolean addVertex(V v) {
    boolean isSuccess = super.addVertex(v);
    checkState(isSuccess, "The vertex is present.\nVertex=%s", v);
    return true;
  }

  /**
   * Returns a list of pairs of a edges and the child.
   */
  public ArrayList<Pair<E, V>> childPairs(V v) {
    checkArgument(containsVertex(v), "Does not contain vertex.\nVertex=%s", v);
    ArrayList<Pair<E, V>> childPairs = Lists.newArrayList();
    for (E e : outgoingEdgesOf(v)) {
      childPairs.add(Pair.with(e, getEdgeTarget(e)));
    }
    return childPairs;
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  public boolean containsEdge(@Nonnull V source, @Nonnull V target, @Nonnull Predicate<E> p) {
    return getAllEdges(source, target).stream().filter(p).findAny().isPresent();
  }

  public ImmutableSet<E> edgeSet(Predicate<E> predicate) {
    return edgeSet().stream()
        .filter(predicate)
        .collect(ImmutableCollectors.toSet());
  }

  @Override
  public Set<E> edgesOf(V vertex) {
    return Sets.union(incomingEdgesOf(vertex), outgoingEdgesOf(vertex));
  }

  @Override
  public List<E> getAllEdges() {
    return ImmutableList.copyOf(edgeSet());
  }

  @Override
  public Set<V> getAllVertices() {
    return vertexSet();
  }

  public ImmutableSet<V> getChildren(@Nonnull V v, @Nonnull Predicate<E> predicate) {
    return outgoingEdgesOf(v).stream()
        .filter(predicate)
        .map(this::getEdgeTarget)
        .collect(ImmutableCollectors.toSet());
  }

  @Override
  public Set<V> getChildren(V vertex) {
    return outgoingEdgesOf(vertex).stream()
        .map(this::getEdgeTarget)
        .collect(ImmutableCollectors.toSet());
  }

  /**
   * Returns the least common ancestor. We only search as high as grandparents.
   * We return null if no common parent or grandparent is found. Any of the
   * input words can also be the answer if one is the parent or grandparent of
   * other, or if the input words are the same.
   *
   * @return the least common ancestor
   */
  public V getCommonAncestor(V v1, V v2) {
    if (v1.equals(v2)) {
      return v1;
    }

    if (this.isAncestor(v1, v2) >= 1) {
      return v2;
    }

    if (this.isAncestor(v2, v1) >= 1) {
      return v1;
    }

    Set<V> v1Parents = this.getParents(v1);
    Set<V> v2Parents = this.getParents(v2);
    Set<V> v1GrandParents = Sets.newHashSet();
    Set<V> v2GrandParents = Sets.newHashSet();
    // does v1 have any parents that are v2's parents?

    for (V v1Parent : v1Parents) {
      if (v2Parents.contains(v1Parent)) {
        return v1Parent;
      }
      v1GrandParents.addAll(this.getParents(v1Parent));
    }
    // does v1 have any grandparents that are v2's parents?
    for (V v1GrandParent : v1GrandParents) {
      if (v2Parents.contains(v1GrandParent)) {
        return v1GrandParent;
      }
    }
    // build v2 grandparents
    for (V v2Parent : v2Parents) {
      v2GrandParents.addAll(this.getParents(v2Parent));
    }
    // does v1 have any parents or grandparents that are v2's grandparents?
    for (V v2GrandParent : v2GrandParents) {
      if (v1Parents.contains(v2GrandParent)) {
        return v2GrandParent;
      }
      if (v1GrandParents.contains(v2GrandParent)) {
        return v2GrandParent;
      }
    }
    return null;
  }

  @Override
  public List<Set<V>> getConnectedComponents() {
    return ConnectedComponents.getConnectedComponents(this);
  }

  @Override
  public E getEdge(V sourceVertex, V targetVertex) {
    return Iterables.getOnlyElement(getAllEdges(sourceVertex, targetVertex));
  }

  @Override
  public EdgeFactory<V, E> getEdgeFactory() {
    return super.getEdgeFactory();
  }

  @Override
  public double getEdgeWeight(E e) {
    throw new UnsupportedOperationException("Unsupported");
  }

  @Override
  public List<E> getEdges(V source, V dest) {
    return ImmutableList.copyOf(getAllEdges(source, dest));
  }

  @Override
  public int getInDegree(V vertex) {
    return getParents(vertex).size();
  }

  @Override
  public List<E> getIncomingEdges(V v) {
    return ImmutableList.copyOf(incomingEdgesOf(v));
  }

  @Override
  public Set<V> getNeighbors(V v) {
    return Sets.union(getChildren(v), getParents(v));
  }

  @Override
  public int getNumEdges() {
    return edgeSet().size();
  }

  /**
   * Returns the number of nodes in the graph.
   *
   * @return the number of nodes in the graph
   */
  @Override
  public int getNumVertices() {
    return vertexSet().size();
  }

  @Override
  public int getOutDegree(V vertex) {
    return getChildren(vertex).size();
  }

  @Override
  public List<E> getOutgoingEdges(V v) {
    return ImmutableList.copyOf(outgoingEdgesOf(v));
  }

  @Override
  public Set<V> getParents(V vertex) {
    return incomingEdgesOf(vertex).stream()
        .map(this::getEdgeSource)
        .collect(ImmutableCollectors.toSet());
  }

  public ImmutableSet<V> getParents(@Nonnull V v, @Nonnull Predicate<E> edgePredicate) {
    return incomingEdgesOf(v).stream()
        .filter(edgePredicate)
        .map(this::getEdgeSource)
        .collect(ImmutableCollectors.toSet());
  }

  public boolean hasIncomingEdgesOf(@Nonnull V v, @Nonnull Predicate<E> edgePredicate) {
    return incomingEdgesOf(v).stream().filter(edgePredicate).findAny().isPresent();
  }

  public boolean hasOutgoingEdgesOf(@Nonnull V v, @Nonnull Predicate<E> edgePredicate) {
    return outgoingEdgesOf(v).stream().filter(edgePredicate).findAny().isPresent();
  }

  @Override
  public int inDegreeOf(V vertex) {
    return incomingEdgesOf(vertex).size();
  }

  public ImmutableSet<E> incomingEdgesOf(@Nonnull V v, @Nonnull Predicate<E> edgePredicate) {
    return incomingEdgesOf(v).stream().filter(edgePredicate).collect(ImmutableCollectors.toSet());
  }

  /**
   * Searches up to 2 levels to determine how far ancestor is from child (i.e.,
   * returns 1 if "ancestor" is a parent, or 2 if ancestor is a grandparent.
   *
   * @param child    candidate child
   * @param ancestor candidate ancestor
   * @return the number of generations between "child" and "ancestor" (1 is an
   * immediate parent), or -1 if there is no relationship found.
   */
  public int isAncestor(V child, V ancestor) {
    Set<V> parents = this.getParents(child);
    if (parents.contains(ancestor)) {
      return 1;
    }
    for (V parent : parents) {
      Set<V> grandparents = this.getParents(parent);
      if (grandparents.contains(ancestor)) {
        return 2;
      }
    }
    return -1;
  }

  @Override
  public boolean isEdge(V source, V dest) {
    return containsEdge(source, dest);
  }

  @Override
  public boolean isEmpty() {
    return getNumVertices() == 0;
  }

  @Override
  public boolean isNeighbor(V source, V dest) {
    return isEdge(source, dest) || isEdge(dest, source);
  }

  @Override
  public int outDegreeOf(V vertex) {
    return outgoingEdgesOf(vertex).size();
  }

  public ImmutableSet<E> outgoingEdgesOf(@Nonnull V v, @Nonnull Predicate<E> edgePredicate) {
    return outgoingEdgesOf(v).stream().filter(edgePredicate).collect(ImmutableCollectors.toSet());
  }

  /**
   * Returns a list of pairs of a relation name and the parent
   */
  public ArrayList<Pair<E, V>> parentPairs(V v) {
    checkArgument(containsVertex(v), "Does not contain vertex.\nVertex=%s", v);
    ArrayList<Pair<E, V>> parentPairs = Lists.newArrayList();
    for (E e : incomingEdgesOf(v)) {
      parentPairs.add(Pair.with(e, getEdgeSource(e)));
    }
    return parentPairs;
  }

  @Override
  public boolean removeEdge(V source, V dest, E data) {
    for (E e : edgeSet()) {
      if (e.equals(data)) {
        return removeEdge(e);
      }
    }
    return false;
  }

  @Override
  public boolean removeEdge(E e) {
    boolean isSuccess = super.removeEdge(e);
    checkArgument(isSuccess, "The graph does not contain edge.\nEdge=%s", e);
    return true;
  }

  @Override
  public boolean removeEdges(V source, V dest) {
    return !removeAllEdges(source, dest).isEmpty();
  }

  @Override
  public boolean removeVertex(V v) {
    boolean isSuccess = super.removeVertex(v);
    checkArgument(isSuccess, "The graph does not contain vertex\nVertex=%s", v);
    return true;
  }

  @Override
  public boolean removeVertices(Collection<V> vertices) {
    return super.removeAllVertices(vertices);
  }

  @Override
  public void removeZeroDegreeNodes() {
    Set<V> toDelete = vertexSet().stream()
        .filter(v -> getInDegree(v) == 0 && getOutDegree(v) == 0)
        .collect(Collectors.toSet());
    removeAllVertices(toDelete);
  }

  @Override
  public String toString() {
    StringJoiner sj = new StringJoiner("\n");
    // vertex
    sj.add("Vertices:");
    vertexSet().stream().forEach(v -> sj.add(v.toString()));
    // edge
    sj.add("Edges:");
    edgeSet().stream().forEach(e -> sj.add(e.toString()));
    return sj.toString();
  }

  public ImmutableSet<V> vertexSet(Predicate<V> predicate) {
    return vertexSet().stream()
        .filter(predicate)
        .collect(ImmutableCollectors.toSet());
  }
}