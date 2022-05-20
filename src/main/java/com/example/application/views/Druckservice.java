package com.example.application.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.service.RezeptService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

public class Druckservice {

    public String getHref(Rezept service) {
        generatePDF(service);
        Anchor anchor = new Anchor(new StreamResource("Rezept.pdf", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                File file = new File("Rezept.pdf");
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    // TODO: handle FileNotFoundException somehow
                    throw new RuntimeException(e);
                }
            }
        }), "");
        UI.getCurrent().getPage().open(anchor.getHref());
        return anchor.getHref();

    }

    public void generatePDF(Rezept rezept) {

        FileOutputStream file = null;
        try {
            file = new FileOutputStream("Rezept.pdf");
            Document document = new Document(PageSize.A4, 30.0F, 20.0f, 10.0F, 50.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            Paragraph titel = new Paragraph(rezept.getTitel(),
                    FontFactory.getFont(FontFactory.COURIER, 16));
            /*
             * FontFactory.TIMES_BOLDITALIC, "UFT-8", false, 20, Font.ITALIC,
             * new BaseColor(255, 0,
             * 0)));
             */
            titel.setAlignment(Element.ALIGN_CENTER);
            titel.setSpacingBefore(0f);
            titel.setSpacingAfter(20f);

            document.add(titel);
            Image test = Image.getInstance(rezept.getBild().getSrc());
            test.scaleAbsolute(250f, 250f);
            test.setAbsolutePosition(300f, 530f);
            document.add(test);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(50);
            table.setSpacingBefore(0f);
            float[] columnsWidths = { 0.5f, 0.5f, 1f };
            table.setWidths(columnsWidths);
            PdfPCell caption = new PdfPCell(new Paragraph("Zutaten für " + rezept.getPortionen() + " Portionen:",
                    FontFactory.getFont(FontFactory.COURIER, 16)));
            caption.setColspan(3);
            caption.setBorder(0);
            table.addCell(caption);
            PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FontFactory.getFont(FontFactory.COURIER, 12)));
            PdfPCell cellEinheit = new PdfPCell(new Paragraph("Einheit", FontFactory.getFont(FontFactory.COURIER, 12)));
            PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FontFactory.getFont(FontFactory.COURIER, 12)));
            cellMenge.setBorder(0);
            cellEinheit.setBorder(0);
            cellZutat.setBorder(0);
            table.addCell(cellMenge);
            table.addCell(cellEinheit);
            table.addCell(cellZutat);
            Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
            for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge()),
                        FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                        FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                        FontFactory.getFont(FontFactory.COURIER, 10)));
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
            }
            PdfContentByte canvas = writer.getDirectContent();
            table.setTotalWidth(250f);
            table.writeSelectedRows(0, table.getRows().size(), 300, 525, canvas);
            document.add(new Paragraph(""));
            PdfPTable table2 = new PdfPTable(1);
            PdfPCell cellZubereitung = new PdfPCell(new Paragraph(rezept.getZubereitung(),
                    FontFactory.getFont(FontFactory.COURIER, 14)));
            cellZubereitung.setBorderWidth(0);
            table2.addCell(cellZubereitung);
            table2.setWidthPercentage(45);
            table2.setHorizontalAlignment(Element.ALIGN_LEFT);
            document.add(table2);
            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createRezept(Rezept rezept) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream("Rezept.pdf");
            Document document = new Document(PageSize.A4, 10.0F, 10.0f, 10.0F, 10.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

    
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100);
                Paragraph titel = new Paragraph(rezept.getTitel(),
                        FontFactory.getFont(FontFactory.COURIER, 16));
                titel.setAlignment(Element.ALIGN_CENTER);
                PdfPCell titleTable = new PdfPCell(titel);
                titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleTable.setBorder(0);
                titleTable.setColspan(2);
                mainTable.addCell(titleTable);

                PdfPCell cellZubereitung = new PdfPCell(new Paragraph(rezept.getZubereitung(),
                        FontFactory.getFont(FontFactory.COURIER, 12)));
                cellZubereitung.setBorderWidth(0);

                cellZubereitung.setRowspan(2);
                cellZubereitung.setPaddingRight(5f);
                mainTable.addCell(cellZubereitung);

                Image test = Image.getInstance(rezept.getBild().getSrc());
                test.scaleToFit(150f, 150f);
                PdfPCell imageCell = new PdfPCell(test);
                imageCell.setBorder(0);
                imageCell.setPaddingTop(5f);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                mainTable.addCell(imageCell);

                PdfPTable table = new PdfPTable(3);
                float[] columnsWidths = { 0.3f, 0.3f, 1f };
                table.setWidths(columnsWidths);
                PdfPCell caption = new PdfPCell(new Paragraph("Zutaten für " + rezept.getPortionen() + " Portionen:",
                        FontFactory.getFont(FontFactory.COURIER, 14)));
                caption.setColspan(3);
                caption.setBorder(0);
                caption.setPaddingLeft(0f);
                table.addCell(caption);
                PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cellEinheit = new PdfPCell(
                        new Paragraph("Einheit", FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FontFactory.getFont(FontFactory.COURIER, 10)));
                cellMenge.setBorder(0);
                cellEinheit.setBorder(0);
                cellZutat.setBorder(0);
                table.addCell(cellMenge);
                table.addCell(cellEinheit);
                table.addCell(cellZutat);
                Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
                for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
                    PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                }
                PdfPCell cell1 = new PdfPCell(new Paragraph());
                PdfPCell cell2 = new PdfPCell(new Paragraph());
                PdfPCell cell3 = new PdfPCell(new Paragraph());
                cell1.setBorder(0);
                cell2.setBorder(0);
                cell3.setBorder(0);
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                PdfPCell tableCell = new PdfPCell(table);
                tableCell.setBorder(0);
                mainTable.addCell(tableCell);
                document.add(mainTable);

               
            
            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createRezept(List<Rezept> rezeptListe) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream("Rezeptliste.pdf");
            Document document = new Document(PageSize.A4, 10.0F, 10.0f, 10.0F, 10.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            for (int i = 0; i < rezeptListe.size(); i++) {

                Rezept rezept = rezeptListe.get(i);
                PdfPTable mainTable = new PdfPTable(2);
                mainTable.setWidthPercentage(100);
                Paragraph titel = new Paragraph(rezept.getTitel(),
                        FontFactory.getFont(FontFactory.COURIER, 16));
                titel.setAlignment(Element.ALIGN_CENTER);
                PdfPCell titleTable = new PdfPCell(titel);
                titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
                titleTable.setBorder(0);
                titleTable.setColspan(2);
                mainTable.addCell(titleTable);

                PdfPCell cellZubereitung = new PdfPCell(new Paragraph(rezept.getZubereitung(),
                        FontFactory.getFont(FontFactory.COURIER, 12)));
                cellZubereitung.setBorderWidth(0);

                cellZubereitung.setRowspan(2);
                cellZubereitung.setPaddingRight(5f);
                mainTable.addCell(cellZubereitung);

                Image test = Image.getInstance(rezept.getBild().getSrc());
                test.scaleToFit(150f, 150f);
                PdfPCell imageCell = new PdfPCell(test);
                imageCell.setBorder(0);
                imageCell.setPaddingTop(5f);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                mainTable.addCell(imageCell);

                PdfPTable table = new PdfPTable(3);
                float[] columnsWidths = { 0.3f, 0.3f, 1f };
                table.setWidths(columnsWidths);
                PdfPCell caption = new PdfPCell(new Paragraph("Zutaten für " + rezept.getPortionen() + " Portionen:",
                        FontFactory.getFont(FontFactory.COURIER, 14)));
                caption.setColspan(3);
                caption.setBorder(0);
                caption.setPaddingLeft(0f);
                table.addCell(caption);
                PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cellEinheit = new PdfPCell(
                        new Paragraph("Einheit", FontFactory.getFont(FontFactory.COURIER, 10)));
                PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FontFactory.getFont(FontFactory.COURIER, 10)));
                cellMenge.setBorder(0);
                cellEinheit.setBorder(0);
                cellZutat.setBorder(0);
                table.addCell(cellMenge);
                table.addCell(cellEinheit);
                table.addCell(cellZutat);
                Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
                for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
                    PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                            FontFactory.getFont(FontFactory.COURIER, 10)));
                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                }
                PdfPCell cell1 = new PdfPCell(new Paragraph());
                PdfPCell cell2 = new PdfPCell(new Paragraph());
                PdfPCell cell3 = new PdfPCell(new Paragraph());
                cell1.setBorder(0);
                cell2.setBorder(0);
                cell3.setBorder(0);
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                PdfPCell tableCell = new PdfPCell(table);
                tableCell.setBorder(0);
                mainTable.addCell(tableCell);
                document.add(mainTable);

                if (i + 1 != rezeptListe.size()) {
                    document.newPage();
                }
            }
            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    byte[] genertePdftoByte(RezeptService service) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {

            Document document = new Document(PageSize.A4, 36.0F, 0, 50.0F, 50.0F);
            PdfWriter writer = PdfWriter.getInstance(document, stream);
            document.open();
            Rezept rezept = (service.findByTitel("Test"));
            Image test = Image.getInstance(rezept.getBild().getSrc());
            test.scaleAbsolute(250f, 250f);
            test.setAbsolutePosition(300f, 600f);
            document.add(test);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(50);
            table.setSpacingBefore(10f);
            float[] columnsWidths = { 1f, 1f, 1f };
            table.setWidths(columnsWidths);
            Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
            for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
                PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge())));
                PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat())));
                PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat())));
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
            }

            document.add(table);
            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
}
