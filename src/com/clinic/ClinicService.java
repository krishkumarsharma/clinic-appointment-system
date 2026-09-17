package com.clinic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service orchestrator managing clinical business workflows, triage queuing,
 * appointment scheduling conflict checks, and persistence synchronization.
 */
public class ClinicService {

    private final Map<Integer, Patient> patientMap;
    private final Map<Integer, Doctor> doctorMap;
    private final Map<Integer, Appointment> appointmentMap;
    private final StorageManager storageManager;

    private static final String PATIENTS_FILE = "patients.csv";
    private static final String DOCTORS_FILE = "doctors.csv";
    private static final String APPOINTMENTS_FILE = "appointments.csv";

    public ClinicService(String dataDirectory) {
        this.storageManager = new StorageManager(dataDirectory);
        this.patientMap = new LinkedHashMap<>();
        this.doctorMap = new LinkedHashMap<>();
        this.appointmentMap = new LinkedHashMap<>();
        loadAllData();
    }

    public void loadAllData() {
        patientMap.clear();
        doctorMap.clear();
        appointmentMap.clear();

        patientMap.putAll(storageManager.loadPatients(PATIENTS_FILE));
        doctorMap.putAll(storageManager.loadDoctors(DOCTORS_FILE));
        appointmentMap.putAll(storageManager.loadAppointments(APPOINTMENTS_FILE));
    }

    public void syncAllData() {
        storageManager.savePatients(PATIENTS_FILE, patientMap.values());
        storageManager.saveDoctors(DOCTORS_FILE, doctorMap.values());
        storageManager.saveAppointments(APPOINTMENTS_FILE, appointmentMap.values());
    }

    // ==================== Patient Operations ====================

    public boolean registerPatient(Patient patient) {
        if (patient == null || patientMap.containsKey(patient.getId())) {
            return false;
        }
        patientMap.put(patient.getId(), patient);
        storageManager.savePatients(PATIENTS_FILE, patientMap.values());
        return true;
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public Optional<Patient> findPatientById(int id) {
        return Optional.ofNullable(patientMap.get(id));
    }

    public boolean deletePatient(int id) {
        Patient removed = patientMap.remove(id);
        if (removed != null) {
            storageManager.savePatients(PATIENTS_FILE, patientMap.values());
            return true;
        }
        return false;
    }

    /**
     * Generates a triage-prioritized patient queue where CRITICAL and HIGH
     * priority patients are sorted ahead of MEDIUM and LOW patients.
     */
    public List<Patient> getTriagePriorityQueue() {
        return patientMap.values().stream()
                .sorted(Comparator.comparingInt((Patient p) -> p.getTriageSeverity().getPriorityRank())
                        .thenComparing(Patient::getId))
                .collect(Collectors.toList());
    }

    // ==================== Doctor Operations ====================

    public boolean registerDoctor(Doctor doctor) {
        if (doctor == null || doctorMap.containsKey(doctor.getId())) {
            return false;
        }
        doctorMap.put(doctor.getId(), doctor);
        storageManager.saveDoctors(DOCTORS_FILE, doctorMap.values());
        return true;
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctorMap.values());
    }

    public Optional<Doctor> findDoctorById(int id) {
        return Optional.ofNullable(doctorMap.get(id));
    }

    public List<Doctor> searchDoctorsBySpecialization(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDoctors();
        }
        String q = query.trim().toLowerCase();
        return doctorMap.values().stream()
                .filter(d -> d.getSpecialization().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // ==================== Appointment Operations ====================

    /**
     * Checks if a doctor already has an active appointment for a given date and time slot.
     */
    public boolean hasSchedulingConflict(int doctorId, String date, String timeSlot) {
        return appointmentMap.values().stream()
                .filter(a -> a.getDoctorId() == doctorId)
                .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED)
                .filter(a -> a.getAppointmentDate().equalsIgnoreCase(date))
                .anyMatch(a -> a.getTimeSlot().equalsIgnoreCase(timeSlot));
    }

