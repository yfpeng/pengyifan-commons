package com.pengyifan.nlp.bioc;

import java.util.HashMap;
import java.util.Map;

import bioc.BioCAnnotation;
import bioc.BioCLocation;
import bioc.BioCRelation;

import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;
import com.pengyifan.nlp.brat.BratRelation;

public class BioCBratFactory {

  public static BioCRelation createRelation(BratRelation bratRel) {
    BioCRelation biocRel = new BioCRelation();
    biocRel.setID(bratRel.getId());
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", bratRel.getType());
    biocRel.setInfons(infons);
    // arg
    for (int i = 0; i < bratRel.arguments.size(); i++) {
      biocRel.addNode(bratRel.getArgId(i), bratRel.getArgRole(i));
    }
    return biocRel;
  }

  public static BioCRelation createRelation(BratEvent event) {
    BioCRelation biocRel = new BioCRelation();
    biocRel.setID(event.getId());
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", event.getType());
    biocRel.setInfons(infons);
    // trigger
    biocRel.addNode(event.getTriggerId(), event.getType());
    // arg
    for (int i = 0; i < event.arguments.size(); i++) {
      biocRel.addNode(event.getArgId(i), event.getArgRole(i));
    }
    return biocRel;
  }

  public static BioCAnnotation createAnnotation(BratEntity entity) {
    BioCAnnotation ann = new BioCAnnotation();
    ann.setID(entity.getId());
    ann.setText(entity.getText());
    for (int i = 0; i < entity.numberOfSpans(); i++) {
      BioCLocation loc = new BioCLocation(entity.start(i), entity.end(i)
          - entity.start(i));
      ann.addLocation(loc);
    }
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", entity.getType());
    ann.setInfons(infons);
    return ann;
  }
}
