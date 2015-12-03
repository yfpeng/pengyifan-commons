package com.pengyifan.nlp.process;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The ExtractAbbrev class implements a simple algorithm for extraction of
 * abbreviations and their definitions from biomedical text. Abbreviations
 * (short forms) are extracted from the input file, and those abbreviations for
 * which a definition (long form) is found are printed out, along with that
 * definition, one per line.
 *
 * A file consisting of short-form/long-form pairs (tab separated) can be
 * specified in tandem with the -testlist option for the purposes of evaluating
 * the algorithm.
 *
 * @see <a href="http://biotext.berkeley.edu/papers/psb03.pdf">A Simple
 *      Algorithm for Identifying Abbreviation Definitions in Biomedical
 *      Text</a> A.S. Schwartz, M.A. Hearst; Pacific Symposium on Biocomputing
 *      8:451-462(2003) for a detailed description of the algorithm.
 *
 * @author Ariel Schwartz
 * @version 03/12/03
 * @author Yifan Peng
 * @version 12/07/2014
 */
public class ExtractAbbrev {

  public ExtractAbbrev() {
  }

  private boolean isValidShortForm(String str) {
    return hasLetter(str)
        && (Character.isLetterOrDigit(str.charAt(0)) || (str.charAt(0) == '('));
  }

