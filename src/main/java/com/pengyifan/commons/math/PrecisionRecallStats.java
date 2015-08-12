package com.pengyifan.commons.math;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.text.NumberFormat;
import java.util.List;

public class PrecisionRecallStats<E> {
  private List<E> tps;
  private List<E> fps;
  private List<E> fns;

  public PrecisionRecallStats(List<E> tps, List<E> fps, List<E> fns) {
    this.tps = Lists.newArrayList(tps);
    this.fps = Lists.newArrayList(fps);
    this.fns = Lists.newArrayList(fns);
  }

  public PrecisionRecallStats() {
    tps = Lists.newArrayList();
    fps = Lists.newArrayList();
    fns = Lists.newArrayList();
  }

  public List<E> getTPs() {
    return tps;
  }

  public List<E> getFPs() {
    return fps;
  }

  public List<E> getFNs() {
    return fns;
  }

  public int getTP() {
    return tps.size();
  }

  public int getFP() {
    return fps.size();
  }

  public int getFN() {
    return fns.size();
  }

  public void incrementTP(E e) {
    tps.add(e);
  }

  public void incrementFP(E e) {
    fps.add(e);
  }

  public void incrementFN(E e) {
    fns.add(e);
  }

  /**
   * Returns the current precision: <tt>tp/(tp+fp)</tt>.
   * Returns 1.0 if tp and fp are both 0.
   */
  public double getPrecision() {
    if (getTP() == 0 && getFP() == 0) {
      return Double.NaN;
    }
    return ((double) getTP()) / (getTP() + getFP());
  }

  /**
   * Returns the current precision: <tt>tp/(tp+fp)</tt>.
   * Returns 1.0 if tp and fp are both 0.
   */
  public String getPrecision(int numDigits) {
    double d = getPrecision();
    return Double.isNaN(d) ? "NaN" : getNumberFormat(numDigits).format(d);
  }

  /**
   * Returns the current recall: <tt>tp/(tp+fn)</tt>.
   * Returns NaN if tp and fn are both 0.
   */
  public double getRecall() {
    if (getTP() == 0 && getFN() == 0) {
      return Double.NaN;
    }
    return ((double) getTP()) / (getTP() + getFN());
  }

  /**
   * Returns the current recall: <tt>tp/(tp+fn)</tt>.
   * Returns NaN if tp and fn are both 0.
   */
  public String getRecall(int numDigits) {
    double d = getRecall();
    return Double.isNaN(d) ? "NaN" : getNumberFormat(numDigits).format(d);
  }

  /**
   * Returns the current F1 measure (<tt>alpha=0.5</tt>).
   */
  public double getFMeasure() {
    return getFMeasure(0.5);
  }

  /**
   * Returns the current F1 measure (<tt>alpha=0.5</tt>).
   */
  public String getFMeasure(int numDigits) {
    return getNumberFormat(numDigits).format(getFMeasure());
  }

  /**
   * Returns the F-Measure with the given mixing parameter (must be between 0 and 1).
   * If either precision or recall are 0, return 0.0.
   * <tt>F(alpha) = 1/(alpha/precision + (1-alpha)/recall)</tt>
   */
  public double getFMeasure(double alpha) {
    double pr = getPrecision();
    double re = getRecall();
    if (pr == 0 || re == 0) {
      return 0.0;
    }
    return 1.0 / ((alpha / pr) + (1.0 - alpha) / re);
  }

  /**
   * Returns the F-Measure with the given mixing parameter (must be between 0 and 1).
   * If either precision or recall are 0, return 0.0.
   * <tt>F(alpha) = 1/(alpha/precision + (1-alpha)/recall)</tt>
   */
  public String getFMeasure(double alpha, int numDigits) {
    return getNumberFormat(numDigits).format(getFMeasure(alpha));
  }

  /**
   * Returns a String representation of this PrecisionRecallStats, indicating the number of tp, fp,
   * fn counts.
   */
  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("tp", getTP())
        .append("fp", getFP())
        .append("fn", getFN())
        .toString();
  }

  private NumberFormat getNumberFormat(int numDigits) {
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(numDigits);
    return nf;
  }
}
