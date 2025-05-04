package com.automating_text_processing.DTO;

import lombok.Getter;

public enum SearchPatterns {
  EMAIL("\\b(?!.*[.]{2})[a-zA-Z0-9](?:[a-zA-Z0-9._-]{0,62}[a-zA-Z0-9])?@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z]{2,})+\\b"),
  PHONE("\\b\\+?[0-9]{1,4}?[-.\\s]?(\\(?\\d{1,4}\\)?)[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}\\b"),
  IP_ADDRESS(
    "\\b(" +
    "(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1?[0-9]{1,2})){3}" + // IPv4
    "|" +
    "([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}" + // Full IPv6
    "|" +
    "([0-9a-fA-F]{1,4}:){1,7}:" + // IPv6 ending in ::
    "|" +
    ":([0-9a-fA-F]{1,4}:){1,7}" + // IPv6 starting with ::
    "|" +
    "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}" + // IPv6 compressed in the middle
    ")\\b"),
  DATE("\\b(\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])|\\d{2}[/-]\\d{2}[/-]\\d{4})\\b"),
  TIME("\\b([01]?[0-9]|2[0-3]):([0-5]?[0-9])(:[0-5]?[0-9])?(\\s*(AM|PM))?\\b"),
  WEEK_DAYS("\\b(monday|tuesday|wednesday|thursday|friday|saturday|sunday)\\b");

  @Getter private final String regex;

  SearchPatterns(String regex) {
    this.regex = regex;
  }

  @Override
  public String toString() { return getRegex(); }
}
