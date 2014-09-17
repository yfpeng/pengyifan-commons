package com.pengyifan.nlp.brat;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BratEvent extends BratRelation {

  /**
   * 
   */
  private static final long serialVersionUID = 734750949536851798L;
  private String triggerId;

  public BratEvent() {
    super();
  }

  public void setTriggerId(String id) {
    this.triggerId = id;
  }

  /**
   * Returns he event triggers, annotations marking the word or words stating
   * each event
   */
  public String getTriggerId() {
    return triggerId;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getType())
        .append(getTriggerId())
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
    BratEvent rhs = (BratEvent) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getType(), rhs.getType())
        .append(getTriggerId(), rhs.getTriggerId())
        .append(getArguments(), rhs.getArguments())
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("triggerId", getTriggerId())
        .append("arguments", getArguments())
        .toString();
  }
}