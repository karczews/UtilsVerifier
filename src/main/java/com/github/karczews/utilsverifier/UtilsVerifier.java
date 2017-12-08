package com.github.karczews.utilsverifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class UtilsVerifier<T> {

    private final Class<T> classUnderTest;

    private Class<? extends Throwable> expectedConstructorException;
    private boolean suppressFinalClassCheck = false;
    private boolean suppressOnlyOneConstructorCheck = false;
    private boolean suppressPrivateConstructorCheck = false;
    private boolean suppressInstanceFieldCheck = false;

    private UtilsVerifier(final Class<T> type) {
        classUnderTest = type;
    }

    /**
     * Creates UtilsVerifier for provided type.
     */
    public static <T> UtilsVerifier<T> forClass(final Class<T> type) {
        return new UtilsVerifier<T>(type);
    }

    public UtilsVerifier<T> withConstructorThrowing(final Class<? extends Throwable> type) {
        expectedConstructorException = type;
        return this;
    }

    public UtilsVerifier<T> suppressFinalClassCheck(final boolean suppressCheck) {
        suppressFinalClassCheck = suppressCheck;
        return this;
    }

    public UtilsVerifier<T> suppressOnlyOneConstructorCheck(final boolean suppressCheck) {
        suppressOnlyOneConstructorCheck = suppressCheck;
        return this;
    }

    public UtilsVerifier<T> suppressPrivateConstructorCheck(final boolean suppressCheck) {
        suppressPrivateConstructorCheck = suppressCheck;
        return this;
    }

    public UtilsVerifier<T> suppressInstanceFieldCheck(final boolean suppressCheck) {
        suppressInstanceFieldCheck = suppressCheck;
        return this;
    }

    /**
     * Performs verification for provided type.
     * {@link AssertionError} will be thrown if provided type is not well formed util class.
     */
    public void verify() {
        checkIfClassIsFinal();
        hasOnlyOneConstructor();
        verifyPrivateConstructor();
        hasNoInstanceFields();
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
        } catch (final NoSuchMethodException noSuchMethod) {
            throw new AssertionError(classUnderTest.getSimpleName() + " has no constructor", noSuchMethod);
        }

        try {
            constructor.setAccessible(true);
            constructor.newInstance();
            if (expectedConstructorException != null) {
                throw new AssertionError("should not be able to instantiate " + classUnderTest.getSimpleName());
            }
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (final InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (final InvocationTargetException e) {
            if (!expectedConstructorException.isInstance(e.getTargetException())) {
                throw new AssertionError("expected exception: " + expectedConstructorException.getName() +
                        " got: " + e.getTargetException().getClass().getName());
            }
        }
    }

    private void hasNoInstanceFields() {
        if (suppressInstanceFieldCheck) return;
        final Field[] fields = classUnderTest.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            final Field field = fields[index];
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new AssertionError(classUnderTest.getName() + " contains instance field " + field.getName());
            }
        }
    }
    //TODO: add check for mutable static fields
}
