package com.pengyifan.nlp.ling.verb;

public enum VerbTense {
  BASE, PRESENT, PRESENT_PERFECT, PRESENT_PROGRESSIVE,
  PRESENT_PERFECT_PROGRESSIVE, // present
  FUTURE, FUTURE_PERFECT, FUTURE_PROGRESSIVE, FUTURE_PERFECT_PROGRESSIVE, // future
  PAST, PAST_PROGRESSIVE, PAST_PERFECT, PAST_PERFECT_PROGRESSIVE, // past
  NO_TENSE, ANTI_TENSE, // others
  ;

  // I do
  // you do
  // she does
  public boolean isPresent() {
    return this == BASE //
        || this == PRESENT //
        || this == PRESENT_PERFECT //
        || this == PRESENT_PROGRESSIVE //
        || this == PRESENT_PERFECT_PROGRESSIVE;
  }

  // I will do
  // you will do
  // she will do
  public boolean isFuture() {
    return this == FUTURE //
        || this == FUTURE_PERFECT//
        || this == FUTURE_PERFECT_PROGRESSIVE//
        || this == FUTURE_PROGRESSIVE;
  }

  // I did
  // you did
  // she did
  public boolean isPast() {
    return this == PAST //
        || this == PAST_PERFECT//
        || this == PAST_PERFECT_PROGRESSIVE//
        || this == PAST_PROGRESSIVE;
  }

  public boolean isOthers() {
    return this == BASE //
        || this == NO_TENSE//
        || this == ANTI_TENSE;
  }
}
