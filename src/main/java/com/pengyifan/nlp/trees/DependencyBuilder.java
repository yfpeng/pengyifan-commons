package com.pengyifan.nlp.trees;

import java.util.Collection;

import org.apache.commons.lang3.Validate;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

public class DependencyBuilder {

  private GrammaticalStructure gs;
  private DependencyType       type;

  private DependencyBuilder() {

  }

  public static DependencyBuilder newBuilder() {
    return new DependencyBuilder();
  }

  public DependencyBuilder setGrammaticalStructure(GrammaticalStructure gs) {
    this.gs = gs;
    return this;
  }

  public DependencyBuilder setDependencyType(DependencyType type) {
    this.type = type;
    return this;
  }

  public GrammaticalStructure getGrammaticalStructure() {
    return gs;
  }

  public DependencyType getDependencyType() {
    return type;
  }

  public Collection<TypedDependency> build() {

    Validate.notNull(gs, "Must set the grammatical structure");
    Validate.notNull(type, "Must set typed dependencies");

    Collection<TypedDependency> dependencies = null;
    switch (type) {
    case Basic:
      dependencies = gs.typedDependencies();
      break;
    case Collapsed:
      dependencies = gs.typedDependenciesCollapsed();
      break;
    case CCprocessed:
      dependencies = gs.typedDependenciesCCprocessed();
      break;
    case CollapsedTree:
      dependencies = gs.typedDependenciesCollapsedTree();
      break;
    case All:
      dependencies = gs.allTypedDependencies();
      break;
    default:
    }
    return dependencies;
  }
}
