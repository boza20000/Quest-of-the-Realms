package com.questoftherealm.exceptions;

public class PlayerNotFound extends RuntimeException {
    public PlayerNotFound(String message) {
        super(message);
    }
}
