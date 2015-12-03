package com.pengyifan.commons.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Range;
import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCLocation;
import com.pengyifan.bioc.BioCNode;
import com.pengyifan.bioc.BioCRelation;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEvent;
import com.pengyifan.brat.BratRelation;
@Deprecated
public class BioCBratFactory {

  public static BioCRelation createRelation(BratRelation bratRel) {
    BioCRelation biocRel = new BioCRelation();
    biocRel.setID(bratRel.getId());
    Map<String, String> infons = new HashMap<String, String>();
    infons.put("type", bratRel.getType());
    biocRel.setInfons(infons);
    // arg
    for (Entry<String, String> pair : bratRel.getArguments().entrySet()) {
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
    for (Entry<String, String> pair : event.getArguments().entrySet()) {
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
