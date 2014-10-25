package com.pengyifan.nlp.ling.pos;

public class PennPosLabelAdapter {

  public static String adapt(String postag) {
    if (postag.equals("(")) {
      return "LEFT_PARENTHESES";
    } else if (postag.equals(")")) {
      return "RIGHT_PARENTHESES";
    } else if (postag.equals(".")) {
      return "PERIOD";
    } else if (postag.equals(":")) {
      return "COLON";
    } else if (postag.equals(";")) {
      return "SEMICOLON";
    } else if (postag.equals(",")) {
      return "COMMA";
    } else if (postag.equals("-")) {
      return "HYPHEN";
    } else if (postag.equals("\"")) {
      return "QUOTATION";
    } else if (postag.equals("#")) {
      return "POUND";
    }
    return postag;
  }
}
