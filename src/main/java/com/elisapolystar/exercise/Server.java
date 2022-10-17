package com.elisapolystar.exercise;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int port;
    private final String path;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private FileInputStream input;
    private DataOutputStream output;

    public Server(int port, String path) {
        this.port = port;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            output = new DataOutputStream(clientSocket.getOutputStream());
            loadFile(path);
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFile(String path) throws IOException {

        File file = new File(path);
        input = new FileInputStream(file);
        output.writeLong(file.length());

        int bytes = 0;
        byte[] buffer = new byte[4*1024];
        while ((bytes=input.read(buffer))!=-1){
            output.write(buffer,0,bytes);
            output.flush();
        }
    }

    private void stop() throws IOException {
        output.close();
        input.close();
        clientSocket.close();
        serverSocket.close();
    }
}
