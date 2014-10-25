package com.pengyifan.nlp.ling.string;

public class NumberString {

  public static final String NUMBER[] = { "zero",//
      "one", //
      "two", //
      "three", "four", //
      "five", //
      "six", //
      "seven", //
      "eight", //
      "nine", //
      "ten", //
      "eleven",//
      "twelve",//
      "thirteen", //
      "fourteen", //
      "fifteen", //
      "sixteen", //
      "seventeen",//
      "eighteen", //
      "nineteen", //
      "twenty" //
  };

  public static boolean isNumberTwoOrMore(String str) {
    for (int i = 2; i < NumberString.NUMBER.length; i++) {
      if (str.equals(NumberString.NUMBER[i])) {
        return true;
      }
    }
    return false;
  }

  public static boolean isNumberOne(String str) {
    return str.equals(NumberString.NUMBER[1]);
  }

  public static boolean isNumber(String str) {
    for (String s : NumberString.NUMBER) {
      if (str.equals(s)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isSameNumber(String str, int num) {
    assert num < NUMBER.length;
    return NUMBER[num].equals(str);
  }
}
