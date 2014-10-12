package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;

import org.apache.commons.collections4.Equator;

import com.pengyifan.nlp.brat.BratAttribute;
import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;

/**
 * Can only handle Entity's Attribute
 */
@Deprecated
public class BratAttributeEquator implements Equator<BratAttribute> {

  Equator<BratEntity> entityEquator;
  BratDocument        doc1;
  BratDocument        doc2;

  public BratAttributeEquator(
      Equator<BratEntity> entityEquator,
      BratDocument doc1,
      BratDocument doc2) {
    this.entityEquator = entityEquator;
    this.doc1 = doc1;
    this.doc2 = doc2;
  }

  @Override
  public boolean equate(BratAttribute a1, BratAttribute a2) {
    // type
    if (!a1.getType().equals(a2.getType())) {
      return false;
    }
    // refId
    if (entityEquator.equate(
        (BratEntity) doc1.getAnnotation(a1.getRefId()),
        (BratEntity) doc2.getAnnotation(a2.getRefId()))) {
      return false;
    }

    // arg
    boolean[] founds = new boolean[a2.numberOfAttributes()];
    Arrays.fill(founds, false);

    for (int i = 0; i < a1.numberOfAttributes(); i++) {
      String s1 = a1.getAttribute(i);

      boolean foundP1 = false;
      for (int j = 0; j < a2.numberOfAttributes(); j++) {
        String s2 = a2.getAttribute(j);

        if (s1.equals(s2)) {
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
  public int hash(BratAttribute arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

}
