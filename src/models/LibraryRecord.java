package models;

import java.time.LocalDate;

public class LibraryRecord {

    // Tracks next available record ID for new records
    private static int nextId = 1;

    // Record fields stored in the system
    private int recordId;
    private String recordType;
    private LocalDate date;
    private String studentId;
    private String status;
    private String librarianId;

    // Constructor for creating a new record (fresh request)
    public LibraryRecord(String recordType, String studentId, String status) {
        this.recordId = nextId++;
        this.recordType = recordType;
        this.date = LocalDate.now();
        this.studentId = studentId;
        this.status = status;
        this.librarianId = "";
    }

    // Constructor used when loading from saved file (preserves ID and date)
    public LibraryRecord(int recordId, String recordType, LocalDate date, String studentId, String status, String librarianId) {
        this.recordId = recordId;
        this.recordType = recordType;
        this.date = date;
        this.studentId = studentId;
        this.status = status;
        this.librarianId = (librarianId == null) ? "" : librarianId;

        // Update nextId so IDs remain unique after loading
        if (recordId >= nextId) nextId = recordId + 1;
    }

    // Basic getters
    public int getRecordId() {
        return recordId;
    }

    public String getRecordType() {
        return recordType;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    public String getLibrarianId() {
        return librarianId;
    }

    // Update record status or assigned librarian
    public void setStatus(String status) {
        this.status = status;
    }

    public void setLibrarianId(String librarianId) {
        this.librarianId = librarianId;
    }

    // Readable record output for displaying to clients
    @Override
    public String toString() {
        return "RecordID: " + recordId + ", Type: " + recordType + ", Date: " + date +
                ", StudentID: " + studentId + ", Status: " + status + ", LibrarianID: " + librarianId;
    }

    // Converts record into a file-safe format
    public String toPersistString() {
        return recordId + "|" + escape(recordType) + "|" + date.toString() + "|" + escape(studentId) + "|" + escape(status) + "|" + escape(librarianId);
    }

    // Reconstructs a LibraryRecord from a stored text line
    public static LibraryRecord fromPersistString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        try {
            int id = Integer.parseInt(p[0]);
            String type = unescape(p[1]);
            LocalDate date = LocalDate.parse(p[2]);
            String studentId = unescape(p[3]);
            String status = unescape(p[4]);
            String librarianId = unescape(p[5]);
            return new LibraryRecord(id, type, date, studentId, status, librarianId);
        } catch (Exception e) {
            return null;
        }
    }

    // Escapes special characters so data can be safely stored
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("|", "\\|");
    }

    // Restores original text from escaped data
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\|", "|");
    }
}
