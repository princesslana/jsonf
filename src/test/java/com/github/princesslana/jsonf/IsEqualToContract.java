package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface IsEqualToContract extends WithJsonFParser {

  @Property
  default void isEqualTo_whenJsonLong_shouldBeEqualToSelf(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).isEqualTo(l)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonLong_shouldNotBeEqualToSelfPlusOne(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).isEqualTo(l + 1)).isFalse();
  }

  @Property
  default void isEqualTo_whenJsonDouble_shouldBeEqualToSelf(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).isEqualTo(d)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonDouble_shouldNotBeEqualToString(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).isEqualTo(Double.toString(d))).isFalse();
  }

  @Property
  default void isEqualTo_whenJsonString_shouldBeEqualToSelf(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).isEqualTo(s)).isTrue();
  }

  @Property
  default void isEqualTo_whenJsonBoolean_shouldBeEqualToSelf(@ForAll boolean b) {
    Assertions.assertThat(parse(Json.value(b).toString()).isEqualTo(b)).isTrue();
  }
}
