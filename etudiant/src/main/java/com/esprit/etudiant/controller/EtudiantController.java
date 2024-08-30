package com.esprit.etudiant.controller;

import com.esprit.etudiant.model.AuthenticationRequest;
import com.esprit.etudiant.model.Convention;
import com.esprit.etudiant.model.Etudiant;
import com.esprit.etudiant.security.JwtUtil;
import com.esprit.etudiant.service.EtudiantDetailsService;
import com.esprit.etudiant.service.EtudiantService;
import com.esprit.etudiant.service.MapValidationErrorService;
import com.esprit.etudiant.service.PasswordResetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private JmsTemplate jmsTemplate;


    @PostMapping("/save")
    public ResponseEntity<?> saveEtudiant(@Valid @RequestBody Etudiant e, BindingResult result){

        ResponseEntity<?> errorMap =mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        Etudiant newEtudiant = etudiantService.save(e);
        return new ResponseEntity<Etudiant>(newEtudiant, HttpStatus.CREATED);

    }
    @GetMapping("/{id}")
    public Etudiant getEtudiant(@PathVariable String id){
        Etudiant etudiant = etudiantService.findEtudiantById(id);
        return etudiant;

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

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody Map<String, String> passwordRequest,
            Authentication authentication) {

        User userDetails = (User) authentication.getPrincipal();
        String idEtudiant = userDetails.getUsername();

        String oldPassword = passwordRequest.get("oldPassword");
        String newPassword = passwordRequest.get("newPassword");

        boolean success = etudiantService.updatePassword(idEtudiant, oldPassword, newPassword);

        if (success) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Old password is incorrect.");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.initiatePasswordReset(email);
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }


    @PostMapping("/apply")
    public ResponseEntity<String> applyForConvention(@RequestBody Convention convention) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String conventionJson = objectMapper.writeValueAsString(convention);
        jmsTemplate.convertAndSend("conventionQueue", conventionJson);
        return ResponseEntity.ok("Convention application sent successfully");
    }

}
