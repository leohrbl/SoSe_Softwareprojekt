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
    private final RezeptService service;

    @Autowired
    public RezeptZutatenService(RezeptZutatenRepository rezeptZutatenRepository, RezeptService service) {
        this.rezeptZutatenRepository = rezeptZutatenRepository;
        this.service = service;
    }

    public void createRezeptZutaten(Rezept rezept, Zutat zutat, int menge) {
        rezeptZutatenRepository.save(new Rezept_Zutat(rezept, zutat, menge));
    }

    public List<Rezept_Zutat> findAllByRezept(Rezept rezept) {
        return rezeptZutatenRepository.findAllByRezept(rezept);
    }

    public List<Zutat> findAllZutatenByRezept(Rezept rezept) {
        List<Zutat> liste = new LinkedList<>();
        for (Rezept_Zutat zutat : rezeptZutatenRepository.findAllByRezept(rezept)) {
            liste.add(zutat.getZutat());
        }
        return liste;
    }

    public List<Rezept> findAllRezepteByZutat(Zutat zutat) {
        List<Rezept> liste = new LinkedList<>();
        for (Rezept_Zutat rezept : rezeptZutatenRepository.findAllByZutat(zutat)) {
            liste.add(rezept.getRezept());
        }
        return liste;
    }

    // DelteById

    public void deleteAllByRezept(Rezept rezept) {
        rezept.setZutaten(new HashSet<Rezept_Zutat>());
        service.createRezept(rezept);
        // rezeptZutatenRepository.deleteAllByRezept(rezept);
    }
}
