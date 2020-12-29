package com.github.princesslana.jsonf;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * JsonF provides an interface for fetching data from Json. It is designed to allow retrieval from
 * nested structures without having to check each level of nesting for its existence. Typical usage
 * involves using the get methods to navigate to your chosen element, and the asXxx methods to fetch
 * the value.
 */
public interface JsonF extends Iterable<JsonF> {

  /**
   * Fetch the given value as a boolean. Will return empty if the value is not a boolean or does not
   * exist.
   *
   * @return empty or the referenced value as a boolean
   */
  Optional<Boolean> asBoolean();

  /**
   * Fetch the given value as a BigDecimal. Will return empty if the value is not a number or does
   * not exist.
   *
   * @return empty or the referenced value as a BigDecimal
   */
  Optional<BigDecimal> asNumber();

  /**
   * Fetch the given value as a Long. Will return empty if the value is not a number or does not
   * exist. The semantics of and any conversion is as for BigDecimal#longValue.
   *
   * @return empty or the referenced value as a Long
   */
  default Optional<Long> asLong() {
    return asNumber().map(Number::longValue);
  }

  /**
   * Fetch the given value as a Double. Will return empty if the value is not a number or does not
   * exist. The semantics of any conversion is as for BigDecimal#doubleValue.
   *
   * @return empty or the referenced value as a Double
   */
  default Optional<Double> asDouble() {
    return asNumber().map(Number::doubleValue);
  }

  /**
   * Fetch the given value as a String. Will return empty if the value is not a string or does not
   * exist.
   *
   * @return empty or the referenced value as a String
   */
  Optional<String> asString();

  /**
   * Check if this is a value equal to rhs.
   *
   * @param rhs value to compare with for equality
   * @return true if the value exists, is a boolean, and equal to rhs
   */
  default boolean isEqualTo(Boolean rhs) {
    return asBoolean().map(lhs -> lhs.equals(rhs)).orElse(false);
  }

  /**
   * Check if this is a value equal to rhs.
   *
   * @param rhs value to compare with for equality
   * @return true if the value exists, is a Number, and equal to rhs
   */
  default boolean isEqualTo(long rhs) {
    return isEqualTo(BigDecimal.valueOf(rhs));
  }

  /**
   * Check if this is a value equal to rhs.
   *
   * @param rhs value to compare with for equality
   * @return true if the value exists, is a Number, and equal to rhs
   */
  default boolean isEqualTo(double rhs) {
    return isEqualTo(BigDecimal.valueOf(rhs));
  }

  /**
   * Check if this is a value equal to rhs.
   *
   * @param rhs value to compare with for equality
   * @return true if the value exists, is a Number, and equal to rhs
   */
  default boolean isEqualTo(BigDecimal rhs) {
    return asNumber().map(lhs -> lhs.compareTo(rhs) == 0).orElse(false);
  }

  /**
   * Check if this is a value equal to rhs.
   *
   * @param rhs value to compare with for equality
   * @return true if the value exists, is a String, and equal to rhs
   */
  default boolean isEqualTo(String rhs) {
    return asString().map(lhs -> lhs.equals(rhs)).orElse(false);
  }

  /**
   * Navigate into a Json Object via the given key. This method will succeed whether the given key
   * exists or not. If the key does not exist, then attempts to get values from the resulting JsonF
   * instance will simply return empty.
   *
   * @param key key to navigate to
   * @return JsonF instance that can be used to query the json present at that key
   */
  JsonF get(String key);

  /**
   * Navigate into a Json Array via the given index. This method will succeed whether the given
   * index exists or not. If the index does not exist, then attempts to get values from the
   * resulting JsonF instance will simply return empty.
   *
   * @param idx the index to navigate to
   * @return JsonF instance that can be used to query the json present at that index.
   */
  JsonF get(int idx);

  /**
   * Navigate into a Json structure via the given keys and indexes. Only Strings and Integers are
   * valid parameters. This method will succeed whether the given path can be navigated or not. If
   * any step of the path does not exist, then attempts to get values from the resulting JsonF
   * instance will simply return empty.
   *
   * @param path the keys and indexes to navigate through
   * @return JsonF instance that can be used to query the json present at that path
   */
  default JsonF get(Object... path) {
    var json = this;

    for (var p : path) {
      if (p instanceof String) {
        json = json.get((String) p);
      } else if (p instanceof Integer) {
        json = json.get((Integer) p);
      } else {
        throw new IllegalArgumentException("Bad path: '" + p + "'. Must be String or Integer");
      }
    }

    return json;
  }

  /**
   * Stream the elements of this Json Array. If this is not an Array then an empty Stream will be
   * returned.
   *
   * @return Stream of JsonF instances for elements in this Json Array
   */
  Stream<JsonF> stream();

  @Override
  default Iterator<JsonF> iterator() {
    return stream().iterator();
  }

  /**
   * Apply a function to all elements in this Json Array. If this is not an Array then an empty
   * Stream will be returned. Otherwise f is applied to each element of the array, with the
   * resulting stream consisting of the value from all results that were present. Applications of f
   * that result in an empty result are thrown away.
   *
   * @param <T> result type of applying f to an element
   * @param f Function to apply to each element of the Json Array
   * @return Stream of present results of applying f to each element
   */
  default <T> Stream<T> flatMap(Function<JsonF, Optional<T>> f) {
    return stream().map(f).flatMap(Optional::stream);
  }

  /**
   * Parse the given string using an available Json lib.
   *
   * @param json input string
   * @return created JsonF instance
   * @throws JsonFException if parsing failed
   */
  static JsonF parse(String json) {
    return Implementation.parse(json);
  }

  /**
   * Create a JsonF instance from the given Json value. The implementation chosen will be based upon
   * the type of the provided parameter.
   *
   * @param json the json value
   * @return created JsonF instance
   */
  static JsonF from(Object json) {
    return Implementation.from(json);
  }
}
