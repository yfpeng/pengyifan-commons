package com.pengyifan.commons.lang;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public final class FromStringBuilder {

  private static final Pattern pattern = Pattern
      .compile("([a-z]+)=\"([^\"]+|\")\"");

  private FromStringBuilder() throws InstantiationException {
    throw new InstantiationException("This class is not for instantiation");    
  }

  /**
   * Parses a string formatted with toStringBuilder
   * 
   * @param input - ex.
   *          "Path[id=1039916,displayName=School Home,description=<null>,...]"
   * @return hashmap of name value pairs - ex. id=1039916,...
   */
  public static Map<String, String> stringToMap(String input) {
    Map<String, String> map = Maps.newHashMap();

    int firstIndex = input.indexOf('[');
    int lastIndex = input.lastIndexOf(']');

    String partsString = input.substring(firstIndex + 1, lastIndex);
    checkNotNull(partsString, "input is null: %s", input);
    Matcher matcher = pattern.matcher(partsString);
    while (matcher.find()) {
      if (!StringUtils.equals("<null>", matcher.group(2))) {
        map.put(matcher.group(1), matcher.group(2));
      }
    }
    return map;
  }
}
