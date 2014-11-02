package com.pengyifan.nlp.trees;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

public class TreeBuilderTest {

  @Test
  public void test_str2() {

    TreeBuilder builder = new TreeBuilder(
        OffsetTreeTransformer.treeTransformer());

    Tree tree = builder.build(TreeSamples.TREE_STR2);

    // tree.pennPrint();

    List<Tree> leaves = tree.getLeaves();
    assertEquals(leaves.size(), 3);

    CoreLabel label1 = (CoreLabel) leaves.get(0).label();
    assertEquals("He", label1.word());
    assertEquals(0, label1.beginPosition());
    assertEquals(2, label1.endPosition());

    CoreLabel label2 = (CoreLabel) leaves.get(1).label();
    assertEquals("eats", label2.word());
    assertEquals(3, label2.beginPosition());
    assertEquals(7, label2.endPosition());

    CoreLabel label3 = (CoreLabel) leaves.get(2).label();
    assertEquals("fiber", label3.word());
    assertEquals(8, label3.beginPosition());
    assertEquals(13, label3.endPosition());
  }

}
