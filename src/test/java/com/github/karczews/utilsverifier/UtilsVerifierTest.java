package com.github.karczews.utilsverifier;


import com.github.karczews.utilsverifier.subjects.InstanceFields;
import com.github.karczews.utilsverifier.subjects.MultipleConstructors;
import com.github.karczews.utilsverifier.subjects.NonFinalClass;
import com.github.karczews.utilsverifier.subjects.ThrowingConstuctor;
import com.github.karczews.utilsverifier.subjects.WellFormed;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;

public class UtilsVerifierTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFailOnNonFinalClassVerification() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("not final"));

        suppressedVerifier(NonFinalClass.class)
                .suppressFinalClassCheck(false)
                .verify();
    }

    @Test
    public void shouldFailOnInstanceFields() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("instanceInt"));

        suppressedVerifier(InstanceFields.class)
                .suppressInstanceFieldCheck(false)
                .verify();
    }

    @Test
    public void shouldFailOnMultipleConstructors() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("has too many constructors"));

        suppressedVerifier(MultipleConstructors.class)
                .suppressOnlyOneConstructorCheck(false)
                .verify();
    }

    @Test
    public void shouldFailWhenConstructorDoesNotThrow() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("should not be able to instantiate"));

        suppressedVerifier(WellFormed.class)
                .withConstructorThrowing(IllegalStateException.class)
                .verify();
    }

    @Test
    public void shouldFailWhenExpectingDifferentException() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(allOf(
                containsString("IllegalStateException"),
                containsString("IllegalArgumentException")));

        suppressedVerifier(ThrowingConstuctor.class)
                .withConstructorThrowing(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void shouldPassWhenExpectedExceptionReceived() {
        suppressedVerifier(ThrowingConstuctor.class)
                .withConstructorThrowing(IllegalStateException.class)
                .verify();
    }

    @Test
    public void shouldPassForWellFormedUtil() {
        UtilsVerifier.forClass(WellFormed.class).verify();
    }

    private static <T> UtilsVerifier<T> suppressedVerifier(final Class<T> type) {
        return UtilsVerifier.forClass(type)
                .suppressFinalClassCheck(true)
                .suppressOnlyOneConstructorCheck(true)
                .suppressPrivateConstructorCheck(true)
                .suppressInstanceFieldCheck(true);
    }
}
