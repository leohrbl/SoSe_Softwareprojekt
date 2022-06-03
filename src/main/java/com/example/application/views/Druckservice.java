package com.example.application.views;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeExpansionEvent;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Philipp Laupichler
 *         DruckService für Rezepte, dieser generiert eine PDF für ein einzeles
 *         Rezeptz oder eine Menge von Rezepten
 */
public class Druckservice {
    /**
     * Name der PDF, für die Generierung einer PDF von einer Menge an Rezepten
     */
    private static final String REZEPTLISTE_PDF = "Rezeptliste.pdf";
    /**
     * Name der PDF, für die Generierung einer PDF von einem Rezept
     */
    private static final String REZEPT_PDF = "Rezept.pdf";
    /**
     * Variale, die die Schriftart und Größe für die Tabelle Rezeptzutaten enthält
     */
    private static final Font FONT_TABLE_REZEPTZUTATEN = FontFactory.getFont(FontFactory.COURIER, 10);
    //
    // FontFactory.getFont(FontFactory.COURIER, 10);
    // FontFactory.getFont(FontFactory.HELVETICA, 10);
    // FontFactory.getFont(FontFactory.SYMBOL, 10);
    // FontFactory.getFont(FontFactory.TIMES, 10);
    // FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
    // FontFactory.getFont(FontFactory.ZAPFDINGBATS, 10);
    //
    /**
     * Variale, die die Schriftart und Größe für die Zubereitung enthält
     */
    private static final Font FONT_ZUBEREITUNG = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
    /**
     * Variale, die die Schriftart und Größe für die Portionen enthält
     */
    private static final Font FONT_PORTIONEN = FontFactory.getFont(FontFactory.COURIER, 14);
    /**
     * Variale, die die Schriftart und Größe für den Titel enthält
     */
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.COURIER, 16);

    /**
     * Der Druckservice wurde nach dem Singleton-Pattern implmentiert
     */
    static Druckservice druckservice;

    private Druckservice() {

    }

    public static Druckservice getInstance() {
        if (druckservice == null) {
            druckservice = new Druckservice();
        }
        return druckservice;
    }

    /**
     * Diese Methode erstellt die PDF "Rezept.pdf"
     * 
     * @param rezept       Rezept, dass gedruckt werden soll
     * @param rezept_Zutat Rezeptzutaten, die gedruckt werden sollen
     * @param portionen    Anzahl der Portionen
     */
    public void createRezept(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(REZEPT_PDF);
            Document document = new Document(PageSize.A4, 30.0F, 30.0f, 20.0F, 20.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();

            PdfPTable mainTable = createMainTable();

            PdfPCell titleTable = createTitle(rezept);

            mainTable.addCell(titleTable);

            PdfPCell cellZubereitung = getZubereitung(rezept);
            mainTable.addCell(cellZubereitung);

            PdfPCell imageCell = getImage(rezept);
            mainTable.addCell(imageCell);

            PdfPCell tableCell = addRezeptZuatenToTable(rezept, rezept_Zutat, portionen);
            mainTable.addCell(tableCell);
            document.add(mainTable);

            document.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Diese Methode erstellt die PDF "Rezept.pdf"
     * 
     * @param rezept       Rezept, dass gedruckt werden soll
     * @param rezept_Zutat Rezeptzutaten, die gedruckt werden sollen
     * @param portionen    Anzahl der Portionen
     */
    public byte[] createRezeptByteArray(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            Document document = new Document(PageSize.A4, 30.0F, 30.0f, 20.0F, 20.0F);
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            PdfPTable mainTable = createMainTable();

            PdfPCell titleTable = createTitle(rezept);

            mainTable.addCell(titleTable);

            PdfPCell cellZubereitung = getZubereitung(rezept);
            mainTable.addCell(cellZubereitung);

            PdfPCell imageCell = getImage(rezept);
            mainTable.addCell(imageCell);

            PdfPCell tableCell = addRezeptZuatenToTable(rezept, rezept_Zutat, portionen);
            mainTable.addCell(tableCell);
            document.add(mainTable);

            document.close();
            writer.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Diese Methode generiert die PDF "Rezeptliste.pdf" anhand einer Menge von
     * Rezepten
     * 
     * @param rezeptListe Liste mit Rezepten, die gedruckt werden sollen
     */
    public void createRezept(List<Rezept> rezeptListe) {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(REZEPTLISTE_PDF);
            Document document = new Document(PageSize.A4, 30.0F, 30.0f, 20.0F, 20.0F);
            PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
            if (rezeptListe.size() == 0) {
                document.add(new Paragraph("Es sind keine Rezept verhanden"));
            }

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

    public byte[] createRezeptByte(List<Rezept> rezeptListe) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30.0F, 30.0f, 20.0F, 20.0F);
        try {

            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            if (rezeptListe.size() == 0) {
                document.add(new Paragraph("Es sind keine Rezept verhanden"));
            }

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
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
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
        PdfPTable table = createTableRezeptZutaten();

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

    /**
     * Diese Methode fügt der Tabelle die Rezeptzuaten hinzu
     * 
     * @param rezept Rezept, aus dem die Rezeptzutaten stammen
     * @param table  Tabelle, in den die Rezeptzutaten eingefügt werden sollen
     */
    private void addRezeptZuatenToTable(Rezept rezept, PdfPTable table) {
        Set<Rezept_Zutat> rezept_Zutat = rezept.getZutatenFromRezept_Zutaten();
        for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
            PdfPCell cell1 = new PdfPCell(new Paragraph((rezept_Zutat2.getMengeString()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell1.setBorder(0);
            cell1.setBorderWidthTop(0.3f);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell2.setBorder(0);
            cell2.setBorderWidthTop(0.3f);
            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell3.setBorder(0);
            cell3.setBorderWidthTop(0.3f);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
        }
    }

    /**
     * 
     * @param rezept
     * @param rezept_Zutat
     * @param portionen
     * @return
     * @throws DocumentException
     */
    private PdfPCell addRezeptZuatenToTable(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen)
            throws DocumentException {
        PdfPTable table = createTableRezeptZutaten();

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

    /**
     * Fügt der Tabelle drei leere Zellen ein
     * 
     * @param table Tabelle, in die die Zellen eingefügt werden sollen
     */
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

    /**
     * Methode, die Rezeptzutaten einer Tabelle hinzufügt
     * 
     * @param rezept_Zutat Liste der Rezeptzutaten
     * @param table        Tabelle, in die die Rezeptzutaten eingefügt werden sollen
     */
    private void addRezeptZutat(List<Rezept_Zutat> rezept_Zutat, PdfPTable table) {
        for (Rezept_Zutat rezept_Zutat2 : rezept_Zutat) {
            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getMengeString()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell1.setBorder(0);
            cell1.setBorderWidthTop(0.3f);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell2.setBorder(0);
            cell2.setBorderWidthTop(0.3f);
            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell3.setBorder(0);
            cell3.setBorderWidthTop(0.3f);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
        }
    }

    /**
     * Die Methode erstellt eine Zelle mit dem Inhalt "Zutat"
     * 
     * @return PdfPCell
     */
    private PdfPCell createCellZutat() {
        PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FONT_TABLE_REZEPTZUTATEN));

        cellZutat.setBorder(0);
        return cellZutat;
    }

    /**
     * Die Methode erstellt eine Zelle mit dem Inhalt "Einheit"
     * 
     * @return
     */
    private PdfPCell createCellEinheit() {
        PdfPCell cellEinheit = new PdfPCell(
                new Paragraph("Einheit", FONT_TABLE_REZEPTZUTATEN));
        cellEinheit.setBorder(0);
        return cellEinheit;
    }

    /**
     * Die Methode erstellt eine Zelle mit dem Inhalt "Menge"
     * 
     * @return
     */
    private PdfPCell createCellMenge() {
        PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FONT_TABLE_REZEPTZUTATEN));
        cellMenge.setBorder(0);
        return cellMenge;
    }

    /**
     * Die Methode erstellt eine Zelle mit dem Inhalt "Zutaten für {Anzahl
     * Portionen} Portionen"
     * 
     * @param portionen Anzahl der Portionen
     * @return PdfPCell
     */
    private PdfPCell createCaptionTable(int portionen) {
        PdfPCell captionTable = new PdfPCell(new Paragraph("Zutaten für " + portionen + " Portionen:",
                FONT_PORTIONEN));
        captionTable.setColspan(3);
        captionTable.setBorder(0);
        captionTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        return captionTable;
    }

    /**
     * Erstellt eine Tabelle mit 3 Spalten und einer definierten Spaltenbreite
     * 
     * @return PdfPTable
     * @throws DocumentException
     */
    private PdfPTable createTableRezeptZutaten() throws DocumentException {
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
        Image bild;
        try {
            bild = Image.getInstance(rezept.getBild());
        } catch (Exception e) {
            bild = Image.getInstance(
                    System.getProperty("user.dir")
                            + "/src/main/resources/META-INF/resources/images/image-placeholder.png");
        }
        bild.scaleToFit(200f, 300f);
        PdfPCell imageCell = new PdfPCell(bild);
        imageCell.setBorder(0);
        imageCell.setPaddingTop(5f);
        imageCell.setPaddingBottom(10f);
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
                FONT_ZUBEREITUNG));
        cellZubereitung.setBorderWidth(0);

        cellZubereitung.setRowspan(2);
        cellZubereitung.setPaddingRight(10f);
        cellZubereitung.setSpaceCharRatio(20f);
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
        titleTable.setPaddingBottom(15f);
        return titleTable;
    }

}
