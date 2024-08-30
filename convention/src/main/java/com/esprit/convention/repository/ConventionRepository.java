package com.esprit.convention.repository;

import com.esprit.convention.model.Convention;
import com.esprit.convention.model.ConventionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConventionRepository extends JpaRepository<Convention, String> {
    List<Convention> findByStatus(ConventionStatus status);
}

