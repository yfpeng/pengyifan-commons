package com.pengyifan.nlp.ling.string;

public final class GreekString {

  public static final String GREEK[] = { "alpha", //
      "beta", //
      "gamma", //
      "delta",//
      "epsilon", //
      "zeta", //
      "eta", //
      "theta", //
      "iota", //
      "kappa", //
      "lambda", //
      "mu", //
      "nu",//
      "xi", //
      "omicron", //
      "pi", //
      "rho", //
      "sigma",//
      "tau", //
      "upsilon",//
      "phi", //
      "chi",//
      "psi", //
      "omega" //
  };

  public static boolean isGreek(String str) {
    for (String s : GREEK) {
      if (str.equals(s)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEndWithGreek(String str) {
    for (String s : GREEK) {
      if (str.endsWith(s)) {
        return true;
      }
    }
    return false;
  }
}
