package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class HW_Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        new HW_Client();
    }

    public HW_Client() {
       start();
       send();
    }

    private void send() {
        Scanner scanner1 = new Scanner(System.in);
        while (true) {
            try {
                if (socket.isClosed()){
                    System.out.println("Socket is closed");
                    break;
                }
                out.writeUTF(scanner1.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
        try {
            socket = new Socket("localhost",8089);
            in = new DataInputStream(socket.getInputStream());
            out =  new DataOutputStream(socket.getOutputStream());
            new Thread(() ->  {
                try {
                    while (true) {
                        final String message = in.readUTF();
                        System.out.println("Server: " + message);
                        if ("/end".equalsIgnoreCase(message)) {
                            out.writeUTF("/end");
                            System.out.println("Received command: end");
                            closeConnection();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

