package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Output;

public class ConsoleOutput implements Output {
    @Override
    public void println(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public void println() {
        System.out.println();
    }

    @Override
    public void flush() {
        System.out.flush();
    }
}
