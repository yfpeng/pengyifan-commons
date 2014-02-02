package com.pengyifan.nlp.brat.comp;

import org.apache.commons.collections4.Equator;

import com.pengyifan.nlp.brat.BratEntity;

public class BratEntityEquator implements Equator<BratEntity> {

  @Override
  public boolean equate(BratEntity e1, BratEntity e2) {
    if (!e1.getType().equals(e2.getType())) {
      return false;
    }
    if (e1.numberOfSpans() != e2.numberOfSpans()) {
      return false;
    }
    for (int i = 0; i < e1.numberOfSpans(); i++) {
      if (!e1.span(i).equals(e2.span(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hash(BratEntity arg0) {
    // TODO Auto-generated method stub
    return 0;
  }
}
