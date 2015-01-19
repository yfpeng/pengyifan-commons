package com.pengyifan.nlp.brat.operation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEquivRelation;
import com.pengyifan.nlp.brat.BratEvent;
import com.pengyifan.nlp.brat.BratNote;
import com.pengyifan.nlp.brat.BratRelation;

/**
 * Merge brat documents into one. Reset IDs for all annotations.
 * 
 * Cannot handle recursive annotations
 */
public class BratMerge {

  private BratDocument newDoc;
  private final BratEntityEquator entityEquator = new BratEntityEquator();

  public BratMerge() {
    newDoc = new BratDocument();
  }

  private BratEntity find(BratEntity oldEntity) {
    for (BratEntity newEntity : newDoc.getEntities()) {
      if (entityEquator.equate(newEntity, oldEntity)) {
        return newEntity;
      }
    }
    return null;
  }

  private BratEvent find(BratEvent oldEvent, BratDocument oldDoc) {
    BratEventEquator eventEquator = new BratEventEquator(oldDoc, newDoc);
    for (BratEvent newEvent : newDoc.getEvents()) {
      if (eventEquator.equate(oldEvent, newEvent)) {
        return newEvent;
      }
    }
    return null;
  }

  private BratRelation find(BratRelation oldRel, BratDocument oldDoc) {
    BratRelationEquator relEquator = new BratRelationEquator(oldDoc, newDoc);
    for (BratRelation newRel : newDoc.getRelations()) {
      if (relEquator.equate(oldRel, newRel)) {
        return newRel;
      }
    }
    return null;
  }

  // map: old --> new
  private BratEquivRelation find(BratEquivRelation oldRel, BratDocument oldDoc) {
    BratRelationEquator relEquator = new BratRelationEquator(oldDoc, newDoc);
    for (BratEquivRelation newRel : newDoc.getEquivRelations()) {
      if (relEquator.equate(oldRel, newRel)) {
        return newRel;
      }
    }
    return null;
  }

  // map: old --> new
  // private BratAttribute find(BratAttribute oldAttr, BratDocument oldDoc) {
  // BratAttributeEquator attEquator = new BratAttributeEquator(entityEquator,
  // oldDoc, newDoc);
  // for (BratAttribute newAttr : newDoc.getAttributes()) {
  // if (attEquator.equate(oldAttr, newAttr)) {
  // return newAttr;
  // }
  // }
  // return null;
  // }

  // map: old --> new
  private BratNote find(BratNote oldNote, Map<String, String> idMap) {
    String newRefId = idMap.get(oldNote.getRefId());
    if (newRefId != null) {
      List<BratNote> newNotes = newDoc.getNotes(newRefId);
      for (BratNote newNote : newNotes) {
        if (newNote.getType().equals(oldNote.getType())
            && newNote.getText().equals(oldNote.getText())) {
          return newNote;
        }
      }
    }
    return null;
  }

  public void addDocument(BratDocument oldDoc) {
    // old: new
    Map<String, String> idMap = new HashMap<String, String>();

    // entity
    for (BratEntity oldEntity : oldDoc.getEntities()) {
      // if contained
      BratEntity newEntity = find(oldEntity);
      if (newEntity == null) {
        newEntity = new BratEntity(oldEntity);
        newEntity.setId("T" + newDoc.getEntities().size());
        newDoc.addAnnotation(newEntity);
      }
      idMap.put(oldEntity.getId(), newEntity.getId());
    }

    // event
    for (BratEvent oldEvent : oldDoc.getEvents()) {
      BratEvent newEvent = find(oldEvent, oldDoc);
      if (newEvent == null) {
        newEvent = new BratEvent();
        newEvent.setId("E" + newDoc.getEvents().size());
        newEvent.setType(oldEvent.getType());

        Validate.isTrue(
            idMap.containsKey(oldEvent.getTriggerId()),
            "dont contain: " + oldEvent.getTriggerId());
        newEvent.setTriggerId(idMap.get(oldEvent.getTriggerId()));

        for (Pair<String, String> arg : oldEvent.getArguments()) {
          // skip recursive events "E"
          if (arg.getValue().startsWith("E")) {
            continue;
          }
          Validate.isTrue(idMap.containsKey(arg.getValue()), "dont contain: "
              + arg.getValue());
          newEvent.addArgument(arg.getKey(), idMap.get(arg.getValue()));
        }
        newDoc.addAnnotation(newEvent);
      }
      idMap.put(oldEvent.getId(), newEvent.getId());
    }

    // relation
    for (BratRelation oldRel : oldDoc.getRelations()) {
      BratRelation newRel = find(oldRel, oldDoc);
      if (newRel == null) {
        newRel = new BratRelation();
        newRel.setId("R" + newDoc.getRelations().size());
        newRel.setType(oldRel.getType());

        for (Pair<String, String> arg : oldRel.getArguments()) {
          Validate.isTrue(idMap.containsKey(arg.getValue()), "dont contain: "
              + arg.getValue());
          newRel.addArgument(arg.getKey(), idMap.get(arg.getValue()));
        }
        newDoc.addAnnotation(newRel);
      }
      idMap.put(oldRel.getId(), newRel.getId());
    }

    // attribute
    // for (BratAttribute oldAtt : oldDoc.getAttributes()) {
    // BratAttribute newAtt = find(oldAtt, idMap);
    // if (newAtt == null) {
    // newAtt = new BratAttribute();
    // newAtt.setId("A" + newDoc.getAttributes().size());
    // newAtt.setType(oldAtt.getType());
    // newAtt.setRefId(idMap.get(oldAtt.getRefId()));
    // for (String att : oldAtt.getAttributes()) {
    // newAtt.addAttribute(att);
    // }
    // newDoc.addAnnotation(newAtt);
    // }
    // idMap.put(oldAtt.getId(), newAtt.getId());
    // }

    // equiv
    for (BratEquivRelation oldRel : oldDoc.getEquivRelations()) {
      BratEquivRelation newRel = find(oldRel, oldDoc);
      if (newRel == null) {
        newRel = new BratEquivRelation();
        newRel.setId("*");
        newRel.setType(oldRel.getType());

        for (Pair<String, String> arg : oldRel.getArguments()) {
          Validate.isTrue(idMap.containsKey(arg.getValue()), "dont contain: "
              + arg.getValue());
          newRel.addArgument(arg.getKey(), idMap.get(arg.getValue()));
        }
        newDoc.addAnnotation(newRel);
      }
      idMap.put(oldRel.getId(), newRel.getId());
    }

    // note
    for (BratNote oldNote : oldDoc.getNotes()) {
      BratNote newNote = find(oldNote, idMap);
      if (newNote == null) {
        newNote = new BratNote();
        newNote.setId("#" + newDoc.getNotes().size());
        newNote.setType(oldNote.getType());

        Validate.isTrue(idMap.containsKey(oldNote.getRefId()), "dont contain: "
            + oldNote.getRefId());
        newNote.setRefId(idMap.get(oldNote.getRefId()));

        newNote.setText(oldNote.getText());
        newDoc.addAnnotation(newNote);
      }
      idMap.put(oldNote.getId(), newNote.getId());
    }
  }

