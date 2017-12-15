package com.github.karczews.utilsverifier;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Arrays2Test {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowNullPointerExceptionForNullArray() {
        expectedException.expect(NullPointerException.class);

        final String[] array = null;
        final String item = "";

        Arrays2.contains(item, array);
    }

    @Test
    public void shouldReturnFalseForEmptyArray() {
        final String[] array = {};
        final String item = "";

        Assert.assertFalse(Arrays2.contains(item, array));
    }

    @Test
    public void shouldReturnFalse() {
        final String[] array = {"item1", "item2"};
        final String item = "";

        Assert.assertFalse(Arrays2.contains(item, array));
    }

    @Test
    public void shouldReturnFalseForNullItem() {
        final String[] array = {"item1", "item2"};
        final String item = null;

        Assert.assertFalse(Arrays2.contains(item, array));
    }

    @Test
    public void shouldReturnTrue() {
        final String[] array = {"item1", "item2"};
        final String item = "item1";

        Assert.assertTrue(Arrays2.contains(item, array));
    }

    @Test
    public void verifyArraysTool() {
        UtilsVerifier.forClass(Arrays2.class).verify();
    }
}
