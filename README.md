# UtilsVerifier
UtilsVerifier is a pure java library that can be used in junit tests to verify that Utility method is properly defined.

[![Build Status](https://travis-ci.org/karczews/UtilsVerifier.svg?branch=master)](https://travis-ci.org/karczews/UtilsVerifier)
[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.karczews/utilsverifier.svg?style=flat)](https://oss.sonatype.org/content/repositories/releases/com/github/karczews/utilsverifier/)
[![codecov](https://codecov.io/gh/karczews/UtilsVerifier/branch/master/graph/badge.svg)](https://codecov.io/gh/karczews/UtilsVerifier)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.karczews/utilsverifier.svg?style=flat)](https://oss.sonatype.org/content/repositories/snapshots/com/github/karczews/utilsverifier/)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/karczews/UtilsVerifier/blob/master/LICENSE)

By default verification includes:
 * class has only one private constructor
 * class is final
 * class has no instance fields
 * class has no instance methods
 
Usage
--------
```java
    UtilsVerifier.forClass(ClassToTest.class).verify();
```


Download
--------

To use library with Gradle

```groovy
dependencies {
  compile 'com.github.karczews:utilsverifier:0.0.2'
}
```

or using Maven:

```xml
<dependency>
    <groupId>com.github.karczews</groupId>
    <artifactId>utilsverifier</artifactId>
    <version>0.0.2</version>
</dependency>
```