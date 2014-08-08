package com.pengyifan.nlp.trees;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pengyifan.nlp.trees.GrammaticalStructureBuilder;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;

public class GrammaticalStructureBuilderTest {

  private static final Tree TREE2 = TreeSamples.getTree(TreeSamples.TREE_STR2);

  @Test
  public void test_str1() {

    Injector injector = Guice.createInjector(new TreeTestModule());
    GrammaticalStructureBuilder gsBuilder = injector
        .getInstance(GrammaticalStructureBuilder.class);

    GrammaticalStructure gs = gsBuilder.setTree(TREE2).build();
    
//    System.out.println(gs);
    
    CoreLabel label1 = gs.getNodeByIndex(1).label();
    assertEquals("He", label1.word());
    assertEquals(0, label1.beginPosition());
    assertEquals(2, label1.endPosition());
    
    CoreLabel label2 = gs.getNodeByIndex(2).label();
    assertEquals("eats", label2.word());
    assertEquals(3, label2.beginPosition());
    assertEquals(7, label2.endPosition());
    
    CoreLabel label3 = gs.getNodeByIndex(3).label();
    assertEquals("fiber", label3.word());
    assertEquals(8, label3.beginPosition());
    assertEquals(13, label3.endPosition());
  }

}
