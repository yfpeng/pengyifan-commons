package com.pengyifan.nlp.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class BratRelation extends BratAnn {

  /**
   * 
   */
  private static final long           serialVersionUID = 6844925115644892961L;
  /**
   * ROLE:ID
   */
  private List<Pair<String, BratAnn>> arguments;

  public BratRelation() {
    arguments = new ArrayList<Pair<String, BratAnn>>();
  }

  public void addArgument(String role, String id) {
    BratAnn arg = new BratAnn();
    arg.setId(id);
    addArgument(role, arg);
  }

  public void addArgument(String role, BratAnn ann) {
    arguments.add(Pair.of(role, ann));
  }

  public void setArgId(int i, String id) {
    BratAnn arg = getArg(i);
    arg.setId(id);
  }

  public void setArgId(int i, BratAnn ann) {
    arguments.set(i, Pair.of(arguments.get(i).getKey(), ann));
  }

  public String getArgRole(int i) {
    return arguments.get(i).getLeft();
  }

  public String getArgId(int i) {
    return getArg(i).getId();
  }

  public BratAnn getArg(int i) {
    return arguments.get(i).getRight();
  }

  public Pair<String, BratAnn> getArgPair(int i) {
    return arguments.get(i);
  }

  public List<Pair<String, BratAnn>> getArguments() {
    return arguments;
  }

  public int numberOfArguments() {
    return arguments.size();
  }
}