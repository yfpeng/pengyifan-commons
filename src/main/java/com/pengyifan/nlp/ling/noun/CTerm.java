package com.pengyifan.nlp.ling.noun;

import java.util.regex.Pattern;

public class CTerm {

  // combinations of letters and numbers only
  private final static Pattern p1 = Pattern.compile("[a-z]+\\-?[0-9]+");
  private final static Pattern p2 = Pattern.compile("[0-9]+\\-?[a-z]+");

  // combinations of letters and numbers only
  private final static Pattern p3 = Pattern
      .compile("^[0-9]*[a-z]+[0-9]+[a-z]*$");

  // all capital letters
  private final static Pattern p4 = Pattern.compile("^[A-Z]{2,}");

  // combinations of upper and lower case letters and dashes and slashes
  private final static Pattern p5 = Pattern.compile("^.*[a-z]+.*$");
  private final static Pattern p6 = Pattern.compile("^.*[A-Z]+.*$");
  private final static Pattern p7 = Pattern.compile("^.*[\\-\\/]+.*$");

  // multiple words ending in a number
  private final static Pattern p8 = Pattern.compile("^(.*) [0-9]+$");

  // Consonant
  private final static Pattern p9 = Pattern
      .compile("^[^ aeiouAEIOU\\d]{3,}$");
  private final static Pattern p10 = Pattern.compile("^\\w+{4,}ase$");

  private final static Pattern[] ps = new Pattern[] { p1, p2, p3, p4, p5, p6,
      p7, p8, p9, p10 };

  public static boolean isCterm(String str) {

    for (Pattern p : ps) {
      if (p.matcher(str).find()) {
        return true;
      }
    }

    return false;
  }
}
