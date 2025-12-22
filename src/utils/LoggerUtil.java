package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {

    // Standardized timestamp format for server log messages
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Prints a message to console with a timestamp prefix
    public static void log(String message) {
        System.out.println("[" + LocalDateTime.now().format(format) + "] " + message);
    }
}
