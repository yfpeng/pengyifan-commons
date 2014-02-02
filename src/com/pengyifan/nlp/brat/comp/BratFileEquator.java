package com.pengyifan.nlp.brat.comp;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.Validate;

import com.pengyifan.nlp.brat.BratEntity;
import com.pengyifan.nlp.brat.BratEvent;
import com.pengyifan.nlp.brat.BratRelation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEntitiesAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratEventsAnnotation;
import com.pengyifan.nlp.brat.BratAnnotations.BratRelationsAnnotation;

import edu.stanford.nlp.util.CoreMap;

public class BratFileEquator implements Equator<CoreMap> {

  Equator<BratEntity>   entityEquator;
  Equator<BratRelation> relationEquator;
  Equator<BratEvent>    eventEquator;

  public BratFileEquator(
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
  public boolean equate(CoreMap map1, CoreMap map2) {

    List<BratEvent> events1 = map1.get(BratEventsAnnotation.class);
    List<BratEntity> entities1 = map1.get(BratEntitiesAnnotation.class);
    List<BratRelation> relations1 = map1.get(BratRelationsAnnotation.class);

    List<BratEvent> events2 = map2.get(BratEventsAnnotation.class);
    List<BratEntity> entities2 = map2.get(BratEntitiesAnnotation.class);
    List<BratRelation> relations2 = map2.get(BratRelationsAnnotation.class);

    // compare entities
    compareList(entities1, entities2, entityEquator);

    // compare relations
    compareList(relations1, relations2, relationEquator);

    // compare events
    compareList(events1, events2, eventEquator);

    return true;
  }

  private static <E> boolean compareList(List<E> list1, List<E> list2,
      Equator<E> equator) {
    boolean[] founds = new boolean[list2.size()];
    Arrays.fill(founds, false);

    for (int i = 0; i < list1.size(); i++) {
      E e1 = list1.get(i);
      if (e1 instanceof BratEntity && ((BratEntity)e1).getType().equals("question")) {
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

      Validate.isTrue(foundEnt1, "cannot find in (1): %s", e1);
    }

    for (int i = 0; i < founds.length; i++) {
      Validate.isTrue(founds[i], "cannot find in (2): %s", list2.get(i));
    }
    return true;
  }

  @Override
  public int hash(CoreMap arg0) {
    // TODO Auto-generated method stub
    return 0;
  }

}
