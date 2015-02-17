package com.pengyifan.nlp.trees;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TreeGraphNode;

public class GrammaticalStructureUtilsTest {

  private static final int BEGIN1 = 1755;
  private static final int END1 = 1990;
  private static final GrammaticalStructure GS1 = TreeSamples
      .getGrammaticalStructure(TreeSamples.TREE_STR1);

  private static final int BEGIN2 = 0;
  private static final int END2 = 13;
  private static final GrammaticalStructure GS2 = TreeSamples
      .getGrammaticalStructure(TreeSamples.TREE_STR2);

  @Test
  public void testGetFirstNode() {
    TreeGraphNode firstNode = GrammaticalStructureUtils.getFirstNode(GS1);
    assertEquals(BEGIN1, firstNode.label().beginPosition());

    firstNode = GrammaticalStructureUtils.getFirstNode(GS2);
    assertEquals(BEGIN2, firstNode.label().beginPosition());
  }

  @Test
  public void testGetLastNode() {
    TreeGraphNode lastNode = GrammaticalStructureUtils.getLastNode(GS1);
    assertEquals(END1, lastNode.label().endPosition());

    lastNode = GrammaticalStructureUtils.getLastNode(GS2);
    assertEquals(END2, lastNode.label().endPosition());
  }

  @Test
  public void testGetLeafNodes() {
    List<TreeGraphNode> leaves = GrammaticalStructureUtils.getLeafNodes(GS1);
    assertEquals(END1, leaves.get(leaves.size() - 1).label().endPosition());

    leaves = GrammaticalStructureUtils.getLeafNodes(GS2);
    assertEquals(END2, leaves.get(leaves.size() - 1).label().endPosition());
  }
}
