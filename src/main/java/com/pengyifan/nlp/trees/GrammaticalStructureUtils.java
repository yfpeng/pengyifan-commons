package com.pengyifan.nlp.trees;

import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.Trees;

public class GrammaticalStructureUtils {

  /**
   * only leaves with arc
   * @param grammaticalStructure
   * @return list of leaves in the dependency graph
   */
  public static List<TreeGraphNode> getLeafNodes(
      GrammaticalStructure grammaticalStructure) {
    return Trees.leaves(grammaticalStructure.root()).stream().map(tree -> {
      return (TreeGraphNode) tree;
    }).collect(Collectors.toList());
  }

  public static TreeGraphNode getFirstNode(
      GrammaticalStructure grammaticalStructure) {
    return (TreeGraphNode) Trees.leaves(grammaticalStructure.root()).get(0);
  }

  public static TreeGraphNode getLastNode(
      GrammaticalStructure grammaticalStructure) {
    List<Tree> leaves = Trees.leaves(grammaticalStructure.root());
    return (TreeGraphNode) leaves.get(leaves.size() - 1);
  }
}
