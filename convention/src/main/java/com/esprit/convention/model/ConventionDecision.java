package com.esprit.convention.model;

public class ConventionDecision {

    private String conventionId;
    private ConventionStatus status; // ACCEPTED or REJECTED

    // Constructors
    public ConventionDecision() {
    }

    public ConventionDecision(String conventionId, ConventionStatus status) {
        this.conventionId = conventionId;
        this.status = status;
    }

    // Getters and Setters


    public String getConventionId() {
        return conventionId;
    }

    public void setConventionId(String conventionId) {
        this.conventionId = conventionId;
    }

    public ConventionStatus getStatus() {
        return status;
    }

    public void setStatus(ConventionStatus status) {
        this.status = status;
    }
}

