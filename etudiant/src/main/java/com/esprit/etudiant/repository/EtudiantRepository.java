package com.esprit.etudiant.repository;

import com.esprit.etudiant.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, String> {
    Etudiant findEtudiantByidEtudiant(String idEtudiant);
}
