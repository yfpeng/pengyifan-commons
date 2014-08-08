package com.pengyifan.kernel.svm;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.math3.util.Precision;

public class LibSVMPrintStream implements Closeable {

  private final DecimalFormat format;
  private final PrintStream ps;

  public LibSVMPrintStream(PrintStream ps) {
    this(ps, 4, 2);
  }

  public LibSVMPrintStream(PrintStream ps, DecimalFormat format) {
    this.ps = ps;
    this.format = format;
  }

  public LibSVMPrintStream(
      PrintStream ps,
      int maxFractionDigits,
      int minFractionDigits) {
    this.format = new DecimalFormat();
    format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
    format.setMinimumIntegerDigits(1);
    format.setMaximumFractionDigits(maxFractionDigits);
    format.setMinimumFractionDigits(minFractionDigits);
    format.setGroupingUsed(false);
    this.ps = ps;
  }

  /**
   * New training instance for xi:
   * <p>
   * The pair "index:value" gives a feature (attribute) value: "index" is an
   * integer starting from 1 and "value" is a real number.
   * 
   * @param label the target value which can be any real number
   * @param row Number starts at 1
   */
  public void printRow(int label, double row[]) {
    StringBuilder sb = new StringBuilder()
        .append(label);// label
    // kernel
    for (int j = 0; j < row.length; j++) {
      if (!Precision.equals(row[j], 0.0)) {
        sb.append(' ').append(j + 1).append(':')
            .append(format.format(row[j]));
      }
    }
    ps.println(sb);
  }

  public void printRow(int label, int row[]) {
    StringBuilder sb = new StringBuilder()
        .append(label);// label
    // kernel
    for (int j = 0; j < row.length; j++) {
      if (row[j] != 0) {
        sb.append(' ').append(j + 1).append(':').append(row[j]);
      }
    }
    ps.println(sb);
  }

  /**
   * New training instance for xi:
   * 
   * label 0:i 1:K(xi,x1) ... L:K(xi,xL)
   * 
   * New testing instance for any x:
   * 
   * label 0:? 1:K(x,x1) ... L:K(x,xL)
   * 
   * That is, in the training file the first column must be the "ID" of xi. In
   * testing, ? can be any value.
   * 
   * @param label
   * @param row
   * @param rowNumber starts at 1
   */
  public void printPrecomputedRow(int label, double row[], int rowNumber) {
    StringBuilder sb = new StringBuilder()
        .append(label)// label
        .append(" 0:" + (rowNumber));// ID
    // kernel
    for (int j = 0; j < row.length; j++) {
      sb.append(' ').append(j + 1).append(':').append(format.format(row[j]));
    }
    ps.println(sb);
  }

  public void printPrecomputedRow(int label, int row[], int rowNumber) {
    StringBuilder sb = new StringBuilder()
        .append(label)// label
        .append(" 0:" + rowNumber);// ID
    // kernel
    for (int j = 0; j < row.length; j++) {
      sb.append(' ').append(j + 1).append(':').append(row[j]);
    }
    ps.println(sb);
  }

  @Override
  public void close()
      throws IOException {
    ps.close();
  }
}
