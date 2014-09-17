package com.pengyifan.nlp.brat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    set(BratEntitySpansAnnotation.class, new ArrayList<Range<Integer>>());
  }

  public BratEntity(BratEntity ent) {
    this();
    setId(ent.getId());
    setType(ent.getType());
    setText(ent.getText());
    for (Range<Integer> span : getSpans()) {
      addSpan(span);
    }
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
   * 
   * @param span start-offset, end-offset
   */
  public void addSpan(Range<Integer> span) {
    addSpan(span.getMinimum(), span.getMaximum());
  }

  public List<Range<Integer>> getSpans() {
    return UnmodifiableList
        .unmodifiableList(get(BratEntitySpansAnnotation.class));
  }

  /**
   * 
   * @param start the index of the first character of the annotated span in the
   *          text
   * @param end the index of the first character after the annotated span
   */
  public void addSpan(int start, int end) {
    get(BratEntitySpansAnnotation.class).add(Range.between(start, end));
    if (!has(CoreAnnotations.CharacterOffsetBeginAnnotation.class)) {
      set(CoreAnnotations.CharacterOffsetBeginAnnotation.class, start);
      set(CoreAnnotations.CharacterOffsetEndAnnotation.class, end);
    } else {
      // update start and end
      if (beginPosition() > start) {
        set(CoreAnnotations.CharacterOffsetBeginAnnotation.class, start);
      }
      if (endPosition() < end) {
        set(CoreAnnotations.CharacterOffsetEndAnnotation.class, end);
      }
    }
  }

  public int start(int i) {
    return span(i).getMinimum();
  }

  public int end(int i) {
    return span(i).getMaximum();
  }

  public Range<Integer> span(int i) {
    return getSpans().get(i);
  }

  public int numberOfSpans() {
    return getSpans().size();
  }

  public Range<Integer> totalSpan() {
    return Range.between(beginPosition(), endPosition());
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
      CoreAnnotation<List<Range<Integer>>> {

    @Override
    public Class<List<Range<Integer>>> getType() {
      return ErasureUtils
          .<Class<List<Range<Integer>>>> uncheckedCast(List.class);
    }

  }
}