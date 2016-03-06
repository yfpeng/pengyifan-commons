package com.pengyifan.util.regex;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pengyifan.util.regex.RegExpPattern.FsaTable;

import java.util.Iterator;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkState;

public class RegExpMatcher {

  private static final int NOT_FOUND = -2;

  /**
   * The Pattern object that created this Matcher.
   */
  RegExpPattern pattern;

  /**
   * Contains the current patterns accessed
   */
  private int patternIndex;
  /**
   * The original string being matched.
   */
  private final CharSequence text;
  /**
   * Contains all found patterns
   */
  private LinkedList<CharSequence> groupList;
  /**
   * Contains all position where these pattern start in the text
   */
  private LinkedList<Integer> startList;

  RegExpMatcher(RegExpPattern pattern, CharSequence text) {
    this.pattern = pattern;
    this.text = text;
    reset();
  }

  /**
   * Returns the pattern that is interpreted by this matcher.
   *
   * @return The pattern for which this matcher was created
   */
  public RegExpPattern pattern() {
    return pattern;
  }

  /**
   * Returns the start index of the previous matcher.
   *
   * @return the start index of the previous matcher
   * @throws IllegalStateException If no matcher has yet been attempted, or if the
   *                               previous matcher operation failed
   */
  public int start() {
    if (patternIndex == NOT_FOUND) {
      throw new IllegalStateException("No matcher available");
    }
    if (patternIndex >= startList.size()) {
      throw new IllegalStateException("No matcher available");
    }
    return startList.get(patternIndex);
  }

  /**
   * Returns the offset after the last character matched.
   *
   * @return The offset after the last character matched
   * @throws java.lang.IllegalStateException If no matcher has yet been attempted, or if the
   *                                         previous
   *                                         matcher operation failed
   */
  public int end() {
    return startList.get(patternIndex) + groupList.get(patternIndex).length();
  }

  /**
   * Returns the input subsequence matched by the previous matcher.
   *
   * @return the input subsequence matched by the previous matcher
   * @throws IllegalStateException If no matcher has yet been attempted, or if the
   *                               previous matcher operation failed
   */
  public String group() {
    checkState(patternIndex != NOT_FOUND, "No matcher available");
    checkState(patternIndex < groupList.size(), "No matcher available");
    return groupList.get(patternIndex).toString();
  }

  /**
   * Attempts to find the next subsequence of the input sequence that matches the pattern. This
   * method starts at the beginning of this matcher's region, or, if a previous invocation of the
   * method was successful and the matcher has not since been reset, at the first character not
   * matched by the previous matcher. If the matcher succeeds then more information can be obtained
   * via
   * the start, end, and group methods.
   *
   * @return true if, and only if, a subsequence of the input sequence matches this matcher's
   * pattern
   */
  public boolean find() {
    if (patternIndex == -2) {
      return false;
    }
    ++patternIndex;
    return patternIndex < startList.size();
  }

  /**
   * Searches the text for all occurrences of the patterns
   */
  void reset() {
    groupList = Lists.newLinkedList();
    startList = Lists.newLinkedList();

    LinkedList<PatternState> patternStateList = Lists.newLinkedList();
    FsaTable dfaTable = pattern.getDfaTable();

    // if there is no DFA then there is no matching
    if (dfaTable.isEmpty()) {
      patternIndex = NOT_FOUND;
      return;
    }
    // Go through all input charactes
    for (int i = 0; i < text.length(); ++i) {
      char c = text.charAt(i);

      // Check all patterns states
      Iterator<PatternState> itr = patternStateList.iterator();
      while (itr.hasNext()) {
        PatternState pPatternState = itr.next();
        // must be at most one because this is DFA
        LinkedList<RegExpState> Transition = pPatternState.getState().getTransition(c);
        if (!Transition.isEmpty()) {
          pPatternState.setState(Transition.getFirst());
          if (Transition.getFirst().isAcceptingState()) {
            startList.add(pPatternState.getStartIndex());
            groupList.add(text.subSequence(pPatternState.getStartIndex(), i + 1));
          }
        } else {
          // Delete this pattern state
          itr.remove();
        }
      }

      // Check it against state 1 of the DFA
      RegExpState pState = dfaTable.getFirst();
      // must be at most one because this is DFA
      LinkedList<RegExpState> Transition = pState.getTransition(c);
      if (!Transition.isEmpty()) {
        PatternState pPatternState = new PatternState(i, Transition.getFirst());
        patternStateList.add(pPatternState);

        // Check is this accepting state
        if (Transition.getFirst().isAcceptingState()) {
          startList.add(i);
          groupList.add(String.valueOf(c));
        }
      } else {
        // Check here is the entry state already accepting
        // because a* for example would accept 0 or many a's
        // whcih means that any character is actually accepted
        if (pState.isAcceptingState()) {
          startList.add(i);
          groupList.add(String.valueOf(c));
        }
      }
    }


    if (startList.isEmpty()) {
      patternIndex = NOT_FOUND;
    } else {
      patternIndex = -1;
    }
  }
}
