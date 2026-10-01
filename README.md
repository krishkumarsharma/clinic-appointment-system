# Healthcare Clinic & Patient Appointment System

A console-based clinic management system written in Java. This project was developed as part of the Object-Oriented Programming (Java) flipped course curriculum at VIT Bhopal University.

It provides a clean, dependency-free command-line interface for clinic staff to register patients, manage doctor schedules, prioritize emergency cases through clinical triage, prevent double-bookings, and generate billing invoices with persistent CSV storage.

---

## Author & Project Info

- **Student Name**: Krish Kumar
- **Registration Number**: 25BAI10528
- **Email**: [krish.25bai10528@vitbhopal.ac.in](mailto:krish.25bai10528@vitbhopal.ac.in)
- **GitHub**: [@krishkumarsharma](https://github.com/krishkumarsharma)
- **Institution**: VIT Bhopal University
- **Course**: Object-Oriented Programming in Java (Flipped Course)
- **Platform**: VITyarthi Project Submission

### Documentation Links
- **Project Report**: [PROJECT_REPORT.md](PROJECT_REPORT.md)
- **Project Scope & Statement**: [statement.md](statement.md)
- **Compiled PDF Report**: [PROJECT_REPORT.pdf](PROJECT_REPORT.pdf)

---

## Why I Built This

Small clinics and outpatient departments often rely on handwritten logs or scattered spreadsheets to track patients and visits. This frequently results in two major issues:
1. **Accidental double-booking** of doctors in the same time slot.
2. **Lack of triage prioritization**, where patients with severe or emergency symptoms wait behind routine check-ups.

This project addresses these problems with a focused, reliable console application that enforces scheduling validation, organizes patients by urgency rank, and keeps all records saved in local CSV files across runs.

---

## Key Features

1. **Patient Intake & Triage Classification**:
   - Register patient profiles with name, age, contact details, blood group, and emergency triage level (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`).
2. **Emergency Triage Priority Queue**:
   - Automatically sort the patient waiting list based on clinical urgency using Java Stream comparators so emergency patients are addressed first.
3. **Doctor Directory**:
   - Add physicians with their specialty, consultation charges, and available working days.
   - Filter and search doctors by specialization (e.g., Cardiology, Pediatrics, General Medicine).
4. **Conflict-Free Appointment Scheduling**:
   - Checks doctor availability before confirming a slot. Rejects any attempt to double-book a physician on the same date and time.
   - Ensures appointments can only be created for existing patient and doctor IDs.
5. **Visit Lifecycle & Clinical Notes**:
   - Update appointment status (`SCHEDULED` &rarr; `COMPLETED` or `CANCELLED`).
   - Record physician consultation findings and outcome notes upon completion.
6. **Automated Billing Invoices**:
   - Computes an itemized receipt combining the doctor's base consultation fee with a triage surcharge ($50 for Critical, $25 for High).
7. **Persistent CSV Storage**:
   - All data is automatically saved to `data/patients.csv`, `data/doctors.csv`, and `data/appointments.csv`, and reloaded whenever the app starts.
8. **Reliable Input Validation**:
   - Built with line-based parsing to avoid classic `Scanner` buffer issues (like skipped lines after reading numbers) and handles invalid inputs with clean user prompts.

---

## Object-Oriented Concepts Used

- **Inheritance**: `Person` serves as an abstract base class containing common attributes (`id`, `name`, `phone`, `email`), extended by `Patient` and `Doctor`.
- **Encapsulation**: Private fields across all entity models accessed through explicit getters and validated setters.
- **Polymorphism**: Abstract method `getRole()` in `Person` overridden by `Patient` ("Patient") and `Doctor` ("Doctor").
- **Enums**: Strongly-typed `TriageSeverity` (with priority ranking and descriptions) and `AppointmentStatus` (`SCHEDULED`, `COMPLETED`, `CANCELLED`).
- **Collections & Streams**: `LinkedHashMap` used for fast key-based retrieval while maintaining entry order; Java 8 Streams used for sorting triage queues and filtering doctor specialties.
- **File I/O**: Custom CSV reader and writer handling quote escaping without relying on external dependencies.

---

## Project Structure

```
clinic-appointment-system/
├── src/
│   └── com/
│       └── clinic/
│           ├── Person.java             # Abstract base class
│           ├── Patient.java            # Patient model (inherits Person)
│           ├── Doctor.java             # Doctor model (inherits Person)
│           ├── Appointment.java        # Appointment model
│           ├── TriageSeverity.java     # Urgency enum (CRITICAL to LOW)
│           ├── AppointmentStatus.java  # Status enum (SCHEDULED, COMPLETED, CANCELLED)
│           ├── StorageManager.java     # CSV read/write persistence
│           ├── ClinicService.java      # Scheduling & business logic
│           ├── ValidationUtils.java    # Input sanitization and validators
│           └── Main.java               # Menu loop and CLI interaction
├── data/
│   ├── patients.csv                    # Saved patient records
│   ├── doctors.csv                     # Saved doctor records
│   └── appointments.csv                # Saved appointments
├── statement.md                        # Project problem statement
├── PROJECT_REPORT.md                   # Full academic report
├── README.md                           # Documentation & quick start
└── .gitignore                          # Build & OS ignore rules
```

---

## Getting Started

### Requirements
- **JDK 17 or higher** installed on your system. Verify by running:
  ```bash
  java -version
  javac -version
  ```

### How to Compile and Run

1. **Clone or navigate into the repository**:
   ```bash
   cd clinic-appointment-system
   ```

2. **Compile the source code into the `bin` directory**:
   ```bash
   javac -d bin src/com/clinic/*.java
   ```

3. **Run the program**:
   ```bash
   java -cp bin com.clinic.Main
   ```

---

## Sample Menu & Output

When you start the application, you will see the interactive menu:

```
==========================================================
   HEALTHCARE CLINIC & PATIENT APPOINTMENT SYSTEM (CLI)   
==========================================================
Storage Status: Loaded 4 patient(s), 3 doctor(s), 2 appointment(s).

==========================================================
                        MAIN MENU                         
==========================================================
  1. Register New Patient
  2. View All Patients
  3. View Emergency Triage Priority Queue
  4. Register New Doctor
  5. View / Search Doctors by Specialization
  6. Schedule Clinical Appointment
  7. View All Appointments
  8. Update Appointment Status / Complete Visit
  9. Generate Patient Billing Invoice
 10. Exit System
==========================================================
Enter your choice (1-10): 
```

### 1. Emergency Triage Priority Queue (Option 3)
Patients are automatically sorted by medical urgency:
```
--- [3] Emergency Triage Priority Queue ---
Patients sorted by medical urgency (CRITICAL -> HIGH -> MEDIUM -> LOW):
+------+----------------------+-------+---------------+---------------------------------+
| ID   | Name                 | Blood | Triage Level  | Urgency Clinical Standing       |
+------+----------------------+-------+---------------+---------------------------------+
| 101  | Eleanor Vance        | O+    | CRITICAL      | Immediate Emergency             |
| 102  | Marcus Brody         | A+    | HIGH          | Urgent / High Priority          |
| 103  | Sophia Chen          | B+    | MEDIUM        | Moderate / Needs Attention      |
| 104  | Lucas Miller         | AB-   | LOW           | Routine / Non-urgent            |
+------+----------------------+-------+---------------+---------------------------------+
Total Patients in Triage: 4
```

### 2. Generating a Billing Invoice (Option 9)
```
--- [9] Generate Patient Billing Invoice ---
Enter Appointment ID: 501

=======================================================
               CLINICAL BILLING INVOICE                
=======================================================
Invoice Ref     : INV-00501
Appointment ID  : #501
Date & Time     : 2026-09-20 at 10:00 AM
Patient Name    : Eleanor Vance (ID: 101, O+)
Triage Level    : CRITICAL
Attending Doctor: Dr. Sarah Jenkins (Cardiology)
-------------------------------------------------------
Base Consultation Fee  : $150.00
Triage Urgency Charge  : $50.00
-------------------------------------------------------
TOTAL AMOUNT PAYABLE   : $200.00
Payment Status         : PENDING
=======================================================
```

---

## Batch Testing & Automated Input

The application is designed to be compatible with piped standard input for automated grading:

```bash
java -cp bin com.clinic.Main < test_input.txt
```

- When end-of-file (EOF) is reached on the input stream, the program saves all current records and exits cleanly without throwing `NoSuchElementException`.
- All inputs are read line-by-line using `Scanner.nextLine()` to prevent newline-skipping bugs.
