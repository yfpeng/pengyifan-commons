package tests.com.pengyifan.commons.math;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import Jama.Matrix;

import com.pengyifan.commons.math.MatrixUtils2;

public class MatrixUtilsTest {

  @Test
  public void test() {
    double[][] a = { { 0, .9, 0 }, { 0, 0, 0.9 }, { 0, 0, 0 } };
    Matrix A = new Matrix(a);
    System.out.println("A=\n" + MatrixUtils2.print(A));

    RealMatrix m = MatrixUtils.createRealMatrix(a);
    System.out.println("A=\n" + MatrixUtils2.print(m));
  }

}
