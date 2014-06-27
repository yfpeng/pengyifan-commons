package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.tuple.Pair;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;

public class BratEventEquator implements Equator<BratEvent> {

  Equator<BratEntity> entityEquator;
  BratDocument        doc1;
  BratDocument        doc2;

  public BratEventEquator(
      Equator<BratEntity> entityEquator,
      BratDocument doc1,
      BratDocument doc2) {
    this.entityEquator = entityEquator;
    this.doc1 = doc1;
    this.doc2 = doc2;
  }

  @Override
  public boolean equate(BratEvent e1, BratEvent e2) {
    // type
    if (!e1.getType().equals(e2.getType())) {
      return false;
    }
    // trigger
    if (!entityEquator.equate(
        (BratEntity) doc1.getAnnotation(e1.getTriggerId()),
        (BratEntity) doc2.getAnnotation(e2.getTriggerId()))) {
      return false;
    }

    // arg
    boolean[] founds = new boolean[e2.numberOfArguments()];
    Arrays.fill(founds, false);

    for (int i = 0; i < e1.numberOfArguments(); i++) {
      Pair<String, String> p1 = e1.getArgPair(i);

      boolean foundP1 = false;
      for (int j = 0; j < e2.numberOfArguments(); j++) {
        Pair<String, String> p2 = e2.getArgPair(j);

        if (p1.getKey().equals(p2.getKey())
            && entityEquator.equate(
                (BratEntity) doc1.getAnnotation(p1.getValue()),
                (BratEntity) doc2.getAnnotation(p2.getValue()))) {
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
