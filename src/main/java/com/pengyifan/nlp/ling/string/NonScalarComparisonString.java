package com.pengyifan.nlp.ling.string;

public class NonScalarComparisonString {

  public static final String COMPARISON[] = { "such as", //
      /* "including", *///
      "ie",//
      "eg", //
      "i.e.", //
      "e.g." //
  };

  public static boolean isNonScalarComparison(String str) {
    for (String s : COMPARISON) {
      if (str.equalsIgnoreCase(s)) {
        return true;
      }
    }
    return false;
  }
}
