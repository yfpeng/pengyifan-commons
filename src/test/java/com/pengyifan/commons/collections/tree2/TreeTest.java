package com.pengyifan.commons.collections.tree2;

import com.google.common.collect.Lists;
import com.pengyifan.commons.collections.tree.Tree;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TreeTest {

  private final StringTree a = new StringTree("A");
  private final StringTree b = new StringTree("B");
  private final StringTree c = new StringTree("C");
  private final StringTree d = new StringTree("D");
  private final StringTree e = new StringTree("E");
  private final StringTree f = new StringTree("F");
  private final StringTree g = new StringTree("G");

  @Before
  public void setUp() {
    // A (B (C F) D E)
    a.add(b);
    a.add(d);
    a.add(e);
    b.add(c);
    b.add(f);
  }

  @Test
  public void testAdd() {
    a.add(g);
    assertEquals(g, a.getChild(3));
  }

  @Test
  public void testAddIndex() {
    assertEquals(b, a.getChild(0));
    a.add(0, g);
    assertEquals(g, a.getChild(0));
  }

  @Test
  public void testBreadthFirstIterator() {
    assertThat(Lists.newArrayList(a.breadthFirstIterator()),
        is(Lists.newArrayList(a, b, d, e, c, f)));
  }

  @Test
  public void testBreadthFirstList() {
    assertThat(a.breadthFirstList(), is(Lists.newArrayList(a, b, d, e, c, f)));
  }

  @Test
  public void testChildren() {
    assertThat(a.children(), is(Lists.newArrayList(b, d, e)));
  }

  @Test
  public void testChildrenIterator() {
    assertThat(Lists.newArrayList(a.children()), is(Lists.newArrayList(b, d, e)));
  }

  @Test
  public void testDeepCopy() {
    StringTree dst = a.deepCopy(StringTree::new);
    assertThat(dst.postorderList(), is(Lists.newArrayList(c, f, b, d, e, a)));
    assertThat(dst.preorderList(), is(Lists.newArrayList(a, b, c, f, d, e)));
  }

  @Test
  public void testDepthFirstIterator() {
    assertThat(Lists.newArrayList(a.depthFirstIterator()),
        is(Lists.newArrayList(c, f, b, d, e, a)));
  }

  @Test
  public void testGetChild() {
    assertEquals(d, a.getChild(1));
  }

  @Test
  public void testGetChildAfter() {
    assertEquals(f, b.getChildAfter(c));
    assertNull(b.getChildAfter(f));
  }

  @Test
  public void testGetChildBefore() {
    assertEquals(c, b.getChildBefore(f));
    assertNull(b.getChildBefore(c));
  }

  @Test
  public void testGetChildCount() {
    assertEquals(3, a.getChildCount());
  }

  @Test
  public void testGetDepth() {
    assertEquals(2, a.getDepth());
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetFirstChild() {
    assertEquals(c, b.getFirstChild());
    c.getFirstChild();
  }

  @Test
  public void testGetFirstLeaf() {
    assertEquals(c, a.getFirstLeaf());
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetLastChild() {
    assertEquals(e, a.getLastChild());
    f.getLastChild();
  }

  @Test
  public void testGetLastLeaf() {
    assertEquals(e, a.getLastLeaf());
  }

  @Test
  public void testGetLeaves() {
    assertThat(a.getLeaves(), is(Lists.newArrayList(c, f, d, e)));
  }

  @Test
  public void testGetLeafObjects() {
    assertThat(a.getLeafObjects(), is(Lists.newArrayList("C", "F", "D", "E")));
  }

  @Test
  public void testGetLevel() {
    assertEquals(0, a.getLevel());
    assertEquals(2, c.getLevel());
  }

  @Test
  public void testGetNextSibling() {
    assertEquals(f, c.getNextSibling());
    assertNull(f.getNextSibling());
  }

  @Test
  public void testGetObject() {
    assertEquals("B", b.getObject());
  }

  @Test
  public void testGetParent() {
    assertEquals(a, b.getParent());
    assertNull(a.getParent());
  }

  @Test
  public void testGetPathToRoot() {
    assertThat(f.getPathToRoot(), is(Lists.newArrayList(f, b, a)));
  }

  @Test
  public void testGetPathFromRoot() {
    assertThat(f.getPathFromRoot(), is(Lists.newArrayList(a, b, f)));
  }

  @Test
  public void testHasNextSiblingNode() {
    assertTrue(c.hasNextSiblingNode());
    assertFalse(f.hasNextSiblingNode());
  }

  @Test(expected = NullPointerException.class)
  public void testIndexOf() {
    assertEquals(2, a.indexOf(e));
    assertEquals(-1, a.indexOf(c));
    a.indexOf(null);
  }

  @Test
  public void testIsLeaf() {
    assertTrue(c.isLeaf());
    assertFalse(a.isLeaf());
  }

  @Test
  public void testIsNodeAncestor() {
    assertFalse(b.isNodeAncestor(c));
    assertTrue(c.isNodeAncestor(a));
  }

  @Test
  public void testIsNodeChild() {
    assertTrue(b.isNodeChild(c));
    assertFalse(c.isNodeChild(a));
  }

  @Test
  public void testIsNodeSibling() {
    assertFalse(c.isNodeSibling(e));
    assertTrue(b.isNodeSibling(e));
  }

  @Test
  public void testIsRoot() {
    assertTrue(a.isRoot());
    assertFalse(c.isRoot());
  }

  @Test
  public void testIterator() {
    assertThat(Lists.newArrayList(a.iterator()),
        is(Lists.newArrayList(a, b, c, f, d, e)));
  }

  @Test
  public void testLeavesIterator() {
    assertThat(Lists.newArrayList(a.leavesIterator()),
        is(Lists.newArrayList(c, f, d, e)));
  }

  @Test
  public void testPostorderIterator() {
    assertThat(Lists.newArrayList(a.postorderIterator()),
        is(Lists.newArrayList(c, f, b, d, e, a)));
  }

  @Test
  public void testPostorderList() {
    assertThat(a.postorderList(), is(Lists.newArrayList(c, f, b, d, e, a)));
  }

  @Test
  public void testPreorderIterator() {
    assertThat(Lists.newArrayList(a.preorderIterator()),
        is(Lists.newArrayList(a, b, c, f, d, e)));
  }

  @Test
  public void testPreorderList() {
    assertThat(a.preorderList(), is(Lists.newArrayList(a, b, c, f, d, e)));
  }

  @Test
  public void testRemove() {
    assertEquals(b, a.getChild(0));
    a.remove(b);
    assertEquals(d, a.getChild(0));
  }

  @Test
  public void testRemoveIndex() {
    assertEquals(d, a.getChild(1));
    a.remove(1);
    assertEquals(e, a.getChild(1));
  }

  @Test
  public void testReversal() {
    a.reversal();
    assertEquals(e, a.getChild(0));
    assertEquals(d, a.getChild(1));
    assertEquals(b, a.getChild(2));
    assertEquals(f, b.getChild(0));
    assertEquals(c, b.getChild(1));
  }

  @Test
  public void testSetObject() {
    assertEquals("A", a.getObject());
    a.setObject("Z");
    assertEquals("Z", a.getObject());
  }

  @Test
  public void testSetParent() {
    assertEquals(a, b.getParent());
    b.setParent(c);
    assertEquals(c, b.getParent());
  }

  @Test
  public void testDominates() {
    assertTrue(a.dominates(b));
    assertTrue(a.dominates(f));
    assertFalse(f.dominates(e));
  }

  @Test
  public void testGetDominationPath() {
    assertThat(a.getDominationPath(f), is(Lists.newArrayList(a, b, f)));
  }

  @Test
  public void testToString() {
    assertEquals("B", b.toString());
  }

  private class StringTree extends Tree<String, StringTree> {
    StringTree(String s) {
      setObject(s);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof StringTree)) {
        return false;
      }
      StringTree rhs = (StringTree) obj;
      return Objects.equals(getObject(), rhs.getObject());
    }
  }
}