package pengyifan.string.tree;

import java.util.Iterator;

import pengyifan.string.StringUtils;
import pengyifan.tree.TreeNode;

/**
 * Given a <code>TreeNode</code> structure, <code>TreeString</code> will print
 * a string like
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
 * @author Yifan Peng
 */
public class TreeString {

  public static String toString(TreeNode tree) {
    StringBuffer sb = new StringBuffer();

    Iterator<TreeNode> itr = tree.preorderIterator();

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
      sb.append(tn.getObject() + "\n");

    }

    return sb.toString();
  }
}
