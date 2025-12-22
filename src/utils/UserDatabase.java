package utils;

import models.User;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class UserDatabase {

    // Thread-safe list storing all registered users
    private static final List<User> users = new CopyOnWriteArrayList<>();

    // File path for saving and loading user data
    private static final String FILE_PATH = "users.txt";

    // Registers a new user if email and student ID are unique
    public static synchronized boolean register(User user) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail()) || u.getStudentId().equalsIgnoreCase(user.getStudentId())) {
                return false;
            }
        }
        users.add(user);
        saveToFile();
        return true;
    }

    // Looks up user by email and password for login
    public static synchronized User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // Updates password for a user and stores changes
    public static synchronized boolean updatePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.length() < 4) return false;
        user.setPassword(newPassword);
        saveToFile();
        return true;
    }

    // Returns an unmodifiable copy of all users
    public static synchronized List<User> getAllUsers() {
        return List.copyOf(users);
    }

    // Saves all users to file for persistence
    public static synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                writer.println(u.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads user records from persistence at server startup
    public static synchronized void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User u = User.fromString(line);
                if (u != null) users.add(u);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
