package com.pengyifan.commons.math;

import static org.junit.Assert.*;

import org.apache.commons.lang3.Range;
import org.junit.Test;


public class RangeUtilsTest {

  @Test
  public void testIsBefore() {
    Range<Integer> range1 = Range.between(299, 311);
    Range<Integer> range2 = Range.between(299, 305);
    
    assertTrue(RangeUtils.isBefore(range1, range2));
    assertTrue(RangeUtils.isBefore(range2, range1));
  }

  @Test
  public void testIsBefore2() {
    Range<Integer> range1 = Range.between(146, 151);
    Range<Integer> range2 = Range.between(148, 151);
    
    assertTrue(RangeUtils.isBefore(range1, range2));
  }
}
