package com.clinic;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main application driver providing an interactive CLI menu loop
 * for the Clinic & Patient Appointment Management System.
 */
public class Main {

    private static final String DATA_DIR = "data";
    private static final ClinicService clinicService = new ClinicService(DATA_DIR);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==========================================================");
        System.out.println("   HEALTHCARE CLINIC & PATIENT APPOINTMENT SYSTEM (CLI)   ");
        System.out.println("==========================================================");

        int pCount = clinicService.getPatientCount();
        int dCount = clinicService.getDoctorCount();
        int aCount = clinicService.getAppointmentCount();
        System.out.printf("Storage Status: Loaded %d patient(s), %d doctor(s), %d appointment(s).\n", pCount, dCount, aCount);

        while (running) {
            printMainMenu();
            System.out.print("Enter your choice (1-10): ");

            String choiceInput = ValidationUtils.readLineOrNull(scanner);
            if (choiceInput == null) {
                clinicService.syncAllData();
                System.out.println("\nEnd of input detected. Data synced. Goodbye!");
                break;
            }

            String choice = choiceInput.trim();

            switch (choice) {
                case "1":
                    handleRegisterPatient(scanner);
                    break;
                case "2":
                    handleViewAllPatients();
                    break;
                case "3":
                    handleViewTriageQueue();
                    break;
                case "4":
                    handleRegisterDoctor(scanner);
                    break;
                case "5":
                    handleViewDoctors(scanner);
                    break;
                case "6":
                    handleScheduleAppointment(scanner);
                    break;
                case "7":
                    handleViewAppointments();
                    break;
                case "8":
                    handleUpdateAppointment(scanner);
                    break;
                case "9":
                    handleGenerateInvoice(scanner);
                    break;
                case "10":
                    clinicService.syncAllData();
                    System.out.println("\nAll clinic data safely saved. Thank you for using Clinic Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please enter a valid menu option between 1 and 10.\n");
                    break;
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n==========================================================");
        System.out.println("                        MAIN MENU                         ");
        System.out.println("==========================================================");
        System.out.println("  1. Register New Patient");
        System.out.println("  2. View All Patients");
        System.out.println("  3. View Emergency Triage Priority Queue");
        System.out.println("  4. Register New Doctor");
        System.out.println("  5. View / Search Doctors by Specialization");
        System.out.println("  6. Schedule Clinical Appointment");
        System.out.println("  7. View All Appointments");
        System.out.println("  8. Update Appointment Status / Complete Visit");
        System.out.println("  9. Generate Patient Billing Invoice");
        System.out.println(" 10. Exit System");
        System.out.println("==========================================================");
    }

    // ==================== 1. Register Patient ====================
    private static void handleRegisterPatient(Scanner scanner) {
        System.out.println("\n--- [1] Register New Patient ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Patient ID: ");
        if (id == null) return;

        if (clinicService.findPatientById(id).isPresent()) {
            System.out.printf("Error: Patient ID %d already registered in the system.\n", id);
            return;
        }

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Full Name: ");
        if (name == null) return;

        String phone = ValidationUtils.readPhone(scanner, "Enter Phone (e.g. +1234567890): ");
        if (phone == null) return;

        String email = ValidationUtils.readEmail(scanner, "Enter Email Address: ");
        if (email == null) return;

        Integer age = ValidationUtils.readPositiveInt(scanner, "Enter Age: ");
        if (age == null) return;

        String gender = ValidationUtils.readNonEmptyString(scanner, "Enter Gender (Male/Female/Other): ");
        if (gender == null) return;

        String blood = ValidationUtils.readNonEmptyString(scanner, "Enter Blood Group (e.g. O+, A+, B-, AB+): ");
        if (blood == null) return;

        TriageSeverity triage = ValidationUtils.readTriageSeverity(scanner);

        Patient patient = new Patient(id, name, phone, email, age, gender, blood, triage);
        boolean success = clinicService.registerPatient(patient);

        if (success) {
            System.out.println("Patient successfully registered and persisted!");
            System.out.println(patient);
        } else {
            System.out.println("Failed to register patient. Please check your inputs.");
        }
    }

    // ==================== 2. View All Patients ====================
    private static void handleViewAllPatients() {
        System.out.println("\n--- [2] View All Registered Patients ---");
        List<Patient> list = clinicService.getAllPatients();
        if (list.isEmpty()) {
            System.out.println("No patient records found.");
            return;
        }

        System.out.println("+------+----------------------+------+--------+-------+---------------+-----------------+");
        System.out.printf("| %-4s | %-20s | %-4s | %-6s | %-5s | %-13s | %-15s |\n",
                "ID", "Name", "Age", "Gender", "Blood", "Triage Level", "Phone");
        System.out.println("+------+----------------------+------+--------+-------+---------------+-----------------+");
        for (Patient p : list) {
            System.out.printf("| %-4d | %-20s | %-4d | %-6s | %-5s | %-13s | %-15s |\n",
                    p.getId(),
                    ValidationUtils.truncate(p.getName(), 20),
                    p.getAge(),
                    ValidationUtils.truncate(p.getGender(), 6),
                    ValidationUtils.truncate(p.getBloodGroup(), 5),
                    p.getTriageSeverity().name(),
                    ValidationUtils.truncate(p.getPhone(), 15));
        }
        System.out.println("+------+----------------------+------+--------+-------+---------------+-----------------+");
        System.out.printf("Total Registered Patients: %d\n", list.size());
    }

    // ==================== 3. View Triage Priority Queue ====================
    private static void handleViewTriageQueue() {
        System.out.println("\n--- [3] Emergency Triage Priority Queue ---");
        System.out.println("Patients sorted by medical urgency (CRITICAL -> HIGH -> MEDIUM -> LOW):");
        List<Patient> queue = clinicService.getTriagePriorityQueue();
        if (queue.isEmpty()) {
            System.out.println("No patients currently in triage queue.");
            return;
        }

        System.out.println("+------+----------------------+-------+---------------+---------------------------------+");
        System.out.printf("| %-4s | %-20s | %-5s | %-13s | %-31s |\n",
                "ID", "Name", "Blood", "Triage Level", "Urgency Clinical Standing");
        System.out.println("+------+----------------------+-------+---------------+---------------------------------+");
        for (Patient p : queue) {
            System.out.printf("| %-4d | %-20s | %-5s | %-13s | %-31s |\n",
                    p.getId(),
                    ValidationUtils.truncate(p.getName(), 20),
                    ValidationUtils.truncate(p.getBloodGroup(), 5),
                    p.getTriageSeverity().name(),
                    ValidationUtils.truncate(p.getTriageSeverity().getDescription(), 31));
        }
        System.out.println("+------+----------------------+-------+---------------+---------------------------------+");
        System.out.printf("Total Patients in Triage: %d\n", queue.size());
    }

    // ==================== 4. Register Doctor ====================
    private static void handleRegisterDoctor(Scanner scanner) {
        System.out.println("\n--- [4] Register New Medical Doctor ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Doctor ID: ");
        if (id == null) return;

        if (clinicService.findDoctorById(id).isPresent()) {
            System.out.printf("Error: Doctor ID %d already exists.\n", id);
            return;
        }

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Doctor Name: ");
        if (name == null) return;

        String phone = ValidationUtils.readPhone(scanner, "Enter Phone: ");
        if (phone == null) return;

        String email = ValidationUtils.readEmail(scanner, "Enter Email: ");
        if (email == null) return;

        String spec = ValidationUtils.readNonEmptyString(scanner, "Enter Specialization (e.g. Cardiology, Pediatrics, General): ");
        if (spec == null) return;

        Double fee = ValidationUtils.readDoubleInRange(scanner, "Enter Consultation Fee ($): ", 0.0, 5000.0);
        if (fee == null) return;

        String days = ValidationUtils.readNonEmptyString(scanner, "Enter Working Days (e.g. Mon-Wed-Fri): ");
        if (days == null) return;

        Doctor doctor = new Doctor(id, name, phone, email, spec, fee, days);
        boolean success = clinicService.registerDoctor(doctor);

        if (success) {
            System.out.println("Doctor registered successfully and persisted!");
            System.out.println(doctor);
        } else {
            System.out.println("Failed to register doctor.");
        }
    }

    // ==================== 5. View Doctors ====================
    private static void handleViewDoctors(Scanner scanner) {
        System.out.println("\n--- [5] View / Search Doctors ---");
        System.out.print("Filter by specialization (press Enter to list all): ");
        String query = ValidationUtils.readLineOrNull(scanner);
        List<Doctor> doctors = clinicService.searchDoctorsBySpecialization(query);

        if (doctors.isEmpty()) {
            System.out.println("No matching doctors found.");
            return;
        }

        System.out.println("+------+----------------------+----------------------+---------+----------------------+");
        System.out.printf("| %-4s | %-20s | %-20s | %-7s | %-20s |\n",
                "ID", "Doctor Name", "Specialization", "Fee ($)", "Available Days");
        System.out.println("+------+----------------------+----------------------+---------+----------------------+");
        for (Doctor d : doctors) {
            System.out.printf("| %-4d | %-20s | %-20s | %7.2f | %-20s |\n",
                    d.getId(),
                    ValidationUtils.truncate("Dr. " + d.getName(), 20),
                    ValidationUtils.truncate(d.getSpecialization(), 20),
                    d.getConsultationFee(),
                    ValidationUtils.truncate(d.getAvailableDays(), 20));
        }
        System.out.println("+------+----------------------+----------------------+---------+----------------------+");
        System.out.printf("Total Matching Doctors: %d\n", doctors.size());
    }

    // ==================== 6. Schedule Appointment ====================
    private static void handleScheduleAppointment(Scanner scanner) {
        System.out.println("\n--- [6] Schedule Clinical Appointment ---");
        Integer aptId = ValidationUtils.readPositiveInt(scanner, "Enter New Appointment ID: ");
        if (aptId == null) return;

        if (clinicService.findAppointmentById(aptId).isPresent()) {
            System.out.printf("Error: Appointment #%d already exists.\n", aptId);
            return;
        }

        Integer patientId = ValidationUtils.readPositiveInt(scanner, "Enter Patient ID: ");
        if (patientId == null) return;
        Optional<Patient> ptOpt = clinicService.findPatientById(patientId);
        if (!ptOpt.isPresent()) {
            System.out.printf("Error: Patient ID %d does not exist. Please register patient first.\n", patientId);
            return;
        }

        Integer doctorId = ValidationUtils.readPositiveInt(scanner, "Enter Doctor ID: ");
        if (doctorId == null) return;
        Optional<Doctor> docOpt = clinicService.findDoctorById(doctorId);
        if (!docOpt.isPresent()) {
            System.out.printf("Error: Doctor ID %d does not exist.\n", doctorId);
            return;
        }

        String date = ValidationUtils.readDate(scanner, "Enter Appointment Date (YYYY-MM-DD): ");
        if (date == null) return;

        String timeSlot = ValidationUtils.readNonEmptyString(scanner, "Enter Time Slot (e.g. 10:00 AM, 02:30 PM): ");
        if (timeSlot == null) return;

        if (clinicService.hasSchedulingConflict(doctorId, date, timeSlot)) {
            System.out.printf("Error: Scheduling conflict! Dr. %s already has a scheduled appointment on %s at %s.\n",
                    docOpt.get().getName(), date, timeSlot);
            return;
        }

        String notes = ValidationUtils.readNonEmptyString(scanner, "Enter Reason for Visit / Symptoms: ");
        if (notes == null) return;

        Appointment apt = new Appointment(aptId, patientId, doctorId, date, timeSlot, AppointmentStatus.SCHEDULED, notes);
        boolean success = clinicService.scheduleAppointment(apt);

        if (success) {
            System.out.println("Appointment successfully scheduled and recorded!");
            System.out.println(apt);
        } else {
            System.out.println("Failed to schedule appointment.");
        }
    }

    // ==================== 7. View Appointments ====================
    private static void handleViewAppointments() {
        System.out.println("\n--- [7] View All Appointments ---");
        List<Appointment> list = clinicService.getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("No appointments scheduled.");
            return;
        }

        System.out.println("+--------+-----------+----------+------------+----------+-----------+----------------------+");
        System.out.printf("| %-6s | %-9s | %-8s | %-10s | %-8s | %-9s | %-20s |\n",
                "Apt ID", "PatientID", "DoctorID", "Date", "Slot", "Status", "Clinical Notes");
        System.out.println("+--------+-----------+----------+------------+----------+-----------+----------------------+");
        for (Appointment a : list) {
            System.out.printf("| %-6d | %-9d | %-8d | %-10s | %-8s | %-9s | %-20s |\n",
                    a.getAppointmentId(),
                    a.getPatientId(),
                    a.getDoctorId(),
                    a.getAppointmentDate(),
                    ValidationUtils.truncate(a.getTimeSlot(), 8),
                    a.getStatus().name(),
                    ValidationUtils.truncate(a.getClinicalNotes(), 20));
        }
        System.out.println("+--------+-----------+----------+------------+----------+-----------+----------------------+");
        System.out.printf("Total Appointments: %d\n", list.size());
    }

