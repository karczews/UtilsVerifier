# UtilsVerifier
UtilsVerifier is a pure Java library that can be used in Junit tests to verify that Utility class is properly defined.

[![Build Status](https://travis-ci.org/karczews/UtilsVerifier.svg?branch=master)](https://travis-ci.org/karczews/UtilsVerifier)
[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.karczews/utilsverifier.svg?style=flat)](https://oss.sonatype.org/content/repositories/releases/com/github/karczews/utilsverifier/)
[![codecov](https://codecov.io/gh/karczews/UtilsVerifier/branch/master/graph/badge.svg)](https://codecov.io/gh/karczews/UtilsVerifier)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.karczews/utilsverifier.svg?style=flat)](https://oss.sonatype.org/content/repositories/snapshots/com/github/karczews/utilsverifier/)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/karczews/UtilsVerifier/blob/master/LICENSE)

By default verification includes:
 * class having only one private constructor
 * class being final
 * class having no instance fields
 * class having no instance methods
 * class having no static mutable fields
 
Usage
--------
```java
    UtilsVerifier.forClass(ClassToTest.class).verify();
```

It's possible to disable one or more of the default verification checks by using set of "suppress" methods.

```java
    UtilsVerifier.forClass(ClassToTest.class)
                 .suppressFinalClassCheck(true)
                 .suppressOnlyOneConstructorCheck(true)
                 .verify();
```

If the tested class constructor should throw an exception when used it's possible to specify the type of the exception.

```java
    UtilsVerifier.forClass(ClassToTest.class)
                 .withConstructorThrowing(DesiredException.class)
                 .verify();
```

Download
--------

To use library with Gradle

```groovy
dependencies {
  compile 'com.github.karczews:utilsverifier:1.0.0'
}
```

or using Maven:

```xml
<dependency>
    <groupId>com.github.karczews</groupId>
    <artifactId>utilsverifier</artifactId>
    <version>1.0.0</version>
</dependency>
```
