package com.automating_text_processing.util;

import java.util.Objects;

public class WebUtils {

  public static String escaped(String raw) {
    if (Objects.isNull(raw)) return "";
    return raw.replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace("'", "&#39;")
      .replace("\n", "<br>")
      .replace("\r", "")
      .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;")
      .replace(" |\u00A0", "&nbsp;");
  }
}
