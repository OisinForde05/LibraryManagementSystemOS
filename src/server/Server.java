package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {

        // Load user and record data from persistence files
        LoggerUtil.log("Loading databases...");
        UserDatabase.loadFromFile();
        RecordDatabase.loadFromFile();

        // Start server logging
        LoggerUtil.log("Server starting on port " + PORT);

        // ServerSocket listens for incoming client connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // Continuously accept new client connections and handle each in a separate thread
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LoggerUtil.log("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            // Log if server stops due to an unexpected exception
            LoggerUtil.log("Server stopped due to exception: " + e.getMessage());

        } finally {
            // Save latest data before shutdown
            LoggerUtil.log("Server shutting down, saving data...");
            UserDatabase.saveToFile();
            RecordDatabase.saveToFile();
        }
    }
}
