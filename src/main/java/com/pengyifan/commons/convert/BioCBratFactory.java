package com.pengyifan.commons.convert;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCLocation;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCRelation;
import com.google.common.collect.Range;
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
      biocRel.addNode(new BioCNode(pair.getValue(), pair.getKey()));
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
    biocRel.addNode(new BioCNode(event.getTriggerId(), event.getType()));
    // arg
    for (Pair<String, String> pair : event.getArguments()) {
      biocRel.addNode(new BioCNode(pair.getValue(), pair.getKey()));
    }
    return biocRel;
  }

  public static BioCAnnotation createAnnotation(BratEntity entity) {
    BioCAnnotation ann = new BioCAnnotation();
    ann.setID(entity.getId());
    ann.setText(entity.getText());
    for (Range<Integer> range : entity.getSpans().asRanges()) {
      ann.addLocation(new BioCLocation(range.lowerEndpoint(), range
          .upperEndpoint() - range.lowerEndpoint()));
    }
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", entity.getType());
    ann.setInfons(infons);
    return ann;
  }
}
