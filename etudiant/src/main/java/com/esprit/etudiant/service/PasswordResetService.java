package com.esprit.etudiant.service;

import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.model.PasswordResetToken;
import com.esprit.etudiant.repository.EtudiantRepository;
import com.esprit.etudiant.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void initiatePasswordReset(String email) {
        Etudiant etudiant = etudiantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Etudiant not found with email: " + email));

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Create a new PasswordResetToken
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setIdEtudiant(etudiant.getIdEtudiant());
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        tokenRepository.save(resetToken);

        // Send email with reset link
        String resetLink = "http://yourapp.com/reset-password?token=" + token;
        emailService.sendEmail(etudiant.getEmail(), "Password Reset Request",
                "Click the link to reset your password: " + resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }

        Etudiant etudiant = etudiantRepository.findByIdEtudiant(resetToken.getIdEtudiant())
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        etudiant.setPassword(passwordEncoder.encode(newPassword));
        etudiantRepository.save(etudiant);

        // Optionally, delete the token after it's been used
        tokenRepository.delete(resetToken);
    }
}

