package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Event {

  public String                     id;
  /**
   * TYPE:ID
   */
  public Pair<String, String>       trigger;
  /**
   * ROLE:ID
   */
  public List<Pair<String, String>> arguments;

  public Event() {
    arguments = new ArrayList<Pair<String, String>>();
  }

  public void setTrigger(String type, String id) {
    trigger = Pair.of(type, id);
  }

  public void addArgument(String role, String id) {
    arguments.add(Pair.of(role, id));
  }

  public String getTriggerType() {
    return trigger.getLeft();
  }

  public String getTriggerId() {
    return trigger.getRight();
  }

  public String getArgRole(int i) {
    return arguments.get(i).getLeft();
  }

  public String getArgId(int i) {
    return arguments.get(i).getRight();
  }
}