package com.clinic;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Defensive utility class providing bulletproof input validation,
 * stream sanitation, and formatting routines.
 */
public final class ValidationUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private ValidationUtils() {
        // Prevent instantiation of utility class
    }

    /**
     * Safely reads a line from Scanner without throwing NoSuchElementException on EOF.
     *
     * @param scanner Scanner instance
     * @return trimmed string or null if stream reaches EOF
     */
    public static String readLineOrNull(Scanner scanner) {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
        return null;
    }

    /**
     * Prompts for a non-empty string.
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Error: Input cannot be blank. Please try again.");
        }
    }

    /**
     * Prompts for a strictly positive integer (> 0).
     */
    public static Integer readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            try {
                int val = Integer.parseInt(line.trim());
                if (val > 0) {
                    return val;
                }
                System.out.println("Error: Value must be a positive integer (> 0).");
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid whole number.");
            }
        }
    }

    /**
     * Prompts for a floating point number within [min, max].
     */
    public static Double readDoubleInRange(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            try {
                double val = Double.parseDouble(line.trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Error: Value must be between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid decimal number.");
            }
        }
    }

    /**
     * Prompts for a valid telephone number.
     */
    public static String readPhone(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (PHONE_PATTERN.matcher(line).matches()) {
                return line;
            }
            System.out.println("Error: Invalid phone number format (7-15 digits, optional '+' prefix).");
        }
    }

    /**
     * Prompts for a valid email address.
     */
    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (EMAIL_PATTERN.matcher(line).matches()) {
                return line;
            }
            System.out.println("Error: Invalid email format (e.g. user@example.com).");
        }
    }

    /**
     * Prompts for a calendar date in YYYY-MM-DD format.
     */
    public static String readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (DATE_PATTERN.matcher(line).matches()) {
                return line;
            }
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD (e.g. 2026-10-15).");
        }
    }

    /**
     * Prompts user to select a triage severity level (1-4).
     */
    public static TriageSeverity readTriageSeverity(Scanner scanner) {
        System.out.println("Select Triage Severity Level:");
        System.out.println("  1. LOW      - Routine / Non-urgent");
        System.out.println("  2. MEDIUM   - Moderate / Needs attention");
        System.out.println("  3. HIGH     - Urgent priority");
        System.out.println("  4. CRITICAL - Immediate emergency triage");
        while (true) {
            System.out.print("Enter severity choice (1-4) [Default 1]: ");
            String line = readLineOrNull(scanner);
            if (line == null) return TriageSeverity.LOW;
            line = line.trim();
            if (line.isEmpty() || line.equals("1")) return TriageSeverity.LOW;
            if (line.equals("2")) return TriageSeverity.MEDIUM;
            if (line.equals("3")) return TriageSeverity.HIGH;
            if (line.equals("4")) return TriageSeverity.CRITICAL;
            System.out.println("Invalid selection. Enter a number between 1 and 4.");
        }
    }

    /**
     * Truncates strings exceeding column widths to prevent ASCII table distortion.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
