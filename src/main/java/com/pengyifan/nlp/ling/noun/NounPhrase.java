package com.pengyifan.nlp.ling.noun;

import java.util.regex.Pattern;

import com.pengyifan.nlp.ling.string.GreekString;
import com.pengyifan.nlp.ling.string.NumberString;

public class NounPhrase {

  final String word;

  final private static Pattern isInteger = Pattern
      .compile("^[+-]?[0-9]+$");

  final private static Pattern isRationalNumber = Pattern
      .compile("^[+-]?[0-9]+[/][0-9]+$");

  final private static Pattern isFiniteDecimal = Pattern
      .compile("^[-+]?[0-9]*[.][0-9]+([eE][-+]?[0-9]+)?$");

  final private static Pattern isLetterNumber = Pattern
      .compile("^[a-z][\\w\\-\\/ ]*\\d$");

  final private static Pattern isLETTERNumber = Pattern
      .compile("^[A-Z][\\w\\-\\/ ]*\\d$");

  final private static Pattern isUpperCasesLongerThanTwo = Pattern
      .compile("^[A-Z]{2,}s?$");

  final private static Pattern isSingleUpperCase = Pattern
      .compile("^[A-Z]$");

  final private static Pattern beginsWithHypen = Pattern
      .compile("^[\\-\\/]\\w+$");

  final private static Pattern endsWithHypen = Pattern
      .compile("^\\w+[\\-\\/]$");

  final private static Pattern endsWithUpperCases = Pattern
      .compile("^[A-Z]*[a-z0-9]+[A-Z]+$");

  final private static Pattern middlesWithHypen = Pattern
      .compile("^\\w[\\w -]+[\\-\\/][\\w -]*\\w$");

  final private static Pattern middlesWithNumber = Pattern
      .compile("^[a-zA-Z]+\\d+[a-zA-Z]+$");

  public NounPhrase(String word) {
    this.word = word;
  }

  public String word() {
    return word;
  }

  private boolean matches(Pattern pattern) {
    return pattern.matcher(word).matches();
  }

  private boolean find(Pattern pattern) {
    return pattern.matcher(word).find();
  }

  public final boolean isFTerm() {
    return FTerm.isFterm(word());
  }

  public final boolean isGreek() {
    return GreekString.isGreek(word) || GreekString.isEndWithGreek(word);
  }

  public final boolean isUpperCasesLongerThanTwo() {
    return matches(isUpperCasesLongerThanTwo);
  }

  public final boolean endsWithHypen() {
    return find(endsWithHypen);
  }

  public final boolean startsWithHypen() {
    return find(beginsWithHypen);
  }

  public final boolean endsWithNumber() {
    return isLetterNumber() || isLETTERNumber();
  }

  public final boolean endsWithUpperCases() {
    return find(endsWithUpperCases);
  }

  public final boolean isMiddleWithHypen() {
    return find(middlesWithHypen);
  }

  public final boolean isMiddleWithNumber() {
    return find(middlesWithNumber);
  }

  public final boolean isLetterNumber() {
    return matches(isLetterNumber);
  }

  public final boolean isLETTERNumber() {
    return matches(isLETTERNumber);
  }

  public final boolean isInteger() {
    return matches(isInteger);
  }

  public final boolean isFiniteDecimal() {
    return matches(isFiniteDecimal);
  }

  public final boolean isRationalNumber() {
    return matches(isRationalNumber);
  }

  public final boolean isCTerm() {
    return isRationalNumber() //
        || isFiniteDecimal() //
        || isInteger() //
        || isLETTERNumber()//
        || isLetterNumber()//
        || isMiddleWithNumber()//
        || isMiddleWithHypen()//
        || endsWithUpperCases()//
        || endsWithHypen()//
        || endsWithNumber()//
        || startsWithHypen() //
        || isUpperCasesLongerThanTwo() //
        || CTerm.isCterm(word) //
    ;
  }

  public boolean isSingleUpperCase() {
    return matches(isSingleUpperCase);
  }

  public boolean isNumber() {
    return NumberString.isNumber(word());
  }

  public boolean isNumberOne() {
    return NumberString.isNumberOne(word());
  }

  public boolean isNumberTwoOrMore() {
    return NumberString.isNumberTwoOrMore(word());
  }
}
