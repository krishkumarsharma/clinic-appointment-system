# Project Statement: Healthcare Clinic & Patient Appointment System

**Student / Developer**: Krish Kumar (25BAI10528)  
**Email**: [krish.25bai10528@vitbhopal.ac.in](mailto:krish.25bai10528@vitbhopal.ac.in)  
**Course**: Object-Oriented Programming in Java (Flipped Course)  
**Institution**: VIT Bhopal University  

---

## 1. Problem Statement

Small-to-medium outpatient clinics and community health centers often struggle with disjointed administrative processes. Patient records, doctor availability calendars, and clinical triage are frequently handled using fragmented paper files or basic spreadsheets. This lack of centralized automation leads to:
- **Scheduling Conflicts**: Accidental double-booking of physicians during identical time slots.
- **Triage Inefficiency**: Failure to systematically identify and prioritize acute or emergency patients ahead of routine consultations.
- **Data Volatility & Loss**: Unstructured record tracking prone to accidental loss, lack of audit trails, and desynchronization upon system restarts.
- **Administrative Delays**: Manual billing invoice generation that slows patient discharge and creates billing discrepancies.

The **Healthcare Clinic & Patient Appointment System** solves these challenges by providing a unified, reliable, and fault-tolerant console platform for patient intake, medical doctor directory management, conflict-free appointment scheduling, triage prioritization, and persistent record storage.

---

## 2. Scope of the Project

The scope of this software solution encompasses:
- **Patient Management & Triage**: Registration of patient demographic and clinical data (blood group, emergency triage severity from `LOW` to `CRITICAL`), and dynamic priority queue generation.
- **Physician Directory**: Registration and specialization-based querying of medical staff, tracking daily consultation fees and scheduled working days.
- **Appointment Lifecycle**: End-to-end scheduling with strict automated conflict detection, status lifecycle progression (`SCHEDULED` -> `COMPLETED` / `CANCELLED`), and clinical visit notes.
- **Billing & Invoice Generation**: Automated invoice generation incorporating base consultation fees and dynamic triage urgency surcharges.
- **Persistent Disk Storage**: Multi-entity serialization and deserialization via CSV flat-files (`patients.csv`, `doctors.csv`, `appointments.csv`) ensuring data survival across application restarts.
- **Command-Line Interface (CLI)**: A robust, non-GUI terminal application tailored for rapid keyboard navigation, automated evaluation test harnesses, and zero external runtime dependencies.

### Out of Scope:
- Online payment gateway processing (represented via status flags and computed invoice summaries).
- Network-distributed multi-node replication (single-node local storage).

---

## 3. Target Users

1. **Clinic Front-Desk Administrators**: Staff responsible for patient intake, appointment bookings, rescheduling, and billing invoice generation.
2. **Triage Nurses & Healthcare Workers**: Medical staff assessing incoming patients to categorize clinical urgency (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`) and review triage queues.
3. **Attending Physicians**: Doctors reviewing scheduled patient rosters and logging post-consultation outcomes.
4. **Academic Evaluators**: Automated test pipelines and instructors verifying Object-Oriented software engineering principles, stream safety, and execution correctness.

---

## 4. High-Level Features

- **Triage Priority Queuing**: Sorts patients based on medical urgency using multi-level Java stream comparators.
- **Conflict-Free Appointment Scheduling**: Validates doctor availability and rejects double-bookings in identical time slots.
- **Relational Integrity Checks**: Enforces referential validation (appointments require existing patients and doctors).
- **Multi-Entity CSV Persistence**: Dedicated storage manager handling automated persistence and recovery of all entities.
- **Bulletproof Input Handling**: Validates positive IDs, phone patterns, emails, dates (`YYYY-MM-DD`), and prevents `Scanner` buffer skips.
- **Automated Billing Engine**: Formats itemized clinical invoices with fee calculations.
