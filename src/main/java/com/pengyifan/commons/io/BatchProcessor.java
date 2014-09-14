package com.pengyifan.commons.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

/**
 * 
 * If the path is a filtered file, then process the file. If the path is a
 * directory, then process all filtered files. Otherwise throw Exception
 * 
 * @author Yifan Peng
 * 
 */
public abstract class BatchProcessor {

  private final File path;
  private final FileFilter filter;
  private boolean debug;

  public BatchProcessor(File path, FileFilter filter) {
    this.path = path;
    this.filter = filter;
    debug = false;
  }

  public final void process()
      throws Exception {
    if (!path.exists()) {
      throw new IOException("Cannot find file/dir: " + path);
    } else if (path.isFile()) {
      // check if it is a filtered file
      Validate.isTrue(filter.accept(path),
          "Not a %s file: %s", filter, path);
      String basename = FilenameUtils.getBaseName(path.getAbsolutePath());
      File dir = new File(FilenameUtils.getFullPath(path.getAbsolutePath()));
      Validate.isTrue(dir.isDirectory());
      preprocess(dir);
      preprocessFile(dir, basename);
      processFile(dir, basename);
      postprocessFile(dir, basename);
      postprocess(dir);
    } else {
      File[] files = path.listFiles(filter);
      Arrays.sort(files);
      preprocess(path);
      for (File file : files) {
        String basename = FilenameUtils.getBaseName(file.getAbsolutePath());
        readResource(basename);
        preprocessFile(path, basename);
        processFile(path, basename);
        postprocessFile(path, basename);
      }
      postprocess(path);
    }
  }

  public void setDebugMode(boolean debug) {
    this.debug = debug;
  }

  public boolean isInDebugMode() {
    return debug;
  }

  /**
   * Pre-process and initiates the given directory.
   */
  protected void preprocess(File dir)
      throws Exception {
  }

  /**
   * Post-processes and completes the given directory.
   */
  protected void postprocess(File dir)
      throws Exception {
  }

  protected void processFile(File dir, String basename)
      throws Exception {
  }

  /**
   * read resources according to the basename
   * 
   * @deprecated replaced by {@link #preprocessFile(File,String)}.
   * @param basename
   * @throws IOException
   */
  @Deprecated
  protected void readResource(String basename)
      throws IOException {
  }

  protected void preprocessFile(File dir, String basename)
      throws Exception {
  }

  protected void postprocessFile(File dir, String basename)
      throws Exception {
  }
}
