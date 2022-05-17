package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Zutat;
import com.example.application.data.repository.EinheitenRepository;
import com.helger.commons.hierarchy.visit.EHierarchyVisitorReturn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Philipp Laupichler
 *         Diese Klasse dient als Service-Klasse für die Entity "Einheit"
 *         Der Service greift auf das Repository zu
 */
@Service
public class EinheitService {

    private final EinheitenRepository einheitenRepository;

    @Autowired
    public EinheitService(EinheitenRepository einheitenRepository) {
        this.einheitenRepository = einheitenRepository;
    }

    /**
     * Noch nicht getestet
     *
     * @return Liste mit allen Einheiten
     */
    public List<Einheit> getEinheiten() {
        return einheitenRepository.findAll();
    }

    /**
     * Diese Methode speichert eine Einheit in der Datenbank
     *
     * @param name
     */
    public String createEinheit(String name) {
        try{
            Einheit einheit = new Einheit();
            einheit.setEinheit(name);
            einheitenRepository.save(einheit);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    /**
     * Diese Methode ändert den Namen einer Einheit, falls die Id nicht in der
     * Datenbank zufinden ist, wird ein String zurückgegeben. Wenn die Id existiert
     * wird das Einheiten Objekt gesucht und der Name Mithilfe der Setter-Methode
     * geändert und gespeichert. "Seccess" wird zurückgegeben.
     *
     * @param name
     * @param id
     */
    public String updateEinheit(String name, Long id) {
        if (!einheitenRepository.existsById(id)) {
            return "Einheit konnte nicht geändert werden";
        }

        Einheit einheit = einheitenRepository.findById(id).get();
        einheit.setEinheit(name);
        einheitenRepository.save(einheit);

        return "success";
    }

    /**
     * Diese Methode löscht eine Einheit, ist allerdings noch nicht getestet!!!
     *
     * @param einheit
     */
    public String deleteEinheit(Einheit einheit) {
        try {
            einheitenRepository.delete(einheit);
            return "success";
        }catch(Exception e){
            return e.getMessage();
        }
    }

    public Einheit findById(Long id) {
        return einheitenRepository.findById(id).get();
    }

    public Einheit findByName(String name){
        return einheitenRepository.findByEinheit(name);
    }
    
    /** 
     * @author Anna Karle
     * @param bezeichnung
     * @return
     */
    public Einheit searchEinheitByBezeichnung(String bezeichnung) {
    	return this.einheitenRepository.findByEinheit(bezeichnung);
    }
    
}
