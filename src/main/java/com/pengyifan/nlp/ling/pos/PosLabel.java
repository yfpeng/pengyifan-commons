package com.pengyifan.nlp.ling.pos;

/**
 * PennTreeBank
 */
public enum PosLabel {
  $, CC, CD, COLON, COMMA, DT, EX, FW, HYPHEN, IN, JJ, JJR, JJS, LS, MD, NN,
  NNP, NNPS, NNS, PDT, PERIOD, POS, POUND, PRP, PRP$, RB, RBR, RBS, RP,
  SEMICOLON, SYM, TO, UH, VB, VBD, VBG, VBN, VBP, VBZ, WDT, WP, WP$, WRB, //
  LEFT_PARENTHESES, RIGHT_PARENTHESES, //
  LEFT_QUOTATION, RIGHT_QUOTATION, QUOTATION, //
  O;

  public boolean isStop() {
    return this == SEMICOLON || this == PERIOD;// || isQuestion() ||
                                               // isExclamation();
  }

  public boolean isV() {
    return this == VB
        || this == VBD
        || this == VBG
        || this == VBN
        || this == VBP
        || this == VBZ
        || this == TO
        || this == MD;
  }
}
