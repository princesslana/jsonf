package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.ParseException;
import net.jqwik.api.Assume;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface ParseContract extends WithJsonFParser {

  @Property
  default void parse_whenNotValidJson_shouldThrowException(@ForAll String s) {
    Assume.that(!isValidJson(s));
    Assertions.assertThatThrownBy(() -> parse(s)).isInstanceOf(JsonFException.class);
  }

  @Example
  default void parse_whenNull_shouldThrowException() {
    Assertions.assertThatThrownBy(() -> parse(null)).isInstanceOf(JsonFException.class);
  }

  static boolean isValidJson(String s) {
    try {
      Json.parse(s);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }
}
