package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Output;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocketOutput implements Output {
    private final PrintWriter printWriter;

    public ClientSocketOutput(Socket socket) throws IOException {
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public synchronized void println(String msg) {
        printWriter.println(msg);
    }

    @Override
    public synchronized void print(String msg) {
        printWriter.print(msg);
    }

    @Override
    public synchronized void println() {
        printWriter.println();
    }

    @Override
    public synchronized void flush() {
        printWriter.flush();
    }


}
