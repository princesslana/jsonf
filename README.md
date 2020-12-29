# JsonF

![Maven Central](https://img.shields.io/maven-central/v/com.github.princesslana/jsonf)
[![javadoc](https://javadoc.io/badge2/com.github.princesslana/jsonf/javadoc.svg)](https://javadoc.io/doc/com.github.princesslana/jsonf)
![Build](https://github.com/princesslana/jsonf/workflows/Build/badge.svg)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=princesslana_jsonf&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=princesslana_jsonf)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=princesslana_jsonf&metric=coverage)](https://sonarcloud.io/dashboard?id=princesslana_jsonf)
[![Discord](https://img.shields.io/discord/417389758470422538)](https://discord.gg/3aTVQtz)

JsonF is a straight forward way to fetch values from JSON.

It does not implement JSON parsing itself, but instead presents a consistent
and straight forward API on top of whichver JSON library you are already using.

## Installing

JsonF is distributed via maven central.

To use with maven:

```xml
  <dependency>
    <groupId>com.github.princesslana</groupId>
    <artifactId>jsonf</artifactId>
    <version>LASTEST_VERSION</version>
  </dependency>
```

With gradle:

```kotlin
  implementation("com.github.princesslana:jsonf:LATEST_VERSION")
```

You will also need a supported JSON library on your classpath.
JsonF currently supports:

  * [Jackson](https://github.com/FasterXML/jackson)
  * [minimal-json](https://github.com/ralfstx/minimal-json)
  * [Gson](https://github.com/google/gson)

## Examples

Consider the following json object:

```js
{
  "id": 123,
  "name": {
    "title": "Princess"
    "given_name": "Lana"
  },
  "interests": [ "Java", "Purple" ]
}
```
To create a JsonF instance JSON can be parsed directly from a String:

```java
  JsonF jsonf = JsonF.parse(json);
```

It may also be created from an instance of a JSON object created via a supported library.
For exampe a JsonObject from GSON or Jackson.

```java
  JsonF jsonf = JsonF.from(jsonObj);
```

The `get` methods can be used to navigate through the JSON structure,
while the `asXxx` methods (e.g., `asString`) fetch values.
All `asXxx` methods return `Optional`s.
`Optional.empty` will be returned if navigation was to a non-existent element, 
or if the element is not of the matching data type.

For the document given above:

```java
  jsonf.get("id").asLong();                  // Optional.of(123)
  jsonf.get("id").asString();                // Optional.empty()

  jsonf.get("name").get("title").asString(); // Optional.of("Princess")
  jsonf.get("interests").get(0).asString();  // Optional.of("Java")

  jsonf.get("foo").asString();               // Optional.empty()
```

A varags variant of `get` exists as a convenince for successive get calls:

```java
  jsonf.get("name", "given_name").asString(); // Optional.of("Lana")
  jsonf.get("interests", 1).asString();       // Optional.of("Purple")
  jsonf.get("name", "surname").asString();    // Optional.empty()
```

`JsonF` is able to iterate over JSON arrays.
If the element navigated to is not an array then zero iterations will be performed.
There is also `stream` and `flatMap` to assist with collecting data from a JSON array.

```java
  // Will output "Java" and "Purple"
  for (var s : json.get("interests")) {
    System.out.println(s); 
  }

  // Stream version of the above
  json.get("interests").stream().forEach(s -> System.out.println(s));

  // No output, no error. No iterations as not an array
  for (var s : json.get("name")) {
    System.out.println(s);
  }
```

## Contact

Reach out to the [Discord Projects Hub](https://discord.gg/3aTVQtz) on Discord
and look for the jsonf channel.

## Development

To run the junit tests:
```bash
$ mvn test
```

To run the code formatter:
```bash
$ mvn spotless:apply
```

To run the full set of verifications (including style checks):
```bash
$ mvn verify
```
