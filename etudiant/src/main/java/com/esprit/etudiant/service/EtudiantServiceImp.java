package com.esprit.etudiant.service;

import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EtudiantServiceImp implements EtudiantService{
    @Autowired
    private EtudiantRepository EtudiantRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Etudiant save(Etudiant e) {
        e.setPassword(passwordEncoder.encode(e.getPassword()));
        return EtudiantRepo.save(e);
    }

    @Override
    public Etudiant findEtudiantById(String id) {
        return EtudiantRepo.findEtudiantByidEtudiant(id);
    }
}
