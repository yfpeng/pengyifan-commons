package com.pengyifan.util.regex;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * A compiled representation of a regular expression.
 * <p>
 * A regular expression, specified as a string, must first be compiled into an instance of this
 * class. The resulting pattern can then be used to create a {@link com.pengyifan.util.regex.RegExpMatcher}
 * object that can match arbitrary {@link java.lang.CharSequence} against the regular expression.
 * All of the state involved in performing a match resides in the matcher, so many matchers can
 * share the same pattern. A typical invocation sequence is thus
 * <pre>
 * RegExpPattern p = RegExpPattern.compile("a*b");
 * RegExpMatcher m = p.matcher("aaaaab");
 * while (m.find()) {
 *   ...
 * }
 * </pre>
 * Only three regular-expression constructs are implemented
 * <ul>
 * <li>Characters: x (the character x)</li>
 * <li>Logical operators: XY (X followed by Y), X|Y (either X or Y)</li>
 * <li>Greedy quantifiers: X* (X, zero or more times)</li>
 * </ul>
 */
public class RegExpPattern {

  private final static char epsilon = 0;
  private final static char contactChar = 8;

  /**
   * NFA Table is stored in a deque of CAG_States. Each RegExpState object has a multimap of
   * transitions where the key is the input character and values are the references to states to
   * which it transfers.
   */
  private FsaTable nfaTable;
  //! DFA table is stores in same format as NFA table
  private FsaTable dfaTable;
  //! Operand Stack
  /*! If we use the Thompson's Algorithm then we realize
    that each operand is a NFA automata on its own!
	*/
  private Stack<FsaTable> operandStack;
  //! Operator Stack
  /*! Operators are simple characters like "*" & "|" */
  private Stack<Character> operatorStack;
  //! Keeps track of state IDs
  private int nextStateID;
  //! Set of input characters
  private Set<Character> inputSet;

  /**
   * The original regular-expression pattern string.
   */
  private String pattern;

  /**
   * This private constructor is used to create all Patterns. The pattern string is all that is
   * needed to completely describe a Pattern.
   *
   * @param pattern pattern
   */
  private RegExpPattern(String pattern) {
    nextStateID = 0;
    nfaTable = new FsaTable();
    dfaTable = new FsaTable();
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
    inputSet = Sets.newHashSet();
    this.pattern = pattern;

    if (!compile()) {
      throw new PatternSyntaxException("Illegal pattern", pattern, 0);
    }
  }

  /**
   * Compiles the given regular expression into a pattern.
   *
   * @param regex The expression to be compiled
   * @return The compiled pattern object
   * @throws PatternSyntaxException If the expression's syntax is invalid
   */
  public static RegExpPattern compile(String regex) {
    return new RegExpPattern(regex);
  }

  /**
   * Creates a matcher that will match the given input against this pattern.
   *
   * @param input The character sequence to be matched
   * @return A new matcher for this pattern
   */
  public RegExpMatcher matcher(CharSequence input) {
    return new RegExpMatcher(this, input);
  }

  protected FsaTable getDfaTable() {
    return dfaTable;
  }

  /**
   * Constructs basic NFA for single character and pushes it onto the stack.
   *
   * @param ch The input character
   */
  private void push(char ch) {
    // Create 2 new states on the heap
    RegExpState s0 = new RegExpState(++nextStateID);
    RegExpState s1 = new RegExpState(++nextStateID);

    // Add the transition from s0.s1 on input character
    s0.addTransition(ch, s1);

    // Create a NFA from these 2 states
    FsaTable NFATable = new FsaTable();
    NFATable.add(s0);
    NFATable.add(s1);

    // push it onto the operand stack
    operandStack.push(NFATable);

    // Add this character to the input character Set
    inputSet.add(ch);

    // System.err.printf("PUSH %c\n", ch);
  }

  /**
   * Pops an element from the operand stack
   *
   * @return an element was poped successfully, otherwise empty (syntax error)
   */
  private FsaTable pop() {
    // If the stack is empty we cannot pop anything
    FsaTable table = new FsaTable();
    if (!operandStack.isEmpty()) {
      table.addAll(operandStack.pop());
    }
    return table;
  }

