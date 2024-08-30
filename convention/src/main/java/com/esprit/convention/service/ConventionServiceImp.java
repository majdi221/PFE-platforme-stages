package com.esprit.convention.service;

import com.esprit.convention.Utils.PdfGenerator;
import com.esprit.convention.model.Convention;
import com.esprit.convention.model.ConventionDecision;
import com.esprit.convention.repository.ConventionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ConventionServiceImp implements ConventionService {

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Convention> findAll() {
        return conventionRepository.findAll();
    }

    @Override
    public Optional<Convention> findById(String id) {
        return conventionRepository.findById(id);
    }

    @Override
    public Convention save(Convention convention) {
        return conventionRepository.save(convention);
    }

    @Override
    public void deleteById(String id) {
        conventionRepository.deleteById(id);
    }


    @Override
    public Convention getConventionById(String id) {
        return conventionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convention not found with id: " + id));
    }

    /*@Override
    public File generateConventionPdf(String id) {
        Convention convention = getConventionById(id);
        String pdfPath = "convention_" + id + ".pdf";
        pdfGenerator.generateConventionPdf(convention, pdfPath);
        return new File(pdfPath);
    }*/

    @JmsListener(destination = "conventionQueue")
    public void handleConventionApplication(String conventionJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Convention convention = objectMapper.readValue(conventionJson, Convention.class);
        conventionRepository.save(convention);
    }

    @JmsListener(destination = "reviewQueue")
    public void handleConventionReview(String decisionJson) throws DocumentException, IOException {

        try {
            ConventionDecision decision = objectMapper.readValue(decisionJson, ConventionDecision.class);

            Convention convention = conventionRepository.findById(decision.getConventionId())
                    .orElseThrow(() -> new RuntimeException("Convention not found"));

            convention.setStatus(decision.getStatus());

            if ("ACCEPTED".equalsIgnoreCase(decision.getStatus().toString())) {
                // Generate PDF
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, pdfOutputStream);
                document.open();

                // Title
                Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                Paragraph title = new Paragraph("Convention de Stage Tunisie", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                document.add(Chunk.NEWLINE); // Empty line

                // Company Information
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1, 2});

                addCell(table, "Entreprise :");
                addCell(table, convention.getSociete());

                addCell(table, "Adresse :");
                addCell(table, convention.getAdresse());

                addCell(table, "Représenté par :");
                addCell(table, convention.getRepresentePar());

                addCell(table, "Adresse Électronique :");
                addCell(table, convention.getEmail());

                document.add(table);
                document.add(Chunk.NEWLINE); // Empty line

                // Student Information
                addSectionTitle(document, "Étudiant(e)");

                PdfPTable studentTable = new PdfPTable(2);
                studentTable.setWidthPercentage(100);
                studentTable.setWidths(new float[]{1, 2});

                addCell(studentTable, "Nom et Prénom :");
                addCell(studentTable, convention.getNomPrenomEtudiant());

                addCell(studentTable, "Option :");
                addCell(studentTable, convention.getOption());

                addCell(studentTable, "Diplôme :");
                addCell(studentTable, convention.getDiplome());

                document.add(studentTable);
                document.add(Chunk.NEWLINE); // Empty line

                // Adding articles and clauses as in the template
                addArticle(document, "ARTICLE 1 : Objet de la Convention", "La présente convention règle les rapports entre ESPRIT, l’Entreprise et l’Étudiant pour ce qui concerne le Projet de Fin d’Études (ci-après désigné par « PFE ») que l’Étudiant est appelé à effectuer au sein de l’Entreprise.");
                addArticle(document, "ARTICLE 2 : Objet du PFE", "Le PFE a pour objet essentiel l'application pratique de l'enseignement dispensé à ESPRIT...");

                // More articles would go here, following the structure of the provided template.

                // Signatures
                document.add(Chunk.NEWLINE); // Empty line
                addSignatureSection(document, "Directeur de l’Entreprise", "Étudiant", convention.getNomPrenomEtudiant());

                document.close();
                convention.setPdfDocument(pdfOutputStream.toByteArray());
            }

            conventionRepository.save(convention);

        } catch (Exception e) {
            System.err.println("Failed to process convention review: " + e.getMessage());
        }

    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph sectionTitle = new Paragraph(title, sectionFont);
        sectionTitle.setAlignment(Element.ALIGN_LEFT);
        document.add(sectionTitle);
        document.add(Chunk.NEWLINE); // Empty line
    }

    private void addArticle(Document document, String title, String content) throws DocumentException {
        Font articleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font contentFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

        Paragraph articleTitle = new Paragraph(title, articleFont);
        document.add(articleTitle);

        Paragraph articleContent = new Paragraph(content, contentFont);
        document.add(articleContent);
        document.add(Chunk.NEWLINE); // Empty line
    }

    private void addSignatureSection(Document document, String leftTitle, String rightTitle, String studentName) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);

        PdfPCell leftCell = new PdfPCell(new Phrase(leftTitle));
        leftCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(leftCell);

        PdfPCell rightCell = new PdfPCell(new Phrase(rightTitle));
        rightCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(rightCell);

        // Adding student name and placeholders for other signatures
        table.addCell(new PdfPCell(new Phrase("Nom et Signature du Représentant de l'Entreprise\n+ Cachet de l'Entreprise", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC))));
        table.addCell(new PdfPCell(new Phrase("Nom et Signature de l'Étudiant\n" + studentName, FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC))));

        document.add(table);
    }


}
