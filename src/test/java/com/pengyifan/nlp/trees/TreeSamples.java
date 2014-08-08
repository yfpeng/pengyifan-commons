package com.pengyifan.nlp.trees;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;

public class TreeSamples {

  public static final String TREE_STR1 =
      "(S1 (S (S (NP (NN BMP-6_1755_1760)) (VP (MD can_1761_1764) "
          + "(VP (VB signal_1765_1771) (PP (IN through_1772_1779) "
          + "(NP (NP (DT the_1780_1783) (NN ligation_1784_1792)) "
          + "(PP (IN of_1793_1795) (NP (NP (NP (DT the_1796_1799) "
          + "(NN type_1800_1804) (NN I_1805_1806) (NNS receptors_1807_1816)) "
          + "(NP (NP (NN Act-RIA_1817_1824)) (, ,_1824_1825) "
          + "(NP (NN BMP-RIA_1826_1833)) (, ,_1833_1834) (CC and_1835_1838) "
          + "(NP (NN BMP-RIB_1839_1846)))) (CC and_1847_1850) "
          + "(NP (NP (DT the_1851_1854) (NP (NN type_1855_1859) (CD II_1860_1862)) "
          + "(NNS receptors_1863_1872)) (NP (NP (NN BMP-RII_1873_1880)) "
          + "(, ,_1880_1881) (NP (NN Act-RIIA_1882_1890)) (CC and_1891_1894) "
          + "(NP (NN Act-RIIB_1895_1903)))))) (, ,_1903_1904) (SBAR (WHNP "
          + "(WDT which_1905_1910)) (S (VP (VBP lead_1911_1915) "
          + "(PP (TO to_1916_1918) (NP (NP (DT the_1919_1922) "
          + "(NN phosphorylation_1923_1938)) (PP (IN of_1939_1941) "
          + "(NP (NP (DT the_1942_1945) (NN receptor_1946_1954) "
          + "(NN Smads_1955_1960)) (PRN (-LRB- -LRB-_1961_1962) "
          + "(NP (NP (NN Smad-1_1962_1968)) (, ,_1968_1969) "
          + "(NP (NN Smad-5_1970_1976)) (, ,_1976_1977) "
          + "(CC and_1978_1981) (NP (NN Smad-8_1982_1988))) "
          + "(-RRB- -RRB-_1988_1989)))))))))))))) (. ._1989_1990)))";

  public static final String TREE_STR2 =
      "(S (NP (NN He_0_2)) (VP (VBZ eats_3_7) (NP (NN fiber_8_13))))";

  private static TreeBuilder treeBuilder;
  private static GrammaticalStructureBuilder gsBuilder;

  static {
    Injector injector = Guice.createInjector(new TreeTestModule());
    gsBuilder = injector
        .getInstance(GrammaticalStructureBuilder.class);
    treeBuilder = injector.getInstance(TreeBuilder.class);
  }

  public static Tree getTree(String treeStr) {
    return treeBuilder.setTreeString(treeStr).build();
  }

  public static GrammaticalStructure getGrammaticalStructure(Tree tree) {
    return gsBuilder.setTree(tree).build();
  }

  public static GrammaticalStructure getGrammaticalStructure(String treeStr) {
    return getGrammaticalStructure(getTree(treeStr));
  }
}
