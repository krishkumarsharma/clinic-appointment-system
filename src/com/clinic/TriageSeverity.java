package com.clinic;

/**
 * Enumeration representing clinical triage severity levels for patient prioritization.
 */
public enum TriageSeverity {
    LOW("Routine / Non-urgent", 4),
    MEDIUM("Moderate / Needs Attention", 3),
    HIGH("Urgent / High Priority", 2),
    CRITICAL("Immediate Emergency", 1);

    private final String description;
    private final int priorityRank; // Lower rank number = higher urgency

    TriageSeverity(String description, int priorityRank) {
        this.description = description;
        this.priorityRank = priorityRank;
    }

    public String getDescription() {
        return description;
    }

    public int getPriorityRank() {
        return priorityRank;
    }

    public static TriageSeverity fromString(String text) {
        if (text == null) return LOW;
        try {
            return TriageSeverity.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return LOW;
        }
    }
}
