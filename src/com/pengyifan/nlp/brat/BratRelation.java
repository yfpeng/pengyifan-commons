package com.pengyifan.nlp.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class BratRelation extends BratAnnotation {

  /**
   * 
   */
  private static final long          serialVersionUID = 6844925115644892961L;
  /**
   * ROLE:ID
   */
  private List<Pair<String, String>> argumentIds;

  public BratRelation() {
    argumentIds = new ArrayList<Pair<String, String>>();
  }

  /**
   * 
   * @param role task-specific argument role
   * @param id the entity or event filling that role
   */
  public void addArgId(String role, String id) {
    getArguments().add(Pair.of(role, id));
  }

  public void setArgId(int i, String id) {
    getArguments().set(i, Pair.of(getArgRole(i), id));
  }

  public String getArgRole(int i) {
    return getArgPair(i).getKey();
  }

  /**
   * 
   * @param i
   * @return task-specific argument role
   */
  public String getArgId(int i) {
    return getArgPair(i).getValue();
  }

  public Pair<String, String> getArgPair(int i) {
    return getArguments().get(i);
  }

  public List<Pair<String, String>> getArguments() {
    return argumentIds;
  }

  public int numberOfArguments() {
    return getArguments().size();
  }
}