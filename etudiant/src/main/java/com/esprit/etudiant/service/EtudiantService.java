package com.esprit.etudiant.service;

import com.esprit.etudiant.model.Etudiant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EtudiantService {

    // CRUD
    public Etudiant save(Etudiant e);
    public Etudiant findEtudiantById(String id);
    public boolean updatePassword(String idEtudiant, String oldPassword, String newPassword);
    public List<Etudiant> getAllEtudiants();

}
