package icu.buzz;

import icu.buzz.chat.ChatClient;

public class Client {
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", 8888);
        client.run();
    }
}