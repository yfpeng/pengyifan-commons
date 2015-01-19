package com.pengyifan.commons.io;

import java.util.Collection;

public abstract class AbstractBatchProcessor {

  private final Collection<String> basenames;

  public AbstractBatchProcessor(Collection<String> basenames) {
    this.basenames = basenames;
  }

  public final void process()
      throws Exception {
    preprocess();
    for (String basename : basenames) {
      preprocessFile(basename);
      processFile(basename);
      postprocessFile(basename);
    }
    postprocess();
  }

  protected void preprocess()
      throws Exception {
  }

  protected void postprocess()
      throws Exception {
  }

  protected void processFile(String basename)
      throws Exception {
  }

  protected void preprocessFile(String basename)
      throws Exception {
  }

  protected void postprocessFile(String basename)
      throws Exception {
  }
}
