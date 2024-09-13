package com.esprit.encadrantprofessionnel.service;

import com.esprit.encadrantprofessionnel.model.EncadrantProfessionnel;
import com.esprit.encadrantprofessionnel.repository.EncadrantProfessionnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EncadrantProfessionnelDetailsService implements UserDetailsService {

    @Autowired
    private EncadrantProfessionnelRepository encadrantProfessionnelRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EncadrantProfessionnel encadrant = encadrantProfessionnelRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new org.springframework.security.core.userdetails.User(encadrant.getEmail(), encadrant.getPassword(), new ArrayList<>());
    }
}

