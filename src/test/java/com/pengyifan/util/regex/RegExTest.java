package com.pengyifan.util.regex;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RegExTest {
  @Test
  public void test() throws IOException {
    String patternStr = "a";
    String str = "axa";

    RegExpPattern pattern = RegExpPattern.compile(patternStr);
    RegExpMatcher matcher = pattern.matcher(str);
    assertTrue(matcher.find());
    assertPattern(0, patternStr, matcher);
    assertTrue(matcher.find());
    assertPattern(2, patternStr, matcher);
    assertFalse(matcher.find());
  }

  private void assertPattern(int nPos, String strPattern, RegExpMatcher matcher) {
    assertEquals(nPos, matcher.start());
    assertEquals(strPattern, matcher.group());
  }

  @Test
  public void test2() throws IOException {
    String patternStr = "ab";
    String str = "abxxbab";

    RegExpPattern pattern = RegExpPattern.compile(patternStr);
    RegExpMatcher matcher = pattern.matcher(str);
    assertTrue(matcher.find());
    assertPattern(0, patternStr, matcher);
    assertTrue(matcher.find());
    assertPattern(5, patternStr, matcher);
    assertFalse(matcher.find());
  }

  @Test
  public void test3() throws IOException {
    String patternStr = "abc";
    String str = "xxabcxx";

    RegExpPattern pattern = RegExpPattern.compile(patternStr);
    RegExpMatcher matcher = pattern.matcher(str);
    assertTrue(matcher.find());
    assertPattern(2, patternStr, matcher);
    assertFalse(matcher.find());
  }

  @Test
  public void test4() throws IOException {
    String patternStr = "(a|c)";
    String str = "xxabcxx";

    RegExpPattern pattern = RegExpPattern.compile(patternStr);
    RegExpMatcher matcher = pattern.matcher(str);
    assertTrue(matcher.find());
    assertPattern(2, "a", matcher);
    assertTrue(matcher.find());
    assertPattern(4, "c", matcher);
    assertFalse(matcher.find());
  }

  @Test
  public void test5() throws IOException {
    String patternStr = "(a|c)b";
    String str = "xxabxcbxx";

    RegExpPattern pattern = RegExpPattern.compile(patternStr);
    // pattern.writeDfaGraph(Paths.get("dfa.gv"));

    RegExpMatcher matcher = pattern.matcher(str);
    assertTrue(matcher.find());
    assertPattern(2, "ab", matcher);
    assertTrue(matcher.find());
    assertPattern(5, "cb", matcher);
    assertFalse(matcher.find());
  }
}