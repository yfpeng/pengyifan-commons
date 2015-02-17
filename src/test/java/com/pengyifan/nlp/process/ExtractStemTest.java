package com.pengyifan.nlp.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ExtractStemTest {

  private final static String LINE = "Such an analysis can reveal features"
      + " that are not easily visible from the variations in the individual"
      + " genes and can lead to a picutre of expression that is more"
      + " biologically transparent and accessible to interpretation";

  // porter stemmer
  private final static String EXPECTED = "Such an analysis can reveal features"
      + " that are not easily visible from the variations in the individual"
      + " genes and can lead to a picutre of expression that is more"
      + " biologically transparent and accessible to interpret";

  @Test
  public void test() {
    ExtractStem stemmer = new ExtractStem();
    String actual = stemmer.stem(LINE);
    assertEquals(EXPECTED, actual);
  }

}
