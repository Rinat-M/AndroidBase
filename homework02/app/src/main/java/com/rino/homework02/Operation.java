package com.rino.homework02;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public enum Operation {
    PERCENT("%"),
    DIVIDE("/"),
    MULTIPLY("*"),
    MINUS("-"),
    PLUS("+"),
    CALCULATE("="),
    NONE("NONE");

    private final String value;

    Operation(String value) {
        this.value = value;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return value;
    }
}
