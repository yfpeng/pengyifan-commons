package com.pengyifan.nlp.process;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import edu.stanford.nlp.process.Morphology;

/**
 * Extract lemma of the word.
 * <p>
 * Lemmatization usually refers to doing things properly with the use of a
 * vocabulary and morphological analysis of words, normally aiming to remove
 * inflectional endings only and to return the base or dictionary form of a
 * word, which is known as the lemma . If confronted with the token saw,
 * stemming might return just s, whereas lemmatization would attempt to return
 * either see or saw depending on whether the use of the token was as a verb or
 * a noun.
 */
public class ExtractLemma {

  private Morphology morphology;

  public ExtractLemma() {
    morphology = new Morphology();
  }

  /**
   * Returns the lemma of the word.
   * 
   * @param word the word
   * @return the lemma of the word
   */
  public String lemmatize(String word) {
    return morphology.stem(word);
  }
  
  /**
   * Returns the lemma of the word.
   * 
   * @param word the word
   * @param tag the part-of-speech of the word
   * @return the lemma of the word
   */
  public String lemmatize(String word, String tag) {
    return morphology.lemma(word, tag);
  }

  /**
   * Returns the lemma result of the sentence.
   * 
   * @param sentence the sentence
   * @return lemma of the sentence
   */
  public String lemmatizeSentence(String sentence) {
    List<String> words = Lists.newArrayList(sentence.split(" "));
    List<String> stems = Lists.newArrayList();
    for (String word : words) {
      stems.add(lemmatize(word));
    }
    return Joiner.on(' ').join(stems);
  }
}
