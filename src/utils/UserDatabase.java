package utils;

import models.User;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class UserDatabase {
    private static final List<User> users = new CopyOnWriteArrayList<>();
    private static final String FILE_PATH = "users.txt";

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

    public static synchronized User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static synchronized boolean updatePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.length() < 4) return false;
        user.setPassword(newPassword);
        saveToFile();
        return true;
    }

    public static synchronized List<User> getAllUsers() {
        return List.copyOf(users);
    }

    public static synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                writer.println(u.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
