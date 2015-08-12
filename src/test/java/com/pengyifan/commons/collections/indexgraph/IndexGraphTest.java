package com.pengyifan.commons.collections.indexgraph;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

//import static org.hamcrest.CoreMatchers.hasItem;

public class IndexGraphTest {

  private static final IndexObject VA = new IndexObject(1);
  private static final IndexObject VB = new IndexObject(2);
  private static final IndexObject VC = new IndexObject(3);

  private static final IndexObject EA = new IndexObject(4);
  private static final IndexObject EB = new IndexObject(5);
  private static final IndexObject EC = new IndexObject(6);

  @Rule
  public ExpectedException exception = ExpectedException
      .none();

  @Test
  public void test_getVertex() {
    IndexGraph<IndexObject, IndexObject> graph =
        new IndexGraph<IndexObject, IndexObject>(IndexObject.class);
    graph.addVertex(VA);
    assertEquals(1, graph.getVertex(1).getIndex());
  }

  @Test
  public void test_getAllEdges() {
    IndexGraph<IndexObject, IndexObject> graph =
        new IndexGraph<IndexObject, IndexObject>(IndexObject.class);
    graph.addVertex(VA);
    graph.addVertex(VB);
    graph.addVertex(VC);
    graph.addEdge(VA, VB, EA);
    graph.addEdge(VA, VB, EB);
    assertEquals(2, graph.getAllEdges(VA, VB).size());
//    assertThat(graph.getAllEdges(VA, VB), hasItem(EA));
//    assertThat(graph.getAllEdges(VA, VB), hasItem(EB));
  }
  
  @Test
  public void test_MultiEdges() {
    IndexGraph<IndexObject, IndexObject> graph =
        new IndexGraph<IndexObject, IndexObject>(IndexObject.class);
    graph.addVertex(VA);
    graph.addVertex(VB);
    graph.addVertex(VC);
    graph.addEdge(VA, VB, EA);
    exception.expect(IllegalArgumentException.class);
    graph.addEdge(VA, VB, EA);
  }

  @Test
  public void test_getMaxIndex() {
    IndexGraph<IndexObject, IndexObject> graph =
        new IndexGraph<IndexObject, IndexObject>(IndexObject.class);
    graph.addVertex(VA);
    graph.addVertex(VB);
    graph.addVertex(VC);
    assertEquals(graph.getMaxIndex(), 3);
  }

  @Test
  public void test_selfLoop() {
    IndexGraph<IndexObject, IndexObject> graph =
        new IndexGraph<IndexObject, IndexObject>(IndexObject.class);
    graph.addVertex(VA);
    graph.addVertex(VB);
    exception.expect(IllegalArgumentException.class);
    graph.addEdge(VA, VA, EC);
  }
}
