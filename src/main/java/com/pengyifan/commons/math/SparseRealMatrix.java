package com.pengyifan.commons.math;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.linear.AnyMatrix;

public class SparseRealMatrix implements AnyMatrix {

  private int                                rowDimension;
  private int                                columnDimension;
  private HashMap<Integer, SparseRealVector> map;

  public SparseRealMatrix(int rowDimension, int columnDimension) {
    this.rowDimension = rowDimension;
    this.columnDimension = columnDimension;
    map = new HashMap<Integer, SparseRealVector>();
  }

  @Override
  public int getColumnDimension() {
    return columnDimension;
  }

  @Override
  public int getRowDimension() {
    return rowDimension;
  }

  @Override
  public boolean isSquare() {
    return rowDimension == columnDimension;
  }

  public void setEntry(int row, int column, double value) {

    Validate.isTrue(row < rowDimension);
    Validate.isTrue(column < columnDimension);

    SparseRealVector vec = map.get(row);
    if (vec == null) {
      vec = new SparseRealVector(columnDimension);
      map.put(row, vec);
    }
    vec.setEntry(column, value);
  }

  public double getEntry(int row, int column) {

    Validate.isTrue(row < rowDimension);
    Validate.isTrue(column < columnDimension);

    SparseRealVector vec = map.get(row);
    if (vec == null) {
      return 0.0;
    } else {
      return vec.getEntry(column);
    }
  }

  public double dotProduct(SparseRealMatrix matrix) {
    Validate.isTrue(
        matrix.rowDimension == rowDimension,
          "both matrix must have same row dimention");
    Validate.isTrue(
        matrix.columnDimension == columnDimension,
          "both matrix must have same column dimention");

    double d = 0;

    for (Map.Entry<Integer, SparseRealVector> row : map.entrySet()) {
      int rowInt = row.getKey();
      SparseRealVector vec = matrix.map.get(rowInt);
      if (vec != null) {
        d += row.getValue().dotProduct(vec);
      }
    }

    return d;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Integer, SparseRealVector> row : map.entrySet()) {
      int rowInt = row.getKey();
      for (Map.Entry<Integer, Double> col : row.getValue().map.entrySet()) {
        sb.append(String.format(
            "(%d,%d,%.4f),",
              rowInt,
              col.getKey(),
              col.getValue()));
      }
    }
    if (sb.length() == 0) {
      return "";
    } else {
      return sb.substring(0, sb.length() - 1);
    }
  }

  class SparseRealVector {

    int                      dimension;
    HashMap<Integer, Double> map;

    public SparseRealVector(int dimension) {
      this.dimension = dimension;
      map = new HashMap<Integer, Double>();
    }

    public void setEntry(int index, double value) {
      map.put(index, value);
    }

    public double getEntry(int index) {
      Double value = map.get(index);
      return value == null ? 0.0 : value;
    }

    public int getDimension() {
      return dimension;
    }

    public double dotProduct(SparseRealVector v) {
      Validate.isTrue(dimension == v.dimension);
      double dotProduct = 0;

      for (Map.Entry<Integer, Double> cell : map.entrySet()) {
        double vv = v.getEntry(cell.getKey());
        if (vv != 0) {
          dotProduct += vv * cell.getValue();
        }
      }

      return dotProduct;
    }
  }
}
