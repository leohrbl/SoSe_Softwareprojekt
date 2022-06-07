package com.example.application.data.einheit;

import java.util.List;

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
     * Diese Methode gibt eine Liste mit allen Einheiten, die in der Datenbank
     * gespeichert sind zurück
     *
     * @return Liste mit allen Einheiten
     */
    public List<Einheit> getEinheiten() {
        return einheitenRepository.findAll();
    }

    /**
     * Diese Methode speichert eine Einheit in der Datenbank
     *
     * @param name Name der Einheit
     */
    public String createEinheit(String name) {
        try {
            Einheit einheit = new Einheit();
            einheit.setEinheit(name.trim());
            einheitenRepository.save(einheit);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Diese Methode ändert den Namen einer Einheit, falls die Id nicht in der
     * Datenbank zufinden ist, wird ein String zurückgegeben. Wenn die Id existiert
     * wird das Einheiten Objekt gesucht und der Name Mithilfe der Setter-Methode
     * geändert und gespeichert. "Seccess" wird zurückgegeben.
     *
     * @param name Name, der Einheit
     * @param id   Id, der Einheit
     */
    public String updateEinheit(String name, Long id) {
        if (!einheitenRepository.existsById(id)) {
            return "Einheit konnte nicht geändert werden";
        }

        Einheit einheit = einheitenRepository.findById(id).get();
        einheit.setEinheit(name.trim());
        einheitenRepository.save(einheit);

        return "success";
    }

    /**
     * Diese Methode löscht eine Einheit aus der Datenbank
     * 
     * @param einheit Einheit die gelöscht werden soll
     * @return Erfolgsmeldung, bei erfolgreicher Löschung oder Fehlermeldung
     */
    public String deleteEinheit(Einheit einheit) {
        try {
            einheitenRepository.delete(einheit);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Diese Methode sucht nach einem Einheiten-Objekt und gibt dieses zurück
     * 
     * @param id Id, der Einheit, nach der gesucht wird
     * @return Einheit, mit der entsprechenden Id
     */
    public Einheit findById(Long id) {
        return einheitenRepository.findById(id).get();
    }

    /**
     * Diese Methode sucht nach einem Einheiten-Objekt und gibt dieses zurück
     * 
     * @param name Name, der Einheit, nach der gesucht wird
     * @return Einheit, mit dem entsprechenden Namen
     * @return Einheit
     */
    public Einheit findByName(String name) {
        return einheitenRepository.findByEinheit(name.trim());
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
