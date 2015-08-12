package com.pengyifan.commons.collections.indexgraph;

import org.jgrapht.DirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;

public class DirectedMultiLoopGraph<V, E>
    extends AbstractBaseGraph<V, E>
    implements DirectedGraph<V, E>, WeightedGraph<V, E> {

  private static final long serialVersionUID = 6915751290785053251L;

  public DirectedMultiLoopGraph(Class<E> edgeClass) {
    super(new ClassBasedEdgeFactory<V, E>(edgeClass), true, true);
  }

}
