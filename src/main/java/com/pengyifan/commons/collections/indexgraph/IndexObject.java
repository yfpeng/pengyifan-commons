package com.pengyifan.commons.collections.indexgraph;

import edu.stanford.nlp.ling.CoreLabel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

public class IndexObject {

  protected CoreLabel label;

  public IndexObject(int index) {
    label = new CoreLabel();
    label.setIndex(index);
  }

  public int getIndex() {
    checkArgument(label.index() != -1, "index is not set");
    return label.index();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("index", getIndex())
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getIndex());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof IndexObject)) {
      return false;
    }
    IndexObject rhs = (IndexObject) obj;
    return Objects.equals(getIndex(), rhs.getIndex());
  }

}
