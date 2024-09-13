package com.esprit.encadrantprofessionnel.repository;

import com.esprit.encadrantprofessionnel.model.EncadrantProfessionnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncadrantProfessionnelRepository extends JpaRepository<EncadrantProfessionnel, String> {
    Optional<EncadrantProfessionnel> findByEmail(String email);
}

