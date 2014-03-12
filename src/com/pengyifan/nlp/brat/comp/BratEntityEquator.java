package com.pengyifan.nlp.brat.comp;

import org.apache.commons.collections4.Equator;

import com.pengyifan.nlp.brat.BratEntity;

public abstract class BratEntityEquator implements Equator<BratEntity> {

  public static final int StrictMatching  = 1;
  public static final int ApproximateSpan = 2;

  public static BratEntityEquator create(int mode) {
    switch (mode) {
    case StrictMatching:
      return new BratEntityStrictEquator();
    case ApproximateSpan:
      return new BratEntityApproximateEquator();
    }
    throw new IllegalArgumentException("cannot find mode: " + mode);
  }
}

/**
 * beg1 == beg2 && end2 == end1
 */
class BratEntityStrictEquator extends BratEntityEquator {

  @Override
  public boolean equate(BratEntity e1, BratEntity e2) {
    if (!e1.getType().equals(e2.getType())) {
      return false;
    }
    return e1.totalSpan().equals(e2.totalSpan());
  }

  @Override
  public int hash(BratEntity arg0) {
    // TODO Auto-generated method stub
    return arg0.hashCode();
  }
}

/**
 * beg1 <= beg2 && end2 <= end1
 */
class BratEntityApproximateEquator extends BratEntityEquator {

  @Override
  public boolean equate(BratEntity e1, BratEntity e2) {
    if (!e1.getType().equals(e2.getType())) {
      return false;
    }
    return e1.totalSpan().containsRange(e2.totalSpan());
  }

  @Override
  public int hash(BratEntity arg0) {
    // TODO Auto-generated method stub
    return arg0.hashCode();
  }
}