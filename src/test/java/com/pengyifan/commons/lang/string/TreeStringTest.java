package com.pengyifan.commons.lang.string;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.pengyifan.commons.collections.tree.TreeNode;

/**
 * @author Yifan Peng
 */
public class TreeStringTest {

  private static final String EXPECTED =
            "└ a\n"
          + "  ├ b\n"
          + "  ├ c\n"
          + "  │ ├ e\n"
          + "  │ └ f\n"
          + "  └ d\n";

  @Test
  public void test() {
    TreeNode a = new TreeNode("a");
    TreeNode c = new TreeNode("c");
    a.add(new TreeNode("b"));
    a.add(c);
    a.add(new TreeNode("d"));
    c.add(new TreeNode("e"));
    c.add(new TreeNode("f"));
    assertEquals(EXPECTED, TreeString.toString(a));
  }
}
