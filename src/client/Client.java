package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            // First line from server
            String welcome = input.readLine();
            if (welcome != null) {
                System.out.println(welcome);
            }

            while (true) {
                System.out.println();
                System.out.println("Menu:");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Create Record");
                System.out.println("4. Get Records");
                System.out.println("5. Assign Record (Librarian)");
                System.out.println("6. Update Status");
                System.out.println("7. Update Password");
                System.out.println("8. View Assigned (Librarian)");
                System.out.println("9. Exit");
                System.out.print("Select option: ");

                String choice = scanner.nextLine().trim();
                String cmd;

                switch (choice) {
                    case "1":
                        System.out.println("Enter: name studentId email password department role");
                        System.out.print("> ");
                        String regLine = scanner.nextLine();
                        cmd = "REGISTER " + regLine;
                        break;

                    case "2":
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Password: ");
                        String password = scanner.nextLine().trim();
                        cmd = "LOGIN " + email + " " + password;
                        break;

                    case "3":
                        System.out.print("Record type (e.g. NewBook or BorrowRequest): ");
                        String type = scanner.nextLine().trim();
                        cmd = "CREATE_RECORD " + type;
                        break;

                    case "4":
                        cmd = "GET_RECORDS";
                        break;

                    case "5":
                        System.out.print("Record ID: ");
                        String rid = scanner.nextLine().trim();
                        System.out.print("Librarian ID: ");
                        String lid = scanner.nextLine().trim();
                        cmd = "ASSIGN_RECORD " + rid + " " + lid;
                        break;

                    case "6":
                        System.out.print("Record ID: ");
                        String rid2 = scanner.nextLine().trim();
                        System.out.print("New status (Available/Requested/Borrowed/Returned): ");
                        String status = scanner.nextLine().trim();
                        cmd = "UPDATE_STATUS " + rid2 + " " + status;
                        break;

                    case "7":
                        System.out.print("New password: ");
                        String newPass = scanner.nextLine().trim();
                        cmd = "UPDATE_PASSWORD " + newPass;
                        break;

                    case "8":
                        cmd = "VIEW_ASSIGNED";
                        break;

                    case "9":
                        cmd = "EXIT";
                        output.println(cmd);
                        String resp = input.readLine();
                        if (resp != null) {
                            System.out.println(resp);
                        }
                        System.out.println("Client closing.");
                        return;

                    default:
                        System.out.println("Invalid option.");
                        continue;
                }

                // Send command to server
                output.println(cmd);

                // Read response(s) from server
                while (true) {
                    String response = input.readLine();
                    if (response == null) {
                        System.out.println("Server closed the connection.");
                        return;
                    }
                    if (response.equals("END_OF_RECORDS")) {
                        break;
                    }
                    System.out.println(response);
                    if (!input.ready()) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
