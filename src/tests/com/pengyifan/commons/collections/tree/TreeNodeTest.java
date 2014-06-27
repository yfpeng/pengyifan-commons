package tests.com.pengyifan.commons.collections.tree;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

import com.pengyifan.commons.collections.tree.TreeNode;

/**
 * @author Yifan Peng
 */
public class TreeNodeTest {

  static TreeNode a = new TreeNode("A");
  static TreeNode b = new TreeNode("B");
  static TreeNode c = new TreeNode("C");
  static TreeNode d = new TreeNode("D");
  static TreeNode e = new TreeNode("E");
  static TreeNode f = new TreeNode("F");

  static {
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
    assertEquals(null, b.getChildAfter(f));
  }

  @Test
  public void testGetChildBefore() {
    assertEquals(c, b.getChildBefore(f));
    assertEquals(null, b.getChildBefore(c));
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
    assertEquals(null, f.getNextSibling());
  }

  @Test
  public void testGetObject() {
    assertEquals("B", b.getObject());
  }

  @Test
  public void testGetParent() {
    assertEquals(a, b.getParent());
    assertEquals(null, a.getParent());
  }

  @Test
  public void testGetPathFromRoot() {
    assertEquals("FBA", toString(f.getPathFromRoot().iterator()));
  }

  @Test
  public void testGetPathToRoot() {
    assertEquals("ABF", toString(f.getPathToRoot().iterator()));
  }

  @Test
  public void testHasNextSiblingNode() {
    assertEquals(true, c.hasNextSiblingNode());
    assertEquals(false, f.hasNextSiblingNode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIndexOf() {
    assertEquals(2, a.indexOf(e));
    assertEquals(-1, a.indexOf(c));
    a.indexOf(null);
  }

  @Test
  public void testIsLeaf() {
    assertEquals(true, c.isLeaf());
    assertEquals(false, a.isLeaf());
  }

  @Test
  public void testIsNodeAncestor() {
    assertEquals(false, b.isNodeAncestor(c));
    assertEquals(true, c.isNodeAncestor(a));
  }

  @Test
  public void testIsNodeChild() {
    assertEquals(true, b.isNodeChild(c));
    assertEquals(false, c.isNodeChild(a));
  }

  @Test
  public void testIsNodeSibling() {
    assertEquals(false, c.isNodeSibling(e));
    assertEquals(true, b.isNodeSibling(e));
  }

  @Test
  public void testIsRoot() {
    assertEquals(true, a.isRoot());
    assertEquals(false, c.isRoot());
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

  String toString(Iterator<TreeNode> itr) {
    String str = "";
    while (itr.hasNext()) {
      str += (itr.next().getObject());
    }
    return str;
  }
}
