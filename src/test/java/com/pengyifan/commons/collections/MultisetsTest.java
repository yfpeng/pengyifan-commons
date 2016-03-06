package com.pengyifan.commons.collections;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MultisetsTest {

  private Multiset<Integer> multisets;
  private Multiset<Integer> emptysets;

  @Before
  public void setUp() {
    emptysets = HashMultiset.create();
    multisets = HashMultiset.create();
    multisets.add(1);
    multisets.add(2);
    multisets.add(2);
    multisets.add(3);
    multisets.add(3);
    multisets.add(3);
    multisets.add(4);
  }

  @Test
  public void testArgmax() {
    assertEquals(3, Multisets.argmax(multisets).get().intValue());
    assertFalse(Multisets.argmax(emptysets).isPresent());
  }

  @Test
  public void testArgmaxSet() {
    assertThat(Multisets.argmaxSet(multisets), is(Sets.newHashSet(3)));
    assertTrue(Multisets.argmaxSet(emptysets).isEmpty());
  }

  @Test
  public void testArgmin() {
    assertEquals(1, Multisets.argmin(multisets).get().intValue());
    assertFalse(Multisets.argmax(emptysets).isPresent());
  }

  @Test
  public void testArgminSet() {
    assertThat(Multisets.argminSet(multisets), is(Sets.newHashSet(1, 4)));
    assertTrue(Multisets.argminSet(emptysets).isEmpty());
  }

  @Test
  public void testAverageCount() {
    assertEquals(1.75, Multisets.averageCount(multisets), 0f);
    assertEquals(0, Multisets.averageCount(emptysets), 0f);
  }

  @Test
  public void testElementsAbove() {
    assertThat(Multisets.elementsAbove(multisets, 0), is(Sets.newHashSet(1, 2, 3, 4)));
    assertThat(Multisets.elementsAbove(multisets, 1), is(Sets.newHashSet(1, 2, 3, 4)));
    assertThat(Multisets.elementsAbove(multisets, 2), is(Sets.newHashSet(2, 3)));
    assertThat(Multisets.elementsAbove(multisets, 3), is(Sets.newHashSet(3)));
    assertTrue(Multisets.elementsAbove(multisets, 4).isEmpty());

    assertTrue(Multisets.elementsAbove(emptysets, 4).isEmpty());
  }

  @Test
  public void testElementsAt() {
    assertThat(Multisets.elementsAt(multisets, 3), is(Sets.newHashSet(3)));
    assertThat(Multisets.elementsAt(multisets, 2), is(Sets.newHashSet(2)));
    assertThat(Multisets.elementsAt(multisets, 1), is(Sets.newHashSet(1, 4)));
    assertTrue(Multisets.elementsAt(multisets, 0).isEmpty());

    assertTrue(Multisets.elementsAt(emptysets, 0).isEmpty());
  }

  @Test
  public void testElementsBelow() {
    assertThat(Multisets.elementsBelow(multisets, 4), is(Sets.newHashSet(1, 2, 3, 4)));
    assertThat(Multisets.elementsBelow(multisets, 3), is(Sets.newHashSet(1, 2, 3, 4)));
    assertThat(Multisets.elementsBelow(multisets, 2), is(Sets.newHashSet(1, 2, 4)));
    assertThat(Multisets.elementsBelow(multisets, 1), is(Sets.newHashSet(1, 4)));
    assertTrue(Multisets.elementsBelow(multisets, 0).isEmpty());

    assertTrue(Multisets.elementsBelow(emptysets, 0).isEmpty());
  }

  @Test
  public void testMax() {
    assertEquals(3, Multisets.max(multisets).getAsInt());
    assertFalse(Multisets.max(emptysets).isPresent());
  }

  @Test
  public void testMin() {
    assertEquals(1, Multisets.min(multisets).getAsInt());
    assertFalse(Multisets.min(emptysets).isPresent());
  }
}