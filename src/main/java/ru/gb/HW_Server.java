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
       receive();
        send();
        }

        private void send() {
            String s;
            Scanner scanner2 = new Scanner(System.in);
            s = scanner2.nextLine();
            // По какой-то причине пришлось делать такой небольшой костыль для первого сообщения, иначе IDE выдвала nullpointerexception на первое введенное из scanner2 значение
            try {
                out.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // В бесконечном цикле, отсылать все, что получил scanner2. В случае закрытия сокета, цикл прекращается
            while (true) {
                try {
                    if (socket.isClosed()) {
                        System.out.println("Socket is closed");
                        break;
                        // При нажатии на enter или попытке что-либо послать, срабатывает вышеуказанный код, так как сокет уже закрыт, цикл прекращается,
                    // ошибка обрабатывается с exit code 0
                    }
                    out.writeUTF(scanner2.nextLine());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    private void receive() {
         socket = null;
         // Запускаю поток, считывающий все, приходящее на порт
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(8089)) {
                System.out.println("Server is on, waiting for connection...");
                socket = serverSocket.accept();
                // Добавил возможность увидеть имя клиента, который присоединился
                String hostName = socket.getInetAddress().getHostName();
                System.out.printf("The client %s has connected.", hostName);
                System.out.println("\n");
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                // В бесконечном цикле, получаю все сообщения, приходящие на порт. Если оно значит "/end", отсылаю обратно то же сообщение и прерываю цикл после подтверждения в консоли
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
            // Если на сокете ничего нет, закрываю его
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