  private boolean hasLetter(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetter(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  private boolean hasCapital(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (Character.isUpperCase(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<AbbrInfo> extractAbbrPairsString(String str) {

    ArrayList<AbbrInfo> candidates = new ArrayList<AbbrInfo>();

    String tmpStr;
    String longForm = "";
    String shortForm = "";
    int newCloseParenIndex;
    int tmpIndex = -1;
    int longFormIndex = 0;
    int shortFormIndex = 0;

    if (str.length() == 0) {
      return candidates;
    }

    str += " ";
    String currSentence = str;
    String sentence = str;
    int openParenIndex = currSentence.indexOf(" (");
    int closeParenIndex = -1;

    do {
      if (openParenIndex > -1) {
        openParenIndex++;
      }
      int sentenceEnd = Math.max(
          currSentence.lastIndexOf(". "),
          currSentence.lastIndexOf(", "));
      if ((openParenIndex == -1) && (sentenceEnd == -1)) {
        ; // Do nothing
      } else if (openParenIndex == -1) {
        currSentence = currSentence.substring(sentenceEnd + 2);
      } else if ((closeParenIndex = currSentence.indexOf(')', openParenIndex)) > -1) {
        sentenceEnd = Math.max(
            currSentence.lastIndexOf(". ", openParenIndex),
            currSentence.lastIndexOf(", ", openParenIndex));
        if (sentenceEnd == -1) {
          sentenceEnd = -2;
        }
        longForm = currSentence.substring(sentenceEnd + 2, openParenIndex);
        shortForm = currSentence.substring(openParenIndex + 1, closeParenIndex);
        shortFormIndex = sentence.indexOf(currSentence) + openParenIndex + 1;
      }
      if (shortForm.length() > 0 || longForm.length() > 0) {
        if (shortForm.length() > 1 && longForm.length() > 1) {
          if ((shortForm.indexOf('(') > -1)
              && ((newCloseParenIndex = currSentence.indexOf(
                  ')',
                  closeParenIndex + 1)) > -1)) {
            shortForm = currSentence.substring(
                openParenIndex + 1, newCloseParenIndex);
            shortFormIndex = sentence.indexOf(currSentence)
                + openParenIndex + 1;
            closeParenIndex = newCloseParenIndex;
          }
          shortFormIndex = sentence.indexOf(currSentence) + openParenIndex + 1;
          if ((tmpIndex = shortForm.indexOf(", ")) > -1) {
            shortForm = shortForm.substring(0, tmpIndex);
          }
          if ((tmpIndex = shortForm.indexOf("; ")) > -1) {
            shortForm = shortForm.substring(0, tmpIndex);
          }
          StringTokenizer shortTokenizer = new StringTokenizer(shortForm);
          if (shortTokenizer.countTokens() > 2
              || shortForm.length() > longForm.length()) {
            // Long form in ( )
            tmpIndex = currSentence.lastIndexOf(" ", openParenIndex - 2);
            tmpStr = currSentence.substring(tmpIndex + 1, openParenIndex - 1);
            longForm = shortForm;
            shortForm = tmpStr;
            if (!hasCapital(shortForm)) {
              shortForm = "";
            }
          }
          if (isValidShortForm(shortForm)) {
            tmpStr = extractAbbrPair(shortForm.trim(), longForm.trim());
            if (tmpStr != null) {
              longFormIndex = sentence.lastIndexOf(tmpStr, shortFormIndex);
              AbbrInfo info = new AbbrInfo(shortForm, shortFormIndex, tmpStr,
                  longFormIndex);
              candidates.add(info);
            }
          }
        }
        currSentence = currSentence.substring(closeParenIndex + 1);
      } else if (openParenIndex > -1) {
        if ((currSentence.length() - openParenIndex) > 200) {
          // Matching close paren was not found
          currSentence = currSentence.substring(openParenIndex + 1);
        }
        return candidates; // Read next line
      }
      shortForm = "";
      longForm = "";
    } while ((openParenIndex = currSentence.indexOf(" (")) > -1);

    return candidates;
  }

  private String findBestLongForm(String shortForm, String longForm) {
    int sIndex = shortForm.length() - 1;
    int lIndex = longForm.length() - 1;
    for (; sIndex >= 0; sIndex--) {
      char currChar = Character.toLowerCase(shortForm.charAt(sIndex));
      if (!Character.isLetterOrDigit(currChar)) {
        continue;
      }
      while ((lIndex >= 0
          && Character.toLowerCase(longForm.charAt(lIndex)) != currChar)
          || (sIndex == 0 && lIndex > 0
          && Character.isLetterOrDigit(longForm.charAt(lIndex - 1)))) {
        lIndex--;
      }
      if (lIndex < 0) {
        return null;
      }
      lIndex--;
    }
    lIndex = longForm.lastIndexOf(" ", lIndex) + 1;
    return longForm.substring(lIndex);
  }

  private String extractAbbrPair(String shortForm, String longForm) {
    if (shortForm.length() == 1) {
      return null;
    }
    String bestLongForm = findBestLongForm(shortForm, longForm);
    if (bestLongForm == null) {
      return null;
    }
    StringTokenizer tokenizer = new StringTokenizer(bestLongForm, " \t\n\r\f-");
    int longFormSize = tokenizer.countTokens();
    int shortFormSize = shortForm.length();
    for (int i = shortFormSize - 1; i >= 0; i--) {
      if (!Character.isLetterOrDigit(shortForm.charAt(i))) {
        shortFormSize--;
      }
    }
    if (bestLongForm.length() < shortForm.length()
        || bestLongForm.indexOf(shortForm + " ") > -1
        || bestLongForm.endsWith(shortForm)
        || longFormSize > shortFormSize * 2
        || longFormSize > shortFormSize + 5
        || shortFormSize > 10) {
      return null;
    }

    return bestLongForm;
  }

  public static class AbbrInfo {

    public final String shortForm;
    public final String longForm;
    public final int shortFormIndex;
    public final int longFormIndex;

    public AbbrInfo(
        String inShortForm,
        int inShortFormIndex,
        String inLongForm,
        int inLongFormIndex) {
      shortForm = inShortForm;
      shortFormIndex = inShortFormIndex;
      longForm = inLongForm;
      longFormIndex = inLongFormIndex;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
          .append("shortForm", shortForm)
          .append("longForm", longForm)
          .append("shortFormIndex", shortFormIndex)
          .append("longFormIndex", longFormIndex)
          .toString();
    }
  }
}
