package com.pengyifan.commons.lang3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.Validate;

public class RangeUtils {

  // [xxx..xxx]
  public static final Pattern RANGE_PATTERN = Pattern
      .compile("\\[(.+)([.]{2})(.+)\\]");

  public static Range<Integer> parseRangeInt(String str) {
    Matcher m = RANGE_PATTERN.matcher(str);
    Validate.isTrue(m.find(), "Illegal range str: %s", str);
    return Range.between(
        Integer.parseInt(m.group(1)),
        Integer.parseInt(m.group(3)));
  }

  public static Range<Double> parseRangeDouble(String str) {
    Matcher m = RANGE_PATTERN.matcher(str);
    Validate.isTrue(m.find(), "Illegal range str: %s", str);
    return Range.between(
        Double.parseDouble(m.group(1)),
        Double.parseDouble(m.group(3)));
  }
}
