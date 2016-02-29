package com.pengyifan.commons.collections.tree2;

import com.google.common.collect.Iterators;

import java.util.Iterator;
import java.util.Stack;

public class PreorderIterator<T extends Tree> implements Iterator<T> {

  private final Stack<Iterator<T>> stack;

  public PreorderIterator(T t) {
    stack = new Stack<>();
    stack.push(Iterators.singletonIterator(t));
  }

  @Override
  public boolean hasNext() {
    return (!stack.empty() && stack.peek().hasNext());
  }

  @Override
  public T next() {
    Iterator<T> itr = stack.peek();
    T node = itr.next();
    Iterator<T> childrenItr = node.childrenIterator();

    if (!itr.hasNext()) {
      stack.pop();
    }
    if (childrenItr.hasNext()) {
      stack.push(childrenItr);
    }
    return node;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove() is not supported.");
  }

}