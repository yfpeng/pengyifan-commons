package com.pengyifan.commons.collections.tree2;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.Queue;

public class BreadthFirstIterator<T extends Tree> implements Iterator<T> {

  private Queue<Iterator<T>> queue;

  public BreadthFirstIterator(T rootNode) {
    queue = Lists.newLinkedList();
    queue.offer(Iterators.singletonIterator(rootNode));
  }

  @Override
  public boolean hasNext() {
    return (!queue.isEmpty() && queue.peek().hasNext());
  }

  @Override
  public T next() {
    Iterator<T> itr = queue.peek();
    T node = itr.next();
    Iterator<T> children = node.childrenIterator();

    if (!itr.hasNext()) {
      queue.poll();
    }
    if (children.hasNext()) {
      queue.offer(children);
    }
    return node;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove() is not supported.");
  }
}