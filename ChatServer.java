package chatapplication;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static Map<Integer, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("connection done with new user ");
                new ClientHandler(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int userId;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {

                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                out = new PrintWriter(socket.getOutputStream(), true);


                userId = Integer.parseInt(in.readLine());

                clients.put(userId, out);

                String message;

                while ((message = in.readLine()) != null) {

                    String[] parts = message.split(":", 2);

                    int receiverId = Integer.parseInt(parts[0]);
                    String msg = parts[1];

                    PrintWriter receiver = clients.get(receiverId);

                    if (receiver != null) {
                        receiver.println(userId + ":" + msg);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}