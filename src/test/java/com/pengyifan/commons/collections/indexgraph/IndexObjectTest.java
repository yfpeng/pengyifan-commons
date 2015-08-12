package com.pengyifan.commons.collections.indexgraph;

import com.google.common.testing.EqualsTester;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class IndexObjectTest {

  private static final int INDEX1 = 1;
  private static final int INDEX2 = 2;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void test_index() {
    IndexObject obj = new IndexObject(INDEX1);
    assertEquals(INDEX1, obj.getIndex());
  }

  @Test
  public void testEquals() {
    IndexObject obj = new IndexObject(INDEX1);
    IndexObject diffIndex = new IndexObject(INDEX2);
    new EqualsTester()
        .addEqualityGroup(obj)
        .addEqualityGroup(diffIndex)
        .testEquals();
  }

  @Test
  public void test_allFields() {
    IndexObject obj = new IndexObject(INDEX1);
    assertEquals(INDEX1, obj.getIndex());
  }
}
