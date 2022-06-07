package com.example.application.views.drucken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.example.application.data.rezeptzutat.Rezept_Zutat;
import com.example.application.data.rezept.Rezept;
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
public class DruckserviceRezept {
    /**
     * Margin des Dokuments unten und oben
     */
    private static final float MARGIN_BOTTOM_TOP = 20.0F;
    /**
     * Margin des Dokuments an den Seiten
     * Falls das gedruckte Dokument gelocht werden soll, wird dies nicht den Text
     * betreffen
     */
    private static final float MARGIN_SIDES = 50.0F;
    /**
     * Breite des Bildes, berechnet über die Breite des Dokuments 595 (A4), davon
     * wird der Margin abgezogen und durch zwei geteilt
     */
    private static final float imageWidt = (595.0f - MARGIN_SIDES * 2) / 2;
    /**
     * Variale, die die Schriftart und Größe für die Tabelle Rezeptzutaten enthält
     */
    private static final Font FONT_TABLE_REZEPTZUTATEN = FontFactory.getFont(FontFactory.TIMES, 12);
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
    private static final Font FONT_ZUBEREITUNG = FontFactory.getFont(FontFactory.TIMES, 16);
    /**
     * Variale, die die Schriftart und Größe für die Portionen enthält
     */
    private static final Font FONT_PORTIONEN = FontFactory.getFont(FontFactory.TIMES, 16);
    /**
     * Variale, die die Schriftart und Größe für den Titel enthält
     */
    private static final Font FONT_TITLE = FontFactory.getFont(FontFactory.TIMES, 16);

    /**
     * Der Druckservice wurde nach dem Singleton-Pattern implmentiert
     */
    static DruckserviceRezept druckservice;

    private DruckserviceRezept() {

    }

    public static DruckserviceRezept getInstance() {
        if (druckservice == null) {
            druckservice = new DruckserviceRezept();
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
    public byte[] createRezeptByteArray(Rezept rezept, List<Rezept_Zutat> rezept_Zutat, int portionen) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            Document document = new Document(PageSize.A4, MARGIN_SIDES, MARGIN_SIDES, MARGIN_BOTTOM_TOP,
                    MARGIN_BOTTOM_TOP);
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

    public byte[] createRezeptByte(List<Rezept> rezeptListe) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, MARGIN_SIDES, MARGIN_SIDES, MARGIN_BOTTOM_TOP, MARGIN_BOTTOM_TOP);
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

                PdfPCell tableCell = getRezeptZutatenTable(rezept);
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
    private PdfPCell getRezeptZutatenTable(Rezept rezept) throws DocumentException {
        PdfPTable table = createTableRezeptZutaten();

        PdfPCell captionTable = createCaptionTable(rezept.getPortionen());
        table.addCell(captionTable);

        // PdfPCell cellMenge = createCellMenge();
        // PdfPCell cellEinheit = createCellEinheit();
        // PdfPCell cellZutat = createCellZutat();
        // table.addCell(cellMenge);
        // table.addCell(cellEinheit);
        // table.addCell(cellZutat);

        getRezeptZutatandAddToTable(rezept, table);
        cleanTable(table);

        PdfPCell tableCellTableRezeptZutat = new PdfPCell(table);
        tableCellTableRezeptZutat.setBorder(0);
        return tableCellTableRezeptZutat;
    }

    /**
     * Diese Methode wandelt speichert die RezeptZutaten in einer Liste und übergibt
     * diese der Methode "addRezeptZutat"
     * 
     * @param rezept Rezept, aus dem die Rezeptzutaten stammen
     * @param table  Tabelle, in den die Rezeptzutaten eingefügt werden sollen
     */
    private void getRezeptZutatandAddToTable(Rezept rezept, PdfPTable table) {
        Set<Rezept_Zutat> rezept_ZutatSet = rezept.getZutatenFromRezept_Zutaten();
        List<Rezept_Zutat> rezept_zutatList = new LinkedList<>();
        rezept_zutatList.addAll(rezept_ZutatSet);
        addRezeptZutat(rezept_zutatList, table);
    }

    private PdfPCell stylingCell(PdfPCell cell) {
        cell.setBorder(0);
        cell.setBorderWidthTop(0.3f);
        cell.setPaddingTop(5f);
        cell.setPaddingBottom(5f);
        return cell;
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

        // PdfPCell cellMenge = createCellMenge();
        // PdfPCell cellEinheit = createCellEinheit();
        // PdfPCell cellZutat = createCellZutat();
        // table.addCell(cellMenge);
        // table.addCell(cellEinheit);
        // table.addCell(cellZutat);

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

            PdfPCell cell1 = new PdfPCell(new Paragraph(rezept_Zutat2.getMengeString(),
                    FONT_TABLE_REZEPTZUTATEN));
            cell1 = stylingCell(cell1);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getEinheitFromZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell2 = stylingCell(cell2);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(rezept_Zutat2.getZutat()),
                    FONT_TABLE_REZEPTZUTATEN));
            cell3 = stylingCell(cell3);

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
        cellMenge.setHorizontalAlignment(Element.ALIGN_MIDDLE);
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
        String anzahlPortionen = portionen == 1 ? "Portion" : "Portionen";
        PdfPCell captionTable = new PdfPCell(new Paragraph("Zutaten für " + portionen + " " + anzahlPortionen + ":",
                FONT_PORTIONEN));
        stylingCaptionTable(captionTable);
        return captionTable;
    }

    private void stylingCaptionTable(PdfPCell captionTable) {
        captionTable.setColspan(3);
        captionTable.setBorder(0);
        captionTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        captionTable.setPaddingBottom(5f);
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
        bild.scaleToFit(imageWidt, 350f);

        PdfPCell imageCell = new PdfPCell(bild);
        imageCell = stylingImageCell(imageCell);
        return imageCell;
    }

    private PdfPCell stylingImageCell(PdfPCell imageCell) {
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
        cellZubereitung = stylingZubereitung(cellZubereitung);
        return cellZubereitung;
    }

    private PdfPCell stylingZubereitung(PdfPCell cellZubereitung) {
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
        PdfPCell titleTable = new PdfPCell(new Paragraph(rezept.getTitel(),
                FONT_TITLE));
        titleTable = stylingTitleTable(titleTable);
        return titleTable;
    }

    private PdfPCell stylingTitleTable(PdfPCell titleTableCell) {
        titleTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTableCell.setBorder(0);
        titleTableCell.setColspan(2);
        titleTableCell.setPaddingBottom(15f);
        return titleTableCell;
    }

}
