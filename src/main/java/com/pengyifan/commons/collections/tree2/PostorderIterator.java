package com.pengyifan.commons.collections.tree2;

import java.util.Collections;
import java.util.Iterator;

public class PostorderIterator<T extends Tree> implements Iterator<T> {

  private T root;
  private Iterator<T> children;
  private Iterator<T> subtree;

  public PostorderIterator(T rootNode) {
    root = rootNode;
    children = root.childrenIterator();
    subtree = Collections.emptyIterator();
  }

  @Override
  public boolean hasNext() {
    return root != null;
  }

  @Override
  public T next() {
    T retval;

    if (subtree.hasNext()) {
      retval = subtree.next();
    } else if (children.hasNext()) {
      subtree = new PostorderIterator(children.next());
      retval = subtree.next();
    } else {
      retval = root;
      root = null;
    }

    return retval;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove() is not supported.");
  }

}