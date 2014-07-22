package com.pengyifan.commons.lang;

import org.apache.commons.lang3.Validate;

public class IndentStringBuilder {

  private int indent;
  private int width;
  private boolean splitAtWhiteSpaces;
  private boolean isHangIndent;

  public static IndentStringBuilder newBuilder() {
    return new IndentStringBuilder();
  }

  private IndentStringBuilder() {
    indent = 2;
    width = 80;
    splitAtWhiteSpaces = true;
    isHangIndent = false;
  }

  /**
   * indent indentation. indent >= 0
   */
  public IndentStringBuilder setIndent(int indent) {
    this.indent = indent;
    return this;
  }

  /**
   * the width of formatted paragraph
   */
  public IndentStringBuilder setWidth(int width) {
    this.width = width;
    return this;
  }

  /**
   * true if only split at white spaces.
   */
  public IndentStringBuilder setSplitAtWhiteSpaces(boolean splitAtWhiteSpaces) {
    this.splitAtWhiteSpaces = splitAtWhiteSpaces;
    return this;
  }

  /**
   * If true, format a paragraph to that has all lines but the first indented.
   * 
   * If false, format a paragraph to that has the first indented.
   */
  public IndentStringBuilder setHangIndent(boolean isHangIndent) {
    this.isHangIndent = isHangIndent;
    return this;
  }

  public String build(String text) {
    checkArguments();
    if (isHangIndent) {
      return hangIndent(text);
    } else {
      return indent(text);
    }
  }

  private String hangIndent(String text) {
    StringBuilder sb = new StringBuilder(text.substring(0, indent));
    // Needed to handle last line correctly.
    // Will be trimmed at last
    text = text.substring(indent) + "\n";
    // hang indent
    String spaces = org.apache.commons.lang3.StringUtils
        .repeat(' ', indent);
    String replacement = spaces + "$1\n";
    String regex = "(.{1," + (width - indent) + "})";
    if (splitAtWhiteSpaces) {
      regex += "\\s+";
    }
    text = text.replaceAll(regex, replacement);
    // remove first spaces and last "\n"
    text = text.substring(indent, text.length() - 1);
    if (!splitAtWhiteSpaces) {
      text = text.substring(0, text.length() - 1);
    }
    return sb.append(text).toString();
  }

  private String indent(String text) {
    String spaces = org.apache.commons.lang3.StringUtils.repeat(' ', indent);

    // Needed to handle last line correctly.
    // Will be trimmed at last
    text = spaces + text + "\n";
    // split
    String replacement = "$1\n";
    String regex = "(.{1," + width + "})";
    if (splitAtWhiteSpaces) {
      regex += "\\s+";
    }
    text = text.replaceAll(regex, replacement);
    // remove first spaces and last "\n"
    text = text.substring(0, text.length() - 1);
    if (!splitAtWhiteSpaces) {
      text = text.substring(0, text.length() - 1);
    }
    return text;
  }

  private void checkArguments() {
    Validate.isTrue(indent >= 0, "indent should not be negative: %d", indent);
    Validate.isTrue(width > 0, "text width should not be negative or zero: %d", width);
    Validate.isTrue(
        indent < width,
        "indent should not be less than width: indent=%d, width=%d",
        indent,
        width);
  }

}
