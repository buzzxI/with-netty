package icu.buzz;

import icu.buzz.chat.ChatServer;
import icu.buzz.chat.WebSocketServer;
import icu.buzz.rpc.Provider;

public class Server {
    public static void main(String[] args) {
        // chat server
//        ChatServer server = new ChatServer(8888);
        // websocket server
//        WebSocketServer server = new WebSocketServer("/ws", 8888);
//        server.run();

        // rpc server
        Provider provider = new Provider(8888);
        provider.run();
    }
}