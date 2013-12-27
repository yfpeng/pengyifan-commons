package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Relation {

  public String               id;
  public String               type;
  /**
   * ROLE:ID
   */
  public List<Pair<String, String>> arguments;

  public Relation() {
    arguments = new ArrayList<Pair<String, String>>();
  }
  
  public void addArgument(String role, String id) {
    arguments.add(Pair.of(role, id));
  }
  
  public String getArgRole(int i) {
    return arguments.get(i).getLeft();
  }

  public String getArgId(int i) {
    return arguments.get(i).getRight();
  }
}