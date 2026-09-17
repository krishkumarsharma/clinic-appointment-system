package com.clinic;

import java.util.Objects;

/**
 * Model class representing a Clinical Appointment linking a Patient and Doctor.
 */
public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private String appointmentDate; // Format: YYYY-MM-DD
    private String timeSlot;        // Format: HH:MM AM/PM
    private AppointmentStatus status;
    private String clinicalNotes;

    public Appointment() {
        this.status = AppointmentStatus.SCHEDULED;
    }

    public Appointment(int appointmentId, int patientId, int doctorId, String appointmentDate, String timeSlot, AppointmentStatus status, String clinicalNotes) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.status = status != null ? status : AppointmentStatus.SCHEDULED;
        this.clinicalNotes = clinicalNotes != null ? clinicalNotes : "N/A";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status != null ? status : AppointmentStatus.SCHEDULED;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes != null ? clinicalNotes : "N/A";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return appointmentId == that.appointmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }

    @Override
    public String toString() {
        return String.format("Appointment [ID=%d, PatientID=%d, DoctorID=%d, Date='%s', Time='%s', Status=%s, Notes='%s']",
                appointmentId, patientId, doctorId, appointmentDate, timeSlot, status.name(), clinicalNotes);
    }
}
