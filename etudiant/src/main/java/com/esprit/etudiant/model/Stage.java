package com.esprit.etudiant.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Stage {

    @Id
    private String idStage;
    private String typeStage;
    private String theme;
    private Date dateDebut;
    private Date dateFin;
    private String convention;
    private String etudiantId;
    private String encadrantProId;
    private String encadrantAcademiqueId;
    private String etatValidation;
    private Date dateDepotRapport;
    private String rapport;
    private Date dateSoutenance;
    private String heureSoutenance;
    private String lieuSoutenance;
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "stage_encadrantAcademique",
            joinColumns = @JoinColumn(name = "idStage"),
            inverseJoinColumns = @JoinColumn(name = "idEncadrantAcademique")
    )
    private List<EncadrantAcademique> jury = new ArrayList<>();
    private Float note;

    public String getIdStage() {
        return idStage;
    }

    public void setIdStage(String idStage) {
        this.idStage = idStage;
    }

    public String getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(String typeStage) {
        this.typeStage = typeStage;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getConvention() {
        return convention;
    }

    public void setConvention(String convention) {
        this.convention = convention;
    }

    public String getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(String etudiantId) {
        this.etudiantId = etudiantId;
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

    public String getEtatValidation() {
        return etatValidation;
    }

    public void setEtatValidation(String etatValidation) {
        this.etatValidation = etatValidation;
    }

    public Date getDateDepotRapport() {
        return dateDepotRapport;
    }

    public void setDateDepotRapport(Date dateDepotRapport) {
        this.dateDepotRapport = dateDepotRapport;
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
    }

    public Date getDateSoutenance() {
        return dateSoutenance;
    }

    public void setDateSoutenance(Date dateSoutenance) {
        this.dateSoutenance = dateSoutenance;
    }

    public String getHeureSoutenance() {
        return heureSoutenance;
    }

    public void setHeureSoutenance(String heureSoutenance) {
        this.heureSoutenance = heureSoutenance;
    }

    public String getLieuSoutenance() {
        return lieuSoutenance;
    }

    public void setLieuSoutenance(String lieuSoutenance) {
        this.lieuSoutenance = lieuSoutenance;
    }

    public List<EncadrantAcademique> getJury() {
        return jury;
    }

    public void setJury(List<EncadrantAcademique> jury) {
        this.jury = jury;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }
}
