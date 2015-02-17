package com.pengyifan.commons.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SparseRealMatrixTest {
  
  @Rule
  public ExpectedException thrown= ExpectedException.none();

  private static final SparseRealMatrix EMPTY                = new SparseRealMatrix(
                                                                 0, 0);

  private static SparseRealMatrix       matrix;
  private static final double[][]       MATRIX_DOUBLE        = { { 1, 0, 2 },
      { 3, 4, 0 }                                           };

  private static final String           EXPECT_MATRIX_STRING = "(0,0,1.0000),(0,2,2.0000),(1,0,3.0000),(1,1,4.0000)";

  @Before
  public void setUp() {
    matrix = new SparseRealMatrix(MATRIX_DOUBLE.length, MATRIX_DOUBLE[0].length);
    for (int i = 0; i < MATRIX_DOUBLE.length; i++) {
      for (int j = 0; j < MATRIX_DOUBLE[i].length; j++) {
        if (MATRIX_DOUBLE[i][j] != 0) {
          matrix.setEntry(i, j, MATRIX_DOUBLE[i][j]);
        }
      }
    }
  }

  @Test
  public void test_success() {
    assertFalse(matrix.isSquare());
    assertEquals(matrix.getColumnDimension(), MATRIX_DOUBLE[0].length);
    assertEquals(matrix.getRowDimension(), MATRIX_DOUBLE.length);
    assertEquals(matrix.toString(), EXPECT_MATRIX_STRING);
  }

  @Test
  public void test_EmptyMatrix() {
    assertTrue(EMPTY.isSquare());
    assertEquals(EMPTY.getColumnDimension(), 0);
    assertEquals(EMPTY.getRowDimension(), 0);
    assertEquals(EMPTY.toString(), "");
  }

  @Test
  public void test_dotProduct() {
    double dotProduct = matrix.dotProduct(matrix);
    assertEquals(dotProduct, 30, 0.00001f);
  }
  
  @Test
  public void test_dotProductFailure() {
    thrown.expect(IllegalArgumentException.class);
    matrix.dotProduct(EMPTY);
  }
}
