package com.esprit.convention.service;

import com.esprit.convention.Utils.PdfGenerator;
import com.esprit.convention.model.Convention;
import com.esprit.convention.repository.ConventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ConventionServiceImp implements ConventionService {

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Override
    public List<Convention> findAll() {
        return conventionRepository.findAll();
    }

    @Override
    public Optional<Convention> findById(String id) {
        return conventionRepository.findById(id);
    }

    @Override
    public Convention save(Convention convention) {
        return conventionRepository.save(convention);
    }

    @Override
    public void deleteById(String id) {
        conventionRepository.deleteById(id);
    }


    public Convention getConventionById(String id) {
        return conventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convention not found with id: " + id));
    }

    public File generateConventionPdf(String id) {
        Convention convention = getConventionById(id);
        String pdfPath = "convention_" + id + ".pdf";
        pdfGenerator.generateConventionPdf(convention, pdfPath);
        return new File(pdfPath);
    }

}
