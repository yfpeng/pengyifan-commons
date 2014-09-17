package com.pengyifan.nlp.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;

public class BratRelation extends BratAnnotation {

  /**
   * 
   */
  private static final long serialVersionUID = 6844925115644892961L;
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
  public void addArgument(String role, String id) {
    getArguments().add(Pair.of(role, id));
  }

  public void setArgId(int i, String id) {
    getArguments().set(i, Pair.of(getArgRole(i), id));
  }

  public void setArgRole(int i, String role) {
    getArguments().set(i, Pair.of(role, getArgId(i)));
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getType())
        .append(getArguments())
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BratRelation rhs = (BratRelation) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getType(), rhs.getType())
        .append(getArguments(), rhs.getArguments())
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("arguments", getArguments())
        .toString();
  }
}