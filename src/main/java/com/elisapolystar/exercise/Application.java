package com.elisapolystar.exercise;

public class ClientApplication {

    public static void main(String[] args) throws Exception {

        String[] files = {
            "/Users/mauri/Code/Elisa/dracula.txt",
            "/Users/mauri/Code/Elisa/frankenstein.txt",
            "/Users/mauri/Code/Elisa/book3.txt",
            "/Users/mauri/Code/Elisa/copies.txt"
        };

        for (int i = 0; i < files.length; i++) {
            Thread client = new Thread(new Client("127.0.0.1", 1000+i));
            client.start();
        }
    }


}