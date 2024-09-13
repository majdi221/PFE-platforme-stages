package com.esprit.encadrantprofessionnel.service;

import com.esprit.encadrantprofessionnel.model.EncadrantProfessionnel;
import com.esprit.encadrantprofessionnel.repository.EncadrantProfessionnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncadrantProfessionnelService {

    @Autowired
    private EncadrantProfessionnelRepository encadrantProfessionnelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<EncadrantProfessionnel> getAllEncadrants() {
        return encadrantProfessionnelRepository.findAll();
    }

    public Optional<EncadrantProfessionnel> getEncadrantById(String id) {
        return encadrantProfessionnelRepository.findById(id);
    }

    public EncadrantProfessionnel createEncadrant(EncadrantProfessionnel encadrant) {
        encadrant.setPassword(passwordEncoder.encode(encadrant.getPassword()));
        return encadrantProfessionnelRepository.save(encadrant);
    }

    public EncadrantProfessionnel updateEncadrant(String id, EncadrantProfessionnel encadrantDetails) {
        Optional<EncadrantProfessionnel> encadrantOpt = encadrantProfessionnelRepository.findById(id);
        if (encadrantOpt.isPresent()) {
            EncadrantProfessionnel encadrant = encadrantOpt.get();
            encadrant.setEmail(encadrantDetails.getEmail());
            encadrant.setPassword(encadrantDetails.getPassword());
            encadrant.setPost(encadrantDetails.getPost());
            encadrant.setNomEntreprise(encadrantDetails.getNomEntreprise());
            return encadrantProfessionnelRepository.save(encadrant);
        }
        return null;
    }

    public void deleteEncadrant(String id) {
        encadrantProfessionnelRepository.deleteById(id);
    }

}
