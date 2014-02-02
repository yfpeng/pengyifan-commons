package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.tuple.Pair;

import com.pengyifan.nlp.brat.BratAnn;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;

public class BratEventEquator implements Equator<BratEvent> {

  Equator<BratEntity> entityEquator;

  public BratEventEquator(Equator<BratEntity> entityEquator) {
    this.entityEquator = entityEquator;
  }

  @Override
  public boolean equate(BratEvent e1, BratEvent e2) {
    // type
    if (!e1.getType().equals(e2.getType())) {
      return false;
    }
    // trigger
    if (!entityEquator.equate(
        (BratEntity) e1.getTrigger(),
        (BratEntity) e2.getTrigger())) {
      return false;
    }

    // arg
    boolean[] founds = new boolean[e2.numberOfArguments()];
    Arrays.fill(founds, false);

    for (int i = 0; i < e1.numberOfArguments(); i++) {
      Pair<String, BratAnn> p1 = e1.getArgPair(i);

      boolean foundP1 = false;
      for (int j = 0; j < e2.numberOfArguments(); j++) {
        Pair<String, BratAnn> p2 = e2.getArgPair(j);

        if (p1.getKey().equals(p2.getKey())
            && entityEquator.equate(
                (BratEntity) p1.getValue(),
                (BratEntity) p2.getValue())) {
          founds[j] = true;
          foundP1 = true;
        }
      }

      if (!foundP1) {
        return false;
      }
    }

    for (int i = 0; i < founds.length; i++) {
      if (!founds[i]) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hash(BratEvent arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

}
