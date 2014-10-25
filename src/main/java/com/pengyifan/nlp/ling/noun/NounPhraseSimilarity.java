package com.pengyifan.nlp.ling.noun;

public class NounPhraseSimilarity {

  public static final int NO = 0;
  public static final int HIGHER = 5;
  public static final int HIGH = 4;
  public static final int MIDIUM = 3;
  public static final int LOW = 2;
  public static final int LOWER = 1;

  private static final String suffix[][] = { //
  { "sion", "sion" },//
      { "tion", "tion" }, //
      { "ing", "ing" }, //
      { "tal", "tal" }, //
      { "ary", "ary" }, //
      { "tic", "tic" }, //
      { "tic", "gic" },//
      { "mal", "mal" }, //
      { "ous", "ous" }, //
      { "tory", "tory" },//
      { "tive", "tive" }, //
      { "sis", "sis" }, //
      { "gen", "gen" },//
      { "-", "-" }, //
      { "ate", "ate" }, //
      { "ion", "ion" }, //
      { "ity", "ity" } };

  private static final String prefix[] = { "no", "anti", "poly", "pre",
      "tran", "-" };

  private static final String word[][] = { { "vitro", "vivo" },
      { "male", "female" }, //
      { "males", "females" }, //
      { "one", "more" },//
      { "little", "no" }, //
      { "process", "consequences" } };

  private static boolean sameWord(String w1, String w2) {
    if (w1.equalsIgnoreCase(w2)) {
      return true;
    }
    for (String[] element : NounPhraseSimilarity.word) {
      if ((w1.equalsIgnoreCase(element[0]) && //
          w2.equalsIgnoreCase(element[1]))//
          || (w1.equalsIgnoreCase(element[1]) && //
          w2.equalsIgnoreCase(element[0]))) {
        return true;
      }
    }
    return false;
  }

  private static boolean sameSuffix(String w1, String w2) {
    for (String[] element : NounPhraseSimilarity.suffix) {
      if ((w1.endsWith(element[0]) && w2.endsWith(element[1]))
          || (w1.endsWith(element[1]) && w2.endsWith(element[0]))) {
        return true;
      }
    }
    return false;
  }

  private static boolean samePrefix(String w1, String w2) {
    for (String p : NounPhraseSimilarity.prefix) {
      if (w1.startsWith(p) && w2.startsWith(p)) {
        return true;
      } else if (w1.startsWith(p)) {
        String stem = w1.substring(p.length());
        if (w2.equalsIgnoreCase(stem)) {
          return true;
        }
      } else if (w2.startsWith(p)) {
        String stem = w2.substring(p.length());
        if (w1.equalsIgnoreCase(stem)) {
          return true;
        }
      }
    }
    return false;
  }

  public static int compare(NounPhrase np1, NounPhrase np2) {

    String w1 = np1.word();
    String w2 = np2.word();

    if (NounPhraseSimilarity.sameWord(w1, w2)) {
      return HIGHER;
    } else if (np1.isInteger() && np2.isInteger()) {
      return HIGHER;
    } else if (np1.isRationalNumber() && np2.isRationalNumber()) {
      return HIGHER;
    } else if (np1.isFiniteDecimal() && np2.isFiniteDecimal()) {
      return HIGHER;
    } else if (np1.isGreek() && np2.isGreek()) {
      return HIGHER;
    } else if (np1.isLetterNumber() && np2.isLetterNumber()) {
      return HIGHER;
    } else if (np1.isLETTERNumber() && np2.isLETTERNumber()) {
      return HIGHER;
    } else if (np1.isUpperCasesLongerThanTwo()
        && np2.isUpperCasesLongerThanTwo()) {
      return HIGHER;
    } else if (np1.isSingleUpperCase() && np2.isSingleUpperCase()) {
      return HIGHER;
    } else if (np1.startsWithHypen() && np2.startsWithHypen()) {
      return HIGHER;
    } else if (np1.endsWithHypen() && np2.endsWithHypen()) {
      return HIGHER;
    } else if (np1.endsWithUpperCases() && np2.endsWithUpperCases()) {
      return HIGHER;
    } else if (np1.isMiddleWithHypen() && np2.isMiddleWithHypen()) {
      return HIGHER;
    } else if (np1.isMiddleWithNumber() && np2.isMiddleWithNumber()) {
      return HIGHER;
    } else if (np1.isFTerm() && np2.isFTerm()) {
      return HIGHER;
    } else if ((np1.word().equals("a") && np2.word().equals("an"))//
        || (np1.word().equals("an") && np2.word().equals("a"))) {
      return HIGHER;
    } else if (NounPhraseSimilarity.samePrefix(w1, w2)) {
      if (NounPhraseSimilarity.sameSuffix(w1, w2)) {
        return HIGHER;
      } else {
        return HIGH;
      }
    } else if (NounPhraseSimilarity.sameSuffix(w1, w2)) {
      return HIGH;
    } else if ((np1.isLetterNumber() && np2.isLETTERNumber())
        || (np2.isLetterNumber() && np1.isLETTERNumber())) {
      return HIGH;
    } else if (np1.isFTerm() && np2.isFTerm()) {
      return HIGH;
    } else if (np1.isCTerm() && np2.isCTerm()) {
      return HIGH;
    } else {
      return NO;
    }
  }
}