package com.pengyifan.nlp.ling.string;

import java.util.regex.Pattern;

// also called subordinating conjunction
public final class SubordinatorString {

  public static final String COMPLEX[] = { "as far as",//
      "as if", //
      "as long as", //
      "as soon as", //
      "as though", //
      "assuming", //
      "assuming that",//
      "considering", //
      // "given", //
      "given that", //
      "granted", //
      "granted that", //
      "in case", //
      "in order for", //
      "in order that", //
      "indicating", //
      "insofar as", //
      "insomuch as", //
      "in the envent that", //
      "providing", //
      "providing that", //
      "provided", //
      "provided that", //
      "seeing", //
      "seeing as", //
      "seeing that", //
      "showing",//
      "showing that",//
      "so long as",//
      "such that", //
      "supporting",//
      "supporting that",//
      "suggesting", //
      "suggesting that", //
      "supposing", //
      "supposing that" //
  };

  public final static String SIMPLE[] = { //
  // "after", //
      "although", //
      // "as", //
      "because", //
      // "before", //
      // "for", //
      // "how", //
      "however", //
      // "if", //
      // "lest", //
      // "once", //
      "since", //
      // "that",//
      "though", //
      // "till", //
      // "unless", //
      // "until", //
      "whereas", //
      "while", //
      "whilst", //
  };

  public final static String REL_PRON[] = { "that", //
      "which", //
      "whichever", //
      "who", //
      "whoever", //
      "whom", //
      "whose", //
      "whosever", //
      "whether" //
  };

  public final static String REL_ADV[] = { "when", //
      "whenever", //
      "where", //
      "wherever", //
      "why", //
  };

  public final static boolean isSimple(String word) {
    return SubordinatorString.is(word, SIMPLE);
  }

  public final static boolean isComplex(String phrase) {
    return SubordinatorString.is(phrase, COMPLEX);
  }

  public final static boolean isRelativePronoun(String word) {
    return SubordinatorString.is(word, REL_PRON);
  }

  public final static boolean isRelativeAdverb(String word) {
    return SubordinatorString.is(word, REL_ADV);
  }

  static Pattern lowercases = Pattern.compile("^[a-z ]+$");
  static Pattern capitalcases = Pattern.compile("^[A-Z][a-z ]+$");

  private final static boolean is(String word, String subordinators[]) {
    for (String s : subordinators) {
      if (word.equalsIgnoreCase(s) //
          && (SubordinatorString.lowercases.matcher(word).find() //
          || SubordinatorString.capitalcases.matcher(word).find())) {
        return true;
      }
    }
    return false;
  }
}
