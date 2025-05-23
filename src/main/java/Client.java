import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        System.out.println("Client is running...");
        // connect through the local host and open the port for 5000
        try (Socket socket = new Socket("localhost", 5000);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server: " + socket.getInetAddress().getHostAddress());

            while (true) {
                System.out.print("You: ");
                // server can give msg to client
                String message = scanner.nextLine();
                ClientHandler.sendMessage(message);

                // user give bye then server will exit
                if (message.equalsIgnoreCase("bye")) {
                    break;
                }

                String serverMsg = in.readUTF();
                if (serverMsg.equalsIgnoreCase("bye")) {
                    System.out.println("Server has exited the chat.");
                    break;
                }

                System.out.println("Server: " + serverMsg);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
