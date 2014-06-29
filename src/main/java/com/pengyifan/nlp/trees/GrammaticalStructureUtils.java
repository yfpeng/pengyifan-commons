package com.pengyifan.nlp.trees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import com.google.common.primitives.Ints;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TreeGraphNode;


public class GrammaticalStructureUtils {
  /**
   * only leaves with arc
   */
  public static List<TreeGraphNode> getLeafNodes(GrammaticalStructure gs) {
    List<TreeGraphNode> nodes = new ArrayList<TreeGraphNode>(gs.getNodes());
    CollectionUtils.filter(nodes, new Predicate<TreeGraphNode>() {

      @Override
      public boolean evaluate(TreeGraphNode node) {
        return node.isLeaf() && node.index() != 0;
      }
    });
    Collections.sort(nodes, new Comparator<TreeGraphNode>() {

      @Override
      public int compare(TreeGraphNode arg0, TreeGraphNode arg1) {
        return Ints.compare(arg0.index(), arg1.index());
      }
    });
    return nodes;
  }
}
