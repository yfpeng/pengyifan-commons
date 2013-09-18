package pengyifan.tree.string;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

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

  // │
  public static final String BAR    = bar(1);
  // └
  public static final String END    = bar(2);
  // ├
  public static final String MIDDLE = bar(3);

  private static String bar(int i) {
    try {
      switch (i) {
      case 1:
        return new String(new byte[] { -30, -108, -126 }, "utf8");
      case 2:
        return new String(new byte[] { -30, -108, -108 }, "utf8");
      case 3:
        return new String(new byte[] { -30, -108, -100 }, "utf8");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }

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
          sb.append(BAR + " ");
        } else {
          sb.append("  ");
        }
      }
      // if root has sibling node
      if (tn.hasNextSiblingNode()) {
        sb.append(MIDDLE + " ");
      } else {
        sb.append(END + " ");
      }
      sb.append(tn.getObject() + "\n");

    }

    return sb.toString();
  }
}
