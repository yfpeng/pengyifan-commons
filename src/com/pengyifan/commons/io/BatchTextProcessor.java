package com.pengyifan.commons.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.FileFilterUtils;

public class BatchTextProcessor extends BatchProcessor {

  public final static FileFilter TEXT_FILTER = FileFilterUtils
                                                 .suffixFileFilter(".txt");

  public BatchTextProcessor(String path) {
    super(path, TEXT_FILTER);
  }

  public BatchTextProcessor(File path) {
    this(path.toString());
  }

}
