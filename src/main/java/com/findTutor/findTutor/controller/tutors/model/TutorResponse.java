package com.findTutor.findTutor.controller.tutors.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class TutorResponse {
    // treba da ima sva polja koja hoces da vratis nazad kroz GET request

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String city;
    private String phoneNumber;
    private Double rating;
    private String instructionsType;

    public String getInstructionsType() {
        return instructionsType;
    }

    public void setInstructionsType(String instructionsType) {
        this.instructionsType = instructionsType;
    }

    public TutorResponse() {
    }

    public TutorResponse(Long id,
                         String firstName,
                         String lastName,
                         String username,
                         String email,
                         String city,
                         String phoneNumber,
                         String instructionsType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.instructionsType = instructionsType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating == null ? 0 : rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "TutorResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", instructionsType='" + instructionsType + '\'' +
                '}';
    }
}
