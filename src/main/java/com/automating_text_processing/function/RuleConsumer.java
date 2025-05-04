package com.automating_text_processing.function;

import com.automating_text_processing.rule.ReadableAndWritable;

import java.io.IOException;

@FunctionalInterface
public interface RuleConsumer<P extends ReadableAndWritable> {
  void accept(P rule) throws IOException;
}
