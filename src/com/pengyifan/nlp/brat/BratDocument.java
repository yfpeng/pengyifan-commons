package com.pengyifan.nlp.brat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.pengyifan.nlp.brat.BratAnnotations.BratAttributesAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEntitiesAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEquivRelationsAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEventsAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratNotesAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratRelationsAnnotation;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class BratDocument {

  CoreMap                             map;
  private Map<String, BratAnnotation> annotationMap;

  public BratDocument() {
    map = new ArrayCoreMap();
    map.set(BratEntitiesAnnotation.class, new ArrayList<BratEntity>());
    map.set(BratRelationsAnnotation.class, new ArrayList<BratRelation>());
    map.set(
        BratEquivRelationsAnnotation.class,
        new ArrayList<BratEquivRelation>());
    map.set(BratEventsAnnotation.class, new ArrayList<BratEvent>());
    map.set(BratAttributesAnnotation.class, new ArrayList<BratAttribute>());
    map.set(BratNotesAnnotation.class, new ArrayList<BratNote>());
    annotationMap = new HashMap<String, BratAnnotation>();
  }

  public void setText(String text) {
    map.set(CoreAnnotations.TextAnnotation.class, text);
  }

  public String getText() {
    return map.get(CoreAnnotations.TextAnnotation.class);
  }

  public BratAnnotation getAnnotation(String id) {
    Validate.isTrue(annotationMap.containsKey(id), "dont contain %s", id);
    return annotationMap.get(id);
  }

  public void addAnnotation(BratAnnotation ann) {
    if (ann instanceof BratEntity) {
      getEntities().add((BratEntity) ann);
    } else if (ann instanceof BratEvent) {
      getEvents().add((BratEvent) ann);
    } else if (ann instanceof BratEquivRelation) {
      getEquivRelations().add((BratEquivRelation) ann);
    } else if (ann instanceof BratRelation) {
      getRelations().add((BratRelation) ann);
    } else if (ann instanceof BratAttribute) {
      getAttributes().add((BratAttribute) ann);
    } else if (ann instanceof BratNote) {
      getNotes().add((BratNote) ann);
    } else {
      Validate.isTrue(false, "annotation not instanceof %s", ann);
    }
    Validate.isTrue(
        !annotationMap.containsKey(ann.getId()),
        "already have %s",
        ann.getId());
    annotationMap.put(ann.getId(), ann);
  }

  public Collection<BratAnnotation> getAnnotations() {
    return annotationMap.values();
  }

  public List<BratEvent> getEvents() {
    return map.get(BratEventsAnnotation.class);
  }

  public List<BratEntity> getEntities() {
    return map.get(BratEntitiesAnnotation.class);
  }

  public List<BratRelation> getRelations() {
    return map.get(BratRelationsAnnotation.class);
  }

  public List<BratAttribute> getAttributes() {
    return map.get(BratAttributesAnnotation.class);
  }

  public List<BratEquivRelation> getEquivRelations() {
    return map.get(BratEquivRelationsAnnotation.class);
  }

  public List<BratNote> getNotes() {
    return map.get(BratNotesAnnotation.class);
  }

  public List<BratNote> getNotes(String refId) {
    List<BratNote> notes = new ArrayList<BratNote>();
    for (BratNote note : getNotes()) {
      if (note.getRefId().equals(refId)) {
        notes.add(note);
      }
    }
    return notes;
  }

  public void setDocId(String id) {
    map.set(CoreAnnotations.DocIDAnnotation.class, id);
  }

  /**
   * document name
   * 
   * @return
   */
  public String getDocId() {
    return map.get(CoreAnnotations.DocIDAnnotation.class);
  }
}
