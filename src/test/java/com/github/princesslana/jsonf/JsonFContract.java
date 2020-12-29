package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

public interface JsonFContract
    extends AsBooleanContract,
        AsNumberContract,
        AsStringContract,
        GetContract,
        IsEqualToContract,
        ParseContract,
        StreamContract {}

class TestGsonF implements JsonFContract {

  @Override
  public JsonF parse(String json) {
    return GsonF.parse(json);
  }
}

class TestJacksonF implements JsonFContract {

  @Override
  public JsonF parse(String string) {
    return JacksonF.parse(string);
  }
}

class TestMinimalF implements JsonFContract {

  @Override
  public JsonF parse(String json) {
    return MinimalF.parse(json);
  }
}

class TestJsonFReflectionImplementation implements JsonFContract {

  @Override
  public JsonF parse(String json) {
    return JsonF.parse(json);
  }

  @Example
  public void from_whenMinimalJsonObject_shouldCreateMinimalF() {
    var jsonf = JsonF.from(Json.object());
    Assertions.assertThat(jsonf).isInstanceOf(MinimalF.class);
  }
}
