package com.pengyifan.nlp.trees;

import com.google.inject.AbstractModule;
import com.pengyifan.nlp.trees.OffsetTreeTransformer;

import edu.stanford.nlp.trees.EnglishGrammaticalStructureFactory;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.TreeTransformer;

public class TreeTestModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(GrammaticalStructureFactory.class).to(
        EnglishGrammaticalStructureFactory.class);
    bind(TreeTransformer.class).to(OffsetTreeTransformer.class);
  }
}
