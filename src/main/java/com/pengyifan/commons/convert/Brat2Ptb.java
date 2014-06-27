package com.pengyifan.commons.convert;

import java.util.HashMap;

import org.apache.commons.lang3.Validate;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratRelation;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;

public class Brat2Ptb {

  public static MemoryTreebank pennTree(BratDocument bratDoc) {
    MemoryTreebank treebank = new MemoryTreebank();

    HashMap<String, Tree> map = new HashMap<String, Tree>();

    for (BratEntity ent : bratDoc.getEntities()) {

      CoreLabel leafLabel = new CoreLabel();
      leafLabel.setBeginPosition(ent.beginPosition());
      leafLabel.setEndPosition(ent.endPosition());
      leafLabel.setWord(ent.getText());
      Tree terminal = new LabeledScoredTreeNode(leafLabel);

      CoreLabel label = new CoreLabel();
      label.setTag(ent.getType());
      Tree preTerminal = new LabeledScoredTreeNode(label);
      preTerminal.addChild(terminal);

      map.put(ent.getId(), preTerminal);
    }

    for (BratRelation rel : bratDoc.getRelations()) {
      CoreLabel label = new CoreLabel();
      label.setCategory(rel.getType());
      Tree phrasal = new LabeledScoredTreeNode(label);
      for (int i = 0; i < rel.numberOfArguments(); i++) {
        Validate.isTrue(
            Integer.parseInt(rel.getArgRole(i).substring(1)) == i,
              "children is not ordered: %s",
              rel);
        phrasal.addChild(map.get(rel.getArgId(i)));
      }

      map.put(rel.getId(), phrasal);
      // root
      if (rel.getType().equals("S1")) {
        treebank.add(phrasal);
      }
    }

    return treebank;
  }
}
