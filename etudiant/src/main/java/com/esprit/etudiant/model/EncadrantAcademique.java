package com.esprit.etudiant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EncadrantAcademique {

    @Id
    private String idEncadrantAcademique;
    private String password;
    private String grade;

}
