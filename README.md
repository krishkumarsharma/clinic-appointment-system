# Healthcare Clinic & Patient Appointment System (CLI)

A robust, console-based clinical administration and appointment scheduling system developed in standard Java. The application enables healthcare facilities to streamline patient registration, prioritize emergency cases through clinical triage queuing, eliminate doctor scheduling conflicts, and generate itemized billing invoices with persistent CSV storage.

> **Academic Documentation**:
> - Formal Statement & Scope: [statement.md](statement.md)
> - Comprehensive Project Report: [PROJECT_REPORT.md](PROJECT_REPORT.md)
> - PDF Project Report: [PROJECT_REPORT.pdf](PROJECT_REPORT.pdf)

---

## Features

1. **Patient Registration & Triage**:
   - Register patient profiles with demographic and clinical indicators (Age, Gender, Blood Group, Contact details).
   - Assign clinical urgency levels (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`).
2. **Emergency Triage Priority Queue**:
   - View an automated priority queue dynamically sorted by medical urgency using Java stream comparators.
3. **Medical Staff Directory**:
   - Register doctors with medical specializations, consultation rates, and available clinic days.
   - Filter and search doctors by medical department or specialization keyword.
4. **Conflict-Free Appointment Scheduling**:
   - Automated conflict detection preventing physician double-booking during identical time slots.
   - Referential integrity checks ensuring both patient and doctor IDs are valid prior to scheduling.
5. **Clinical Visit Progression**:
   - Update appointment status (`SCHEDULED` -> `COMPLETED` / `CANCELLED`) and record physician outcome notes.
6. **Automated Billing & Invoice Generation**:
   - Generate itemized billing receipts calculating doctor consultation fees plus urgency surcharges.
7. **Persistent CSV Storage**:
   - Multi-entity serialization to `data/patients.csv`, `data/doctors.csv`, and `data/appointments.csv` with automated sync on every update.
8. **Crash-Proof Input Validation**:
   - Universal line-based `Scanner` token parsing preventing buffer desynchronization, numeric errors, and stream EOF termination.

---

## Technologies & Tools Used

- **Programming Language**: Java Standard Edition 17+ (Tested on OpenJDK 21)
- **Architecture**: Three-Tier Layered / MVC-Lite Pattern
- **Persistence Engine**: Multi-Entity CSV File Storage with RFC 4180 Escaping
- **Build & Execution**: Standard JDK command-line tools (`javac`, `java`)
- **Version Control**: Git & GitHub CLI (`gh`)
- **Third-Party Dependencies**: **None** (100% Standard Java SE Libraries: `java.util`, `java.io`, `java.util.regex`)

---

## Project Directory Structure

```
clinic-appointment-system/
├── src/
│   └── com/
│       └── clinic/
│           ├── Person.java             # Abstract base model (Inheritance)
│           ├── Patient.java            # Patient domain model
│           ├── Doctor.java             # Physician domain model
│           ├── Appointment.java        # Appointment domain entity
│           ├── TriageSeverity.java     # Priority level enumeration
│           ├── AppointmentStatus.java  # Appointment lifecycle enumeration
│           ├── StorageManager.java     # CSV serialization & persistence engine
│           ├── ClinicService.java      # Business logic & triage scheduling rules
│           ├── ValidationUtils.java    # Defensive stream parser & regex validators
│           └── Main.java               # Interactive console UI & menu loop
├── data/                               # Persistent flat-file CSV storage
│   ├── patients.csv                    # Stored patient records
│   ├── doctors.csv                     # Stored physician records
│   └── appointments.csv                # Stored appointment schedules
├── statement.md                        # Official VITyarthi Project Statement
├── PROJECT_REPORT.md                   # Complete academic project report
├── README.md                           # Quick-start & operational guide
└── .gitignore                          # Standard Java ignore rules
```

---

## Setup & Execution Guide

### 1. Prerequisites
Ensure a Java Development Kit (JDK 17 or higher) is installed on your operating system:
```bash
java -version
javac -version
```

### 2. Navigate to Directory
```bash
cd clinic-appointment-system
```

### 3. Compilation
Compile all Java source files into the `bin/` output directory:
```bash
javac -d bin src/com/clinic/*.java
```

### 4. Running the Application
Launch the compiled CLI system:
```bash
java -cp bin com.clinic.Main
```

---

## Menu Overview & Interactive Walkthrough

Upon launching the application, you are presented with the main menu:

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

### Sample Operations

#### 1. Viewing Emergency Triage Priority Queue (Option 3)
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

#### 2. Generating a Clinical Invoice (Option 9)
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

## Automated Testing & Evaluation Compatibility

This project is engineered to work reliably with automated evaluation test harnesses and piped standard input:
```bash
# Execute automated batch test script
java -cp bin com.clinic.Main < test_input.txt
```
- **EOF-Proof**: Employs `ValidationUtils.readLineOrNull()` to terminate gracefully upon stream closure without throwing `NoSuchElementException`.
- **Buffer Safety**: Exclusively uses line parsing (`Scanner.nextLine()`) to prevent newline skipping bugs common in `Scanner.nextInt()`.
- **Validation Retries**: Handles malformed types (`NumberFormatException`) cleanly with inline re-prompting.