    // ==================== 8. Update Appointment ====================
    private static void handleUpdateAppointment(Scanner scanner) {
        System.out.println("\n--- [8] Update Appointment Status / Clinical Visit ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Appointment ID to update: ");
        if (id == null) return;

        Optional<Appointment> aptOpt = clinicService.findAppointmentById(id);
        if (!aptOpt.isPresent()) {
            System.out.printf("Appointment #%d not found.\n", id);
            return;
        }

        Appointment existing = aptOpt.get();
        System.out.printf("Current Status: %s | Date: %s | Notes: %s\n",
                existing.getStatus(), existing.getAppointmentDate(), existing.getClinicalNotes());

        System.out.println("Select New Status:");
        System.out.println("  1. COMPLETED  (Consultation finished)");
        System.out.println("  2. CANCELLED  (Appointment revoked)");
        System.out.println("  3. SCHEDULED  (Active / Unchanged)");
        System.out.print("Enter choice (1-3): ");
        String sc = ValidationUtils.readLineOrNull(scanner);
        if (sc == null) return;

        AppointmentStatus newStatus = existing.getStatus();
        if ("1".equals(sc.trim())) newStatus = AppointmentStatus.COMPLETED;
        else if ("2".equals(sc.trim())) newStatus = AppointmentStatus.CANCELLED;
        else if ("3".equals(sc.trim())) newStatus = AppointmentStatus.SCHEDULED;

        String notes = ValidationUtils.readNonEmptyString(scanner, "Enter Consultation / Physician Outcome Notes: ");
        if (notes == null) return;

        boolean success = clinicService.updateAppointmentStatus(id, newStatus, notes);
        if (success) {
            System.out.printf("Appointment #%d updated successfully to %s.\n", id, newStatus);
        } else {
            System.out.println("Failed to update appointment.");
        }
    }

    // ==================== 9. Generate Invoice ====================
    private static void handleGenerateInvoice(Scanner scanner) {
        System.out.println("\n--- [9] Generate Patient Billing Invoice ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Appointment ID: ");
        if (id == null) return;

        String invoice = clinicService.generateInvoiceSummary(id);
        System.out.println(invoice);
    }
}
