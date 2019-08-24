package com.kicksolutions.swagger.plantuml;

public class FormatUtility {


  /**
   * Converts input String to title case, i.e. if there are separate words,
   * each word is capitalized and the spaces removed.
   *  TODO - check if the toTitleCase is required for Swagger...
   * @param input
   * @return
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
