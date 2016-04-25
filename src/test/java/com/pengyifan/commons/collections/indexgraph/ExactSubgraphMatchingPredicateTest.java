package com.pengyifan.commons.collections.indexgraph;

import edu.stanford.nlp.ling.CoreLabel;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExactSubgraphMatchingPredicateTest {

  @Test
  public void test_getSubgraphMatchingMatches() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "DOBJ"));

    Predicate<Vertex> predA = v -> v.word().equals("a");
    Predicate<Vertex> predB = v -> v.word().equals("b");
    DirectedGraph<Predicate<Vertex>, Predicate<Edge>> subgraph =
        new DefaultDirectedGraph<>(
            new Factory());
    subgraph.addVertex(predA);
    subgraph.addVertex(predB);
    subgraph.addEdge(predB, predA, e -> e.word().equals("NSUBJ"));

    ExactSubgraphMatchingPredicate<Vertex, Edge> esm =
        new ExactSubgraphMatchingPredicate<>(subgraph, graph);

    List<Map<Predicate<Vertex>, Vertex>> matches = esm.getSubgraphMatchingMatches();
    assertEquals(1, matches.size());
    Map<Predicate<Vertex>, Vertex> m = matches.get(0);
    Vertex actualA = m.get(predA);
    assertEquals(a.word(), actualA.word());
    Vertex actualB = m.get(predB);
    assertEquals(b.word(), actualB.word());
  }

  @Test
  public void test_getSubgraphMatchingMatches2() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "NSUBJ"));

    Predicate<Vertex> predA = v -> v.word().equals("a") || v.word().equals("c");
    Predicate<Vertex> predB = v -> v.word().equals("b");
    DirectedGraph<Predicate<Vertex>, Predicate<Edge>> subgraph =
        new DefaultDirectedGraph<>(
            new Factory());
    subgraph.addVertex(predB);
    subgraph.addVertex(predA);
    subgraph.addEdge(predB, predA, e -> e.word().equals("NSUBJ"));

    ExactSubgraphMatchingPredicate<Vertex, Edge> esm =
        new ExactSubgraphMatchingPredicate<>(subgraph, graph);

    List<Map<Predicate<Vertex>, Vertex>> matches = esm.getSubgraphMatchingMatches();
    assertEquals(2, matches.size());
    Map<Predicate<Vertex>, Vertex> m = matches.get(0);
    Vertex actualA = m.get(predA);
    assertEquals(a.word(), actualA.word());
    Vertex actualB = m.get(predB);
    assertEquals(b.word(), actualB.word());

    m = matches.get(1);
    actualA = m.get(predA);
    assertEquals(c.word(), actualA.word());
    actualB = m.get(predB);
    assertEquals(b.word(), actualB.word());
  }

  @Test
  public void test_getSubgraphMatchingMatches_false() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "DOBJ"));

    DirectedGraph<Predicate<Vertex>, Predicate<Edge>> subgraph =
        new DefaultDirectedGraph<>(
            new Factory());
    Predicate<Vertex> predA = v -> v.word().equals("a");
    Predicate<Vertex> predB = v -> v.word().equals("b");
    subgraph.addVertex(predA);
    subgraph.addVertex(predB);
    subgraph.addEdge(predB, predA, e -> e.word().equals("DEP"));
    ExactSubgraphMatchingPredicate<Vertex, Edge> esm =
        new ExactSubgraphMatchingPredicate<>(subgraph, graph);
    List<Map<Predicate<Vertex>, Vertex>> matches = esm.getSubgraphMatchingMatches();
    assertTrue(matches.isEmpty());
  }

  @Test
  public void test_isSubgraphIsomorphism_true() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "DOBJ"));

    Predicate<Vertex> predA = v -> v.word().equals("a");
    Predicate<Vertex> predB = v -> v.word().equals("b");
    DirectedGraph<Predicate<Vertex>, Predicate<Edge>> subgraph =
        new DefaultDirectedGraph<>(new Factory());
    subgraph.addVertex(predA);
    subgraph.addVertex(predB);
    subgraph.addEdge(predB, predA, e -> e.word().equals("NSUBJ"));

    ExactSubgraphMatchingPredicate<Vertex, Edge> esm =
        new ExactSubgraphMatchingPredicate<>(subgraph, graph);
    assertTrue(esm.isSubgraphIsomorphism());
  }

  @Test
  public void test_isSubgraphIsomorphism_false() {
    Vertex a = new Vertex(1, "a");
    Vertex b = new Vertex(2, "b");
    Vertex c = new Vertex(3, "c");
    IndexGraph<Vertex, Edge> graph = new IndexGraph<>(Edge.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Edge(4, "NSUBJ"));
    graph.addEdge(b, c, new Edge(5, "DOBJ"));

    DirectedGraph<Predicate<Vertex>, Predicate<Edge>> subgraph = new DefaultDirectedGraph<>(new Factory());
    Predicate<Vertex> predA = v -> v.word().equals("a");
    Predicate<Vertex> predB = v -> v.word().equals("b");
    subgraph.addVertex(predA);
    subgraph.addVertex(predB);
    subgraph.addEdge(predB, predA, e -> e.word().equals("DEP"));
    ExactSubgraphMatchingPredicate<Vertex, Edge> esm =
        new ExactSubgraphMatchingPredicate<>(subgraph, graph);
    assertFalse(esm.isSubgraphIsomorphism());
  }

  private class Edge extends CoreLabel {

    public Edge(int index, String word) {
      setIndex(index);
      setWord(word);
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
      return Objects.equals(word(), rhs.word())
          && Objects.equals(index(), rhs.index());
    }

    @Override
    public String toString() {
      return word();
    }
  }

  private class Vertex extends CoreLabel {

    public Vertex(int index, String word) {
      setIndex(index);
      setWord(word);
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
      return Objects.equals(word(), rhs.word())
          && Objects.equals(index(), rhs.index());
    }

    @Override
    public String toString() {
      return word();
    }
  }

  private class Factory implements
      EdgeFactory<Predicate<Vertex>, Predicate<Edge>> {

    @Override
    public Predicate<Edge> createEdge(Predicate<Vertex> sourceVertex,
        Predicate<Vertex> targetVertex) {
      return e -> true;
    }

  }
}
