package com.automating_text_processing.service;

import com.automating_text_processing.DTO.EditorFindRule;
import com.automating_text_processing.util.WebUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class TextEditorProcessingService extends TextProcessingCoreService {
  private final AtomicInteger currentVersion = new AtomicInteger(0);

  public CompletableFuture<String> replace(EditorFindRule rule) {
    int version = newVersion();
    String original = rule.getReader().get();

    return CompletableFuture.supplyAsync(() -> {
      if (version != currentVersion.get()) return null; // Cancelled
      return original.replaceAll(
        "(?i)" + Pattern.quote(WebUtils.escaped(rule.getTarget())),
        WebUtils.escaped(rule.getReplacement()));
    });
  }

  public CompletableFuture<String> highlight(EditorFindRule rule) {
    int version = newVersion();
    String original = rule.getReader().get();

    return CompletableFuture.supplyAsync(() -> {
      if (version != currentVersion.get()) return null;
      if (rule.getTarget().isEmpty()) return original;
      return original.replaceAll(
        "(?i)" + Pattern.quote(WebUtils.escaped(rule.getTarget())),
        "<span class=\"highlight\">$0</span>");
    });
  }

  private int newVersion() {
    return currentVersion.incrementAndGet();
  }
}
