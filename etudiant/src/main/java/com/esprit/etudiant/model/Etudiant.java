package com.esprit.etudiant.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Etudiant {

// Attributes

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
    private String oid;

    @Column(unique = true)
    @NotBlank(message = "identifier is required")
    private String idEtudiant;
    @NotBlank(message = "password is required")
    private String password;
    @Email(message ="Username need to be an email")
    @NotBlank(message = "username is required")
    private String email;
    @NotBlank(message = "phone number is required")
    private String telephone;
    @NotBlank(message = "address is required")
    private String adresse;
    @NotBlank(message = "classe is required")
    private String classe;
    @NotBlank(message = "departement is required")
    private String departement;
    @NotBlank(message = "specialty is required")
    private String specialite;
    @NotBlank(message = "pole is required")
    private String pole;
    @DateTimeFormat(pattern = "MMMM-yyyy")  // Format month as text
    @JsonFormat(pattern = "MMMM-yyyy")  // JSON serialization/deserialization
    private LocalDate session;
    private String cv;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "etudiant_stage",
            joinColumns = @JoinColumn(name = "idEtudiant"),
            inverseJoinColumns = @JoinColumn(name = "idStage")
    )
    private List<Stage> stages = new ArrayList<>();
    private String encadrantProId;
    private String encadrantAcademiqueId;


// Getters and setters

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(String idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public LocalDate getSession() {
        return session;
    }

    public void setSession(LocalDate session) {
        this.session = session;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public String getEncadrantProId() {
        return encadrantProId;
    }

    public void setEncadrantProId(String encadrantProId) {
        this.encadrantProId = encadrantProId;
    }

    public String getEncadrantAcademiqueId() {
        return encadrantAcademiqueId;
    }

    public void setEncadrantAcademiqueId(String encadrantAcademiqueId) {
        this.encadrantAcademiqueId = encadrantAcademiqueId;
    }
}
