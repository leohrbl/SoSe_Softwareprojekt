package com.example.application.data.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.entity.Zutat;
import com.example.application.data.repository.RezeptZutatenRepository;

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

    public String createRezeptZutatenNew(Rezept rezept, Zutat zutat, double menge) {
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
}
