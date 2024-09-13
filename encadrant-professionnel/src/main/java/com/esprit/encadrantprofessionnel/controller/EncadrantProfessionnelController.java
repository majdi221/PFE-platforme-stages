package com.esprit.encadrantprofessionnel.controller;

import com.esprit.encadrantprofessionnel.model.AuthenticationRequest;
import com.esprit.encadrantprofessionnel.model.EncadrantProfessionnel;
import com.esprit.encadrantprofessionnel.security.JwtUtil;
import com.esprit.encadrantprofessionnel.service.EncadrantProfessionnelDetailsService;
import com.esprit.encadrantprofessionnel.service.EncadrantProfessionnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/encadrant-professionnel")
public class EncadrantProfessionnelController {

    @Autowired
    private EncadrantProfessionnelService encadrantProfessionnelService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EncadrantProfessionnelDetailsService userDetailsService;

    @GetMapping("/all")
    public List<EncadrantProfessionnel> getAllEncadrants() {
        return encadrantProfessionnelService.getAllEncadrants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncadrantProfessionnel> getEncadrantById(@PathVariable String id) {
        Optional<EncadrantProfessionnel> encadrant = encadrantProfessionnelService.getEncadrantById(id);
        return encadrant.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public EncadrantProfessionnel createEncadrant(@RequestBody EncadrantProfessionnel encadrant) {
        return encadrantProfessionnelService.createEncadrant(encadrant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EncadrantProfessionnel> updateEncadrant(@PathVariable String id, @RequestBody EncadrantProfessionnel encadrantDetails) {
        EncadrantProfessionnel updatedEncadrant = encadrantProfessionnelService.updateEncadrant(id, encadrantDetails);
        if (updatedEncadrant != null) {
            return ResponseEntity.ok(updatedEncadrant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEncadrant(@PathVariable String id) {
        encadrantProfessionnelService.deleteEncadrant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

}

