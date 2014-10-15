package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import com.google.inject.Inject;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeTransformer;

public class TreeBuilder {

  private final TreeTransformer treeFransformer;

  @Inject
  public TreeBuilder(TreeTransformer treeFransformer) {
    this.treeFransformer = treeFransformer;
  }
  
  public Tree build(String treeString) {
    Validate.isTrue(treeString != null && !treeString.isEmpty());
    return Tree
        .valueOf(treeString)
        .transform(treeFransformer);
  }

}
