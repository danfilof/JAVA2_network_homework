package ru.gb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                out.writeUTF(scanner.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
        try {
            socket = new Socket("localhost",8788);
            in = new DataInputStream(socket.getInputStream());
            out =  new DataOutputStream(socket.getOutputStream());
            new Thread(() ->  {
                try {
                    while (true) {
                        final String message = in.readUTF();
                        if ("/end".equalsIgnoreCase(message)) {
                            closeConnection();
                            break;
                        }
                        System.out.println(message);
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
