# UtilsVerifier
UtilsVerifier is a pure java library that can be used in junit tests to verify that Utility method is properly defined.

Check by default includes:
 * private constructor check
 * class is final
 * class has no instance fields
 * class has no instance methods
 
Usage
--------
```java
    UtilsVerifier.forClass(ClassToTest.class).verify();
```