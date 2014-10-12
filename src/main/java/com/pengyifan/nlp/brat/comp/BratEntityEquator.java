package com.pengyifan.nlp.brat.comp;

import org.apache.commons.collections4.Equator;

import com.pengyifan.nlp.brat.BratEntity;

@Deprecated
public abstract class BratEntityEquator implements Equator<BratEntity> {

  public static final int StrictMatching     = 1;
  public static final int ApproximateSpan    = 2;
  public static final int VeryStrictMatching = 3;

  public static BratEntityEquator create(int mode) {
    switch (mode) {
    case StrictMatching:
      return new BratEntityStrictEquator();
    case ApproximateSpan:
      return new BratEntityApproximateEquator();
    case VeryStrictMatching:
      return new BratEntityVeryStrictEquator();
    }
    throw new IllegalArgumentException("cannot find mode: " + mode);
  }
}

/**
 * for i in spans, begi == begi && endi == endi
 */
class BratEntityVeryStrictEquator extends BratEntityEquator {

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
    return arg0.hashCode();
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