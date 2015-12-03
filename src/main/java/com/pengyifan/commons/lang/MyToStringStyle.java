package com.pengyifan.commons.lang;

import org.apache.commons.lang3.builder.ToStringStyle;

public class MyToStringStyle extends ToStringStyle {

  /**
   * <p>
   * Constructor.
   * </p>
   *
   * <p>
   * Use the static constant rather than instantiating.
   * </p>
   */
  public MyToStringStyle() {
    super();
    this.setUseShortClassName(true);
    this.setUseIdentityHashCode(false);
  }

  /**
   * <p>
   * Ensure <code>Singleton</ode> after serialization.
   * </p>
   *
   * @return the singleton
   */
  private Object readResolve() {
    return ToStringStyle.SHORT_PREFIX_STYLE;
  }

  @Override
  protected void appendFieldStart(StringBuffer buffer, String fieldName) {
    if (fieldName != null) {
      buffer.append(fieldName);
      buffer.append(getFieldNameValueSeparator());
      buffer.append('"');
    }
  }
  @Override
  protected void appendFieldEnd(StringBuffer buffer, String fieldName) {
    buffer.append('"');
    appendFieldSeparator(buffer);
  }
}
