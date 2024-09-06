package com.esprit.convention.service;

import com.esprit.convention.Utils.PdfGenerator;
import com.esprit.convention.model.Convention;
import com.esprit.convention.model.ConventionDecision;
import com.esprit.convention.repository.ConventionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.layout.Style;
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
                Paragraph title = new Paragraph("Convention de Stage", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                document.add(Chunk.NEWLINE); // Empty line

                Paragraph p = new Paragraph();
                p.add(new Paragraph("1- L’ÉCOLE SUPERIEURE PRIVÉE D'INGENIERIE ET DE TECHNOLOGIES , ESPRIT"));
                p.setAlignment(Element.ALIGN_LEFT);
                document.add(p);

                Paragraph p1 = new Paragraph();
                p1.add("Représentée par son Directeur Fondateur Pr. Tahar Benlakhdar");
                p1.setAlignment(Element.ALIGN_LEFT);
                document.add(p1);

                Paragraph p2 = new Paragraph();
                p2.add("Ci-après dénommée ESPRIT");
                p2.setAlignment(Element.ALIGN_LEFT);
                document.add(p2);


                Paragraph p3 = new Paragraph();
                p3.add("2-");
                p3.setAlignment(Element.ALIGN_LEFT);
                document.add(p3);

                // Company Information
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1, 2});

                addCell(table, "La Société :");
                addCell(table, convention.getSociete());

                addCell(table, "Adresse :");
                addCell(table, convention.getAdresse());

                addCell(table, "Représenté par :");
                addCell(table, convention.getRepresentePar());

                addCell(table, "Adresse Électronique :");
                addCell(table, convention.getEmail());

                document.add(table);

                Paragraph p4 = new Paragraph();
                p4.add("Ci-après dénommé(e) l’Entreprise,");
                p4.setAlignment(Element.ALIGN_LEFT);
                document.add(p4);

                Paragraph p5 = new Paragraph();
                p5.add("3-");
                p5.setAlignment(Element.ALIGN_LEFT);
                document.add(p5);

                // Student Information
                //addSectionTitle(document, "Étudiant(e)");

                PdfPTable studentTable = new PdfPTable(2);
                studentTable.setWidthPercentage(100);
                studentTable.setWidths(new float[]{1, 2});

                addCell(studentTable, "L’Étudiant (e) :");
                addCell(studentTable, convention.getNomPrenomEtudiant());

                addCell(studentTable, "Inscrit(e) à ESPRIT en troisième année option :");
                addCell(studentTable, convention.getOption());

                addCell(studentTable, "En vue d’obtenir le Diplôme National d’Ingénieur en :");
                addCell(studentTable, convention.getDiplome());

                document.add(studentTable);

                Paragraph p6 = new Paragraph();
                p6.add("Ci-après dénommé l’Étudiant,");
                p6.setAlignment(Element.ALIGN_LEFT);
                document.add(p6);

                document.add(Chunk.NEWLINE); // Empty line

                // Adding articles and clauses as in the template
                addArticle(document, "ARTICLE 1 : Objet de la Convention", "La présente convention règle les rapports entre ESPRIT, l’Entreprise et l’Étudiant pour ce qui concerne le Projet de Fin d’Études (ci-après désigné par « PFE ») que l’Étudiant est appelé à effectuer au sein de l’Entreprise.");

                addArticle(document, "ARTICLE 2 : Objet du PFE", "Le PFE a pour objet essentiel l'application pratique de l'enseignement dispensé à ESPRIT, il s'effectue sous la direction d'un Encadrant Technique de l'Entreprise et d'un Encadrant Pédagogique d’ESPRIT.");

                addArticle(document, "ARTICLE 3 : Programme", "Le programme du PFE, fera l’objet d’une étude par le comité de validation d’ESPRIT en concertation avec l’Encadrant de lEntreprise.");

                addArticle(document, "ARTICLE 4 : Statut de l'étudiant", "Pendant la durée de son séjour en Entreprise, l’Étudiant conserve son statut d'Étudiant.Toutefois Il doit se conformer au règlement intérieur de l’Entreprise et à ses règles d’usage.");

                addArticle(document, "ARTICLE 5 : Résiliation", "");

                Paragraph subsection1 = new Paragraph("5.1 Rupture à l'initiative du Stagiaire");
                document.add(subsection1);

                Paragraph subsection1Details = new Paragraph("Le Stagiaire peut rompre la convention de stage après avoir obtenu l'accord express et non équivoque du maître de stage et informé le Responsable des Stages d'Esprit.");
                subsection1Details.setIndentationLeft(20);
                document.add(subsection1Details);

                document.add(Chunk.NEWLINE);

                Paragraph subsection2 = new Paragraph("5.2 Suspension ou Rupture pour raisons médicales");
                document.add(subsection2);

                Paragraph subsection2Details = new Paragraph("Le stage peut être suspendu ou interrompu pour raisons médicales. Dans ce cas, un avenant comportant les aménagements requis où la rupture de la convention de stage sera conclu.");
                subsection2Details.setIndentationLeft(20);
                document.add(subsection2Details);

                document.add(Chunk.NEWLINE);

                Paragraph subsection3 = new Paragraph("5.3 Rupture pour manquement à la discipline");
                document.add(subsection3);

                Paragraph subsection3Details = new Paragraph(
                        "En cas de manquement à la discipline de l'Entreprise par le Stagiaire, le Chef d'Entreprise se réserve le droit de mettre fin au stage après en avoir informé le Responsable de l'Établissement d'Enseignement.");
                subsection3Details.setIndentationLeft(20);
                document.add(subsection3Details);

                document.add(Chunk.NEWLINE);

                addArticle(document, "ARTICLE 6 : Protection sociale", "L’Étudiant conserve sa protection sociale dans le cadre du contrat d’assurance souscrit par ESPRIT au profit de ses étudiants. En cas d'accident dans l'Entreprise ou au cours du trajet, l'Entreprise informe immédiatement le Service de Scolarité d’ESPRIT.");

                addArticle(document, "ARTICLE 7 : Évaluation", "Les modalités de validation du PFE sont définies dans le cadre des modalités de contrôle des connaissances. L'activité de l'Étudiant fera l'objet d'une évaluation. Une fiche d’évaluation est remplie par l’Organisme d’Accueil et le Responsable Pédagogique et remise à ESPRIT.");

                addArticle(document, "ARTICLE 8 : Rapport de Stage", "À la fin du stage, l’Étudiant déposera à l’Entreprise un rapport de stage. De même l’Étudiant doit déposer à ESPRIT un rapport de stage visé par l’Entreprise et le Responsable Pédagogique qui aura assuré le suivi de l'Étudiant.\n" +
                        "Le rapport de stage fera l'objet d'une soutenance orale devant un jury et d'une notation. Cette soutenance est publique sauf dérogation pour cause de confidentialité, sur demande motivée du Responsable de l'Entreprise.À la fin du stage, l’Entreprise délivrera une attestation à l’Étudiant précisant la nature du PFE et sa durée.");

                addArticle(document, "ARTICLE 9 : Confidentialité", "L’Étudiant sera tenu, aussi bien pendant la durée du PFE qu'après celui-ci, à observer le secret professionnel à l'égard des tiers pour tout ce qui concerne son activité pendant son PFE et d'une façon plus générale pour tout ce dont il aurait eu connaissance directement ou indirectement à l'occasion de celui-ci, le contenu des documents qu'il aurait rédigés lui-même, y compris le rapport du PFE, ou qui pourrait lui être remis par l'Entreprise restant bien entendu la propriété de celle-ci.");

                addArticle(document, "ARTICLE 10 : Consentement", "La présente convention est préalablement portée à la connaissance de l’Étudiant ou de son Représentant légal, s'il est mineur, pour consentement express relatif aux clauses ci-dessus énoncées.");

                addArticle(document, "ARTICLE 11 : Organisation du PFE", "Le PFE se déroulera selon l’organisation suivante :\n" +
                        "- Dates de PFE : Le PFE aura lieu du 20-05-2024 au 19-11-2024 et expirera au plus tard à la fin de l'année universitaire en cours. En cas de prolongation pour raison exceptionnelle réalisée avant l'obtention du diplôme et au delà des dates initialement prévues, un avenant devra être établi et signé par les parties contractantes.\n" +
                        "- Adresse de PFE : Le PFE se déroulera à l'adresse de l’Entreprise.");


                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setWidths(new float[]{1, 2});

                addCell(infoTable, "Département :");
                addCell(infoTable, ".........................................................");

                addCell(infoTable, "Service :");
                addCell(infoTable, ".........................................................");

                addCell(infoTable, "Encadrant Technique dans l'Entreprise :");
                addCell(infoTable, ".........................................................");

                addCell(infoTable, "Encadrant Pédagogique d’ESPRIT :");
                addCell(infoTable, ".........................................................");


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
