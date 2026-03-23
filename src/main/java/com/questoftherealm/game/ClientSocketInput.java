package com.questoftherealm.game;

import com.questoftherealm.game.interfaces.Input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketInput implements Input {
    private final Scanner scanner;
    private final InputStream inputStream;

    public ClientSocketInput(Socket socket) throws IOException {
        this.scanner = new Scanner(socket.getInputStream());
        this.inputStream = socket.getInputStream();
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public int nextInt() {
        return Integer.parseInt(scanner.nextLine());
    }
    @Override
    public int available() {
        try {
            return inputStream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public int read() {
        try {
            return inputStream.read();
        } catch (IOException e) {
            return -1;
        }
    }

}
