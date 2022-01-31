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
       receive();
       send();
    }

    private void send() {
        Scanner scanner1 = new Scanner(System.in);
        // В бесконечном цикле, отсылать все, что получил scanner1. В случае закрытия сокета, цикл прекращается
        while (true) {
            try {
                if (socket.isClosed()){
                    System.out.println("Socket is closed");
                    break;
                    // При нажатии на enter или попытке что-либо послать, срабатывает вышеуказанный код, так как сокет уже закрыт, цикл прекращается,
                    // ошибка обрабатывается с exit code 0
                }
                out.writeUTF(scanner1.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void receive() {
        try {
            socket = new Socket("localhost",8089);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            // Запускаю поток, считывающий все, приходящее на порт
            new Thread(() ->  {
                try {
                    // В бесконечном цикле, получаю все сообщения, приходящие на порт. Если оно значит "/end", отсылаю обратно то же сообщение и прерываю цикл после подтверждения в консоли
                    while (true) {
                        final String message = in.readUTF();
                        if ("/end".equalsIgnoreCase(message)) {
                            out.writeUTF("/end");
                            System.out.println("Received command: END");
                            closeConnection();
                            break;
                        }
                        System.out.println("Server: " + message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 // При одном из трех сценариев сокет закрывается. Если не срабатывает одно условие или дает исключение, ошибка обрабатывается и метод работает дальше
    private void closeConnection() {
        if (in != null) {
            try {
                in.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

