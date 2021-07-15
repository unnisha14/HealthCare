package com.example.healthcare;

public class Patient {
    private String username,email,gender;
    private String prev;
    private double height, weight;
    private int age;


    public Patient(String email, String username) {
        this.username = username;
        this.email = email;
    }

    public Patient(double height , double weight , int age , String gender , String prevRecord) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.prev = prevRecord;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPrev() {
        return prev;
    }
}
