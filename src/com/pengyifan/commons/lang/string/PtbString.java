package com.pengyifan.commons.lang.string;

import com.pengyifan.commons.collections.tree.TreeNode;
import com.pengyifan.nlp.trees.PtbWriter;

/**
 * Given a <code>TreeNode</code> structure as
 * 
 * <pre>
 * └ a
 *   ├ b
 *   ├ c
 *   │ ├ e
 *   │ └ f
 *   └ d
 * </pre>
 * 
 * <code>TreeString</code> will print a string like
 * 
 * <pre>
 * (a b (c (e f) d)
 * </pre>
 * 
 * @author Yifan Peng
 * @version 09/18/2013
 */
/**
 * 
 * @deprecated replaced by {@link PtbWriter}.
 */
@Deprecated
public class PtbString {

  public static String toString(TreeNode tree) {
    return toString(tree, new StringBuilder()).toString();
  }

  private static StringBuilder toString(TreeNode tree, StringBuilder sb) {
    if (tree.isLeaf()) {
      sb.append(tree.getObject());
    } else {
      sb.append('(');
      sb.append(tree.getObject());
      for (TreeNode child : tree.children()) {
        sb.append(' ');
        toString(child, sb);
      }
      sb.append(')');
    }
    return sb;
  }
}
