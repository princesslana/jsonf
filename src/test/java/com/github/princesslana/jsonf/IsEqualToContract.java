package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface IsEqualToContract extends WithJsonFParser {

  @Property
  default void isEqualTo_whenJsonLong_shouldFetch(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).isEqualTo(l)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonDouble_shouldFetch(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).isEqualTo(d)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonString_shouldFetchEmpty(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).isEqualTo(s)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonBoolean_shouldFetchEmpty(@ForAll boolean b) {
    Assertions.assertThat(parse(Json.value(b).toString()).isEqualTo(b)).isTrue();
  }
}
