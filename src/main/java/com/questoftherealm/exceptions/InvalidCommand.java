package com.questoftherealm.exceptions;

public class InvalidCommand extends RuntimeException {
    public InvalidCommand(String message) {
        super(message);
    }
}
