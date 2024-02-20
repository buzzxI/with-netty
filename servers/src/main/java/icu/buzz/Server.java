package icu.buzz;

import icu.buzz.chat.ChatServer;

public class Server {
    public static void main(String[] args) {
        ChatServer server = new ChatServer(8888);
        server.run();
    }
}