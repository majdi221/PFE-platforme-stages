package com.esprit.etudiant.service;


import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;

@Service
public class EtudiantDetailsService implements UserDetailsService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Etudiant etudiant = etudiantRepository.findEtudiantByidEtudiant(username);
        if (etudiant == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(etudiant.getIdEtudiant(), etudiant.getPassword(),
                new ArrayList<>());
    }


}
