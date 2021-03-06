package com.example.application.data.rezept;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import com.example.application.data.kategorie.Kategorie;
import com.example.application.views.rezept.display.RezeptuebersichtView;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Die Klasse ist die Serice-Klasse der Enität @Rezept
 *
 * @author Philipp Laupichler
 * @see Rezept
 * @see RezeptRepository
 */
@Service
public class RezeptService {

    private final RezeptRepository rezeptRepository;

    /**
     * Konstruktor, welcher die Instanzvariablen, rezeptService und
     * rezeptZutatenRepository initialisiert
     * 
     * @param rezeptRepository Repository wird als Instanzvariable der Klasse
     *                         initialisiert, damit man auf dessen
     *                         Methoden
     *                         zugreifen kann
     */
    @Autowired
    public RezeptService(RezeptRepository rezeptRepository) {
        this.rezeptRepository = rezeptRepository;
    }

    /**
     * Diese Methode gibt alle Rezepte zurück, die gespeichert sind
     * 
     * @return Liste mit Rezepten
     */
    @Transactional
    public List<Rezept> getAllRezepte() {
        return rezeptRepository.findAll();
    }

    /**
     * Methode, die ein Rezept in der Datenbank speichert, bei der jede Variable
     * einzeln übergeben wird
     * 
     * @param bild        Bild des Rezeptes
     * @param titel       Titel des Rezeptes
     * @param zubereitung Zubereitung des Rezeptes
     * @param portionen   Protionen des Rezeptes
     * @param kategorie   Kategorie des Rezeptes
     * @return Bei einem Fehler wird null zurückgegeben, wenn das Rezept erfolgreich
     *         gespeichert wird, wird dieses zurückgegeben
     */
    @Transactional
    public Rezept createRezept(byte[] bild, String titel, String zubereitung, int portionen, Kategorie kategorie) {
        System.out.println(titel.trim());
        System.out.println(titel.trim().length() == 0);
        if (findByTitel(titel) != null) {
            return null;
        }
        try {
            Rezept rezept = new Rezept();
            rezept.setBild(bild);
            rezept.setTitel(titel.trim());
            rezept.setZubereitung(zubereitung.trim());
            rezept.setKategorie(kategorie);
            rezept.setPortionen(portionen);
            rezeptRepository.save(rezept);
            return rezept;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Methode, die ein Rezept in der Datenbank speichert, bei der das Rezept
     * übergeben wird
     * 
     * @param rezept Rezept, das gespeichert werden soll
     * @return succes beim erfolgreichen speichern, sonst die entsprechende
     *         Fehlermeldung
     */
    public String createRezept(Rezept rezept) {
        try {
            rezeptRepository.save(rezept);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Die Methode erstellt eine Ergebnisliste der Zutaten anhand eines Suchtexts.
     * Anschließend wird geprüft, welche Objekte des Suchergebnisses ebenfalls in
     * den übergebenen bereits
     * gefilterten Rezepten vorhanden sind. Dadurch wird eine gefilterte
     * Ergebnisliste mit der Ergebnisliste der Textsuche kombiniert und
     * zurückgegeben.
     * 
     * @author Léo Hérubel
     * @see RezeptuebersichtView
     * @param searchText
     * @param filteredItemsByZutat
     * @return Liste (Java.Util.Collection) von den kombinierten Ergebnislisten
     */
    @Transactional
    public List<Rezept> getRezepteByFilterAndSearchText(String searchText, List<Rezept> filteredItemsByZutat) {
        List<Rezept> filteredItemsByText = searchRezepteByFilterText(searchText);
        List<Rezept> filteredItemsByTextAndZutat = new LinkedList<>();
        for (Rezept filteredRezeptByZutat : filteredItemsByZutat) {
            for (Rezept filteredRezeptByText : filteredItemsByText) {
                if (filteredRezeptByText.getId() == filteredRezeptByZutat.getId()) {
                    filteredItemsByTextAndZutat.add(filteredRezeptByText);
                }
            }
        }
        return filteredItemsByTextAndZutat;
    }

    /**
     * Methode zum Löschen einer bereits persistierten Rezept Entity.
     *
     * @param rezept Rezept, welche gelöscht werden soll
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String deleteRezept(Rezept rezept) {
        try {
            rezeptRepository.delete(rezept);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Methode die, anhand des Titels nach einem Rezept sucht und dieses zurückgibt
     * 
     * @param titel Titel des Rezeptes
     * @return Rezept, welches den Titel hat
     */
    @Transactional
    public Rezept findByTitel(String titel) {
        return rezeptRepository.findByTitel(titel.trim());
    }

    /**
     * Methode die, anhand einer Id nach einem Rezept sucht und dieses zurückgibt
     * 
     * @param id Id des Rezeptes
     * @return Rezept, welches die Id hat
     */
    @Transactional
    public Rezept findById(long id) {
        return rezeptRepository.findById(id);
    }

    @Transactional
    public List<Rezept> searchRezepteByFilterText(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return null;
        } else {
            return rezeptRepository.search(filterText);
        }
    }

    /**
     * Die Methode setzt alle werte eines alten Rezeptes auf die Werte des Neuen
     * angegeben Rezeptes
     * 
     * @author Joscha Cerny
     */
    public void updateRezept(Rezept oldRezept, Rezept newRezept) {
        oldRezept.setZutaten(newRezept.getZutatenFromRezept_Zutaten());
        oldRezept.setPortionen(newRezept.getPortionen());
        oldRezept.setTitel(newRezept.getTitel());
        oldRezept.setBild(newRezept.getBild());
        oldRezept.setZubereitung(newRezept.getZubereitung());
        oldRezept.setKategorie(newRezept.getKategorie());

        rezeptRepository.save(oldRezept);
    }

    /**
     * Methode zum Löschen einer bereits persistierten Rezept Entity.
     *
     * @param id Id, des Rezeptes, dass gelöscht werden soll
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String delete(long id) {
        try {
            rezeptRepository.deleteById(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Transactional
    public List<Rezept> findAllbyKategorie() {
        return rezeptRepository.findAllByKategorieSequenceNr();
    }

    /**
     * Generiert ein Vaadin Image, aus dem Byte Array, welches in der Datenbank
     * gespeichert ist
     * 
     * @param rezept Rezept, von dem das Bild generiert werden soll
     * @return Image
     */
    public Image generateImage(Rezept rezept) {
        Long id = rezept.getId();
        StreamResource sr = new StreamResource("rezept", () -> {
            Rezept attached = rezeptRepository.findById(id).get();
            return new ByteArrayInputStream(attached.getBild());
        });
        sr.setContentType("image/png");
        Image image = new Image(sr, "image");
        return image;

    }

    /**
     * Diese Methode liest eine Datei ein und gibt ein Byte Array zurück
     *
     * @param imagePath
     *                  Pfad der Datei
     * @return Byte Array von der eingelesenen Datei
     * @throws IOException
     *                     Datei nicht gefunden
     */
    public static byte[] getBytesFromFile(String imagePath) throws IOException {
        File file = new File(imagePath);
        return Files.readAllBytes(file.toPath());
    }
}
