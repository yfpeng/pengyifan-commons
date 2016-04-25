package com.pengyifan.util.regex;

// Objects of this class convert infix expressions to postfix expressions.
// The possible operators are +, -, *, /, and ^.  The operands are floating-
// point constants.  No parentheses are allowed.
//
// This class was adapted from class PostfixInterpreter in the Standish text.

import com.google.common.collect.Lists;

import java.util.*;

/**
 * Objects of this class convert infix expressions to postfix expressions.
 * The possible operators are *, | and "contact".  The operands are floating-
 * point constants.  No parentheses are allowed.
 * <p>
 * This class was adapted from class PostfixInterpreter in the Standish text.
 */
class RegInfixToPostfix {

  public final static char CONTACT_CHAR = 8;

  /**
   * Checks if the specific character is input character
   *
   * @param ch input character
   * @return true if the input character is an input character
   */
  public static boolean isOperator(char ch) {
    return ch == '*' || ch == '|' || ch == '(' || ch == ')' || ch == CONTACT_CHAR;
  }

  /**
   * Tell whether op1 has lower precedence than op2, where op1 is an
   * operator on the left and op2 is an operator on the right.
   * op1 and op2 are assumed to be operator characters (( > ) > * > contact > |).
   *
   * @param op1 operator 1
   * @param op2 operator 2
   * @return true is opt1 < opt2
   */
  private boolean lowerPrecedence(char op1, char op2) {
    switch (op1) {
    case '|':
      return !(op2 == '|');
    case CONTACT_CHAR:
      return op2 == '*' || op2 == '(';
    case '*':
      return op2 == '(';
    case '(':
      return true;
    default:
      return false;
    }
  }

  /**
   * Inserts CONTACT_CHAR where the concatenation needs to occur. The method used to parse regular
   * expression here is similar to method of evaluating the arithmetic expressions. A difference
   * here is that each arithmetic expression is denoted by a sign. In regular expressions the
   * concatenation is not denoted by ny sign so I will detect concatenation and add a character
   * CONTACT_CHAR between operands.
   *
   * @return normalized patterns
   */
  private String normalize(String pattern) {
    StringBuilder strRes = new StringBuilder();

    for (int i = 0; i < pattern.length() - 1; ++i) {
      char cLeft = pattern.charAt(i);
      char cRight = pattern.charAt(i + 1);
      strRes.append(cLeft);
      if (!isOperator(cLeft) || cLeft == ')' || cLeft == '*') {
        if (!isOperator(cRight) || cRight == '(') {
          strRes.append(CONTACT_CHAR);
        }
      }
    }
    strRes.append(pattern.charAt(pattern.length() - 1));
    return strRes.toString();
  }

  /**
   * Method to convert infix to postfix:
   *
   * @param infix infix string
   * @return a postfix representation of the expression in infix.
   */

  public List<Character> convertToPostfix(String infix) {

    infix = normalize(infix);

    // the stack of operators
    Stack<Character> operatorStack = new Stack<>();
    List<Character> postfix = Lists.newArrayList();

    // Process the tokens.
    for (char c : infix.toCharArray()) {
      // if token is an operator
      if (isOperator(c)) {
        while (!operatorStack.empty() && !lowerPrecedence(operatorStack.peek(), c))
          // (Operator on the stack does not have lower precedence, so
          //  it goes before this one.)
          postfix.add(operatorStack.pop());

        if (c == ')') {
          // Output the remaining operators in the parenthesized part.
          char operator = operatorStack.pop();
          while (operator != '(') {
            postfix.add(operator);
            operator = operatorStack.pop();
          }
        } else {
          operatorStack.push(c);// Push this operator onto the stack.
        }
      } else {  // (it is an operand)
        postfix.add(c);  // output the operand
      }

    }

    // Output the remaining operators on the stack.
    while (!operatorStack.empty()) {
      postfix.add(operatorStack.pop());
    }

    return postfix;
  }
}



