package com.pengyifan.commons.io;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Lists;

public final class BasenameUtils {

  private BasenameUtils() {
  }

  public static List<String> getBasenames(Path path,
      DirectoryStream.Filter<Path> filter)
      throws IOException {
    List<String> list = Lists.newArrayList();
    if (Files.isDirectory(path)) {
      DirectoryStream<Path> stream = Files.newDirectoryStream(path, filter);
      for(Path entry: stream) {
        list.add(FilenameUtils.getBaseName(entry.getFileName().toString()));
      }
    } else if (filter.accept(path)) {
      list.add(FilenameUtils.getBaseName(path.getFileName().toString()));
    }
    Collections.sort(list);
    return list;
  }

  public static List<String> getTextBasenames(Path path)
      throws IOException {
    return getBasenames(path, "*.txt");
  }

  public static List<String> getBasenames(Path path, String glob)
      throws IOException {
    List<String> list = Lists.newArrayList();
    if (Files.isDirectory(path)) {
      DirectoryStream<Path> stream = Files.newDirectoryStream(path, glob);
      for(Path entry: stream) {
        list.add(FilenameUtils.getBaseName(entry.getFileName().toString()));
      }
    } else if (glob.equals("*")) {
      list.add(FilenameUtils.getBaseName(path.getFileName().toString()));
    } else {
      FileSystem fs = path.getFileSystem();
      final PathMatcher matcher = fs.getPathMatcher("glob:" + glob);
      if (matcher.matches(path.getFileName())) {
        list.add(FilenameUtils.getBaseName(path.getFileName().toString()));
      }
    }
    Collections.sort(list);
    return list;
  }
}
