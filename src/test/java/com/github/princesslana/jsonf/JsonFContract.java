package com.github.princesslana.jsonf;

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
}
