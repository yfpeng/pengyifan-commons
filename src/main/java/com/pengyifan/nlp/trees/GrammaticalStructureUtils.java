package com.pengyifan.nlp.trees;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.Trees;


public class GrammaticalStructureUtils {
  /**
   * only leaves with arc
   */
  public static List<TreeGraphNode> getLeafNodes(
      GrammaticalStructure grammaticalStructure) {
    TreeGraphNode root = grammaticalStructure.root();
    List<Tree> leaves = Trees.leaves(root);
    return FluentIterable.from(leaves)
        .transform(new Function<Tree, TreeGraphNode>() {

          @Override
          public TreeGraphNode apply(Tree input) {
            return (TreeGraphNode) input;
          }
        }).toList();
  }

  public static TreeGraphNode getFirstNode(
      GrammaticalStructure grammaticalStructure) {
    return getLeafNodes(grammaticalStructure).get(0);
  }

  public static TreeGraphNode getLastNode(
      GrammaticalStructure grammaticalStructure) {
    List<TreeGraphNode> leaves = getLeafNodes(grammaticalStructure);
    return leaves.get(leaves.size() - 1);
  }
}
