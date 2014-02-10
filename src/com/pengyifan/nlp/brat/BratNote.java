package com.pengyifan.nlp.brat;

import edu.stanford.nlp.ling.CoreAnnotations;

/**
 * Note annotations provide a way to associate freeform text with either the
 * document or a specific annotation.
 */
public class BratNote extends BratAnnotation {

  /**
   * 
   */
  private static final long serialVersionUID = -6909882383387258329L;

  private String            refId;

  public BratNote() {
    super();
  }

  /**
   * 
   * @param id the ID of the annotation that the note is attached to
   */
  public void setRefId(String id) {
    this.refId = id;
  }

  /**
   * 
   * @return the ID of the annotation that the note is attached to
   */
  public String getRefId() {
    return refId;
  }

  /**
   * @return the text of the note
   */
  public String getText() {
    return get(CoreAnnotations.TextAnnotation.class);
  }

  /**
   * @param text the text of the note
   */
  public void setText(String text) {
    set(CoreAnnotations.TextAnnotation.class, text);
  }
}
