package com.pengyifan.commons.collections.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.pengyifan.commons.collections.graph.ExactSubgraphMatching;
import com.pengyifan.commons.collections.graph.IndexGraph;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Test;

public class ExactSubgraphMatchingTest {

  @Test
  public void testIsSubgraphIsomorphism() {
    Label a = new Label(1, "a");
    Label b = new Label(2, "b");
    Label c = new Label(3, "c");
    IndexGraph<Label, Label> graph = new IndexGraph<>(Label.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Label(4, "NSUBJ"));
    graph.addEdge(b, c, new Label(5, "DOBJ"));

    Label a1 = new Label(1, "a");
    Label b1 = new Label(2, "b");
    IndexGraph<Label, Label> subgraph = new IndexGraph<>(Label.class);
    subgraph.addVertex(a1);
    subgraph.addVertex(b1);
    subgraph.addEdge(b1, a1, new Label(4, "NSUBJ"));

    ExactSubgraphMatching<Label, Label> esm = new ExactSubgraphMatching<>(subgraph, graph);
    assertTrue(esm.isSubgraphIsomorphism());

    subgraph = new IndexGraph<>(Label.class);
    subgraph.addVertex(a1);
    subgraph.addVertex(b1);
    subgraph.addEdge(b1, a1, new Label(4, "DEP"));
    esm = new ExactSubgraphMatching<>(subgraph, graph);
    assertFalse(esm.isSubgraphIsomorphism());
  }

  @Test
  public void testGetSubgraphMatchingMatches() {
    Label a = new Label(1, "a");
    Label b = new Label(2, "b");
    Label c = new Label(3, "c");
    IndexGraph<Label, Label> graph = new IndexGraph<>(Label.class);
    graph.addVertex(a);
    graph.addVertex(b);
    graph.addVertex(c);
    graph.addEdge(b, a, new Label(4, "NSUBJ"));
    graph.addEdge(b, c, new Label(5, "NSUBJ"));

    Label a1 = new Label(11, "a");
    Label b1 = new Label(12, "b");
    IndexGraph<Label, Label> subgraph = new IndexGraph<>(Label.class);
    subgraph.addVertex(a1);
    subgraph.addVertex(b1);
    subgraph.addEdge(b1, a1, new Label(4, "NSUBJ"));

    ExactSubgraphMatching<Label, Label> esm = new ExactSubgraphMatching<>(subgraph, graph);

    List<Map<Label, Label>> matches = esm.getSubgraphMatchingMatches();
    assertEquals(1, matches.size());
    Map<Label, Label> m = matches.get(0);
    Label actualA = m.get(a1);
    assertEquals(a, actualA);
    Label actualB = m.get(b1);
    assertEquals(b, actualB);
  }

  private class Label extends CoreLabel {

    public Label(int index, String word) {
      setIndex(index);
      setWord(word);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Label)) {
        return false;
      }
      Label rhs = (Label) obj;
      return Objects.equals(word(), rhs.word());
    }

    @Override
    public String toString() {
      return index() + "-" + word();
    }
  }
}
