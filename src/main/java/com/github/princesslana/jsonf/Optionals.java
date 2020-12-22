package com.github.princesslana.jsonf;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Optionals {

  private Optionals() {}

  public static <A, B> Optional<B> mapIf(Optional<A> opt, Predicate<A> pred, Function<A, B> f) {
    return opt.stream().filter(pred).map(f).findFirst();
  }

  public static <A, B> Optional<B> flatMapIf(
      Optional<A> opt, Predicate<A> pred, Function<A, Optional<B>> f) {
    return opt.stream().filter(pred).flatMap(a -> f.apply(a).stream()).findFirst();
  }

  public static <A, B> Optional<B> flatMapNullableIf(
      Optional<A> opt, Predicate<A> pred, Function<A, B> f) {
    return flatMapIf(opt, pred, a -> Optional.ofNullable(f.apply(a)));
  }
}
