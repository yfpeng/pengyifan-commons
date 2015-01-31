package com.pengyifan.commons.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengyifan.brat.BratAnnotation;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratRelation;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;

public class Ptb2Brat {

  public static void bratString(MemoryTreebank treebank, BratDocument bratDoc) {
    for (Tree tree : treebank) {
      bratString(tree, bratDoc);
    }
  }

  public static void bratString(Tree tree, BratDocument bratDoc) {

    int entIndex = bratDoc.getEntities().size();
    int relIndex = bratDoc.getRelations().size();
    Map<Integer, BratAnnotation> map = new HashMap<Integer, BratAnnotation>();

    List<Tree> postOrder = tree.postOrderNodeList();
    for (Tree t : postOrder) {
      CoreLabel label = (CoreLabel) t.label();
      if (t.isLeaf()) {
        ;
      } else if (t.isPreTerminal()) {
        BratEntity ent = new BratEntity();
        ent.setType(label.tag());
        ent.setId("T" + entIndex);
        entIndex++;

        CoreLabel leafLabel = (CoreLabel) t.firstChild().label();
        ent.addSpan(leafLabel.beginPosition(), leafLabel.endPosition());
        ent.setText(leafLabel.word());

        bratDoc.addAnnotation(ent);
        map.put(System.identityHashCode(t), ent);
      } else {
        BratRelation rel = new BratRelation();
        rel.setType(label.category());
        rel.setId("R" + relIndex);
        relIndex++;

        // kids
        Tree[] children = t.children();
        for (int i = 0; i < children.length; i++) {
          BratAnnotation ann = map.get(System.identityHashCode(children[i]));
          rel.addArgument("C" + i, ann.getId());
        }

        bratDoc.addAnnotation(rel);
        map.put(System.identityHashCode(t), rel);
      }
    }
  }
}
