package com.github.princesslana.jsonf;

import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public class TestSanity {

  @Property
  public void true_shouldBeTrue() {
    Assertions.assertThat(true).isTrue();
  }
}
