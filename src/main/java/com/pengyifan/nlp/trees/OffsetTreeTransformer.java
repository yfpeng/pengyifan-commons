package com.pengyifan.nlp.trees;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeTransformer;

/**
 * <p>If the tree node is a leave and is in the form of
 * "word_beginOffset_endOffset", transform the leaf to CoreLabel.beginPosition
 * and CoreLabel.endPosition. Remove the offsets from the leaf label</p>
 * 
 * <p>If the tree node is pre-terminal (Part-of-speech), store the info into
 * CoreLabel.tag</p>
 * 
 * <p>If the tree node is phrasal, store the info into CoreLabel.category</p>
 * 
 * <p>This will change a local (one-level).</p>
 */
public class OffsetTreeTransformer implements TreeTransformer {

  private static final TreeTransformer ttf = new OffsetTreeTransformer();

  public static TreeTransformer treeTransformer() {
    return ttf;
  }

  @Inject
  private OffsetTreeTransformer() {

  }

  @Override
  public Tree transformTree(Tree t) {

    Validate.notNull(t, "tree node cannot be null");
    Validate.isInstanceOf(
        CoreLabel.class,
        t.label(),
        "tree node's label has to be CoreLabel");

    // change label
    CoreLabel label = (CoreLabel) t.label();

    // change attributes
    String value = label.value();
    if (t.isPreTerminal()) {
      label.setTag(value);
      return t;
    }
    if (t.isPhrasal()) {
      label.setCategory(value);
      return t;
    }
    if (t.isLeaf()) {

      int lastUnderline = value.lastIndexOf('_');
      if (lastUnderline == -1) {
        label.setWord(value);
        return t;
      }
      try {
        int to = Integer.parseInt(value.substring(lastUnderline + 1));

        int secondLastUnderline = value.lastIndexOf('_', lastUnderline - 1);

        if (secondLastUnderline == -1) {
          label.setWord(value);
          return t;
        }

        int from = Integer.parseInt(value.substring(
            secondLastUnderline + 1,
            lastUnderline));

        String word = value.substring(0, secondLastUnderline);
        label.setBeginPosition(from);
        label.setEndPosition(to);
        label.setWord(word);
        label.setValue(word);
      } catch (NumberFormatException e) {
        System.err.println("cannot parse: " + e.getMessage());
        label.setWord(value);
      }

    }

    return t;
  }

}
