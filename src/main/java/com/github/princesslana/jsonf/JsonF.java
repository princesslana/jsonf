package com.github.princesslana.jsonf;

import java.math.BigDecimal;
import java.util.Optional;

public interface JsonF {

  Optional<BigDecimal> asNumber();

  Optional<String> asString();
}
