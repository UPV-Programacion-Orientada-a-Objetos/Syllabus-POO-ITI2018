package com.spolancom;

import java.time.LocalDate;

public class Person {

    private int id;
    private LocalDate dob;
    private String firstName;
    private String lastName;

    public Person (String firstName, String lastName, LocalDate dob) {
        if (dob == null) {
            throw new RuntimeException("Date of birth cannot be null");
        }

        this.dob = dob;
        this.firstName = firstName == null ? "" : firstName;
        this.lastName = lastName == null ? "" : lastName;
    }

    public Person(int id, String firstName, String lastName, LocalDate dob) {
        this(firstName, lastName, dob);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public LocalDate getDob() {
        return this.dob;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
