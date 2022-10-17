package com.elisapolystar.exercise;

public class Application {

    public static void main(String[] args) throws Exception {

        String[] files = {
            "/Users/mauri/Code/Elisa/dracula.txt",
            "/Users/mauri/Code/Elisa/frankenstein.txt"
        };

        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            Thread server = new Thread(new Server(1000+i, file));
            server.start();
        }

        for (int i = 0; i < files.length; i++) {
            Thread client = new Thread(new Client("127.0.0.1", 1000+i));
            client.start();
        }
    }
}