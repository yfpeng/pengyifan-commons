package com.pengyifan.util.regex;

import com.google.common.base.Joiner;
import junit.framework.TestCase;

import java.util.List;

public class InfixToPostfixTest extends TestCase {

  private RegInfixToPostfix converter = new RegInfixToPostfix();

  public void testConvertToPostfix() throws Exception {
    myAssertEquals("a", "a");
    myAssertEquals("ab", "ab+");
    myAssertEquals("abc", "ab+c+");
    myAssertEquals("(a|c)", "ac|");
    myAssertEquals("(a|c)b", "ac|b+");
  }

  private void myAssertEquals(String infix, String postfix) {
    assertEquals(postfix, toString(converter.convertToPostfix(infix)));
  }

  private String toString(List<Character> list) {
    list.replaceAll(c -> c== RegInfixToPostfix.CONTACT_CHAR ? '+' : c);
    return Joiner.on("").join(list);
  }
}