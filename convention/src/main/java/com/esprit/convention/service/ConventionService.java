package com.esprit.convention.service;

import com.esprit.convention.model.Convention;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public interface ConventionService {

    public List<Convention> findAll();

    public Optional<Convention> findById(String id);

    public Convention save(Convention convention);

    public void deleteById(String id);

    public Convention getConventionById(String id);

    public File generateConventionPdf(String id);

}
