package com.pengyifan.commons.lang3;

import static org.junit.Assert.*;

import org.apache.commons.lang3.Range;
import org.junit.Test;


public class RangeUtilsTest {
  
  private static final Range<Integer> INT_RANGE = Range.between(1, 2);
  private static final Range<Double> DOUBLE_RANGE = Range.between(1.5, 2.5);

  @Test
  public void testParseRange_int() {
    assertEquals(INT_RANGE, RangeUtils.parseRangeInt(INT_RANGE.toString()));
  }
  
  @Test
  public void testParseRange_float() {
    assertEquals(DOUBLE_RANGE, RangeUtils.parseRangeDouble(DOUBLE_RANGE.toString()));
  }
  
}
