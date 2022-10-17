package com.elisapolystar.exercise.v2;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {

    private final String serverHost;
    private final int serverPort;
    private Socket clientSocket;
    private DataInputStream input; // from server
    private ByteArrayOutputStream output; // to user

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

            System.out.println("File lenght: " + receiveFile().length());
            stop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveFile() {
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
}
