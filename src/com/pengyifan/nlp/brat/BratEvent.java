package com.pengyifan.nlp.brat;

public class BratEvent extends BratRelation {

  /**
   * 
   */
  private static final long serialVersionUID = 734750949536851798L;
  /**
   * ID
   */
  private String            triggerId;

  public BratEvent() {
    super();
  }

  public void setTriggerId(String id) {
    this.triggerId = id;
  }

  /**
   * 
   * @return The event triggers, annotations marking the word or words stating
   *         each event
   */
  public String getTriggerId() {
    return triggerId;
  }
}