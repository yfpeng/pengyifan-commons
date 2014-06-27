package tests.com.pengyifan.commons.lang.string;

import com.pengyifan.commons.collections.tree.TreeNode;
import com.pengyifan.commons.lang.string.PtbString;

/**
 * @author Yifan Peng
 * @version 09/18/2013
 */
@Deprecated
public class PtbStringTest {

  public static void main(String args[]) {
    TreeNode a = new TreeNode("a");
    TreeNode c = new TreeNode("c");
    a.add(new TreeNode("b"));
    a.add(c);
    a.add(new TreeNode("d"));

    c.add(new TreeNode("e"));
    c.add(new TreeNode("f"));

    System.out.println(PtbString.toString(a));
  }
}
