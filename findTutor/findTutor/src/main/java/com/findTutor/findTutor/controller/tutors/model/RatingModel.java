package com.findTutor.findTutor.controller.tutors.model;

public class RatingModel {
    private Double rating;

    public Double getRating() {
        return rating == null ? 0 : rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
