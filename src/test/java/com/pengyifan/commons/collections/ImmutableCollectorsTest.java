package com.pengyifan.commons.collections;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class ImmutableCollectorsTest {

  @Test
  public void test_toList() {
    List<String> list = Lists.newArrayList("a", "b");
    ImmutableList<String> sublist = list
        .stream()
        .filter(s -> s.charAt(0) == 'a')
        .collect(ImmutableCollectors.toList());
    assertTrue(Iterables.getOnlyElement(sublist).equals("a"));
  }

  @Test
  public void test_toSet() {
    List<String> list = Lists.newArrayList("a", "b");
    ImmutableSet<String> subset = list
        .stream()
        .filter(s -> s.charAt(0) == 'a')
        .collect(ImmutableCollectors.toSet());
    assertTrue(Iterables.getOnlyElement(subset).equals("a"));
  }
}
