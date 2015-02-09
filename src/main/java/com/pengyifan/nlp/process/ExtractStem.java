package com.pengyifan.nlp.process;


/**
 * Extract stem of the word.
 * <p>
 * Stemming usually refers to a crude heuristic process that chops off the ends
 * of words in the hope of achieving this goal correctly most of the time, and
 * often includes the removal of derivational affixes.
 */
public class ExtractStem {

  public ExtractStem() {
  }

  /**
   * Returns the stem of the string.
   * 
   * @param str the string
   * @return the stem of the string
   */
  public String stem(String str) {
    PorterStemmer stemmer = new PorterStemmer();
    char[] chars = str.toCharArray();
    stemmer.add(chars, chars.length);
    stemmer.stem();
    return new String(stemmer.getResultBuffer(), 0, stemmer.getResultLength());
  }

}
