package com.esprit.etudiant.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Entity
public class Convention implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private String oid;

    private String societe;

    private String adresse;

    private String representePar;

    private String email;

    private String nomPrenomEtudiant;

    private String option;

    private String diplome;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ConventionStatus status = ConventionStatus.PENDING;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Lob
    @Column(name = "pdf_document")
    private byte[] pdfDocument;

    // getters and setters

    public String getOid() {
        return oid;
    }

    public String getSociete() {
        return societe;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getRepresentePar() {
        return representePar;
    }

    public String getEmail() {
        return email;
    }

    public String getNomPrenomEtudiant() {
        return nomPrenomEtudiant;
    }

    public String getOption() {
        return option;
    }

    public String getDiplome() {
        return diplome;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ConventionStatus getStatus() {
        return status;
    }

    public byte[] getPdfDocument() {
        return pdfDocument;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setRepresentePar(String representePar) {
        this.representePar = representePar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNomPrenomEtudiant(String nomPrenomEtudiant) {
        this.nomPrenomEtudiant = nomPrenomEtudiant;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public void setPdfDocument(byte[] pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    public void setStatus(ConventionStatus status) {
        this.status = status;
    }
}


