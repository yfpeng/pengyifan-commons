package com.pengyifan.nlp.brat;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class BratDocumentTest {

  private static final String TEXT = "text";
  private static final String ID = "id";
  private static final String ANNOTATION = ""
      + "T1\tProtein 48 53\tBMP-6\n"
      + "T2\tProtein 161 164\tId1\n"
      + "T3\tProtein 202 207\tBMP-6\n"
      + "T4\tProtein 321 326\tCD40L\n"
      + "T5\tProtein 342 347\tBMP-6\n"
      + "T6\tProtein 493 498\tBMP-6\n"
      + "T7\tPositive_regulation 135 146\tconsecutive\n"
      + "T8\tGene_expression 147 157\tproduction\n"
      + "E1\tPositive_regulation:T7 Theme:E2\n"
      + "E2\tGene_expression:T8 Theme:T2";

  private BratDocument base;

  @Before
  public void setUp()
      throws IOException {
    base = BratIOUtils.read(new StringReader(ANNOTATION), ID);
    base.setText(TEXT);
  }

  @Test
  public void testBratDocumentBratDocument() {
    BratDocument copy = new BratDocument(base);
    assertEquals(base, copy);
  }

}
