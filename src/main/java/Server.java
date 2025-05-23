import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(5000);
             Socket socket = serverSocket.accept();
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

            LocalTime startTime = LocalTime.now();
            System.out.println("server start time :"+ startTime);

            while (true) {
                System.out.println("Server is running...");
                String clientMsg = in.readUTF();
                if (clientMsg.equalsIgnoreCase("bye")) {
                    System.out.println("Client has exited the chat.");
                    break;
                }

                if (clientMsg.equalsIgnoreCase("help")) {
                    // showing user to commands can be run
                    String cmds = "YOU CAN give commands :  help,date,bye,time";
                    out.writeUTF(cmds);
                    out.flush();
                }

                if (clientMsg.equalsIgnoreCase("date")) {
                    // show the local date to the user
                    String date = LocalDate.now()+"";
                    System.out.println("user asked for date " + date);
                    out.writeUTF(date);
                    out.flush();
                }

                if (clientMsg.equalsIgnoreCase("time")) {
                    // show the user to current time
                    String time = LocalTime.now()+"";
                    out.writeUTF(time);
                    out.flush();
                }

                System.out.println("Client: " + clientMsg);

                System.out.print("You: ");
                String response = scanner.nextLine();
                out.writeUTF(response);
                out.flush();

                if (response.equalsIgnoreCase("bye")) {
                    break;
                }
            }

            System.out.println("Server is shutting down...");
            LocalTime endTime = LocalTime.now();
            System.out.println("server end time :"+ endTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendMessage(String s) {

    }

    public void handleClientMessage(String message, ClientHandler clientHandler) {

    }

    public void handleClientDisconnect(ClientHandler clientHandler) {

    }
}
