import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static Socket socket = null;
    private DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static String clientName;
    private static Server serverController = null;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, Server server) {
        ClientHandler.socket = socket;
        ClientHandler.serverController = server;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Read client name
            clientName = inputStream.readUTF();
            serverController.appendMessage("Client '" + clientName + "' connected from " + socket.getInetAddress().getHostAddress());

            // Listen for messages
            while (running) {
                String message = inputStream.readUTF();
                if (message.equalsIgnoreCase("bye")) {
                    serverController.appendMessage("Client '" + clientName + "' has left the chat");
                    break;
                }
                serverController.handleClientMessage(message, this);
            }
        } catch (IOException e) {
            if (running) {
                serverController.appendMessage("Connection lost with client '" + clientName + "': " + e.getMessage());
            }
        } finally {
            serverController.handleClientDisconnect(this);
            closeConnection();
        }
    }

    public static void sendMessage(String message) {
        try {
            if (socket != null && !socket.isClosed() && outputStream != null) {
                outputStream.writeUTF(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            serverController.appendMessage("Failed to send message to '" + clientName + "': " + e.getMessage());
        }
    }

    public void closeConnection() {
        running = false;
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}