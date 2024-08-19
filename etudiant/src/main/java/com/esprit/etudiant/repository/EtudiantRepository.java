package com.esprit.etudiant.repository;

import com.esprit.etudiant.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, String> {
    Etudiant findEtudiantByidEtudiant(String idEtudiant);
    Optional<Etudiant> findByIdEtudiant(String idEtudiant);

    Optional<Etudiant> findByEmail(String email);
}
