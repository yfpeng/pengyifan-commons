package com.pengyifan.nlp.brat;

public class BratEvent extends BratRelation {

  /**
   * 
   */
  private static final long serialVersionUID = 734750949536851798L;
  /**
   * ID
   */
  public String             triggerId;

  public BratEvent() {
    super();
  }

  public void setTriggerId(String id) {
    this.triggerId = id;
  }

  public String getTriggerId() {
    return triggerId;
  }
}