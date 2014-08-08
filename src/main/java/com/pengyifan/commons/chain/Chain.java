package com.pengyifan.commons.chain;

import org.apache.commons.lang3.Validate;

import com.google.common.annotations.VisibleForTesting;

public class Chain {
  @VisibleForTesting
  Handler headHandler;
  @VisibleForTesting
  Handler tailHandler;

  /**
   * Appends the handler to the end of this list.
   */
  public Chain addHandler(Handler handler) {
    if (tailHandler != null) {
      tailHandler.setSuccessor(handler);
    }
    tailHandler = handler;
    if (headHandler == null) {
      headHandler = handler;
    }
    return this;
  }
  
  public void execute(Context context) throws Exception {
    Validate.notNull(headHandler, "No handler is assigned.");
    Validate.notNull(tailHandler, "No handler is assigned.");
    headHandler.execute(context);
  }
}
