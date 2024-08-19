package com.esprit.etudiant.service;

import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public boolean updatePassword(String idEtudiant, String oldPassword, String newPassword) {
        Etudiant etudiant = EtudiantRepo.findByIdEtudiant(idEtudiant)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        // Check if the old password matches the current password
        if (passwordEncoder.matches(oldPassword, etudiant.getPassword())) {
            // Encode and set the new password
            etudiant.setPassword(passwordEncoder.encode(newPassword));
            EtudiantRepo.save(etudiant);
            return true;
        } else {
            return false; // Old password does not match
        }
    }

    @Override
    public List<Etudiant> getAllEtudiants() {
        return EtudiantRepo.findAll();
    }
}
