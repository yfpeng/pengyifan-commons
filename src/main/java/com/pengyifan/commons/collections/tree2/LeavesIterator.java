package com.pengyifan.commons.collections.tree2;

import java.util.Iterator;

public class LeavesIterator<T extends Tree> implements Iterator<T> {

  private Iterator<T> depthFirstItr;
  private T nextLeaf;

  public LeavesIterator(T rootNode) {
    depthFirstItr = rootNode.depthFirstIterator();
  }

  @Override
  public boolean hasNext() {
    nextLeaf = null;
    while (depthFirstItr.hasNext()) {
      T next = depthFirstItr.next();
      if (next.isLeaf()) {
        nextLeaf = next;
        return true;
      }
    }
    return false;
  }

  @Override
  public T next() {
    return nextLeaf;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove() is not supported.");
  }
}