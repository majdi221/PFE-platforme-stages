package com.esprit.convention.repository;

import com.esprit.convention.model.Convention;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConventionRepository extends JpaRepository<Convention, String> {
}

