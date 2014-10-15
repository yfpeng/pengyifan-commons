package com.pengyifan.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

public class FileUtils2 {

  private static final FileFilter ACCEPT_ALL = new FileFilter() {

    @Override
    public boolean accept(File pathname) {
      return true;
    }
  };

  public static void mergeFile(File destination, List<File> sources)
      throws IOException {
    mergeFile(destination, sources, ACCEPT_ALL);
  }

  public static void mergeFile(File destination,
      File sourcesDir,
      FileFilter filter)
      throws IOException {
    Validate.isTrue(sourcesDir.isDirectory());
    mergeFile(destination, Arrays.asList(sourcesDir.listFiles()), filter);
  }

  public static void mergeFile(File destination, File sourcesDir)
      throws IOException {
    Validate.isTrue(sourcesDir.isDirectory());
    mergeFile(destination, Arrays.asList(sourcesDir.listFiles()), ACCEPT_ALL);
  }

  public static void mergeFile(File destination, List<File> sources,
      FileFilter filter)
      throws IOException {
    OutputStream output = null;
    try {
      output = createAppendableStream(destination);
      for (File source : sources) {
        if (filter.accept(source)) {
          appendFile(output, source);
        }
      }
    } finally {
      IOUtils.closeQuietly(output);
    }
  }

  private static BufferedOutputStream createAppendableStream(File destination)
      throws FileNotFoundException {
    return new BufferedOutputStream(new FileOutputStream(destination, true));
  }

  private static void appendFile(OutputStream output, File source)
      throws IOException {
    InputStream input = null;
    try {
      input = new BufferedInputStream(new FileInputStream(source));
      IOUtils.copy(input, output);
    } finally {
      IOUtils.closeQuietly(input);
    }
  }
}
