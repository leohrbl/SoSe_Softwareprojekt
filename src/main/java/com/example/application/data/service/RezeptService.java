package com.example.application.data.service;

import java.util.LinkedList;
import java.util.List;

import com.example.application.data.entity.Rezept;
import com.example.application.data.repository.RezeptRepository;

import com.example.application.views.rezept.display.RezeptuebersichtView;
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

    /**
     * Die Methode erstellt eine Ergebnisliste der Zutaten anhand eines Suchtexts. Anschließend wird geprüft, welche Objekte des Suchergebnisses ebenfalls in den übergebenen bereits
     * gefilterten Rezepten vorhanden sind. Dadurch wird eine gefilterte Ergebnisliste mit einer Ergebnisliste der Textsuche kombiniert und zurückgegeben.
     * @see RezeptuebersichtView
     * @param searchText
     * @param filteredItemsByZutat
     * @return Liste (Java.Util.Collection) von den kombinierten Ergebnislisten
     * @author Léo Hérubel
     */
    public List<Rezept> getRezeptByFilterAndSearchText(String searchText, List<Rezept> filteredItemsByZutat) {
        List<Rezept> filteredItemsByText = searchRezeptByFilterText(searchText);
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

    public void delete(long id) {
        rezeptRepository.deleteById(id);
    }

}
