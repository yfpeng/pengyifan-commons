package com.pengyifan.nlp.trees;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Trees;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;

public final class TreeUtils {

  private static TregexPattern inVivo1 = TregexPattern
      .compile("ADJP=p <1 FW <2 FW");
  private static TregexPattern inVivo2 = TregexPattern
      .compile("PP=p <1 (IN <<: /in/) <2 (NP <<: /vitro/)");
  private static TsurgeonPattern inVivoOpt = Tsurgeon.parseOperation("prune p");

  public static void prune(Tree tree) {
    TregexMatcher m = inVivo1.matcher(tree);
    while (m.find()) {
      inVivoOpt.evaluate(tree, m);
    }
    m = inVivo2.matcher(tree);
    while (m.find()) {
      inVivoOpt.evaluate(tree, m);
    }
  }

  private TreeUtils() {
  }

  public static String adaptValue(String value) {
    value = value.replace("-LRB-", "(");
    value = value.replace("-RRB-", ")");
    value = value.replace("-LSB-", "[");
    value = value.replace("-RSB-", "]");
    value = value.replace("-LCB-", "{");
    value = value.replace("-RCB-", "}");
    value = value.replace("-lrb-", "(");
    value = value.replace("-rrb-", ")");
    value = value.replace("-lsb-", "[");
    value = value.replace("-rsb-", "]");
    value = value.replace("``", "\"");
    value = value.replace("''", "\"");
    value = value.replace("`", "'");
    return value;
  }

  public static Tree rightMostLeaf(Tree t) {
    return Trees.getLeaf(t, Trees.leaves(t).size() - 1);
  }

  public static Tree leftMostLeaf(Tree t) {
    return Trees.getLeaf(t, 0);
  }
}
