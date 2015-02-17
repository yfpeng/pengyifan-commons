package com.pengyifan.nlp.trees;

import static com.google.common.base.Preconditions.checkNotNull;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;

public class GrammaticalStructureBuilder {

  private final GrammaticalStructureFactory grammaticalStructureFactory;
  
  public GrammaticalStructureBuilder(
      GrammaticalStructureFactory grammaticalStructureFactory) {
    this.grammaticalStructureFactory = grammaticalStructureFactory;
  }
  
  public GrammaticalStructure build(Tree tree) {
    checkNotNull(tree, "Tree is null");
    return grammaticalStructureFactory
        .newGrammaticalStructure(tree);
  }

}
