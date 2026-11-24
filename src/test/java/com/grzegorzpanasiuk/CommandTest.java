package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {

    @Test
    public void fromTextReturnsMatchingCommand() {
        assertEquals(Command.ADD, Command.fromText("ADD"));
    }

    @Test
    public void fromTextReturnsOtherMatchingCommand() {
        assertEquals(Command.LIST, Command.fromText("LIST"));
    }

    @Test
    public void fromTextIgnoresLetterCase() {
        assertEquals(Command.MARK, Command.fromText("mark"));
    }

    @Test
    public void fromTextReturnsUnknownForUnrecognizedText() {
        assertEquals(Command.UNKNOWN, Command.fromText("foo"));
    }

    @Test
    public void allowedCommandsDoNotIncludeUnknown() {
        assertFalse(Command.allowedCommands().contains(Command.UNKNOWN));
    }
}