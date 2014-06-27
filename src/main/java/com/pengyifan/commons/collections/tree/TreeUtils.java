package com.pengyifan.commons.collections.tree;

import java.util.List;

public class TreeUtils {

  private TreeUtils() {

  }

  /**
   * returns the node of a tree which represents the lowest common ancestor of
   * nodes t1 and t2 dominated by root. If either t1 or t2 is not dominated by
   * root, returns null.
   * 
   * @param t1
   * @param t2
   * @param root
   * @return
   */
  public static TreeNode getLowestCommonAncestor(TreeNode t1, TreeNode t2) {
    List<TreeNode> t1Path = t1.getPathFromRoot();
    List<TreeNode> t2Path = t2.getPathFromRoot();
    if (t1Path.isEmpty() || t2Path.isEmpty()) {
      return null;
    }

    int min = Math.min(t1Path.size(), t2Path.size());
    TreeNode commonAncestor = null;
    for (int i = 0; i < min && t1Path.get(i).equals(t2Path.get(i)); ++i) {
      commonAncestor = t1Path.get(i);
    }

    return commonAncestor;
  }

  /**
   * Returns the positional index of the left edge of a tree <i>t</i> within a
   * given root, as defined by the size of the yield of all material preceding
   * <i>t</i>.
   */
  public static int leftEdge(TreeNode t, TreeNode root) {
    int i[] = new int[] { 0 };
    if (leftEdge(t, root, i)) {
      return i[0];
    } else {
      throw new RuntimeException("Tree is not a descendant of root.");
    }
  }

  static boolean leftEdge(TreeNode t, TreeNode t1, int i[]) {
    if (t == t1) {
      return true;
    } else if (t1.isLeaf()) {
      int j = t1.getLeaves().size(); // so that empties don't add size
      i[0] = i[0] + j;
      return false;
    } else {
      for (TreeNode kid : t1.children()) {
        if (leftEdge(t, kid, i)) {
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Returns the positional index of the right edge of a tree <i>t</i> within a
   * given root, as defined by the size of the yield of all material preceding
   * <i>t</i> plus all the material contained in <i>t</i>.
   */
  public static int rightEdge(TreeNode t, TreeNode root) {
    int i[] = new int[] { root.getLeaves().size() };
    if (rightEdge(t, root, i)) {
      return i[0];
    } else {
      throw new RuntimeException("Tree is not a descendant of root.");
      // return root.yield().size() + 1;
    }
  }

  static boolean rightEdge(TreeNode t, TreeNode t1, int i[]) {
    if (t == t1) {
      return true;
    } else if (t1.isLeaf()) {
      int j = t1.getLeaves().size(); // so that empties don't add size
      i[0] = i[0] - j;
      return false;
    } else {
      TreeNode[] kids = t1.children().toArray(new TreeNode[0]);
      for (int j = kids.length - 1; j >= 0; j--) {
        if (rightEdge(t, kids[j], i)) {
          return true;
        }
      }
      return false;
    }
  }
}
