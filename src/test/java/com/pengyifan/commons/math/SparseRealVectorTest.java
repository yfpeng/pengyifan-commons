package com.pengyifan.commons.math;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class SparseRealVectorTest {
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSetEntry() {
    SparseRealVector v = new SparseRealVector(10);
    v.setEntry(0, 1);
    assertEquals(1, v.getEntry(0), 0.000001);
    v.setEntry(2, 2);
    assertEquals(2, v.getEntry(2), 0.000001);
    
    assertEquals(0, v.getEntry(5), 0.000001);
    v.setEntry(5, 0);
    assertEquals(0, v.getEntry(5), 0.000001);
    v.setEntry(5, 1);
    assertEquals(1, v.getEntry(5), 0.000001);
  }
  
  @Test
  public void testDimension() {
    SparseRealVector v = new SparseRealVector(10);
    assertEquals(10, v.getDimension());
  }

  @Test
  public void testSetEntry_outOfDimension() {
    SparseRealVector v = new SparseRealVector(10);
    thrown.expect(IllegalArgumentException.class);
    v.setEntry(11, 0);
  }

  @Test
  public void testDotProduct() {
    SparseRealVector v1 = new SparseRealVector(10);
    v1.setEntry(0, 4);
    v1.setEntry(2, 2);
    
    SparseRealVector v2 = new SparseRealVector(10);
    v2.setEntry(0, 5);
    v2.setEntry(1, 2);
    
    assertEquals(20, v1.dotProduct(v2), 0.000001);
    assertEquals(20, v2.dotProduct(v1), 0.000001);
  }

  
}
