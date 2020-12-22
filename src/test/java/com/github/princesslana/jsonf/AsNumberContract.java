package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import java.math.BigDecimal;
import java.util.function.Consumer;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface AsNumberContract extends WithJsonFParser {

  private static Consumer<BigDecimal> bigDecimalEquals(BigDecimal expected) {
    return actual -> Assertions.assertThat(actual.compareTo(expected)).isZero();
  }

  @Property
  default void asNumber_whenJsonLong_shouldFetch(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).asNumber())
        .isPresent()
        .hasValueSatisfying(bigDecimalEquals(BigDecimal.valueOf(l)));
  }

  @Property
  default void asNumber_whenJsonDouble_shouldFetch(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).asNumber())
        .isPresent()
        .hasValueSatisfying(bigDecimalEquals(BigDecimal.valueOf(d)));
  }

  @Property
  default void asNumber_whenJsonString_shouldFetchEmpty(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).asNumber()).isEmpty();
  }

  @Property
  default void asNumber_whenJsonBoolean_shouldFetchEmpty(@ForAll boolean b) {
    Assertions.assertThat(parse(Json.value(b).toString()).asNumber()).isEmpty();
  }
}