  /**
   * Checks is a specific character and operator
   *
   * @param ch The input character
   * @return true if the character is an operator
   */
  private boolean isOperator(char ch) {
    return ch == '*' || ch == '|' || ch == '(' || ch == ')' || ch == contactChar;
  }

  /**
   * Returns operator precedence
   * <ul>
   * <li> Kleens Closure	- highest
   * <li>Concatenation	- middle
   * <li>Union			- lowest
   * </ul>
   *
   * @param opLeft  left operator
   * @param opRight right operator
   * @return true if precedence of opLeft <= opRight.
   */
  private boolean precedence(char opLeft, char opRight) {
    if (opLeft == opRight) {
      return true;
    }
    if (opLeft == '*') {
      return false;
    }
    if (opRight == '*') {
      return true;
    }
    if (opLeft == contactChar) {
      return false;
    }
    if (opRight == contactChar) {
      return true;
    }
    if (opLeft == '|') {
      return false;
    }
    return true;
  }

  /**
   * Checks if the specific character is input character
   *
   * @param ch input character
   * @return true if the input character is an input character
   */
  private boolean isInput(char ch) {
    return !isOperator(ch);
  }

  /**
   * Checks is a character left parentheses
   * @param ch input character
   * @return true if the input character is left parentheses
   */
  private boolean isLeftParenthesis(char ch) {
    return ch == '(';
  }

  /**
   * Checks is a character right parentheses
   * @param ch input character
   * @return true if the right character is left parentheses
   */
  private boolean isRightParenthesis(char ch) {
    return ch == ')';
  }

  /**
   * Evaluates the next operator from the operator stack
   *
   * @return true if successful
   */
  private boolean eval() {
    // First pop the operator from the stack
    if (!operatorStack.isEmpty()) {
      char chOperator = operatorStack.pop();

      // Check which operator it is
      switch (chOperator) {
      case '*':
        return star();
      case '|':
        return union();
      case contactChar:
        return concat();
      default:
        return false;
      }
    }
    return false;
  }

  /**
   * Evaluates the concatenation operator. This function pops two operands from the stack and
   * evaluates the concatenation on them, pushing the result back on the stack.
   *
   * @return true if successful
   */
  private boolean concat() {
    // pop 2 elements
    FsaTable B = pop();
    FsaTable A = pop();

    if (B.isEmpty() || A.isEmpty()) {
      return false;
    }
    // Now evaluate AB
    // Basically take the last state from A
    // and add an epsilon transition to the
    // first state of B. Store the result into
    // new NFA_TABLE and push it onto the stack
    A.getLast().addTransition(epsilon, B.getFirst());
    A.addAll(B);

    // push the result onto the stack
    operandStack.push(A);

    // System.err.println("CONCAT");

    return true;
  }

  /**
   * Evaluates the Kleen's closure - star operator. Pops one operator from the stack and evaluates
   * the star operator on it. It pushes the result on the operand stack again.
   *
   * @return true if successful
   */
  private boolean star() {
    FsaTable A = pop();

    if (A.isEmpty()) {
      return false;
    }
    // Now evaluate A*
    // Create 2 new states which will be added
    // at each end of deque. Also take A and make
    // a epsilon transition from last to the first
    // state in the queue. Add epsilon transition
    // between two new states so that the one added
    // at the begin will be the source and the one
    // added at the end will be the destination
    RegExpState pStartState = new RegExpState(++nextStateID);
    RegExpState pEndState = new RegExpState(++nextStateID);
    pStartState.addTransition(epsilon, pEndState);

    // add epsilon transition from start state to the first state of A
    pStartState.addTransition(epsilon, A.getFirst());

    // add epsilon transition from A last state to end state
    A.getLast().addTransition(epsilon, pEndState);

    // From A last to A first state
    A.getLast().addTransition(epsilon, A.getFirst());

    // construct new DFA and store it onto the stack
    A.add(pEndState);
    A.addFirst(pStartState);

    // push the result onto the stack
    operandStack.push(A);

    System.out.printf("STAR\n");

    return true;
  }

