package com.questoftherealm.client;

import com.questoftherealm.characters.player.Player;

import java.nio.ByteBuffer;


public class ClientSession {
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Player player = null;


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }



}
