package com.pengyifan.nlp.brat;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;


public class BratUtilsTest {

  @Test
  public void testRemove() {
    List<BratEntity> list = Lists.newArrayList();
    list.add(createEntity("1", "Protein"));
    list.add(createEntity("2", "Protein"));
    list.add(createEntity("3", "Protein2"));
    
    assertEquals(3, list.size());
    
    list = BratUtils.filtEntities(list, "Protein");
    assertEquals(2, list.size());
  }

  private BratEntity createEntity(final String id, final String type) {
    BratEntity ent = new BratEntity();
    ent.setId(id);
    ent.setType(type);
    return ent;
  }
}
