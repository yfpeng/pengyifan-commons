package com.pengyifan.commons.collections.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.pengyifan.commons.lang.StringUtils;

/**
 * @author Yifan Peng
 */
public class TreeNodeTest {

  private final TreeNode a = new TreeNode("A");
  private final TreeNode b = new TreeNode("B");
  private final TreeNode c = new TreeNode("C");
  private final TreeNode d = new TreeNode("D");
  private final TreeNode e = new TreeNode("E");
  private final TreeNode f = new TreeNode("F");

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
  public void testAddIntTreeNode() {
    TreeNode dst = a.deepCopy();
    dst.add(0, new TreeNode("G"));
    assertEquals("AGBCFDE", toString(dst.preorderIterator()));
  }

  @Test
  public void testAddTreeNode() {
    TreeNode dst = a.deepCopy();
    dst.add(new TreeNode("G"));
    assertEquals("ABCFDEG", toString(dst.preorderIterator()));
  }

  @Test
  public void testBreadthFirstIterator() {
    assertEquals("ABDECF", toString(a.breadthFirstIterator()));
  }

  @Test
  public void testChildrenIterator() {
    assertEquals("BDE", toString(a.childrenIterator()));
  }

  @Test
  public void testDeepCopy() {
    TreeNode dst = a.deepCopy();
    assertEquals(
        "deep copy is incorrect",
        toString(a.postorderIterator()),
        toString(dst.postorderIterator()));
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
  public void testGetPathFromRoot() {
    assertEquals("ABF", toString(f.getPathFromRoot().iterator()));
  }

  @Test
  public void testGetPathToRoot() {
    assertEquals("FBA", toString(f.getPathToRoot().iterator()));
  }

  @Test
  public void testHasNextSiblingNode() {
    assertTrue(c.hasNextSiblingNode());
    assertFalse(f.hasNextSiblingNode());
  }

  @Test(expected = IllegalArgumentException.class)
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
    assertEquals(true, a.isRoot());
    assertFalse(c.isRoot());
  }

  @Test
  public void testIterator() {
    assertEquals("ABCFDE", toString(a.iterator()));
  }

  @Test
  public void testLeavesIterator() {
    assertEquals("CFDE", toString(a.leavesIterator()));
  }

  @Test
  public void testPostorderIterator() {
    assertEquals("CFBDEA", toString(a.postorderIterator()));
  }

  @Test
  public void testPreorderIterator() {
    assertEquals("ABCFDE", toString(a.preorderIterator()));
  }

  @Test
  public void testRemoveInt() {
    TreeNode dst = a.deepCopy();
    dst.remove(1);
    assertEquals("ABCFE", toString(dst.preorderIterator()));
  }

  @Test
  public void testRemoveTreeNode() {
    TreeNode dst = a.deepCopy();
    dst.remove(dst.getChild(0));
    assertEquals("ADE", toString(dst.preorderIterator()));
  }

  @Test
  public void testReversal() {
    TreeNode dst = a.deepCopy();
    dst.reversal();
    assertEquals("AEDBFC", toString(dst.preorderIterator()));
  }

  @Test
  public void testToString() {
    assertEquals("B", b.toString());
  }

  @Test
  @Ignore
  public void testToString2() {
    System.out.println(a.toString(new TreeNodeStringFormatter() {

      @Override
      public String format(TreeNode treeNode) {
        StringBuilder sb = new StringBuilder();
        Iterator<TreeNode> itr = treeNode.preorderIterator();
        while (itr.hasNext()) {
          TreeNode tn = itr.next();
          // add prefix
          for (TreeNode p : tn.getPathFromRoot()) {
            // if parent has sibling node
            if (p == tn) {
              ;
            } else if (p.hasNextSiblingNode()) {
              sb.append(StringUtils.BAR + " ");
            } else {
              sb.append("  ");
            }
          }
          // if root has sibling node
          if (tn.hasNextSiblingNode()) {
            sb.append(StringUtils.MIDDLE + " ");
          } else {
            sb.append(StringUtils.END + " ");
          }
          sb.append(tn + "\n");

        }
        return sb.toString();
      }
    }));
  }

  String toString(Iterator<TreeNode> itr) {
    String str = "";
    while (itr.hasNext()) {
      str += (itr.next().getObject());
    }
    return str;
  }
}
