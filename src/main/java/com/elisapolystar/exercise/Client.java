package com.elisapolystar.exercise;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Client implements Runnable {

    private final String serverHost;
    private final int serverPort;
    private Map<String, Integer> wordMap;
    private Socket clientSocket;
    private DataInputStream input;
    private ByteArrayOutputStream output;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(serverHost, serverPort);
            System.out.println("Connecting to " + serverHost + ":" + serverPort);

            input = new DataInputStream(clientSocket.getInputStream());
            output = new ByteArrayOutputStream();

            processText(loadFile());
            printTopWords(5);
            stop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFile() {
        try {
            int bytes = 0;
            long size = input.readLong();
            byte[] buffer = new byte[4*1024];
            while (size > 0 && (bytes = input.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                output.write(buffer,0,bytes);
                size -= bytes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private void stop() {
        try {
            output.close();
            input.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processText(String text) {

        wordMap = new HashMap<>();
        String[] lines = text.split(System.lineSeparator());

        for (String line : lines) {
            line = line.replaceAll("\\p{Punct}", "");
            for (String word : line.split(" ")) {
                Integer count = 1;
                if(wordMap.containsKey(word))
                    count += wordMap.get(word);
                wordMap.put(word, count);
            }
        }
    }

    private void printTopWords(int amount) {

        Map<String, Integer> topWords = wordMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(amount)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        topWords.entrySet().stream().forEach(System.out::println);
        System.out.println();
    }
}