  public BratDocument getDoc() {
    return newDoc;
  }

  class BratEntityEquator implements Equator<BratEntity> {

    @Override
    public boolean equate(BratEntity e1, BratEntity e2) {
      return e1.totalSpan().equals(e2.totalSpan());
    }

    @Override
    public int hash(BratEntity o) {
      return o.hashCode();
    }

  }

  class BratEventEquator implements Equator<BratEvent> {

    private final BratDocument doc1;
    private final BratDocument doc2;

    public BratEventEquator(
        BratDocument doc1,
        BratDocument doc2) {
      this.doc1 = doc1;
      this.doc2 = doc2;
    }

    @Override
    public boolean equate(BratEvent e1, BratEvent e2) {
      // type
      if (!e1.getType().equals(e2.getType())) {
        return false;
      }
      // trigger
      if (!entityEquator.equate(
          doc1.getEntity(e1.getTriggerId()),
          doc2.getEntity(e2.getTriggerId()))) {
        return false;
      }

      // arg
      boolean[] founds = new boolean[e2.numberOfArguments()];
      Arrays.fill(founds, false);

      for (int i = 0; i < e1.numberOfArguments(); i++) {
        Pair<String, String> p1 = e1.getArgPair(i);

        boolean foundP1 = false;
        for (int j = 0; j < e2.numberOfArguments(); j++) {
          Pair<String, String> p2 = e2.getArgPair(j);

          if (p1.getKey().equals(p2.getKey())
              && entityEquator.equate(
                  doc1.getEntity(p1.getValue()),
                  doc2.getEntity(p2.getValue()))) {
            founds[j] = true;
            foundP1 = true;
          }
        }

        if (!foundP1) {
          return false;
        }
      }

      for (int i = 0; i < founds.length; i++) {
        if (!founds[i]) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int hash(BratEvent arg0) {
      throw new UnsupportedOperationException("hash() is not supported yet.");
    }
  }

  class BratRelationEquator implements Equator<BratRelation> {

    BratDocument doc1;
    BratDocument doc2;

    public BratRelationEquator(
        BratDocument doc1,
        BratDocument doc2) {
      this.doc1 = doc1;
      this.doc2 = doc2;
    }

    @Override
    public boolean equate(BratRelation r1, BratRelation r2) {
      // type
      if (!r1.getType().equals(r2.getType())) {
        return false;
      }
      // arg
      boolean[] founds = new boolean[r2.numberOfArguments()];
      Arrays.fill(founds, false);

      for (int i = 0; i < r1.numberOfArguments(); i++) {
        Pair<String, String> p1 = r1.getArgPair(i);

        boolean foundP1 = false;
        for (int j = 0; j < r2.numberOfArguments(); j++) {
          Pair<String, String> p2 = r2.getArgPair(j);

          if (p1.getKey().equals(p2.getKey())
              && entityEquator.equate(
                  (BratEntity) doc1.getAnnotation(p1.getValue()),
                  (BratEntity) doc2.getAnnotation(p2.getValue()))) {
            founds[j] = true;
            foundP1 = true;
          }
        }

        if (!foundP1) {
          return false;
        }
      }

      for (int i = 0; i < founds.length; i++) {
        if (!founds[i]) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int hash(BratRelation arg0) {
      throw new UnsupportedOperationException("hash() is not supported yet.");
    }
  }
}
