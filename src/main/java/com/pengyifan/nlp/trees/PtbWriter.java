package com.pengyifan.nlp.trees;

import edu.stanford.nlp.trees.Tree;

public class PtbWriter {

  public static String print(Tree t, LabelWriter writer) {
    return printHelper(new StringBuilder(), t, writer).toString();
  }

  public static String print(Tree t) {
    return print(t, LabelWriter.createLabelWriter(LabelWriter.CORE_LABEL));
  }

  private static StringBuilder printHelper(StringBuilder sb, Tree t,
      LabelWriter labelPrinter) {
    if (t.isLeaf()) {
      return sb.append(labelPrinter.labelString(t));
    } else {
      sb.append('(');
      if (t.label() != null) {
        sb.append(labelPrinter.labelString(t));
      }
      Tree[] kids = t.children();
      if (kids != null) {
        for (Tree kid : kids) {
          sb.append(' ');
          printHelper(sb, kid, labelPrinter);
        }
      }
      return sb.append(')');
    }
  }
}
