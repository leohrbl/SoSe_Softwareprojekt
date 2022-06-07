package com.example.application.views.drucken;

import com.example.application.data.einkaufslisteneintrag.EinkaufslistenEintrag;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Die Klasse erzeugt die Einkaufsliste als PDF.
 *
 * @author Edwin Polle
 */

public class DruckServiceEinkaufsliste {

    private static DruckServiceEinkaufsliste druckServiceEinkaufsliste;

    private List<EinkaufslistenEintrag> list;

    /**
     * Der Konstruktor initialisiert die Liste für die EinkaufsEinträge.
     */
    private DruckServiceEinkaufsliste() {
        this.list = new LinkedList<>();
    }

    /**
     * Die Methode getInstance() erzeugt über den Konsturktor die Instanz des
     * DruckServices.
     *
     * @return gibt die Instanz des Druckservices zurück.
     */
    public static DruckServiceEinkaufsliste getInstance() {
        if (druckServiceEinkaufsliste == null) {
            druckServiceEinkaufsliste = new DruckServiceEinkaufsliste();
        }
        return druckServiceEinkaufsliste;
    }

    /**
     * Der Methode getEinkaufslistenEintrag() wird die Liste mit den Einträgen
     * übergeben erzeugt die Tabelle für die PDF.
     *
     * @param liste
     * @return Tabelle mit den Einträgen der Einkaufliste.
     */
    private PdfPCell getEinkaufslistenEintrag(List liste) {

        PdfPTable table = new PdfPTable(3);
        PdfPCell cellMenge = new PdfPCell(new Paragraph("Menge", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
        cellMenge.setPadding(5);
        PdfPCell cellEinheit = new PdfPCell(new Paragraph("Einheit", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
        cellEinheit.setPadding(5);
        PdfPCell cellZutat = new PdfPCell(new Paragraph("Zutat", FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
        cellZutat.setPadding(5);

        table.addCell(cellMenge);
        table.addCell(cellEinheit);
        table.addCell(cellZutat);

        this.list.addAll(liste);
        for (EinkaufslistenEintrag element : this.list) {
            Paragraph menge = new Paragraph(String.valueOf(element.getMenge()),
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 10));
            menge.setAlignment(Element.ALIGN_RIGHT);
            PdfPCell cell1 = new PdfPCell(menge);
            cell1.setPadding(5);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell cell2 = new PdfPCell(new Paragraph(String.valueOf(element.getEinheitByZutat()),
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            cell2.setPadding(5);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(element.getZutat()),
                    FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
            cell3.setPadding(5);

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
        }

        PdfPCell tableCell = new PdfPCell(table);
        tableCell.setBorder(0);

        return tableCell;
    }

    /**
     * Die Methode createTitle() erzeugt die Überschrift düe dir PDF.
     *
     * @return Überschrift.
     */
    private PdfPCell createTitle() {
        Paragraph title = new Paragraph("Ihre Einkaufsliste", FontFactory.getFont(FontFactory.TIMES_ROMAN, 16));
        title.setAlignment(Element.ALIGN_CENTER);

        PdfPCell titleTable = new PdfPCell(title);
        titleTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleTable.setBorder(0);
        titleTable.setPaddingBottom(15);

        return titleTable;
    }

    /**
     * Die Methode createMainTable erzeugt das Hauptlayout für die PDF Seite.
     *
     * @return Mainlayout.
     */
    private PdfPTable createMainTable() {
        PdfPTable mainTable = new PdfPTable(1);
        mainTable.setWidthPercentage(100);
        return mainTable;
    }

    /**
     * Die Methode createPDF() erzeugt aus der übergebenen Liste mit den
     * Einkuafslisten-Einträgen die PDF.
     *
     * @param eintragsliste mit den Einträgen zum drucken.
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void createPDF(List eintragsliste) throws DocumentException, FileNotFoundException {
        FileOutputStream file = null;
        try {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream("Einkaufsliste.pdf"));
            document.open();

            PdfPTable maintable = createMainTable();

            PdfPCell titelTable = createTitle();

            PdfPCell table = getEinkaufslistenEintrag(eintragsliste);

            maintable.addCell(titelTable);
            maintable.addCell(table);

            document.add(maintable);

            document.close();
            clearList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Die Methode clearList() bereinigt die Liste nachdem, die PDF erfolgreich
     * erstellt wurde.
     */

    public void clearList() {
        list.clear();
    }

}