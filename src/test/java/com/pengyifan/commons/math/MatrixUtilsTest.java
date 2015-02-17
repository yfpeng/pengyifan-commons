package com.pengyifan.commons.math;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import Jama.Matrix;

public class MatrixUtilsTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final Matrix oneXoneMatrix = new Matrix(new double[][] { { 1 } });
  private final String oneXoneMatrixExpected =
      "  1.00";

  private final Matrix matrix = new Matrix(new double[][] { { 0, .9, 0 },
      { 0, 0, 0.9 }, { 0, 0, 0 } });
  private final String matrixExpected =
      "  0.00  0.90  0.00" + System.lineSeparator()
          + "  0.00  0.00  0.90" + System.lineSeparator()
          + "  0.00  0.00  0.00";

  @Test
  public void test_success() {
    assertEquals(matrixExpected, MatrixUtils2.print(matrix));
  }

  @Test
  public void test_emptyMatrix() {
    thrown.expect(ArrayIndexOutOfBoundsException.class);
    new Matrix(new double[][] {});
  }

  @Test
  public void test_1x1Matrix() {
    assertEquals(oneXoneMatrixExpected, MatrixUtils2.print(oneXoneMatrix));
  }
}
