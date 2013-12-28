package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Range;

public class Entity {

  public String                id;
  public String                type;
  /**
   * discontinuous spans: start-offset, end-offset
   */
  private List<Range<Integer>> spans;
  public String                text;

  int                          start;
  int                          end;

  public Entity() {
    spans = new ArrayList<Range<Integer>>();
    start = Integer.MAX_VALUE;
    end = Integer.MIN_VALUE;
  }

  public void addSpan(int start, int end) {
    spans.add(Range.between(start, end));
    // update start and end
    if (this.start > start) {
      this.start = start;
    }
    if (this.end < end) {
      this.end = end;
    }
  }

  public int start(int i) {
    return span(i).getMinimum();
  }

  public int end(int i) {
    return span(i).getMaximum();
  }

  public Range<Integer> span(int i) {
    return spans.get(i);
  }

  public int numberOfSpans() {
    return spans.size();
  }

  public Range<Integer> totalSpan() {
    return Range.between(start, end);
  }
}