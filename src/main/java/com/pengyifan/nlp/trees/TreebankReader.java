package com.pengyifan.nlp.trees;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;

public class TreebankReader implements Closeable {

  private final TreeBuilder treeBuilder;
  private final File  file;

  public TreebankReader(File file, TreeBuilder treeBuilder)
      throws FileNotFoundException {
    this.treeBuilder = treeBuilder;
    this.file = file;
  }

  public MemoryTreebank read()
      throws IOException {
    MemoryTreebank treebank = new MemoryTreebank();
    for(String line: FileUtils.readLines(file)) {
      Tree tree = treeBuilder.build(line);
      treebank.add(tree);
    }
    return treebank;
  }

  @Override
  public void close()
      throws IOException {
  }
}
