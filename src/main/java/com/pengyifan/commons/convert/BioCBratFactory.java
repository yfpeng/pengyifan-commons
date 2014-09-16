package com.pengyifan.commons.convert;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import org.biocreative.bioc.BioCAnnotation;
import org.biocreative.bioc.BioCLocation;
import org.biocreative.bioc.BioCRelation;

import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;
import com.pengyifan.nlp.brat.BratRelation;

public class BioCBratFactory {

  public static BioCRelation createRelation(BratRelation bratRel) {
    BioCRelation.Builder biocRelBuilder = BioCRelation.newBuilder();
    biocRelBuilder.setID(bratRel.getId());
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", bratRel.getType());
    biocRelBuilder.setInfons(infons);
    // arg
    for (Pair<String, String> pair : bratRel.getArguments()) {
      biocRelBuilder.addNode(pair.getValue(), pair.getKey());
    }
    return biocRelBuilder.build();
  }

  public static BioCRelation createRelation(BratEvent event) {
    BioCRelation.Builder biocRelBuilder = BioCRelation.newBuilder();
    biocRelBuilder.setID(event.getId());
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", event.getType());
    biocRelBuilder.setInfons(infons);
    // trigger
    biocRelBuilder.addNode(event.getTriggerId(), event.getType());
    // arg
    for (Pair<String, String> pair : event.getArguments()) {
      biocRelBuilder.addNode(pair.getValue(), pair.getKey());
    }
    return biocRelBuilder.build();
  }

  public static BioCAnnotation createAnnotation(BratEntity entity) {
    BioCAnnotation.Builder annBuilder = BioCAnnotation.newBuilder();
    annBuilder.setID(entity.getId());
    annBuilder.setText(entity.getText());
    for (int i = 0; i < entity.numberOfSpans(); i++) {
      annBuilder.addLocation(BioCLocation.newBuilder()
          .setOffset(entity.start(i))
          .setLength(entity.end(i) - entity.start(i))
          .build());
    }
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", entity.getType());
    annBuilder.setInfons(infons);
    return annBuilder.build();
  }
}
