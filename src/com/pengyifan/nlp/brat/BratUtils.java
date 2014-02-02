package com.pengyifan.nlp.brat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Equator;
import org.apache.commons.io.FileUtils;

import com.pengyifan.nlp.brat.BratAnnotations.BratEntitiesAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEventsAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratRelationsAnnotation;
import com.pengyifan.nlp.brat.comp.BratEntityEquator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class BratUtils {

  public static void removeSameEntity(CoreMap map) {
    removeSameEntity(map, new BratEntityEquator());
  }

  public static void removeSameEntity(CoreMap map, Equator<BratEntity> equator) {
    List<BratEvent> events = map.get(BratEventsAnnotation.class);
    List<BratEntity> entities = map.get(BratEntitiesAnnotation.class);
    List<BratRelation> relations = map.get(BratRelationsAnnotation.class);

    Map<String, String> ids = new HashMap<String, String>();
    for (int i = 0; i < entities.size(); i++) {
      BratEntity e1 = entities.get(i);
      for (int j = i + 1; j < entities.size(); j++) {
        BratEntity e2 = entities.get(j);
        // equal position
        if (equator.equate(e1, e2)) {
          ids.put(e2.getId(), e1.getId());
          entities.remove(j);
          j--;
        }
      }
    }

    for (BratRelation r : relations) {
      // arg
      for (int i = 0; i < r.numberOfArguments(); i++) {
        String id = r.getArgId(i);
        if (ids.containsKey(id)) {
          r.setArgId(i, ids.get(id));
        }
      }
    }
    for (BratEvent e : events) {
      // trigger
      if (ids.containsKey(e.getTriggerId())) {
        e.setTriggerId(ids.get(e.getTriggerId()));
      }
      // arg
      for (int i = 0; i < e.numberOfArguments(); i++) {
        String id = e.getArgId(i);
        if (ids.containsKey(id)) {
          e.setArgId(i, ids.get(id));
        }
      }
    }
  }

  public static void write(File file, CoreMap map)
      throws IOException {

    List<BratEvent> events = map.get(BratEventsAnnotation.class);
    List<BratEntity> entities = map.get(BratEntitiesAnnotation.class);
    List<BratRelation> relations = map.get(BratRelationsAnnotation.class);

    StringBuilder sb = new StringBuilder();
    for (BratEntity entity : entities) {
      sb.append(write(entity)).append('\n');
    }
    for (BratRelation relation : relations) {
      sb.append(write(relation)).append('\n');
    }
    for (BratEvent event : events) {
      sb.append(write(event)).append('\n');
    }
    FileUtils.write(file, sb);
  }

  public static CoreMap readToMap(File file)
      throws IOException {
    List<BratEvent> events = new ArrayList<BratEvent>();
    List<BratRelation> relations = new ArrayList<BratRelation>();
    List<BratEntity> entities = new ArrayList<BratEntity>();

    String line;
    LineNumberReader reader = new LineNumberReader(new FileReader(file));
    while ((line = reader.readLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith("T")) {
        entities.add(parseEntity(line));
      } else if (line.startsWith("E")) {
        events.add(parseEvent(line));
      } else if (line.startsWith("R")) {
        relations.add(parseRelation(line));
      } else if (line.startsWith("#")) {
        ;
      } else {
        reader.close();
        throw new IOException("cannot parse line: " + line);
      }
    }
    reader.close();

    CoreMap map = new ArrayCoreMap();
    map.set(CoreAnnotations.DocIDAnnotation.class, file.getName());
    map.set(BratEntitiesAnnotation.class, entities);
    map.set(BratRelationsAnnotation.class, relations);
    map.set(BratEventsAnnotation.class, events);
    return map;
  }

  public static BratEvent parseEvent(String line) {
    BratEvent event = new BratEvent();

    String toks[] = line.split("\\t+");
    event.setId(toks[0]);

    toks = toks[1].split(" ");
    int index = toks[0].indexOf(':');
    event.setType(toks[0].substring(0, index));
    event.setTriggerId(toks[0].substring(index + 1));

    for (int i = 1; i < toks.length; i++) {
      index = toks[i].indexOf(':');
      event.addArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
    }

    return event;
  }

  public static BratRelation parseRelation(String line) {
    BratRelation relation = new BratRelation();

    String toks[] = line.split("\\t+");
    relation.setId(toks[0]);

    toks = toks[1].split(" ");
    relation.setType(toks[0]);

    for (int i = 1; i < toks.length; i++) {
      int index = toks[i].indexOf(':');
      relation.addArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
    }

    return relation;
  }

  public static BratEntity parseEntity(String line) {
    BratEntity entity = new BratEntity();
    String tabs[] = line.split("\t");
    entity.setId(tabs[0]);
    entity.setText(tabs[2]);
    int index = tabs[1].indexOf(' ');
    entity.setType(tabs[1].substring(0, index));
    for (String loc : tabs[1].substring(index + 1).split(";")) {
      int space = loc.indexOf(' ');
      entity.addSpan(
          Integer.parseInt(loc.substring(0, space)),
          Integer.parseInt(loc.substring(space + 1)));
    }
    return entity;
  }

  public static BratRelation parseEquivalence(String line) {
    BratRelation relation = new BratRelation();
    String tabs[] = line.split("\t");
    relation.setId(tabs[0]);
    int space = tabs[1].indexOf(' ');
    relation.setType(tabs[1].substring(0, space));
    for (String e : tabs[1].substring(space + 1).split(" ")) {
      relation.addArgument("entity", e);
    }
    return relation;
  }

  public static BratEvent parseEventModification(String line) {
    BratEvent event = new BratEvent();
    String tabs[] = line.split("\t");
    event.setId(tabs[0]);
    int space = tabs[1].indexOf(' ');
    event.setType(tabs[1].substring(0, space));
    event.setTriggerId(tabs[1].substring(space + 1));
    return event;
  }

  public static String write(BratEntity entity) {
    StringBuilder sb = new StringBuilder();
    sb.append(entity.getId()).append('\t').append(entity.getType()).append(' ');
    for (int i = 0; i < entity.numberOfSpans(); i++) {
      if (i != 0) {
        sb.append(';');
      }
      sb.append(entity.start(i)).append(' ').append(entity.end(i));
    }
    sb.append('\t').append(entity.getText());
    return sb.toString();
  }

  public static String write(BratEvent event) {
    StringBuilder sb = new StringBuilder();
    sb.append(event.getId()).append('\t').append(event.getType()).append(':')
        .append(event.getTriggerId());
    for (int i = 0; i < event.numberOfArguments(); i++) {
      sb.append(' ').append(event.getArgRole(i)).append(':')
          .append(event.getArgId(i));
    }
    return sb.toString();
  }

  public static String write(BratRelation relation) {
    StringBuilder sb = new StringBuilder();
    sb.append(relation.getId()).append('\t').append(relation.getType());
    for (int i = 0; i < relation.numberOfArguments(); i++) {
      sb.append(' ').append(relation.getArgRole(i)).append(':')
          .append(relation.getArgId(i));
    }
    return sb.toString();
  }
}
