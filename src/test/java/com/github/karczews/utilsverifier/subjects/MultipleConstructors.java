package com.github.karczews.utilsverifier.subjects;

public final class MultipleConstructors {
    private MultipleConstructors() {

    }

    private MultipleConstructors(int i) {
        this();
    }
}
