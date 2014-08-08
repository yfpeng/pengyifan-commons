package com.pengyifan.nlp.trees;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;

public class TreebankReader implements Closeable {

  private final static Logger LOGGER = Logger.getAnonymousLogger();
  static {
    LOGGER.setLevel(Level.SEVERE);
    ConsoleHandler handler = new ConsoleHandler();
    LOGGER.addHandler(handler);
  }

  private final TreeBuilder treeBuilder;
  private final LineNumberReader reader;

  public TreebankReader(File file, TreeBuilder treeBuilder)
      throws FileNotFoundException {
    this.treeBuilder = treeBuilder;
    this.reader = new LineNumberReader(new FileReader(file));
  }

  public MemoryTreebank read()
      throws IOException {
    MemoryTreebank treebank = new MemoryTreebank();
    String line = null;
    while ((line=reader.readLine()) != null) {
      Tree tree = treeBuilder.setTreeString(line).build();
      treebank.add(tree);
    }
    return treebank;
  }

  @Override
  public void close()
      throws IOException {
    reader.close();
  }
}
