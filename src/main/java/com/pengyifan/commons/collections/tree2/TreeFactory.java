package com.pengyifan.commons.collections.tree2;

/**
 * A tree factory used by <code>Tree</code> for creating new node.
 *
 * @author Yifan Peng
 */
public interface TreeFactory<E, T extends Tree> {
  public T newTree(E e);
}
