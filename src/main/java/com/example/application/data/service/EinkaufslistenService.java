package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.EinkaufslistenEintrag;
import com.example.application.data.repository.EinkaufslistenRepository;
import com.example.application.data.entity.Zutat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Klasse für EinkaufslistenEintrag. Kann neuen Eintrag erstellen, bestehende verändern und bestehende löschen.
 * @author Joscha Cerny
 * @see EinkaufslistenEintrag
 * @see EinkaufslistenRepository
 */

@Service
public class EinkaufslistenService {

    private final EinkaufslistenRepository einkaufslistenRepository;

    /**
     * Konstruktor für Aufruf der Klasse
     */
    @Autowired
    public EinkaufslistenService(EinkaufslistenRepository einkaufslistenEintragRepository) {
        this.einkaufslistenRepository = einkaufslistenEintragRepository;
    }

    /**
     * Gibt alle Einträge der bestehden liste an
     */
    public List<EinkaufslistenEintrag> getAllEintrag() {
        return einkaufslistenRepository.findAll();
    }

    /**
     * Löscht einträge basierend auf ihrer id
     */
    public void deleteEintragByID(long id) {
        einkaufslistenRepository.deleteById(id);
    }

    public EinkaufslistenEintrag findByEintragsID(long id)
    {
        return einkaufslistenRepository.findById(id);
    }

    /**
     * Methode zum Speichern von Einträgen in der Datenbank
     */
    public String saveEintrag(Integer menge, Zutat zutat){
        try{

            EinkaufslistenEintrag eintrag = new EinkaufslistenEintrag();
            eintrag.setMenge(menge);
            eintrag.setZutat(zutat);

            einkaufslistenRepository.save(eintrag);
            return "success1";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    /**
     * Methode zum Aktualisieren von bestehenden Einträgen

    public String updateEintrag(EinkaufslistenEintrag newEintrag) {
        try{
            EinkaufslistenEintrag eintrag = new EinkaufslistenEintrag();
            eintrag.setMenge(newEintrag.getMenge());
            eintrag.setZutatID(newEintrag.getZutatID());
            einkaufslistenRepository.save(eintrag);
            return "success";
        }catch(Exception e) {
            return e.getMessage();
        }
    }
     */

    /**
     * Methode zum Löschen von bestehenden Einträgen
     */
    public String deleteEintrag(EinkaufslistenEintrag eintrag) {
        try {
            einkaufslistenRepository.delete(eintrag);
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    /**
     * Methode zum Löschen von allen Einträgen
     */
    public String deleteAll()
    {
        try {
            einkaufslistenRepository.deleteAll();
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }

    }
}
