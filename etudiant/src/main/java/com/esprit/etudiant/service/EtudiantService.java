package com.esprit.etudiant.service;

import com.esprit.etudiant.model.Etudiant;
import org.springframework.stereotype.Service;

@Service
public interface EtudiantService {

    // CRUD
    public Etudiant save(Etudiant e);
    public Etudiant findEtudiantById(String id);

}
