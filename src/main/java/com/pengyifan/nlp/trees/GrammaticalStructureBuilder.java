package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import com.google.inject.Inject;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;

public class GrammaticalStructureBuilder {

  private GrammaticalStructureFactory grammaticalStructureFactory;
  private Tree tree;
  
  @Inject
  public GrammaticalStructureBuilder(
      GrammaticalStructureFactory grammaticalStructureFactory) {
    this.grammaticalStructureFactory = grammaticalStructureFactory;
  }
  
  public GrammaticalStructureBuilder setTree(Tree tree) {
    Validate.notNull(tree);
    this.tree = tree;
    return this;
  }

  public GrammaticalStructure build() {
    checkArguments();
    return grammaticalStructureFactory
        .newGrammaticalStructure(tree);
  }

  private void checkArguments() {
    Validate.notNull(tree);
  }
}
