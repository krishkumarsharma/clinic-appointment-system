package com.clinic;

/**
 * Model class representing a Patient. Inherits from Person.
 */
public class Patient extends Person {
    private int age;
    private String gender;
    private String bloodGroup;
    private TriageSeverity triageSeverity;

    public Patient() {
        super();
        this.triageSeverity = TriageSeverity.LOW;
    }

    public Patient(int id, String name, String phone, String email, int age, String gender, String bloodGroup, TriageSeverity triageSeverity) {
        super(id, name, phone, email);
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.triageSeverity = triageSeverity != null ? triageSeverity : TriageSeverity.LOW;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public TriageSeverity getTriageSeverity() {
        return triageSeverity;
    }

    public void setTriageSeverity(TriageSeverity triageSeverity) {
        this.triageSeverity = triageSeverity != null ? triageSeverity : TriageSeverity.LOW;
    }

    @Override
    public String getRole() {
        return "Patient";
    }

    @Override
    public String toString() {
        return String.format("Patient [ID=%d, Name='%s', Age=%d, Gender='%s', Blood='%s', Triage=%s, Phone='%s']",
                getId(), getName(), age, gender, bloodGroup, triageSeverity.name(), getPhone());
    }
}
