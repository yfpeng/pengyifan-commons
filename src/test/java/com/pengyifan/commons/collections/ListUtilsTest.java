package com.pengyifan.commons.collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ListUtilsTest {

  private List<Integer> inputList = Arrays.asList(90, 50, 20, 80, 70, 30,
      10, 60, 40);
  private List<Integer> expectedList = Arrays.asList(20, 30, 60 );
  
  private List<Integer> emptyList = Arrays.asList();

  @Before
  public void setUp() {
  }

  @Test
  public void testLongestIncreasingSubsequence_success() {
    List<Integer> sublist = ListUtils.longestIncreasingSubsequence(inputList);
    assertThat(sublist, is(expectedList));
  }
  
  @Test
  public void testLongestIncreasingSubsequence_emptyList() {
    List<Integer> sublist = ListUtils.longestIncreasingSubsequence(emptyList);
    assertThat(sublist, is(emptyList));
  }
  
  @Test
  public void testLongestIncreasingSubsequence_nullList() {
    List<Integer> sublist = ListUtils.longestIncreasingSubsequence(null);
    assertNull(sublist); 
  }
}
