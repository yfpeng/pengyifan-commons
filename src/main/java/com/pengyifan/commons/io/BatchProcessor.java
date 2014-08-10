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

  private final String path;
  private final FileFilter filter;
  private boolean debug;

  public BatchProcessor(File path, FileFilter filter) {
    this(path.toString(), filter);
  }

  public BatchProcessor(String path, FileFilter filter) {
    this.path = path;
    this.filter = filter;
    debug = false;
  }

  public final void process()
      throws Exception {
    File pathFile = new File(path);
    if (!pathFile.exists()) {
      throw new IOException("Cannot find file/dir: " + pathFile);
    } else if (pathFile.isFile()) {
      // check if it is a filtered file
      Validate.isTrue(filter.accept(pathFile),
          "Not a %s file: %s", filter, pathFile);
      String basename = FilenameUtils.getBaseName(pathFile.getAbsolutePath());
      String dir = FilenameUtils.getFullPath(pathFile.getAbsolutePath());
      preprocess(dir);
      preprocessFile(dir, basename);
      processFile(dir, basename);
      postprocessFile(dir, basename);
      postprocess(dir);
    } else {
      File[] files = pathFile.listFiles(filter);
      Arrays.sort(files);
      String dir = FilenameUtils.getFullPath(pathFile.getAbsolutePath());
      preprocess(dir);
      for (File file : files) {
        String basename = FilenameUtils.getBaseName(file.getAbsolutePath());
        readResource(basename);
        preprocessFile(dir, basename);
        processFile(dir, basename);
        postprocessFile(dir, basename);
      }
      postprocess(dir);
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
  protected void preprocess(String dir)
      throws Exception {
  }

  /**
   * Post-processes and completes the given directory.
   */
  protected void postprocess(String dir)
      throws Exception {
  }

  protected void processFile(String dir, String basename)
      throws Exception {
  }

  /**
   * read resources according to the basename
   * 
   * @deprecated replaced by {@link #preprocessFile(String)}.
   * @param basename
   * @throws IOException
   */
  @Deprecated
  protected void readResource(String basename)
      throws IOException {
  }

  protected void preprocessFile(String dir, String basename)
      throws Exception {
  }

  protected void postprocessFile(String dir, String basename)
      throws Exception {
  }
}
