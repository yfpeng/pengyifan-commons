package com.pengyifan.nlp.ling.string;

public class ConjunctionString {

  public static final String CORRELATIVE[] = { "not only", //
      "both",//
      "either",//
      "neither", //
      "between" //
  };

  public static final String COMPLEX[] = { "not only",//
      "but also",//
      "but not",//
      "and also", //
      "as well as",//
      "and / or" //
  };

  public static boolean isComplexConjunction(String str) {
    for (String s : COMPLEX) {
      if (str.endsWith(s)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isCorrelativeConjunction(String str) {
    for (String s : CORRELATIVE) {
      if (str.endsWith(s)) {
        return true;
      }
    }
    return false;
  }
}
