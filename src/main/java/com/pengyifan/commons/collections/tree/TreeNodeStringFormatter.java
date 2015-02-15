package com.pengyifan.commons.collections.tree;

/**
 * An intermediary "Formatter" interface. An implementation of this interface
 * outputs the data of {@link TreeNode} formatted as appropriate.
 */
public interface TreeNodeStringFormatter {

  public String format(TreeNode treeNode);
}
