package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        LoggerUtil.log("Loading databases...");
        UserDatabase.loadFromFile();
        RecordDatabase.loadFromFile();
        LoggerUtil.log("Server starting on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LoggerUtil.log("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            LoggerUtil.log("Server stopped due to exception: " + e.getMessage());
        } finally {
            LoggerUtil.log("Server shutting down, saving data...");
            UserDatabase.saveToFile();
            RecordDatabase.saveToFile();
        }
    }
}
