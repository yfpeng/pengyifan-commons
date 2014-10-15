package com.pengyifan.nlp.trees;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

public class DependencyBuilder {

  private final GrammaticalStructure grammaticalStructure;
  private final DependencyType dependencyType;

  public DependencyBuilder(
      final GrammaticalStructure grammaticalStructure,
      final DependencyType dependencyType) {
    checkNotNull(grammaticalStructure, "Must set the grammatical structure");
    checkNotNull(dependencyType, "Must set typed dependencies");
    this.grammaticalStructure = grammaticalStructure;
    this.dependencyType = dependencyType;
  }

  public Collection<TypedDependency> build() {
    switch (dependencyType) {
    case Basic:
      return grammaticalStructure.typedDependencies();
    case Collapsed:
      return grammaticalStructure.typedDependenciesCollapsed();
    case CCprocessed:
      return grammaticalStructure.typedDependenciesCCprocessed();
    case CollapsedTree:
      return grammaticalStructure.typedDependenciesCollapsedTree();
    case All:
      return grammaticalStructure.allTypedDependencies();
    default:
      throw new IllegalArgumentException("Unknown type: " + dependencyType);
    }
  }
}
