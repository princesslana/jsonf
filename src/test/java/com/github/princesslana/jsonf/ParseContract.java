package com.github.princesslana.jsonf;

import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface ParseContract extends WithJsonFParser {

  @Property
  default void parse_whenNotValidJson_shouldThrowException(@ForAll String s) {
    Assertions.assertThatThrownBy(() -> parse(s)).isInstanceOf(JsonFException.class);
  }

  @Example
  default void parse_whenNull_shouldThrowException() {
    Assertions.assertThatThrownBy(() -> parse(null)).isInstanceOf(JsonFException.class);
  }
}
