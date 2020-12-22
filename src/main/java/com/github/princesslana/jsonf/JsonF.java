package com.github.princesslana.jsonf;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public interface JsonF {

  Optional<Boolean> asBoolean();

  Optional<BigDecimal> asNumber();

  Optional<String> asString();

  JsonF get(String key);

  JsonF get(int idx);

  default JsonF get(Object... keys) {
    var json = this;

    for (var k : keys) {
      if (k instanceof String) {
        json = json.get((String) k);
      } else if (k instanceof Integer) {
        json = json.get((Integer) k);
      } else {
        throw new IllegalArgumentException("Bad key: '" + k + "'. Must be String or Integer");
      }
    }

    return json;
  }

  Stream<JsonF> stream();

  default <T> Stream<T> flatMap(Function<JsonF, Optional<T>> f) {
    return stream().map(f).flatMap(Optional::stream);
  }
}
