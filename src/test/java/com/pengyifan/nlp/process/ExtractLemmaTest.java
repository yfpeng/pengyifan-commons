package com.pengyifan.nlp.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExtractLemmaTest {

  private final static String LINE = "Such an analysisi can reveal features"
      + " that are not easily visible from the variations in the individual"
      + " genes and can lead to a picutre of expression that is more"
      + " biologically transparent and accessible to interpretation";

  private final static String EXPECTED = "such an analysisus can reveal feature"
      + " that be not easily visible from the variation in the individual"
      + " gene and can lead to a picutre of expression that be more"
      + " biologically transparent and accessible to interpretation";

  @Test
  public void test() {
    ExtractLemma lemma = new ExtractLemma();
    String actual = lemma.lemmatizeSentence(LINE);
    System.out.println(actual);
    assertEquals(EXPECTED, actual);
  }

}
