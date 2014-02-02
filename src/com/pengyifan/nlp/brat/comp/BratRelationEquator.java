package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.tuple.Pair;

import com.pengyifan.nlp.brat.BratAnn;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratRelation;

public class BratRelationEquator implements Equator<BratRelation> {

  Equator<BratEntity> entityEquator;

  public BratRelationEquator(Equator<BratEntity> entityEquator) {
    this.entityEquator = entityEquator;
  }

  @Override
  public boolean equate(BratRelation r1, BratRelation r2) {
    // type
    if (!r1.getType().equals(r2.getType())) {
      return false;
    }
    // arg
    boolean[] founds = new boolean[r2.numberOfArguments()];
    Arrays.fill(founds, false);

    for (int i = 0; i < r1.numberOfArguments(); i++) {
      Pair<String, BratAnn> p1 = r1.getArgPair(i);

      boolean foundP1 = false;
      for (int j = 0; j < r2.numberOfArguments(); j++) {
        Pair<String, BratAnn> p2 = r2.getArgPair(j);

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
      if(!founds[i]) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hash(BratRelation arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

}
