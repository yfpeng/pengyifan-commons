package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

public abstract class LabelWriter {

  public static final int CORE_LABEL = 0;

  public static LabelWriter createLabelWriter(int label) {
    if (label == CORE_LABEL) {
      return new CoreLabelWriter();
    }
    return null;
  }

  public abstract String labelString(Tree t);
}

class CoreLabelWriter extends LabelWriter {

  @Override
  public String labelString(Tree t) {

    Validate.isInstanceOf(
        CoreLabel.class,
        t.label(),
        "Wrong class, object is of class %s",
        t.label().getClass().getName());
    CoreLabel label = (CoreLabel) t.label();

    StringBuilder sb = new StringBuilder();
    if (t.isLeaf()) {
      sb.append(label.word()).append('_').append(label.beginPosition())
          .append('_').append(label.endPosition());
    } else if (t.isPreTerminal()) {
      sb.append(label.tag());
    } else {
      sb.append(label.category());
    }
    return sb.toString();
  }
}