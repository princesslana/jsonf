package com.github.princesslana.jsonf;

import net.jqwik.api.Example;
import org.assertj.core.api.Assertions;

public class TestSanity {

  @Example
  public void true_shouldBeTrue() {
    Assertions.assertThat(true).isTrue();
  }
}
