package com.esprit.convention.controller;

import com.esprit.convention.model.Convention;
import com.esprit.convention.model.ConventionDecision;
import com.esprit.convention.model.ConventionStatus;
import com.esprit.convention.service.ConventionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conventions")
public class ConventionController {

    @Autowired
    private ConventionService conventionService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public List<Convention> getAllConventions() {
        return conventionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convention> getConventionById(@PathVariable String id) {
        return conventionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /*@GetMapping("/{id}/pdf")
    public ResponseEntity<FileSystemResource> getConventionPdf(@PathVariable String id) {
        File pdfFile = conventionService.generateConventionPdf(id);
        FileSystemResource resource = new FileSystemResource(pdfFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }*/

    @PostMapping
    public Convention createConvention(@RequestBody Convention convention) {
        return conventionService.save(convention);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Convention> updateConvention(
            @PathVariable String id,
            @RequestBody Convention conventionDetails) {
        return conventionService.findById(id)
                .map(convention -> {
                    convention.setSociete(conventionDetails.getSociete());
                    convention.setAdresse(conventionDetails.getAdresse());
                    convention.setRepresentePar(conventionDetails.getRepresentePar());
                    convention.setEmail(conventionDetails.getEmail());
                    convention.setNomPrenomEtudiant(conventionDetails.getNomPrenomEtudiant());
                    convention.setOption(conventionDetails.getOption());
                    convention.setDiplome(conventionDetails.getDiplome());
                    return ResponseEntity.ok(conventionService.save(convention));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConvention(@PathVariable String id) {
        return conventionService.findById(id)
                .map(convention -> {
                    conventionService.deleteById(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<String> reviewConvention(@PathVariable String id, @RequestParam ConventionStatus status) {
        try {
            ConventionDecision decision = new ConventionDecision(id, status);
            String decisionJson = objectMapper.writeValueAsString(decision);
            jmsTemplate.convertAndSend("reviewQueue", decisionJson);
            return ResponseEntity.ok("Convention reviewed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to process convention review");
        }
    }
}
