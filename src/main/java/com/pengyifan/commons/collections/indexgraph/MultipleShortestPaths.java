package com.pengyifan.commons.collections.indexgraph;

import com.google.common.collect.Lists;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Find all shortest paths between start and end vertices.
 */
public class MultipleShortestPaths<V extends Comparable<V>, E extends DefaultWeightedEdge> {

  private final Comparator<GraphPath<V, E>> byWeight =
      (p1, p2) -> Double.compare(p1.getWeight(), p2.getWeight());

  private final Comparator<GraphPath<V, E>> byEdges =
      (p1, p2) -> Integer.compare(p1.getEdgeList().size(), p2.getEdgeList().size());

  private final DirectedMultiLoopGraph<V, E> graph;

  public MultipleShortestPaths(DirectedMultiLoopGraph<V, E> graph) {
    this.graph = graph;
  }

  public List<GraphPath<V, E>> getShortestPath(V startVertex, V endVertex) {
    checkArgument(graph.containsVertex(startVertex),
        "start vertex is not in the graph: %s", startVertex);
    checkArgument(graph.containsVertex(endVertex),
        "end vertex is not in the graph: %s", endVertex);

    List<GraphPath<V, E>> kpaths = new KShortestPaths<>(graph, startVertex, 1).getPaths(endVertex);
    if (kpaths == null) {
      return Lists.newArrayList();
    }

    Collections.sort(kpaths, byWeight.thenComparing(byEdges));
    //    kpaths = getMinK(kpaths);
    return kpaths;
  }

  private List<GraphPath<V, E>> getMinK(List<GraphPath<V, E>> kpaths) {
    double minWeight = kpaths.get(0).getWeight();
    Iterator<GraphPath<V, E>> itr = kpaths.iterator();
    while (itr.hasNext()) {
      if (itr.next().getWeight() > minWeight) {
        itr.remove();
      }
    }
    return kpaths;
  }
}
