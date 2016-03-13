package com.pengyifan.util.regex;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;


class RegExpState {
  /**
   * Transitions from this state to other
   */
  private Multimap<Character, RegExpState> transition;

  /**
   * State ID
   */
  private int stateID;

  /**
   * Set of NFA state from which this state is constructed
   */
  private Set<RegExpState> nfaStates;

  /**
   * True if this state is accepting state
   */
  private boolean isAcceptingState;

  RegExpState(int stateID) {

    this.stateID = stateID;
    isAcceptingState = false;
    transition = ArrayListMultimap.create();
    nfaStates = Sets.newHashSet();
  }

  RegExpState(Set<RegExpState> nfaStates, int stateID) {
    this(stateID);
    this.nfaStates.addAll(nfaStates);
    // DFA state is accepting state if it is constructed from
    // an accepting NFA state
    isAcceptingState = nfaStates
        .stream()
        .filter(state -> state.isAcceptingState)
        .findFirst()
        .isPresent();
  }

  /**
   * True if this state is accepting state
   */
  public boolean isAcceptingState() {
    return isAcceptingState;
  }

  public void setIsAcceptingState(boolean isAcceptingState) {
    this.isAcceptingState = isAcceptingState;
  }

  public Multimap<Character, RegExpState> getTransition() {
    return transition;
  }

  /**
   * Adds a transition from this state to the other
   *
   * @param ch    signal character
   * @param state next state
   */
  void addTransition(char ch, RegExpState state) {
    checkNotNull(state != null);
    transition.put(ch, state);
  }

  /**
   * Removes all transition that go from this to state
   *
   * @param state target state
   */
  void removeTransition(final RegExpState state) {
    List<Map.Entry<Character, RegExpState>> entries = transition.entries().stream()
        .filter(entry -> entry.getValue().equals(state))
        .collect(Collectors.toList());
    for (Map.Entry<Character, RegExpState> entry : entries) {
      transition.remove(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Returns all transitions from this state on specific input
   *
   * @param ch input character
   * @return all transitions from this state on specific input
   */
  LinkedList<RegExpState> getTransition(char ch) {
    return transition.entries().stream()
        .filter(entry -> entry.getKey() == ch)
        .map(Entry::getValue)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Returns the state id in form of string
   *
   * @return state id
   */
  int getStateID() {
    return stateID;
  }

  /**
   * Returns the set of NFA states from which this DFA state was constructed
   *
   * @return the set of NFA states from which this DFA state was constructed
   */
  Set<RegExpState> getNfaStates() {
    return nfaStates;
  }

  /**
   * Returns true if this state is dead end. By dead end I mean that this state is not accepting
   * state and there are no transitions leading away from this state. This function is used for
   * reducing the DFA.
   *
   * @return true if this state is dead end
   */
  boolean isDeadEnd() {
    if (isAcceptingState) {
      return false;
    }
    if (transition.isEmpty()) {
      return true;
    }

    for (Map.Entry<Character, RegExpState> entry : transition.entries()) {
      if (!entry.getValue().equals(this)) {
        return false;
      }
    }

    System.err.printf("State %d is dead end.\n", stateID);

    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof RegExpState)) {
      return false;
    }
    RegExpState rhs = (RegExpState) obj;
    if (nfaStates.isEmpty())
      return stateID == rhs.stateID;
    else return Objects.equals(nfaStates, rhs.nfaStates);
  }

}