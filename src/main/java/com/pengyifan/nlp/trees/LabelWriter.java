package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;

public abstract class LabelWriter {

  public static final int CORE_LABEL = 0;
  public static final int WORD_LABEL = 1;

  public static LabelWriter createLabelWriter(int label) {
    if (label == CORE_LABEL) {
      return new CoreLabelWriter();
    }
    if (label == WORD_LABEL) {
      return new WordLabelWriter();
    }
    return null;
  }

  public abstract String labelString(Tree t);
}

class WordLabelWriter extends LabelWriter {

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
      sb.append(label.word());
    } else if (t.isPreTerminal() && label.tag() != null) {
      sb.append(label.tag());
    } else if (t.isPhrasal() && label.category() != null) {
      sb.append(label.category());
    } else if (label.value() != null) {
      sb.append(label.value());
    } else {
      throw new IllegalArgumentException("Cannot find the label to print: " + t);
    }
    return sb.toString();
  }
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
    } else if (t.isPreTerminal() && label.tag() != null) {
      sb.append(label.tag());
    } else if (t.isPhrasal() && label.category() != null) {
      sb.append(label.category());
    } else if (label.value() != null) {
      sb.append(label.value());
    } else {
      throw new IllegalArgumentException("Cannot find the label to print: " + t);
    }
    return sb.toString();
  }
}