package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.Validate;

import com.pengyifan.nlp.brat.BratDocument;
import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;
import com.pengyifan.nlp.brat.BratRelation;

public class BratDocumentEquator implements Equator<BratDocument> {

  Equator<BratEntity>   entityEquator;
  Equator<BratRelation> relationEquator;
  Equator<BratEvent>    eventEquator;

  public BratDocumentEquator(
      Equator<BratEntity> entityEquator,
      Equator<BratRelation> relationEquator,
      Equator<BratEvent> eventEquator) {
    this.entityEquator = entityEquator;
    this.relationEquator = relationEquator;
    this.eventEquator = eventEquator;
  }

  /**
   * Assume the entity is attached and there is not recursion
   */
  @Override
  public boolean equate(BratDocument doc1, BratDocument doc2) {
    // compare entities
    compareList(
        doc1.getEntities(),
        doc2.getEntities(),
        entityEquator,
        doc1,
        doc2);

    // compare relations
    compareList(
        doc1.getRelations(),
        doc2.getRelations(),
        relationEquator,
        doc1,
        doc2);

    // compare events
    compareList(doc1.getEvents(), doc2.getEvents(), eventEquator, doc1, doc2);

    return true;
  }

  private static <E> boolean compareList(List<E> list1, List<E> list2,
      Equator<E> equator, BratDocument doc1, BratDocument doc2) {
    boolean[] founds = new boolean[list2.size()];
    Arrays.fill(founds, false);

    for (int i = 0; i < list1.size(); i++) {
      E e1 = list1.get(i);
      if (e1 instanceof BratEntity
          && ((BratEntity) e1).getType().equals("question")) {
        continue;
      }

      boolean foundEnt1 = false;
      for (int j = 0; j < list2.size(); j++) {
        E e2 = list2.get(j);

        if (equator.equate(e1, e2)) {
          founds[j] = true;
          foundEnt1 = true;
        }
      }

      Validate.isTrue(
          foundEnt1,
          "cannot find in (1)%s: %s",
          doc1.getDocId(),
          e1);
    }

    for (int i = 0; i < founds.length; i++) {
      Validate.isTrue(
          founds[i],
          "cannot find in (2)%s: %s",
          doc2.getDocId(),
          list2.get(i));
    }
    return true;
  }

  @Override
  public int hash(BratDocument doc) {
    // TODO Auto-generated method stub
    return 0;
  }

}
