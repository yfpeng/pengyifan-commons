package com.pengyifan.nlp.brat;

import com.pengyifan.nlp.ling.HasId;
import com.pengyifan.nlp.ling.HasType;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;

public class BratAnnotation extends ArrayCoreMap implements HasId, HasType {

  /**
   * 
   */
  private static final long serialVersionUID = -856638373334134048L;

  @Override
  public String getId() {
    return get(CoreAnnotations.IDAnnotation.class);
  }

  @Override
  public void setId(String id) {
    set(CoreAnnotations.IDAnnotation.class, id);
  }

  @Override
  public String getType() {
    return get(CoreAnnotations.EntityTypeAnnotation.class);
  }

  @Override
  public void setType(String type) {
    set(CoreAnnotations.EntityTypeAnnotation.class, type);
  }
}
