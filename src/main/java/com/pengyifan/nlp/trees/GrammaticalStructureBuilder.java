package com.pengyifan.nlp.trees;

import org.apache.commons.lang3.Validate;

import com.google.inject.Inject;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;

public class GrammaticalStructureBuilder {

  private final GrammaticalStructureFactory grammaticalStructureFactory;
  
  @Inject
  public GrammaticalStructureBuilder(
      GrammaticalStructureFactory grammaticalStructureFactory) {
    this.grammaticalStructureFactory = grammaticalStructureFactory;
  }
  
  public GrammaticalStructure build(Tree tree) {
    Validate.notNull(tree, "Tree is null");
    return grammaticalStructureFactory
        .newGrammaticalStructure(tree);
  }

}