  /**
   * Evaluates the union operator. Pops 2 operands from the stack and evaluates the union operator
   * pushing the result on the operand stack.
   *
   * @return true if successful
   */
  private boolean union() {
    // pop 2 elements
    FsaTable B = pop();
    FsaTable A = pop();

    if (B.isEmpty() || A.isEmpty()) {
      return false;
    }
    // Now evaluate A|B
    // Create 2 new states, a start state and
    // a end state. Create epsilon transition from
    // start state to the start states of A and B
    // Create epsilon transition from the end
    // states of A and B to the new end state
    RegExpState pStartState = new RegExpState(++nextStateID);
    RegExpState pEndState = new RegExpState(++nextStateID);
    pStartState.addTransition(epsilon, A.getFirst());
    pStartState.addTransition(epsilon, B.getFirst());
    A.getLast().addTransition(epsilon, pEndState);
    B.getLast().addTransition(epsilon, pEndState);

    // Create new NFA from A
    B.add(pEndState);
    A.addFirst(pStartState);
    A.addAll(B);

    // push the result onto the stack
    operandStack.push(A);

    // System.err.printf("UNION\n");

    return true;
  }

  /**
   * Inserts contactChar where the concatenation needs to occur. The method used to parse regular
   * expression here is similar to method of evaluating the arithmetic expressions. A difference
   * here is that each arithmetic expression is denoted by a sign. In regular expressions the
   * concatenation is not denoted by ny sign so I will detect concatenation and add a character
   * contactChar between operands.
   *
   * @return normalized pattern
   */
  private String normalize() {
    StringBuilder strRes = new StringBuilder();

    for (int i = 0; i < pattern.length() - 1; ++i) {
      char cLeft = pattern.charAt(i);
      char cRight = pattern.charAt(i + 1);
      strRes.append(cLeft);
      if (isInput(cLeft) || isRightParenthesis(cLeft) || cLeft == '*') {
        if (isInput(cRight) || isLeftParenthesis(cRight)) {
          strRes.append(contactChar);
        }
      }
    }
    strRes.append(pattern.charAt(pattern.length() - 1));
    return strRes.toString();
  }

  /**
   * Creates Nondeterministic Finite Automata from a Regular Expression
   *
   * @return true if successful
   */
  private boolean createNfa() {
    // Parse regular expresion using similar
    // method to evaluate arithmetic expressions
    // But first we will detect concatenation and
    // add contactChar at the position where
    // concatenation needs to occur
    String expandedPattern = normalize();

    for (int i = 0; i < expandedPattern.length(); ++i) {
      // get the character
      char c = expandedPattern.charAt(i);

      if (isInput(c)) {
        push(c);
      } else if (operatorStack.isEmpty()) {
        operatorStack.push(c);
      } else if (isLeftParenthesis(c)) {
        operatorStack.push(c);
      } else if (isRightParenthesis(c)) {
        // Evaluate everything in parenthesis
        while (!isLeftParenthesis(operatorStack.peek())) {
          if (!eval()) {
            return false;
          }
        }
        // Remove left parenthesis after the evaluation
        operatorStack.pop();
      } else {
        while (!operatorStack.isEmpty() && precedence(c, operatorStack.peek())) {
          if (!eval()) {
            return false;
          }
        }
        operatorStack.push(c);
      }
    }

    // Evaluate the rest of operators
    while (!operatorStack.isEmpty()) {
      if (!eval()) {
        return false;
      }
    }
    // pop the result from the stack
    FsaTable table = pop();
    if (table.isEmpty()) {
      return false;
    }
    nfaTable.addAll(table);
    // Last NFA state is always accepting state
    nfaTable.getLast().setIsAcceptingState(true);

    return true;
  }

  /**
   * Calculates the Epsilon Closure
   *
   * @param T input state set
   * @return epsilon closure of all states given with the parameter.
   */
  private Set<RegExpState> epsilonClosure(Set<RegExpState> T) {
    // Initialize result with T because each state
    // has epsilon closure to itself
    Set<RegExpState> Res = Sets.newHashSet(T);

    // push all states onto the stack
    Stack<RegExpState> unprocessedStack = new Stack<>();
    for (RegExpState state : T) {
      unprocessedStack.push(state);
    }
    // While the unprocessed stack is not empty
    while (!unprocessedStack.isEmpty()) {
      // pop t, the top element from unprocessed stack
      RegExpState t = unprocessedStack.pop();

      // Get all epsilon transition for this state
      // For each state u with an edge from t to u labeled epsilon
      for (RegExpState u : t.getTransition(epsilon)) {
        // if u not in e-closure(T)
        if (!Res.contains(u)) {
          Res.add(u);
          unprocessedStack.push(u);
        }
      }
    }
    return Res;
  }

