package com.pengyifan.nlp.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.HasOffset;
import edu.stanford.nlp.util.ErasureUtils;

/**
 * CoreAnnotations.IDAnnotation: id <br />
 * CoreAnnotations.EntityTypeAnnotation: type <br />
 * 
 */
public class BratEntity extends BratAnnotation implements HasOffset {

  /**
   * 
   */
  private static final long serialVersionUID = -7731569470434643148L;

  public BratEntity() {
    super();
    set(BratEntitySpansAnnotation.class, TreeRangeSet.create());
  }

  public BratEntity(BratEntity ent) {
    this();
    setId(ent.getId());
    setType(ent.getType());
    setText(ent.getText());
    getSpans().addAll(ent.getSpans());
  }

  /**
   * 
   * @return the text spanned by the annotation
   */
  public String getText() {
    return get(CoreAnnotations.TextAnnotation.class);
  }

  public void setText(String text) {
    set(CoreAnnotations.TextAnnotation.class, text);
  }

  /**
   * [start-offset, end-offset]
   */
  public void addSpan(Range<Integer> span) {
    checkArgument(
        span.lowerBoundType() == BoundType.CLOSED,
        "start-offset has to be closed");
    checkArgument(
        span.upperBoundType() == BoundType.CLOSED,
        "end-offset has to be closed");
    addSpan(span.lowerEndpoint(), span.upperEndpoint());
  }

  public RangeSet<Integer> getSpans() {
    return get(BratEntitySpansAnnotation.class);
  }

  /**
   * 
   * @param start the index of the first character of the annotated span in the
   *          text
   * @param end the index of the first character after the annotated span
   */
  public void addSpan(int start, int end) {
    get(BratEntitySpansAnnotation.class).add(Range.closed(start, end));
    Range<Integer> span = getSpans().span();
    set(
        CoreAnnotations.CharacterOffsetBeginAnnotation.class,
        span.lowerEndpoint());
    set(
        CoreAnnotations.CharacterOffsetEndAnnotation.class,
        span.upperEndpoint());
  }

  public Range<Integer> totalSpan() {
    return getSpans().span();
  }

  @Override
  public int beginPosition() {
    return get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
  }

  @Override
  public int endPosition() {
    return get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getText())
        .append(get(BratEntitySpansAnnotation.class))
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
    BratEntity rhs = (BratEntity) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getText(), rhs.getText())
        .append(getType(), rhs.getType())
        .append(getSpans(), rhs.getSpans())
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("text", getText())
        .append("spans", getSpans())
        .toString();
  }

  @Override
  public void setBeginPosition(int start) {
    // set(CoreAnnotations.CharacterOffsetBeginAnnotation.class, start);
    throw new UnsupportedOperationException(
        "setBeginPosition is not supported. Please use addSpan()");
  }

  @Override
  public void setEndPosition(int end) {
    // set(CoreAnnotations.CharacterOffsetEndAnnotation.class, end);
    throw new UnsupportedOperationException(
        "setEndPosition is not supported. Please use addSpan()");
  }

  private static class BratEntitySpansAnnotation implements
      CoreAnnotation<RangeSet<Integer>> {

    @Override
    public Class<RangeSet<Integer>> getType() {
      return ErasureUtils
          .<Class<RangeSet<Integer>>> uncheckedCast(List.class);
    }

  }
}