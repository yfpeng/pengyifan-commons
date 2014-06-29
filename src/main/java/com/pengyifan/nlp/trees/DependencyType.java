package com.pengyifan.nlp.trees;

public enum DependencyType {
  /**
   * Returns the typed dependencies of this grammatical structure. These are
   * the basic word-level typed dependencies, where each word is dependent on
   * one other thing, either a word or the starting ROOT, and the dependencies
   * have a tree structure. This corresponds to the command-line option
   * "basicDependencies".
   */
  Basic,
  /**
   * Get a list of the typed dependencies, including extras like control
   * dependencies, collapsing them and distributing relations across
   * coordination. This method is generally recommended for best representing
   * the semantic and syntactic relations of a sentence. In general it returns
   * a directed graph (i.e., the output may not be a tree and it may contain
   * (small) cycles).
   */
  CCprocessed,
  /**
   * Returns all the typed dependencies of this grammatical structure. These
   * are like the basic (uncollapsed) dependencies, but may include extra arcs
   * for control relationships, etc. This corresponds to the "nonCollapsed"
   * option.
   */
  All,
  /**
   * Get the typed dependencies after collapsing them. Collapsing dependencies
   * refers to turning certain function words such as prepositions and
   * conjunctions into arcs, so they disappear from the set of nodes. There is
   * no guarantee that the dependencies are a tree. While the dependencies are
   * normally tree-like, the collapsing may introduce not only re-entrancies
   * but even small cycles.
   */
  Collapsed,
  /**
   * Get the typed dependencies after mostly collapsing them, but keep a tree
   * structure. In order to do this, the code does:
   * 
   * no relative clause processing
   * 
   * no xsubj relations
   * 
   * no propagation of conjuncts
   * 
   * This corresponds to the "tree" option.
   */
  CollapsedTree,
}