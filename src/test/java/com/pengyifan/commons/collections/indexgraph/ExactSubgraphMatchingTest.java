package com.pengyifan.commons.collections.indexgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;

public class ExactSubgraphMatchingTest {

  @Test
  public void test_isSubgraphIsomorphism() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<Vertex, Edge>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "DOBJ"));

    Vertex a1 = new Vertex(1, "a");
    Vertex b1 = new Vertex(2, "b");
    IndexGraph<Vertex, Edge> subgraph = new IndexGraph<Vertex, Edge>(Edge.class);
    subgraph.addVertex(a1);
    subgraph.addVertex(b1);
    subgraph.addEdge(b1, a1, new Edge(4, "NSUBJ"));

    ExactSubgraphMatching<Vertex, Edge> esm = new ExactSubgraphMatching<Vertex, Edge>(
        subgraph, graph);
    assertTrue(esm.isSubgraphIsomorphism());

    IndexGraph<Vertex, Edge> subgraph2 = new IndexGraph<Vertex, Edge>(
        Edge.class);
    subgraph2.addVertex(a1);
    subgraph2.addVertex(b1);
    subgraph2.addEdge(b1, a1, new Edge(4, "DEP"));
    esm = new ExactSubgraphMatching<>(subgraph2, graph);
    assertFalse(esm.isSubgraphIsomorphism());
  }

  @Test
  public void test_getSubgraphMatchingMatches() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<Vertex, Edge>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "NSUBJ"));

    Vertex a1 = new Vertex(11, "a");
    Vertex b1 = new Vertex(12, "b");
    IndexGraph<Vertex, Edge> subgraph = new IndexGraph<Vertex, Edge>(Edge.class);
    subgraph.addVertex(a1);
    subgraph.addVertex(b1);
    subgraph.addEdge(b1, a1, new Edge(4, "NSUBJ"));

    ExactSubgraphMatching<Vertex, Edge> esm = new ExactSubgraphMatching<Vertex, Edge>(
        subgraph, graph);

    List<Map<Vertex, Vertex>> matches = esm.getSubgraphMatchingMatches();
    assertEquals(1, matches.size());
    Map<Vertex, Vertex> m = matches.get(0);
    Vertex actualA = m.get(a1);
    assertEquals(a, actualA);
    Vertex actualB = m.get(b1);
    assertEquals(b, actualB);
  }

  private class Edge extends IndexObject {

    public Edge(int index, String word) {
      super(index);
      label.setWord(word);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Edge)) {
        return false;
      }
      Edge rhs = (Edge) obj;
      return Objects.equals(label.word(), rhs.label.word());
    }
  }

  private class Vertex extends IndexObject {

    public Vertex(int index, String word) {
      super(index);
      label.setWord(word);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Vertex)) {
        return false;
      }
      Vertex rhs = (Vertex) obj;
      return Objects.equals(label.word(), rhs.label.word());
    }
  }
}
