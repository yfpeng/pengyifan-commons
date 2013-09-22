package pengyifan.test;

import pengyifan.string.PtbString;
import pengyifan.tree.TreeNode;

/**
 * @author Yifan Peng
 * @version 09/18/2013
 */
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
