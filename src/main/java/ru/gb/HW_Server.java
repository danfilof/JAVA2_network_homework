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
        send();
        }

        private void send() {
            String s;
            Scanner scanner2 = new Scanner(System.in);
            s = scanner2.nextLine();
            try {
                out.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    if (socket.isClosed()) {
                        System.out.println("Socket is closed");
                        break;
                    }
                    out.writeUTF(scanner2.nextLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    private void start() {
         socket = null;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8089)) {
                System.out.println("Server is on, waiting for connection...");
                socket = serverSocket.accept();
                String hostName = socket.getInetAddress().getHostName();
                System.out.printf("The client %s has connected.", hostName);
                System.out.println("\n");
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    final String message = in.readUTF();
                    if ("/end".equalsIgnoreCase(message)) {
                        out.writeUTF("/end");
                        System.out.println("Received command END");
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