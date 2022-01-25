package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class HW_Server {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        new HW_Server();
    }
    public HW_Server() {
        start();
        Scanner scanner2 = new Scanner(System.in);
           System.out.println("Message to be sent to client: " + scanner2.nextLine());

        while (true) {
            try {
                if (socket.isClosed()){
                    break;
                }
                out.writeUTF(scanner2.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
         socket = null;

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8778)) {
                System.out.println("Server is on, waiting for connection...");
                socket = serverSocket.accept();
                System.out.println("The client has connected.");
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    final String message = in.readUTF();
                    if ("/end".equalsIgnoreCase(message)) {
                        out.writeUTF("/end");
                        break;
                    }
                    System.out.println("Client:" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}