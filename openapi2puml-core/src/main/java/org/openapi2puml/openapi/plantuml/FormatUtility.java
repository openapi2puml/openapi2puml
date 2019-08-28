package org.openapi2puml.openapi.plantuml;

public class FormatUtility {

  /**
   * Converts input String to title case, i.e. if there are separate words,
   * each word is capitalized and the spaces removed.
   * @param input string to be title-cased
   * @return string in title case
   */
  public static String toTitleCase(String input) {
    StringBuilder titleCase = new StringBuilder();
    boolean nextTitleCase = true;

    for (char c : input.toCharArray()) {
      if (Character.isSpaceChar(c)) {
        nextTitleCase = true;
      } else if (nextTitleCase) {
        c = Character.toTitleCase(c);
        nextTitleCase = false;
      }

      titleCase.append(c);
    }

    return titleCase.toString();
  }
}
