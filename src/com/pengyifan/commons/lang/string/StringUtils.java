package com.pengyifan.commons.lang.string;

import java.io.UnsupportedEncodingException;

public class StringUtils {

  // │
  public static final String BAR    = bar(1);
  // └
  public static final String END    = bar(2);
  // ├
  public static final String MIDDLE = bar(3);

  private static String bar(int i) {
    try {
      switch (i) {
      case 1:
        return new String(new byte[] { -30, -108, -126 }, "utf8");
      case 2:
        return new String(new byte[] { -30, -108, -108 }, "utf8");
      case 3:
        return new String(new byte[] { -30, -108, -100 }, "utf8");
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
