package com.pengyifan.nlp.brat;

import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.util.ErasureUtils;

public class BratAnnotations {

  public static class BratEntitiesAnnotation implements
      CoreAnnotation<List<BratEntity>> {

    @Override
    public Class<List<BratEntity>> getType() {
      return ErasureUtils.<Class<List<BratEntity>>> uncheckedCast(List.class);
    }

  }

  public static class BratRelationsAnnotation implements
      CoreAnnotation<List<BratRelation>> {

    @Override
    public Class<List<BratRelation>> getType() {
      return ErasureUtils.<Class<List<BratRelation>>> uncheckedCast(List.class);
    }

  }

  public static class BratEventsAnnotation implements CoreAnnotation<List<BratEvent>> {

    @Override
    public Class<List<BratEvent>> getType() {
      return ErasureUtils.<Class<List<BratEvent>>> uncheckedCast(List.class);
    }

  }
}
