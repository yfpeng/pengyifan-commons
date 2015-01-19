package com.pengyifan.commons.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * @deprecated Use {@code AbstractBatchProcessor} and {@code BasenameUtils}
 *             instead
 */
public class BatchTextProcessor extends BatchProcessor {

  public final static FileFilter TEXT_FILTER = FileFilterUtils
      .suffixFileFilter(".txt");

  public BatchTextProcessor(File path) {
    super(path, TEXT_FILTER);
  }

}
