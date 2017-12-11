# UtilsVerifier
UtilsVerifier is a pure java library that can be used in junit tests to verify that Utility method is properly defined.

[![Build Status](https://travis-ci.org/karczews/UtilsVerifier.svg?branch=master)](https://travis-ci.org/karczews/UtilsVerifier)
[![codecov](https://codecov.io/gh/karczews/UtilsVerifier/branch/master/graph/badge.svg)](https://codecov.io/gh/karczews/UtilsVerifier)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.karczews/utilsverifier.svg?style=flat)](https://oss.sonatype.org/content/repositories/snapshots/com/github/karczews/utilsverifier/)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/karczews/UtilsVerifier/blob/master/LICENSE)

By default verification includes:
 * private constructor check
 * class is final
 * class has no instance fields
 * class has no instance methods
 
Usage
--------
```java
    UtilsVerifier.forClass(ClassToTest.class).verify();
```