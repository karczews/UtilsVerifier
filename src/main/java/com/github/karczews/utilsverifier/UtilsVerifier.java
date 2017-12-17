/**
 * Copyright (c) 2017-present, UtilsVerifier Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.github.karczews.utilsverifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * {@code UtilsVerifier} tool can be used in unit tests to verify if certain
 * util class is well formed.
 * <p>
 * By default verifier performs following checks that class:
 * <ul>
 * <li> is final
 * <li> has only one private constructor
 * <li> has no instance fields
 * <li> has no mutable static fields
 * </ul>
 * <p>
 * If any of the following checks fails then an {@link AssertionError} will be
 * thrown with an appropriate information about the cause.
 * <p>
 * Basic usage:
 * <pre>
 * {@code UtilsVerifier.forClass(TestedClass.class).verify();}
 * </pre>
 * It's not advised, but possible to suppress one or more of the checks with
 * builder methods.
 * <p>
 * Example suppress:
 * <pre>
 * {@code UtilsVerifier.forClass(TestClass.class).suppressFinalClassCheck(true).verify();}
 * </pre>
 *
 * @param <T> class under test
 * @see AssertionError
 */
public final class UtilsVerifier<T> {

    private final Class<T> classUnderTest;

    private Class<? extends Throwable> expectedConstructorException;

    private boolean suppressFinalClassCheck = false;
    private boolean suppressOnlyOneConstructorCheck = false;
    private boolean suppressPrivateConstructorCheck = false;
    private boolean suppressInstanceFieldCheck = false;
    private boolean suppressInstanceMethodCheck = false;
    private boolean suppressMutableStaticFieldsCheck = false;

    // list of mutable static fields often added by outside tools like JaCoCo
    private final String[] allowedMutableStaticFields = {"$jacocoData"};

    private UtilsVerifier(final Class<T> type) {
        classUnderTest = type;
    }

    /**
     * Creates UtilsVerifier instance for the provided type.
     *
     * @param type class type for a which verifier will be created
     * @param <T>  type of the class to verify
     * @return UtilsVerifier instance
     */
    public static <T> UtilsVerifier<T> forClass(final Class<T> type) {
        return new UtilsVerifier<T>(type);
    }

    /**
     * Performs verification for the type that the {@link UtilsVerifier} was
     * created with.
     * <p>
     * {@link AssertionError} will be thrown if provided type is not a well
     * formed util class.
     */
    public void verify() {
        checkIfClassIsFinal();
        hasOnlyOneConstructor();
        verifyPrivateConstructor();
        hasNoInstanceFields();
        hasNoInstanceMethods();
        hasNoMutableStaticFields();
    }

    /**
     * Sets exception type that will be expected during construction attempt.
     *
     * @param type expected exception type
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> withConstructorThrowing(final Class<? extends Throwable> type) {
        expectedConstructorException = type;
        return this;
    }

    /**
     * Suppress final class verification. Use if non-final util class is allowed.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressFinalClassCheck(final boolean suppressCheck) {
        suppressFinalClassCheck = suppressCheck;
        return this;
    }

    /**
     * Suppress single constructor verification. Use if util class is allowed to
     * have more than one constructor.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressOnlyOneConstructorCheck(final boolean suppressCheck) {
        suppressOnlyOneConstructorCheck = suppressCheck;
        return this;
    }

    /**
     * Suppress private constructor verification. Use if util class is allowed
     * to have non private constructor.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressPrivateConstructorCheck(final boolean suppressCheck) {
        suppressPrivateConstructorCheck = suppressCheck;
        return this;
    }

    /**
     * Suppress instance field verification. Use if util class is allowed to
     * have instance fields.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressInstanceFieldCheck(final boolean suppressCheck) {
        suppressInstanceFieldCheck = suppressCheck;
        return this;
    }

    /**
     * Suppress instance method verification. Use if util class is allowed to
     * have instance methods.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressInstanceMethodCheck(final boolean suppressCheck) {
        suppressInstanceMethodCheck = suppressCheck;
        return this;
    }

    /**
     * Suppress static mutable fields verification. Use if util class is allowed
     * to have mutable static fields.
     *
     * @param suppressCheck true if check should be suppressed, false otherwise
     * @return UtilsVerifier instance
     */
    public UtilsVerifier<T> suppressMutableStaticFieldsCheck(final boolean suppressCheck) {
        suppressMutableStaticFieldsCheck = suppressCheck;
        return this;
    }

    private void checkIfClassIsFinal() {
        if (suppressFinalClassCheck) return;
        if (!Modifier.isFinal(classUnderTest.getModifiers())) {
            throw new AssertionError(classUnderTest.getName() + " is not final");
        }
    }

    private void hasOnlyOneConstructor() {
        if (suppressOnlyOneConstructorCheck) return;
        if (classUnderTest.getDeclaredConstructors().length != 1) {
            throw new AssertionError(classUnderTest.getName() + " has too many constructors");
        }
    }

    private void verifyPrivateConstructor() {
        final Constructor<?> constructor;
        try {
            constructor = classUnderTest.getDeclaredConstructor();
            if (!suppressPrivateConstructorCheck && !Modifier.isPrivate(constructor.getModifiers())) {
                throw new AssertionError("Constructor should be private");
            }
        } catch (final NoSuchMethodException ignore) {
            throw new AssertionError(classUnderTest.getSimpleName() + " has no constructor");
        }

        try {
            constructor.setAccessible(true);
            constructor.newInstance();
            if (expectedConstructorException != null) {
                throw new AssertionError("should not be able to instantiate " + classUnderTest.getSimpleName());
            }
        } catch (final InvocationTargetException e) {
            if (!expectedConstructorException.isInstance(e.getTargetException())) {
                throw new AssertionError("expected exception: " + expectedConstructorException.getName() +
                        " got: " + e.getTargetException().getClass().getName());
            }
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void hasNoInstanceFields() {
        if (suppressInstanceFieldCheck) return;
        final Field[] fields = classUnderTest.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            final Field field = fields[index];
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new AssertionError(classUnderTest.getName()
                        + " contains instance field " + field.getName());
            }
        }
    }

    private void hasNoInstanceMethods() {
        if (suppressInstanceMethodCheck) return;
        final Method[] methods = classUnderTest.getDeclaredMethods();
        for (int index = 0; index < methods.length; index++) {
            final Method method = methods[index];
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new AssertionError(classUnderTest.getName()
                        + " contains instance method " + method.getName());
            }
        }
    }

    private void hasNoMutableStaticFields() {
        if (suppressMutableStaticFieldsCheck) return;
        final Field[] fields = classUnderTest.getDeclaredFields();

        for (int index = 0; index < fields.length; index++) {
            final Field field = fields[index];
            final int modifiers = field.getModifiers();
            if (Arrays2.contains(field.getName(), allowedMutableStaticFields)) {
                continue;
            }
            if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                throw new AssertionError(classUnderTest.getName()
                        + " contains static mutable field " + field.getName());
            }
        }
    }
}
