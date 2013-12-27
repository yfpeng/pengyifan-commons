package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

public class Entity {

  public String               id;
  public String               type;
  /**
   * start-offset, end-offset
   */
  public List<Range<Integer>> spans;
  public String               text;

  public Entity() {
    spans = new ArrayList<Range<Integer>>();
  }

  public void addSpan(int start, int end) {
    spans.add(Range.between(start, end));
  }

  public int start(int i) {
    return spans.get(i).getMinimum();
  }

  public int end(int i) {
    return spans.get(i).getMaximum();
  }
}