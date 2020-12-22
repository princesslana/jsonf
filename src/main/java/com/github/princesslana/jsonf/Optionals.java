package com.github.princesslana.jsonf;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/** Utility methods for working with Optionals. */
public class Optionals {

  private Optionals() {}

  /**
   * Apply a function if a predicate is met. Will return empty if the input was empty or the
   * predicate test fails. Otherwise will return an Optional with the result of applying f to the
   * value contained within the Optional.
   *
   * @param <A> type of value within Optional
   * @param <B> result type of f
   * @param opt Optional to get value from
   * @param pred Predicate to test
   * @param f Function to apply to value if present
   * @return empty or an Optional containing the result of f applyed to the value
   */
  public static <A, B> Optional<B> mapIf(Optional<A> opt, Predicate<A> pred, Function<A, B> f) {
    return opt.stream().filter(pred).map(f).findFirst();
  }

  /**
   * Apply a function if a predicate is met. Will return empty if the input was empty or the
   * predicate test fails. Otherwise will return the result of applying f to the value contained
   * with in the Optional.
   *
   * @param <A> type of value within Optional
   * @param <B> new type of value within resulting Optional
   * @param opt Optional to get the value from
   * @param pred Predicate to test
   * @param f Function to apply to value if present
   * @return empty or the result of f applyed to the value
   */
  public static <A, B> Optional<B> flatMapIf(
      Optional<A> opt, Predicate<A> pred, Function<A, Optional<B>> f) {
    return opt.stream().filter(pred).flatMap(a -> f.apply(a).stream()).findFirst();
  }

  /**
   * Apply a function if a predicate is met. Will return empty if the input was empty or the
   * predicate test fails. Otherwise will take the result of f and return the result of calling
   * Optional.ofNullable on it.
   *
   * @param <A> type of value within Optional
   * @param <B> result type of f
   * @param opt Optional to get the value from
   * @param pred Predicate to test
   * @param f Function to apply to the value if present
   * @return empty or the result of f applyed to the value wrapped in Optional.ofNullable
   */
  public static <A, B> Optional<B> flatMapNullableIf(
      Optional<A> opt, Predicate<A> pred, Function<A, B> f) {
    return flatMapIf(opt, pred, a -> Optional.ofNullable(f.apply(a)));
  }
}
