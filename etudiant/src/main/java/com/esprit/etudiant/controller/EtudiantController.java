package com.esprit.etudiant.controller;

import com.esprit.etudiant.model.AuthenticationRequest;
import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.security.JwtUtil;
import com.esprit.etudiant.service.EtudiantDetailsService;
import com.esprit.etudiant.service.EtudiantService;
import com.esprit.etudiant.service.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EtudiantDetailsService etudiantDetailsService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping("/save")
    public ResponseEntity<?> saveEtudiant(@Valid @RequestBody Etudiant e, BindingResult result){

        ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        Etudiant newEtudiant = etudiantService.save(e);
        return new ResponseEntity<Etudiant>(newEtudiant, HttpStatus.CREATED);

    }
    @GetMapping("/{id}")
    public Etudiant getEtudiant(@PathVariable String id){
        Etudiant newEtudiant = etudiantService.findEtudiantById(id);
        return newEtudiant;

    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> AuthenticatedUserRealm (@Valid @RequestBody AuthenticationRequest loginRequest,BindingResult result)throws Exception {

        ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = etudiantDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

}
