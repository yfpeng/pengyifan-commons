package com.pengyifan.commons.chain;

/**
 * <p>A {@link Handler} encapsulates a unit of processing work to be
 * performed, whose purpose is to examine and/or modify the state of a
 * transaction that is represented by a {@link Context}.  Individual
 * {@link Handler}s can be assembled into a {@link Chain}, which allows
 * them to either complete the required processing or delegate further
 * processing to the next {@link Handler} in the {@link Chain}.</p>
 */
public abstract class Handler {

  private Handler successor;
  
  public void setSuccessor(Handler handler) {
    successor = handler;
  }

  public void execute(Context context) throws Exception {
    executeThis(context);
    if (successor != null) {
      successor.execute(context);
    }
  }
  
  public abstract void executeThis(Context context) throws Exception;
}
