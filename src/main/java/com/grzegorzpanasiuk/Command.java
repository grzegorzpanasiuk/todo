package com.grzegorzpanasiuk;

import java.util.Arrays;
import java.util.List;

public enum Command {
    ADD,
    DELETE,
    EXIT,
    HELP,
    LIST,
    MARK,
    UNKNOWN;

    public static Command fromText(String text) {
        try {
            return valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public static List<Command> allowedCommands() {
        return Arrays.stream(values())
                .filter(command -> command != UNKNOWN)
                .toList();
    }
}
