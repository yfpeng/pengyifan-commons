package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import com.google.inject.Inject;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeTransformer;

public class TreeBuilder {

  private final TreeTransformer treeFransformer;
  private String treeStr;

  @Inject
  public TreeBuilder(TreeTransformer treeFransformer) {
    this.treeFransformer = treeFransformer;
  }
  
  public TreeBuilder setTreeString(String treeStr) {
    Validate.isTrue(treeStr != null && !treeStr.isEmpty());
    this.treeStr = treeStr;
    return this;
  }

  public Tree build() {
    checkArguments();
    return Tree
        .valueOf(treeStr)
        .transform(treeFransformer);
  }

  private void checkArguments() {
    Validate.isTrue(treeStr != null && !treeStr.isEmpty());
  }
}
