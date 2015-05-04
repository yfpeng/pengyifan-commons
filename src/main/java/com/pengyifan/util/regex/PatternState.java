package com.pengyifan.util.regex;

class PatternState {
  /**
   * Pointer to current state in DFA
   */
  private RegExpState state;

  /**
   * 0-Based index of the starting position
   */
  private int startIndex;

  PatternState(int startIndex, RegExpState state) {
    this.state = state;
    this.startIndex = startIndex;
  }

  /**
   * Returns pointer to current state in DFA
   *
   * @return pointer to current state in DFA
   */
  RegExpState getState() {
    return state;
  }

  void setState(RegExpState state) {
    this.state = state;
  }

  /**
   * Returns 0-Based index of the starting position
   *
   * @return 0-Based index of the starting position
   */
  int getStartIndex() {
    return startIndex;
  }

  void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }
}
