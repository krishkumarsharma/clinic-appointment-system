package com.clinic;

/**
 * Model class representing a Healthcare Provider / Doctor. Inherits from Person.
 */
public class Doctor extends Person {
    private String specialization;
    private double consultationFee;
    private String availableDays;

    public Doctor() {
        super();
    }

    public Doctor(int id, String name, String phone, String email, String specialization, double consultationFee, String availableDays) {
        super(id, name, phone, email);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.availableDays = availableDays;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    @Override
    public String getRole() {
        return "Doctor (" + specialization + ")";
    }

    @Override
    public String toString() {
        return String.format("Doctor [ID=%d, Name='Dr. %s', Specialty='%s', Fee=$%.2f, Days='%s', Phone='%s']",
                getId(), getName(), specialization, consultationFee, availableDays, getPhone());
    }
}
