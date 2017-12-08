package com.github.karczews.utilsverifier.subjects;

public final class ThrowingConstuctor {
    private ThrowingConstuctor() {
        throw new IllegalStateException();
    }
}
