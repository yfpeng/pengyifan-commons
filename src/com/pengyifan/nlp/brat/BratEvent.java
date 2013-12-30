package com.pengyifan.nlp.brat;

public class BratEvent extends BratRelation {

  /**
   * 
   */
  private static final long serialVersionUID = 734750949536851798L;
  /**
   * ID
   */
  public BratAnn            trigger;

  public BratEvent() {
    super();
  }

  public void setTriggerId(String id) {
    BratAnn trigger = new BratAnn();
    trigger.setId(id);
    setTrigger(trigger);
  }

  public void setTrigger(BratAnn trigger) {
    this.trigger = trigger;
  }

  public String getTriggerId() {
    return getTrigger().getId();
  }
  
  public BratAnn getTrigger() {
    return trigger;
  }
}