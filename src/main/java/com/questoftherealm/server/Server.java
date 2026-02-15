package com.questoftherealm.server;

import com.questoftherealm.characters.player.Player;
import com.questoftherealm.client.ClientSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int SERVER_PORT = 2020;
    private ServerSocketChannel serverChannel;
    private static final String SERVER_HOST = "0.0.0.0";
    private volatile boolean isRunning = true;
    private Selector selector;
    private final int MAX_PLAYERS = 15;
    private int clientCount;
    private Map<String, Player> activePlayers = new ConcurrentHashMap<>();

    public void startGameServer() {
        try (ServerSocketChannel newServerChannel = ServerSocketChannel.open(); Selector newSelector = Selector.open()) {
            this.serverChannel = newServerChannel;
            this.selector = newSelector;

            serverInitialization(newServerChannel);

            while (isRunning) {

                if (selector.select() == 0) {
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {

                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (!key.isValid()) {
                        continue;
                    }
                    
                    if (key.isAcceptable()) {
                        acceptClient(selector);
                    } else {
                        readClient(key);
                    }
                    
                }
                
            }

        } catch (IOException e) {

        }
    }

    private void serverInitialization(ServerSocketChannel newServerChannel) throws IOException {
        clientCount = 0;
        newServerChannel.bind(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
        newServerChannel.configureBlocking(false);
        newServerChannel.register(selector, SelectionKey.OP_ACCEPT);
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("Game server started on: " + address + " listening on port " + SERVER_PORT);
    }

    private void readClient(SelectionKey key) throws IOException {
        ClientSession session =(ClientSession) key.attachment();
        ByteBuffer buffer = session.getBuffer();
        SocketChannel clientChannel =(SocketChannel) key.channel();

        buffer.clear();

        int bytesRead = clientChannel.read(buffer);
        if(bytesRead==-1) {
            System.out.println("Player disconnected");
            if(session.getPlayer()!= null){
                activePlayers.remove(session.getPlayer().getName());
            }
            clientChannel.close();
            key.cancel();
            return;
        }
        buffer.flip();

        byte[]data = new byte[buffer.remaining()];
        buffer.get(data);
        System.out.println("{Player} (" + clientChannel.getRemoteAddress() + ") requested: " + new String(data));

        try {
            handleRequest(new String(data), key);
        }
        catch (IOException e){
            //fix this
        }

    }

    private void handleRequest(String message, SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap((message + System.lineSeparator()).getBytes());
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
    }

    private void acceptClient(Selector selector) throws IOException {
        if(clientCount>=MAX_PLAYERS){
            System.out.println("Max players reached.("+ clientCount +"/"+ MAX_PLAYERS +")");
            return;
        }
        System.out.println("Server status: ("+ (activePlayers.size() + 1) +"/15) players connected.");
        SocketChannel clientChannel = serverChannel.accept();

        if(clientChannel==null){
            return;
        }
        ClientSession session = new ClientSession();
        serverChannel.register(selector,SelectionKey.OP_ACCEPT,session);
    }


}
