package com.pengyifan.nlp.ling.noun;

import java.util.regex.Pattern;

public class FTerm {

  public static final String fterm[] = { "protein", "factor",
      "receptor", "enzyme", "anti-?gen", "anti-?body", "gene", "filament",
      "variant", "hormone", "transcript", "chromosome", "genome", "allele",
      "lysate", "oncogene", "channel", "extract", "peptide", "subunit",
      "promoter", "domain", "motif", "site", "sequence", "region", "fragment",
      "filament", "portion", "island", "locus", "enhancer", "tail", "repeat",
      "terminus", "nucleotide", "strand", "residue", "segment", "chain", "box",
      "codon", "reporter", "loop", "amino-?acid", "carbohydrate", "lipid",
      "steroid", "compound", "drug", "chemical", "ionophore", "agonist",
      "antagonist", "radical", "moiety", "unit", "group", "structure",
      "complex", "molecule", "element", "mutant", "family", "construct",
      "adduct", "precursor", "mitogen", "substrate", "ligand", "analogue",
      "analog", "isomer", "isoform", "product", "metabolite", "regulator",
      "derivative", "residue", "inhibitor", "activator", "transactivator",
      "member", "regulator", "mutation", "component", "homologue", "kinase",
      "partner", "acid", "mediator", "marker", "syndrome", "disorder",
      "process", "cell", "line" };

  public static final String ftermForApposition[] = { "protein", "factor",
      "receptor", "enzyme", "anti-?gen", "anti-?body", "gene", "filament",
      "variant", "hormone", "transcript", "chromosome", "genome", "allele",
      "lysate", "oncogene", "channel", "extract", "peptide", "subunit",
      "promoter", "domain", "motif", "site", "sequence", "region", "fragment",
      "filament", "portion", "island", "locus", "enhancer", "tail", "repeat",
      "terminus", "nucleotide", "strand", "residue", "segment", "chain", "box",
      "codon", "reporter", "loop", "amino-?acid", "carbohydrate", "lipid",
      "steroid", "compound", "drug", "chemical", "ionophore", "agonist",
      "antagonist", "unit", "group", "structure", "complex", "molecule",
      "element", "family", "construct", "adduct", "precursor", "mitogen",
      "substrate", "ligand", "analogue", "analog", "isomer", "isoform",
      "product", "metabolite", "regulator", "derivative", "residue",
      "inhibitor", "activator", "transactivator", "member", "regulator",
      "component", "homologue", "kinase", "partner", "acid", "mediator",
      "marker", "line" };

  private static Pattern pattern;

  static {
    String fterms = "";
    for (String s : FTerm.ftermForApposition) {
      fterms += s + "|";
    }
    fterms = fterms.substring(0, fterms.length() - 1);
    FTerm.pattern = Pattern.compile("m/^[a-z]*(" + fterms + ")s?$/");
  }

  public static boolean isFterm(String keyword) {
    return FTerm.pattern.matcher(keyword).find();
  }
}
