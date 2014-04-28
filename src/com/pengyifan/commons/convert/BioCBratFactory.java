package com.pengyifan.commons.convert;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

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
    for (Pair<String, String> pair : bratRel.getArguments()) {
      biocRel.addNode(pair.getValue(), pair.getKey());
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
    for (Pair<String, String> pair : event.getArguments()) {
      biocRel.addNode(pair.getValue(), pair.getKey());
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