  /**
   * Calculates all transitions on specific input char.
   *
   * @param ch input char
   * @param T  input state set
   * @return all states reachable from this Set of states on an input character.
   */
  private Set<RegExpState> move(char ch, Set<RegExpState> T) {
      /* This is very simple since I designed the NFA table
     structure in a way that we just need to loop through
	   each state in T and receive the transition on chInput.
	   Then we will put all the results into the Set, which
	   will eliminate duplicates automatically for us.
	*/
    return T.stream()
        .map(s -> s.getTransition(ch))
        .flatMap(l -> l.stream())
        .collect(Collectors.toSet());
  }

  /**
   * Converts NFA to DFA using the SubSet Construction Algorithm
   */
  private void convertNfaToDfa() {
    // Clean up the DFA Table first
    dfaTable.clear();

    // Check is NFA table empty
    if (nfaTable.isEmpty()) {
      return;
    }
    // ReSet the state id for new naming
    nextStateID = 0;

    // Array of unprocessed DFA states
    LinkedList<RegExpState> unmarkedStates = Lists.newLinkedList();

    // Starting state of DFA is epsilon closure of
    // starting state of NFA state (Set of states)
    Set<RegExpState> NFAStartStateSet = Sets.newHashSet(nfaTable.getFirst());
    Set<RegExpState> DFAStartStateSet = epsilonClosure(NFAStartStateSet);

    // Create new DFA State (start state) from the NFA states
    RegExpState DFAStartState = new RegExpState(DFAStartStateSet, ++nextStateID);

    // Add the start state to the DFA
    dfaTable.add(DFAStartState);

    // Add the starting state to Set of unprocessed DFA states
    unmarkedStates.add(DFAStartState);
    while (!unmarkedStates.isEmpty()) {
      // process an unprocessed state
      RegExpState processingDFAState = unmarkedStates.removeLast();

      // for each input signal a
      for (char c : inputSet) {
        Set<RegExpState> EpsilonClosureRes = epsilonClosure(
            move(c, processingDFAState.getNfaStates())
        );

        if (EpsilonClosureRes.isEmpty()) {
          continue;
        }

        // Check is the resulting Set (EpsilonClosureSet) in the
        // Set of DFA states (is any DFA state already constructed
        // from this Set of NFA states) or in pseudocode:
        // is U in D-States already (U = EpsilonClosureSet)
        Optional<RegExpState> opt = dfaTable.stream()
            .filter(s -> s.getNfaStates().equals(EpsilonClosureRes))
            .findFirst();
        if (opt.isPresent()) {
          processingDFAState.addTransition(c, opt.get());
        } else {
          RegExpState U = new RegExpState(EpsilonClosureRes, ++nextStateID);
          unmarkedStates.add(U);
          dfaTable.add(U);
          // Add transition from processingDFAState to new state on the current character
          processingDFAState.addTransition(c, U);
        }
      }
    }
  }

  /**
   * Optimizes the DFA. This function scanns DFA and checks for states that are not accepting
   * states
   * and there is no transition from that state to any other state. Then after deleting this state
   * we need to go through the DFA and delete all transitions from other states to this one.
   */
  private void reduceDfa() {
    // Get the Set of all dead end states in DFA
    Set<RegExpState> DeadEndSet = dfaTable.stream()
        .filter(s -> s.isDeadEnd())
        .collect(Collectors.toSet());

    // If there are no dead ends then there is nothing to reduce
    if (DeadEndSet.isEmpty()) {
      return;
    }
    // Remove all transitions to these states
    for (RegExpState state : DeadEndSet) {
      // Remove all transitions to this state
      dfaTable.forEach(s -> s.removeTransition(state));
      // Remove this state from the DFA Table
      Iterator<RegExpState> itr = dfaTable.iterator();
      while (itr.hasNext()) {
        RegExpState s = itr.next();
        if (s.equals(state)) {
          itr.remove();
        }
      }
    }
  }

