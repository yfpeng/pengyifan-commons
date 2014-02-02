package com.pengyifan.commons.lang;

import org.apache.commons.lang3.Validate;

public class Validate2 {

  public Validate2() {
    super();
  }

  /**
   * Validate that the specified element is within the array; otherwise
   * throwing an exception.
   * 
   * If the array is null, then the message in the exception is
   * "The validated object is null".
   * 
   * @param <T> the array type
   * @param array the array to check, validated not null by this method
   * @param element
   * @param message the
   *          java.lang.String.format(java.lang.String,java.lang.Object[])
   *          exception message if invalid, not null
   * @param values the optional values for the formatted exception message,
   *          null array not recommended
   * @return the validated array (never null method for chaining)
   * 
   * @throws java.lang.NullPointerException if the array is null
   * @throws java.lang.IllegalArgumentException if an element is null
   * @throws java.lang.IllegalArgumentException if an element is not within the array
   */
  public static <T> T[] validElement(T[] array, T element, String message,
      Object... values) {
    Validate.notNull(array);
    Validate.notNull(element);
    for (T t : array) {
      if (t.equals(element)) {
        return array;
      }
    }
    throw new IllegalArgumentException(String.format(message, values));
  }
}
