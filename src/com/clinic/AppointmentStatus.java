package com.clinic;

/**
 * Enumeration representing the lifecycle status of a clinic appointment.
 */
public enum AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED;

    public static AppointmentStatus fromString(String text) {
        if (text == null) return SCHEDULED;
        try {
            return AppointmentStatus.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return SCHEDULED;
        }
    }
}
