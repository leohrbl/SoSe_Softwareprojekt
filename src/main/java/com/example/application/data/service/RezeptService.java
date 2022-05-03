package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.Rezept;
import com.example.application.data.repository.RezeptRepository;

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

    @Autowired
    public RezeptService(RezeptRepository rezeptRepository) {
        this.rezeptRepository = rezeptRepository;
    }

    public List<Rezept> getAllRezepte() {
        return rezeptRepository.findAll();
    }

    public void createRezept(Rezept rezept) {
        rezeptRepository.save(rezept);
    }

    public boolean updateRezept(Rezept rezept) {
        if (rezept == null) {
            return false;
        }
        /*
         * Check every Value
         * Update Values
         */
        return true;
    }

    public void deleteRezept(Rezept rezept) {
        rezeptRepository.delete(rezept);
    }

    public Rezept findByTitel(String titel) {
        return rezeptRepository.findByTitel(titel);
    }

    public Rezept findById(long id) {
        return rezeptRepository.findById(id);
    }

    public List<Rezept> searchRezeptByFilterText(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return null;
        } else {
            return rezeptRepository.search(filterText);
        }
    }
}
