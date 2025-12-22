package utils;

import models.LibraryRecord;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class RecordDatabase {

    // Thread-safe list storing all library records
    private static final List<LibraryRecord> records = new CopyOnWriteArrayList<>();

    // File path used for persistence storage
    private static final String FILE_PATH = "records.txt";

    // Creates a new record and saves to file
    public static synchronized LibraryRecord createRecord(String recordType, String studentId, String status) {
        LibraryRecord record = new LibraryRecord(recordType, studentId, status);
        records.add(record);
        saveToFile();
        return record;
    }

    // Returns a copy of all records
    public static synchronized List<LibraryRecord> getAllRecords() {
        return new ArrayList<>(records);
    }

    // Returns records assigned to a specific librarian
    public static synchronized List<LibraryRecord> getRecordsByLibrarian(String librarianId) {
        List<LibraryRecord> list = new ArrayList<>();
        for (LibraryRecord r : records) {
            if (r.getLibrarianId() != null && r.getLibrarianId().equalsIgnoreCase(librarianId)) {
                list.add(r);
            }
        }
        return list;
    }

    // Fetch record by ID (used for updates and assignments)
    public static synchronized LibraryRecord getRecordById(int id) {
        for (LibraryRecord r : records) {
            if (r.getRecordId() == id) return r;
        }
        return null;
    }

    // Assigns a record to a librarian and updates its status
    public static synchronized boolean assignRecord(int id, String librarianId) {
        LibraryRecord record = getRecordById(id);
        if (record != null) {
            record.setLibrarianId(librarianId);
            record.setStatus("Requested");
            saveToFile();
            return true;
        }
        return false;
    }

    // Updates the status of a record (Borrowed, Returned, etc.)
    public static synchronized boolean updateStatus(int id, String status) {
        LibraryRecord record = getRecordById(id);
        if (record != null) {
            record.setStatus(status);
            saveToFile();
            return true;
        }
        return false;
    }

    // Writes all records to disk for persistence
    public static synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (LibraryRecord r : records) {
                writer.println(r.toPersistString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads records from disk at server startup
    public static synchronized void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        List<LibraryRecord> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LibraryRecord r = LibraryRecord.fromPersistString(line);
                if (r != null) loaded.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        records.clear();
        records.addAll(loaded);
    }
}
