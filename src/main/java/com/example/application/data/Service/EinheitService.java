package com.example.application.data.Service;

import java.util.List;

import com.example.application.data.entity.Einheit;
import com.example.application.data.repository.EinheitenRepository;

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
     * @param einheit
     */
    public void createEinheit(Einheit einheit) {
        einheitenRepository.save(einheit);
    }

    /**
     * Noch nicht getestet
     * 
     * @param name
     * @param id
     */
    public void updateEinheit(String name, Long id) {
        Einheit einheit = einheitenRepository.findByEinheit(name);
        einheit.setEinheit(name);
        einheitenRepository.save(einheit);
    }

    /**
     * Diese Methode löscht eine Einheit, ist allerdings noch nicht getestet!!!
     * 
     * @param name
     */
    public void deleteEinheit(String name) {
        einheitenRepository.deleteByEinheit(name);
    }
}
