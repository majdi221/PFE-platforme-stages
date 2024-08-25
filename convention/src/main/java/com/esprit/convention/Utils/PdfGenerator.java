package com.esprit.convention.Utils;


import com.esprit.convention.model.Convention;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service

public class PdfGenerator {

    public void generateConventionPdf(Convention convention, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Styles
            Style titleStyle = new Style().setBold().setFontSize(16).setTextAlignment(TextAlignment.CENTER);
            Style boldStyle = new Style().setBold().setFontSize(12);
            Style regularStyle = new Style().setFontSize(12);
            Style sectionTitleStyle = new Style().setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT);

            // Title
            document.add(new Paragraph("CONVENTION DE STAGE").addStyle(titleStyle).setMarginBottom(20));

            // Entre parties
            document.add(new Paragraph("Entre les soussignés :").addStyle(boldStyle).setMarginBottom(10));

            // Table for convention details
            float[] columnWidths = {1, 3};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Add rows to the table
            table.addCell(new Cell().add(new Paragraph("Société:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getSociete()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Adresse:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getAdresse()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Représenté(e) par:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getRepresentePar()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Adresse Électronique:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getEmail()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Nom de l'étudiant(e):").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getNomPrenomEtudiant()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Option:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getOption()).addStyle(regularStyle)).setBorder(null));

            table.addCell(new Cell().add(new Paragraph("Diplôme préparé:").addStyle(boldStyle)).setBorder(null));
            table.addCell(new Cell().add(new Paragraph(convention.getDiplome()).addStyle(regularStyle)).setBorder(null));

            document.add(table.setMarginBottom(20));

            // Add additional sections
            addSection(document, "ARTICLE 1 : Objet de la Convention", "La présente convention règle les rapports entre ESPRIT, l’Entreprise et l’Étudiant pour ce qui concerne le Projet de\n" +
                    "Fin d’Etudes (ci-après désigné par « PFE ») que l’Étudiant est appelé à effectuer au sein de l’Entreprise");
            addSection(document, "ARTICLE 2 : Objet du PFE", "Le PFE a pour objet essentiel l'application pratique de l'enseignement dispensé à ESPRIT, il s'effectue sous la\n" +
                    "direction d'un Encadrant Technique de l'Entreprise et d'un Encadrant Pédagogique d’ESPRIT");
            addSection(document, "ARTICLE 3 : Programme", "Le programme du PFE fera l’objet d’une étude par le comité de validation d’ESPRIT...");
            addSection(document, "ARTICLE 4 : Statut de l’Étudiant", "Pendant la durée de son séjour en Entreprise, l’Étudiant conserve son statut d'Étudiant...");
            addSection(document, "ARTICLE 5 : Résiliation", "La résiliation de la convention peut se faire selon les modalités suivantes...");

            // Signature section
            document.add(new Paragraph("Fait à ................................................ le ....................................")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20)
                    .addStyle(boldStyle));

            document.add(new Paragraph("Directeur de l’Entreprise").addStyle(boldStyle).setMarginTop(20));
            document.add(new Paragraph("Signature et cachet de l'entreprise").addStyle(regularStyle).setMarginBottom(20));

            document.add(new Paragraph("Directeur d'ESPRIT").addStyle(boldStyle).setMarginTop(20));
            document.add(new Paragraph("Signature").addStyle(regularStyle).setMarginBottom(20));

            document.add(new Paragraph("Étudiant(e)").addStyle(boldStyle).setMarginTop(20));
            document.add(new Paragraph(convention.getNomPrenomEtudiant()).addStyle(regularStyle).setMarginBottom(20));

            document.close();
            System.out.println("PDF généré avec succès à l'emplacement : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addSection(Document document, String title, String content) {
        document.add(new Paragraph(title).addStyle(new Style().setBold().setFontSize(14).setTextAlignment(TextAlignment.LEFT)).setMarginBottom(5));
        document.add(new Paragraph(content).addStyle(new Style().setFontSize(12)).setMarginBottom(15));
    }
}
