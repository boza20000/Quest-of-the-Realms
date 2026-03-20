package com.questoftherealm.exceptions;

public class InvalidSpellCommand extends RuntimeException {
    public InvalidSpellCommand(String message) {
        super(message);
    }
}