  /**
   * Cleans up the memory
   */
  private void reset() {
    // Clean up all allocated memory for NFA
    nfaTable.clear();

    // Clean up all allocated memory for DFA
    dfaTable.clear();

    // ReSet the id tracking
    nextStateID = 0;

    // Clear both stacks
    operandStack.clear();
    operatorStack.clear();

    // Clean up the Input Character Set
    inputSet.clear();
  }

  /**
   * @return true if success othewise it returns false.
   */
  private boolean compile() {
    // 1. Clean up old regular expression
    reset();

    // 2. Create NFA
    if (!createNfa()) {
      return false;
    }

    // 3. Convert to DFA
    convertNfaToDfa();

    // 4. Reduce DFA
    reduceDfa();

    return true;
  }


  public void writeNfaTable(Path file) throws IOException {
    StringBuilder strNFATable = new StringBuilder();

    // First line are input characters
    for (char c : inputSet) {
      strNFATable.append("\t\t").append(c);
    }
    // add epsilon
    strNFATable.append("\t\tepsilon").append("\n");

    // Now go through each state and record the transitions
    for (RegExpState pState : nfaTable) {
      // Save the state id
      strNFATable.append(pState.getStateID());
      // now write all transitions for each character
      for (char c : inputSet) {
        strNFATable.append("\t\t").append(getStateString(pState.getTransition(c)));
      }
      // Add all epsilon transitions
      strNFATable.append("\t\t")
          .append(getStateString(pState.getTransition(epsilon)))
          .append("\n");
    }
    // Save table to the file
    FileUtils.writeStringToFile(file.toFile(), strNFATable.toString());
  }

  private String getStateString(List<RegExpState> states) {
    StringJoiner sj = new StringJoiner(",");
    states.forEach(
        s -> sj.add(String.valueOf(s.getStateID()))
    );
    return sj.toString();
  }

  public void writeDfaTable(Path file) throws IOException {
    StringBuilder strDFATable = new StringBuilder();

    // First line are input characters
    strDFATable.append("\t\t")
        .append(Joiner.on("\t\t").join(inputSet))
        .append("\n");

    // Now go through each state and record the transitions
    for (RegExpState state : dfaTable) {
      // Save the state id
      strDFATable.append(state.getStateID());
      // now write all transitions for each character
      for (char c : inputSet) {
        strDFATable.append("\t\t").append(getStateString(state.getTransition(c)));
      }
      strDFATable.append("\n");
    }

    // Save table to the file
    FileUtils.writeStringToFile(file.toFile(), strDFATable.toString());
  }

  public void writeNfaGraph(Path file) throws IOException {
    FileUtils.writeStringToFile(file.toFile(), toDOT(nfaTable));
  }

  public void writeDfaGraph(Path file) throws IOException {
    FileUtils.writeStringToFile(file.toFile(), toDOT(dfaTable));
  }

  /**
   * Return string in graph description language
   *
   * @return string in graph description language
   */
  private String toDOT(FsaTable table) {
    StringBuilder strDFAGraph = new StringBuilder("digraph{\n");

    // Final states are double circled
    table.stream()
        .filter(s -> s.isAcceptingState())
        .forEach(
            s -> strDFAGraph.append("\t")
                .append(s.getStateID())
                .append("\t[shape=doublecircle];\n")
        );

    strDFAGraph.append("\n");

    // Record transitions
    for (RegExpState s1 : table) {
      for (RegExpState s2 : s1.getTransition(epsilon)) {
        // Record transition
        strDFAGraph.append("\t")
            .append(s1.getStateID()).append(" -> ").append(s2.getStateID())
            .append("\t[label=\"epsilon\"];\n");
      }

      for (char c : inputSet) {
        for (RegExpState s2 : s1.getTransition(c)) {
          // Record transition
          strDFAGraph.append("\t")
              .append(s1.getStateID()).append(" -> ").append(s2.getStateID())
              .append("\t[label=\"").append(c).append("\"];\n");
        }
      }
    }

    strDFAGraph.append("}");
    return strDFAGraph.toString();
  }

  static class FsaTable extends LinkedList<RegExpState> {

  }

}
