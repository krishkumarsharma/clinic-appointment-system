package com.clinic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Dedicated persistence manager responsible for serializing and deserializing
 * domain entities to CSV disk storage.
 */
public class StorageManager {

    private final String dataDirectory;

    public StorageManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private File getFile(String filename) {
        return new File(dataDirectory, filename);
    }

    // ==================== Patients CSV ====================

    public Map<Integer, Patient> loadPatients(String filename) {
        Map<Integer, Patient> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 8) {
                    try {
                        int id = Integer.parseInt(p[0].trim());
                        String name = p[1].trim();
                        String phone = p[2].trim();
                        String email = p[3].trim();
                        int age = Integer.parseInt(p[4].trim());
                        String gender = p[5].trim();
                        String bloodGroup = p[6].trim();
                        TriageSeverity severity = TriageSeverity.fromString(p[7].trim());
                        map.put(id, new Patient(id, name, phone, email, age, gender, bloodGroup, severity));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read patients file: " + e.getMessage());
        }
        return map;
    }

    public void savePatients(String filename, Iterable<Patient> patients) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Name,Phone,Email,Age,Gender,BloodGroup,TriageSeverity");
            writer.newLine();
            for (Patient pt : patients) {
                writer.write(String.format("%d,%s,%s,%s,%d,%s,%s,%s",
                        pt.getId(),
                        escape(pt.getName()),
                        escape(pt.getPhone()),
                        escape(pt.getEmail()),
                        pt.getAge(),
                        escape(pt.getGender()),
                        escape(pt.getBloodGroup()),
                        pt.getTriageSeverity().name()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save patients: " + e.getMessage());
        }
    }

    // ==================== Doctors CSV ====================

    public Map<Integer, Doctor> loadDoctors(String filename) {
        Map<Integer, Doctor> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 7) {
                    try {
                        int id = Integer.parseInt(p[0].trim());
                        String name = p[1].trim();
                        String phone = p[2].trim();
                        String email = p[3].trim();
                        String spec = p[4].trim();
                        double fee = Double.parseDouble(p[5].trim());
                        String days = p[6].trim();
                        map.put(id, new Doctor(id, name, phone, email, spec, fee, days));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read doctors file: " + e.getMessage());
        }
        return map;
    }

    public void saveDoctors(String filename, Iterable<Doctor> doctors) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Name,Phone,Email,Specialization,ConsultationFee,AvailableDays");
            writer.newLine();
            for (Doctor d : doctors) {
                writer.write(String.format("%d,%s,%s,%s,%s,%.2f,%s",
                        d.getId(),
                        escape(d.getName()),
                        escape(d.getPhone()),
                        escape(d.getEmail()),
                        escape(d.getSpecialization()),
                        d.getConsultationFee(),
                        escape(d.getAvailableDays())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save doctors: " + e.getMessage());
        }
    }

    // ==================== Appointments CSV ====================

    public Map<Integer, Appointment> loadAppointments(String filename) {
        Map<Integer, Appointment> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 7) {
                    try {
                        int id = Integer.parseInt(p[0].trim());
                        int patientId = Integer.parseInt(p[1].trim());
                        int doctorId = Integer.parseInt(p[2].trim());
                        String date = p[3].trim();
                        String slot = p[4].trim();
                        AppointmentStatus status = AppointmentStatus.fromString(p[5].trim());
                        String notes = p[6].trim();
                        map.put(id, new Appointment(id, patientId, doctorId, date, slot, status, notes));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read appointments file: " + e.getMessage());
        }
        return map;
    }

    public void saveAppointments(String filename, Iterable<Appointment> appointments) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("AppointmentID,PatientID,DoctorID,Date,TimeSlot,Status,ClinicalNotes");
            writer.newLine();
            for (Appointment a : appointments) {
                writer.write(String.format("%d,%d,%d,%s,%s,%s,%s",
                        a.getAppointmentId(),
                        a.getPatientId(),
                        a.getDoctorId(),
                        escape(a.getAppointmentDate()),
                        escape(a.getTimeSlot()),
                        a.getStatus().name(),
                        escape(a.getClinicalNotes())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save appointments: " + e.getMessage());
        }
    }

    // ==================== CSV Utilities ====================

    private String escape(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}
