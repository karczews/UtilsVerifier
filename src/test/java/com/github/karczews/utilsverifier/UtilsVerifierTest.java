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


import com.github.karczews.utilsverifier.subjects.AbstractClass;
import com.github.karczews.utilsverifier.subjects.DefaultConstructor;
import com.github.karczews.utilsverifier.subjects.ImmutableStaticFields;
import com.github.karczews.utilsverifier.subjects.InstanceFields;
import com.github.karczews.utilsverifier.subjects.InstanceMethods;
import com.github.karczews.utilsverifier.subjects.MultipleConstructors;
import com.github.karczews.utilsverifier.subjects.MutableStaticFields;
import com.github.karczews.utilsverifier.subjects.NonFinalClass;
import com.github.karczews.utilsverifier.subjects.NonPrivateConstructor;
import com.github.karczews.utilsverifier.subjects.ThrowingConstructor;
import com.github.karczews.utilsverifier.subjects.WellFormed;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UtilsVerifierTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldFailOnMutableStaticFields() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("staticInt"));

        suppressedVerifier(MutableStaticFields.class)
                .suppressMutableStaticFieldsCheck(false)
                .verify();
    }

    @Test
    public void shouldPassOnMutableStaticFields() {
        suppressedVerifier(ImmutableStaticFields.class)
                .suppressMutableStaticFieldsCheck(false)
                .verify();

        // due to complex condition two asserts in one test
        // to not create confusion with strange test name
        suppressedVerifier(InstanceFields.class)
                .suppressMutableStaticFieldsCheck(false)
                .verify();
    }

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
    public void shouldFailOnInstanceMethod() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("instanceMethod"));

        suppressedVerifier(InstanceMethods.class)
                .suppressInstanceMethodCheck(false)
                .verify();
    }

    @Test
    public void shouldFailWhenNoConstructor() {
        expectedException.expect(AssertionError.class);

        suppressedVerifier(DefaultConstructor.class)
                .suppressPrivateConstructorCheck(false)
                .verify();
    }

    @Test
    public void shouldFailWhenNoPrivateConstructor() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("should be private"));

        suppressedVerifier(NonPrivateConstructor.class)
                .suppressPrivateConstructorCheck(false)
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

        suppressedVerifier(ThrowingConstructor.class)
                .withConstructorThrowing(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void shouldPassWhenExpectedExceptionReceived() {
        suppressedVerifier(ThrowingConstructor.class)
                .withConstructorThrowing(IllegalStateException.class)
                .verify();
    }

    @Test
    public void shouldPassForWellFormedUtil() {
        UtilsVerifier.forClass(WellFormed.class).verify();
    }

    @Test
    public void shouldThrowIllegalStateExceptionOnInstantiationReflectionProblems() {
        expectedException.expect(IllegalStateException.class);

        suppressedVerifier(AbstractClass.class).verify();
    }

    @Test
    public void shouldFailOnClassWithNoConstructor() {
        expectedException.expect(AssertionError.class);
        expectedException.expectMessage(containsString("has no constructor"));

        suppressedVerifier(Runnable.class).verify();
    }

    private static <T> UtilsVerifier<T> suppressedVerifier(final Class<T> type) {
        return UtilsVerifier.forClass(type)
                .suppressFinalClassCheck(true)
                .suppressOnlyOneConstructorCheck(true)
                .suppressPrivateConstructorCheck(true)
                .suppressInstanceFieldCheck(true)
                .suppressInstanceMethodCheck(true)
                .suppressMutableStaticFieldsCheck(true);
    }
}
