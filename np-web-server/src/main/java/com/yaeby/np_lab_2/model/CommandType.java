package com.yaeby.np_lab_2.model;

import lombok.Getter;

@Getter
public enum CommandType {
    WRITE(1), READ(2);
    private final int priority;

    CommandType(int priority) {
        this.priority = priority;
    }
}
