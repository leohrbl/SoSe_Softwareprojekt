package com.example.application.data.rezeptzutat;

import java.util.*;

import javax.transaction.Transactional;

import com.example.application.data.zutat.Zutat;
import com.example.application.data.rezept.Rezept;
import com.example.application.data.rezept.RezeptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Die Klasse ist die Serice-Klasse der Enität @Repzept_Zutaten
 *
 * @author Philipp Laupichler
 * @see Rezept_Zutat
 * @see RezeptZutatenRepository
 */
@Service
public class RezeptZutatenService {

    private final RezeptZutatenRepository rezeptZutatenRepository;
    private final RezeptService rezeptService;

    /**
     * Konstruktor, welcher die Instanzvariablen, rezeptService und
     * rezeptZutatenRepository initialisiert
     *
     * @param rezeptZutatenRepository Repository wird als Instanzvariable der Klasse
     *                                initialisiert, damit man auf dessen Methoden
     *                                zugreifen kann.
     * @param rezeptService           Service wird als Instanzvariable der Klasse
     *                                initialisiert, damit man auf dessen Methoden
     *                                zugreifen kann.
     */
    @Autowired
    public RezeptZutatenService(RezeptZutatenRepository rezeptZutatenRepository, RezeptService rezeptService) {
        this.rezeptZutatenRepository = rezeptZutatenRepository;
        this.rezeptService = rezeptService;
    }

    /**
     * Erstellt einen Datensatz RezeptZutat anhand eines Rezeptes, der Zutat und der
     * entsprechenden Menge
     *
     * @param rezept Rezept
     * @param zutat  Zutat
     * @param menge  Menge
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String createRezeptZutaten(Rezept rezept, Zutat zutat, double menge) {
        try {
            rezeptZutatenRepository.save(new Rezept_Zutat(rezept, zutat, menge));
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Erstellt einen Datensatz RezeptZutat anhand eines Rezeptes, der Zutat und der
     * entsprechenden Menge und fügt diesen dem Rezept hinzu
     * 
     * @param rezept Rezept
     * @param zutat  Zutat
     * @param menge  Menge
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String createRezeptZutatenAndAddToSet(Rezept rezept, Zutat zutat, double menge) {
        try {
            rezept.addZutat(rezeptZutatenRepository.save(new Rezept_Zutat(rezept, zutat, menge)));
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * In dieser Methode wird eine Liste zurückgegeben, die alle Datensätze enthält,
     * die zu einem Rezept gespeichert wurden
     *
     * @param rezept Rezept, nach dem gesucht wird
     * @return Liste mit allen Rezept_Zuatat Datensätzen
     */
    @Transactional
    public List<Rezept_Zutat> findAllByRezept(Rezept rezept) {
        return rezeptZutatenRepository.findAllByRezept(rezept);
    }

    /**
     * In dieser Methode wird eine Liste zurückgegeben, die alle Zutaten enthält,
     * die zu einem Rezept gespeichert wurden
     *
     * @param rezept Rezept, nach dem gesucht wird
     * @return Liste mit allen Zuaten
     */
    @Transactional
    public List<Zutat> findAllZutatenByRezept(Rezept rezept) {
        List<Zutat> liste = new LinkedList<>();
        for (Rezept_Zutat zutat : rezeptZutatenRepository.findAllByRezept(rezept)) {
            liste.add(zutat.getZutat());
        }
        return liste;
    }

    /**
     * In dieser Methode wird eine Liste zurückgegeben, die alle Rezepte enthält,
     * in der eine bestimmte Zutat enthalten ist
     *
     * @param zutat Zutat nach der gesucht wird
     * @return Liste mit Rezepten
     */
    @Transactional
    public List<Rezept> findAllRezepteByZutat(Zutat zutat) {
        List<Rezept> liste = new LinkedList<>();
        for (Rezept_Zutat rezept : rezeptZutatenRepository.findAllByZutat(zutat)) {
            liste.add(rezept.getRezept());
        }
        return liste;
    }

    // DelteById

    /**
     * Diese Methode löscht alle Einträge in dem Set der Zutat und speichert dieses
     * dann
     *
     * @param rezept Rezept, bei dem die Rezept_Zutat Einträge gelöscht werden
     *               sollen
     */
    public void deleteAllByRezept(Rezept rezept) {
        rezept.setZutaten(new HashSet<Rezept_Zutat>());
        rezeptService.createRezept(rezept);
        // rezeptZutatenRepository.deleteAllByRezept(rezept);
    }

    /**
     * Sucht anhand von mehreren gefilterten Zutaten Rezepte.
     *
     * @param zutatSet Set welches aus dem ZutatFilterDialog übergeben wird
     * @return Gibt eine Liste von Rezepten zurück, welche im Frontend angezeigt
     *         werden können.
     * @author Léo Hérubel
     */
    @Transactional
    public List<Rezept> findAllRezepteByZutaten(Set<Zutat> zutatSet) {
        List<Rezept> filteredRezeptByZutaten = new LinkedList<>();
        for (Zutat zutat : zutatSet) {
            for (Rezept rezept : findAllRezepteByZutat(zutat)) {
                boolean isInRezeptList = false;
                for (Rezept filteredRezept : filteredRezeptByZutaten) {
                    if (filteredRezept.getTitel().equals(rezept.getTitel())) {
                        isInRezeptList = true;
                    }
                }
                if (!isInRezeptList) {
                    filteredRezeptByZutaten.add(rezept);
                }
            }
        }
        return filteredRezeptByZutaten;
    }

}