    /**
     * Schedules a new appointment with verification of patient existence,
     * doctor existence, and conflict detection.
     */
    public boolean scheduleAppointment(Appointment appointment) {
        if (appointment == null || appointmentMap.containsKey(appointment.getAppointmentId())) {
            return false;
        }
        if (!patientMap.containsKey(appointment.getPatientId())) {
            return false;
        }
        if (!doctorMap.containsKey(appointment.getDoctorId())) {
            return false;
        }
        if (hasSchedulingConflict(appointment.getDoctorId(), appointment.getAppointmentDate(), appointment.getTimeSlot())) {
            return false; // Conflict exists
        }

        appointmentMap.put(appointment.getAppointmentId(), appointment);
        storageManager.saveAppointments(APPOINTMENTS_FILE, appointmentMap.values());
        return true;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentMap.values());
    }

    public Optional<Appointment> findAppointmentById(int id) {
        return Optional.ofNullable(appointmentMap.get(id));
    }

    public boolean updateAppointmentStatus(int appointmentId, AppointmentStatus status, String notes) {
        Appointment apt = appointmentMap.get(appointmentId);
        if (apt == null) {
            return false;
        }
        apt.setStatus(status);
        if (notes != null && !notes.trim().isEmpty()) {
            apt.setClinicalNotes(notes.trim());
        }
        storageManager.saveAppointments(APPOINTMENTS_FILE, appointmentMap.values());
        return true;
    }

    public boolean cancelAppointment(int appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED, "Cancelled by administrative desk.");
    }

    /**
     * Generates a formal invoice summary for a completed or active appointment.
     */
    public String generateInvoiceSummary(int appointmentId) {
        Appointment apt = appointmentMap.get(appointmentId);
        if (apt == null) return "Error: Appointment not found.";

        Patient pt = patientMap.get(apt.getPatientId());
        Doctor doc = doctorMap.get(apt.getDoctorId());

        if (pt == null || doc == null) return "Error: Corrupted appointment references.";

        double baseFee = doc.getConsultationFee();
        double emergencySurcharge = pt.getTriageSeverity() == TriageSeverity.CRITICAL ? 50.0 :
                                   pt.getTriageSeverity() == TriageSeverity.HIGH ? 25.0 : 0.0;
        double total = baseFee + emergencySurcharge;

        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("               CLINICAL BILLING INVOICE                \n");
        sb.append("=======================================================\n");
        sb.append(String.format("Invoice Ref     : INV-%05d\n", apt.getAppointmentId()));
        sb.append(String.format("Appointment ID  : #%d\n", apt.getAppointmentId()));
        sb.append(String.format("Date & Time     : %s at %s\n", apt.getAppointmentDate(), apt.getTimeSlot()));
        sb.append(String.format("Patient Name    : %s (ID: %d, %s)\n", pt.getName(), pt.getId(), pt.getBloodGroup()));
        sb.append(String.format("Triage Level    : %s\n", pt.getTriageSeverity().name()));
        sb.append(String.format("Attending Doctor: Dr. %s (%s)\n", doc.getName(), doc.getSpecialization()));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format("Base Consultation Fee  : $%.2f\n", baseFee));
        sb.append(String.format("Triage Urgency Charge  : $%.2f\n", emergencySurcharge));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format("TOTAL AMOUNT PAYABLE   : $%.2f\n", total));
        sb.append(String.format("Payment Status         : %s\n", apt.getStatus() == AppointmentStatus.COMPLETED ? "PAID" : "PENDING"));
        sb.append("=======================================================\n");
        return sb.toString();
    }

    public int getPatientCount() {
        return patientMap.size();
    }

    public int getDoctorCount() {
        return doctorMap.size();
    }

    public int getAppointmentCount() {
        return appointmentMap.size();
    }
}
