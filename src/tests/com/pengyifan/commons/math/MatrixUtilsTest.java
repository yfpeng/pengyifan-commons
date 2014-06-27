package tests.com.pengyifan.commons.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Jama.Matrix;

import com.pengyifan.commons.math.MatrixUtils2;

public class MatrixUtilsTest {

  private final Matrix oneXoneMatrix = new Matrix(new double[][] { {1}});
  private final String oneXoneMatrixExpected =
      "  1.00";

  private final Matrix matrix = new Matrix(new double[][] { {0, .9, 0}, {0, 0, 0.9}, {0, 0, 0}});
  private final String matrixExpected =
      "  0.00  0.90  0.00\n" + "  0.00  0.00  0.90\n" + "  0.00  0.00  0.00";


  @Test
  public void test_success() {
    assertEquals(MatrixUtils2.print(matrix), matrixExpected);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void test_emptyMatrix() {
    new Matrix(new double[][] {});
  }

  @Test
  public void test_1x1Matrix() {
    assertEquals(MatrixUtils2.print(oneXoneMatrix), oneXoneMatrixExpected);
  }
}
