package de.zolitas.waystoned.client;

import java.util.Locale;

public class ClientUtils {
  public static String prettifySnakeCase(String pSnakeCaseString) {
    String[] parts = pSnakeCaseString.split("_");

    for (int i = 0; i < parts.length; i++) {
      String part = parts[i];
      part = part.toLowerCase(Locale.ENGLISH);

      char[] chars = part.toCharArray();
      chars[0] = Character.toTitleCase(chars[0]);
      part = String.valueOf(chars);
      parts[i] = part;
    }

    return String.join(" ", parts);
  }
}
