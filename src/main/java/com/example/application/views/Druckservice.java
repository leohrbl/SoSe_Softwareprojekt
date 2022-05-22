package com.example.application.views;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.service.RezeptService;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
    /**
     *
     */
    private static final Font FONT_TABLE_REZEPTZUTATEN = FontFactory.getFont(FontFactory.COURIER, 10);
    /**
     *
     */
    private static final Font FONZ_ZUBEREITUNG = FontFactory.getFont(FontFactory.COURIER, 12);
    /**
    *
    */
    private static final Font FONT_PORTIONEN = FontFactory.getFont(FontFactory.COURIER, 14);
    /**
     *
     */
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.COURIER, 16);

    static Druckservice druckservice;

    private Druckservice() {

    }

    public static Druckservice getInstance() {
        if (druckservice == null) {
            druckservice = new Druckservice();
        }
        return druckservice;
    }

    public void createRezept(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream("Rezept.pdf");
            Document document = new Document(PageSize.A4, 25.0F, 25.0f, 10.0F, 10.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            PdfPTable mainTable = createMainTable();

            PdfPCell titleTable = createTitle(rezept);

            mainTable.addCell(titleTable);

            PdfPCell cellZubereitung = getZubereitung(rezept);
            mainTable.addCell(cellZubereitung);

            PdfPCell imageCell = getImage(rezept);
            mainTable.addCell(imageCell);

            PdfPCell tableCell = getRezeptZutaten(rezept, rezept_Zutat, portionen);
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
            Document document = new Document(PageSize.A4, 25.0F, 25.0f, 10.0F, 10.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            for (int i = 0; i < rezeptListe.size(); i++) {

                Rezept rezept = rezeptListe.get(i);
                PdfPTable mainTable = createMainTable();

                PdfPCell titleTable = createTitle(rezept);

                mainTable.addCell(titleTable);

                PdfPCell cellZubereitung = getZubereitung(rezept);
                mainTable.addCell(cellZubereitung);

                PdfPCell imageCell = getImage(rezept);
                mainTable.addCell(imageCell);

                PdfPCell tableCell = getRezeptZutaten(rezept);
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

    /**
     * Methode, die eine Tabelle mit 2 Spalten und einer Breite von 100% erstellt
     * und diese zurückgibt
     * 
     * @return PdfPTable
     */
    private PdfPTable createMainTable() {
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        return mainTable;
    }

    /**
     * Diese Methode bekommt ein Rezept übergeben und gibt einer PdfPCell zurück, in
     * der eine Tabelle mit den Rezept Zutaten ist
     * 
     * @param rezept Rezept, von dem die Zutaten stammen
     * @return PdfPCell in der sich die Tabelle der Zutaten befinden
     * @throws DocumentException
     */
    private PdfPCell getRezeptZutaten(Rezept rezept) throws DocumentException {
        PdfPTable table = createTable();

        PdfPCell captionTable = createCaptionTable(rezept.getPortionen());
        table.addCell(captionTable);

        PdfPCell cellMenge = createCellMenge();
        PdfPCell cellEinheit = createCellEinheit();
        PdfPCell cellZutat = createCellZutat();
        table.addCell(cellMenge);
        table.addCell(cellEinheit);
        table.addCell(cellZutat);

        addRezeptZuatenToTable(rezept, table);
        cleanTable(table);

        PdfPCell tableCellTableRezeptZutat = new PdfPCell(table);
        tableCellTableRezeptZutat.setBorder(0);
        return tableCellTableRezeptZutat;
    }

    private void addRezeptZuatenToTable(Rezept rezept, PdfPTable table) {
        Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
        for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge()),
                    FONT_TABLE_REZEPTZUTATEN));
            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
        }
    }

    private PdfPCell createParagraphPortionen(Rezept rezept) {
        PdfPCell paragraphProtionen = new PdfPCell(new Paragraph("Zutaten für " + rezept.getPortionen() + " Portionen:",
                FONT_PORTIONEN));
        paragraphProtionen.setColspan(3);
        paragraphProtionen.setBorder(0);
        paragraphProtionen.setPaddingLeft(0f);
        return paragraphProtionen;
    }

    private PdfPCell getRezeptZutaten(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen)
            throws DocumentException {
        PdfPTable table = createTable();

        PdfPCell captionTable = createCaptionTable(portionen);
        table.addCell(captionTable);

        PdfPCell cellMenge = createCellMenge();
        PdfPCell cellEinheit = createCellEinheit();
        PdfPCell cellZutat = createCellZutat();
        table.addCell(cellMenge);
        table.addCell(cellEinheit);
        table.addCell(cellZutat);

        addRezeptZutat(rezept_Zutat, table);
        cleanTable(table);

        PdfPCell tableCell = new PdfPCell(table);
        tableCell.setBorder(0);
        return tableCell;
    }

    private void cleanTable(PdfPTable table) {
        PdfPCell cell1 = new PdfPCell(new Paragraph());
        PdfPCell cell2 = new PdfPCell(new Paragraph());
        PdfPCell cell3 = new PdfPCell(new Paragraph());
        cell1.setBorder(0);
        cell2.setBorder(0);
        cell3.setBorder(0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
    }

    private void addRezeptZutat(List<Rezept_Zutat> rezept_Zutat, PdfPTable table) {
        for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMenge()),
                    FONT_TABLE_REZEPTZUTATEN));
            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
        }
    }

    private PdfPCell createCellZutat() {
        PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FONT_TABLE_REZEPTZUTATEN));

        cellZutat.setBorder(0);
        return cellZutat;
    }

    private PdfPCell createCellEinheit() {
        PdfPCell cellEinheit = new PdfPCell(
                new Paragraph("Einheit", FONT_TABLE_REZEPTZUTATEN));
        cellEinheit.setBorder(0);
        return cellEinheit;
    }

    private PdfPCell createCellMenge() {
        PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FONT_TABLE_REZEPTZUTATEN));
        cellMenge.setBorder(0);
        return cellMenge;
    }

    private PdfPCell createCaptionTable(int portionen) {
        PdfPCell captionTable = new PdfPCell(new Paragraph("Zutaten für " + portionen + " Portionen:",
                FONT_PORTIONEN));
        captionTable.setColspan(3);
        captionTable.setBorder(0);
        captionTable.setPaddingLeft(0f);
        return captionTable;
    }

    private PdfPTable createTable() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        float[] columnsWidths = { 0.3f, 0.3f, 1f };
        table.setWidths(columnsWidths);
        return table;
    }

    /**
     * Methode, die ein Rezept entgegen nimmt und das Bild in einer PdfPCell
     * speichert und diese zurückgibt
     * 
     * @param rezept
     * @return PdfPCell, mit Bild
     * @throws BadElementException
     * @throws MalformedURLException
     * @throws IOException
     */
    private PdfPCell getImage(Rezept rezept) throws BadElementException, MalformedURLException, IOException {
        System.out.println(rezept.getBild().getSrc());
        // Image test = Image.getInstance("src/main/resources/META-INF/resources/" +
        // rezept.getBild().getSrc());
        Image test = Image.getInstance(rezept.getBild().getSrc());
        test.scaleToFit(150f, 150f);
        PdfPCell imageCell = new PdfPCell(test);
        imageCell.setBorder(0);
        imageCell.setPaddingTop(5f);
        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return imageCell;
    }

    private PdfPCell getImageFromByte(Rezept rezept) throws BadElementException, MalformedURLException, IOException {
        com.vaadin.flow.component.html.Image vaadinImage = rezept.getBild();
        byte[] image = null;
        Image test = Image.getInstance(image);
        test.scaleToFit(150f, 150f);
        PdfPCell imageCell = new PdfPCell(test);
        imageCell.setBorder(0);
        imageCell.setPaddingTop(5f);
        imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return imageCell;
    }

    /**
     * Methode, die ein Rezept entgegen nimmt und die Zubereitung in einer PdfPCell
     * speichert
     * 
     * @param rezept
     * @return PdfPCell, mit Zubereitung als Inhalt
     */
    private PdfPCell getZubereitung(Rezept rezept) {
        PdfPCell cellZubereitung = new PdfPCell(new Paragraph(rezept.getZubereitung(),
                FONZ_ZUBEREITUNG));
        cellZubereitung.setBorderWidth(0);

        cellZubereitung.setRowspan(2);
        cellZubereitung.setPaddingRight(5f);
        return cellZubereitung;
    }

    /**
     * Methode, die ein Rezept entgegen nimmt und den Titel in einer PdfPCell
     * speichert
     * 
     * @param rezept
     * @return PdfPCell, mit Titel als Inhalt
     */
    private PdfPCell createTitle(Rezept rezept) {
        Paragraph titel = new Paragraph(rezept.getTitel(),
                FONT_TITLE);
        titel.setAlignment(Element.ALIGN_CENTER);
        PdfPCell titleTable = new PdfPCell(titel);
        titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.setBorder(0);
        titleTable.setColspan(2);
        return titleTable;
    }

    public void generatePDF(Rezept rezept) {

        FileOutputStream file = null;
        try {
            file = new FileOutputStream("Rezept.pdf");
            Document document = new Document(PageSize.A4, 30.0F, 20.0f, 10.0F, 50.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            Paragraph titel = new Paragraph(rezept.getTitel(),
                    FONT_TITLE);
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
                    FONT_TITLE));
            caption.setColspan(3);
            caption.setBorder(0);
            table.addCell(caption);
            PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FONZ_ZUBEREITUNG));
            PdfPCell cellEinheit = new PdfPCell(new Paragraph("Einheit", FONZ_ZUBEREITUNG));
            PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FONZ_ZUBEREITUNG));
            cellMenge.setBorder(0);
            cellEinheit.setBorder(0);
            cellZutat.setBorder(0);
            table.addCell(cellMenge);
            table.addCell(cellEinheit);
            table.addCell(cellZutat);
            addRezeptZuatenToTable(rezept, table);
            PdfContentByte canvas = writer.getDirectContent();
            table.setTotalWidth(250f);
            table.writeSelectedRows(0, table.getRows().size(), 300, 525, canvas);
            document.add(new Paragraph(""));
            PdfPTable table2 = new PdfPTable(1);
            PdfPCell cellZubereitung = new PdfPCell(new Paragraph(rezept.getZubereitung(),
                    FONT_PORTIONEN));
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
}
