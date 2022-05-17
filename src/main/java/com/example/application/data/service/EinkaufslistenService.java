package com.example.application.data.service;

import java.util.List;

import com.example.application.data.entity.EinkaufslistenEintrag;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.repository.EinkaufslistenRepository;
import com.example.application.data.entity.Zutat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Klasse für EinkaufslistenEintrag. Kann neuen Eintrag erstellen, bestehende verändern und bestehende löschen.
 *
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

    public EinkaufslistenEintrag findByEintragsID(long id) {
        return einkaufslistenRepository.findById(id);
    }

    /**
     * Methode zum Speichern von Einträgen in der Datenbank
     */
    public String saveEintrag(double menge, Zutat zutat) {
        try {

            EinkaufslistenEintrag eintrag = new EinkaufslistenEintrag();
            eintrag.setMenge(menge);
            eintrag.setZutat(zutat);

            einkaufslistenRepository.save(eintrag);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Methode zum Hinzufügen von Rezept_Zutat Objekten aus der RezeptView in die Einkaufsliste. Es wird geprüft, ob die Zutaten der Rezept_Zutat Liste bereits in der Einkaufsliste existieren.
     * Ansonsten wird pro nicht existierender Zutat ein neuer Einkaufslisteneintrag erstellt. Die Menge der Rezept_Zutat Objekte wird auf die Einträge mit der gleichen Zutat kumuliert.
     * @param mengenList
     * @see com.example.application.views.einkaufsliste.EinkaufslisteView
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der Exception zurück
     * @author Léo Hérubel
     */
    public String addEintraege(List<Rezept_Zutat> mengenList) {
        try {
            if (mengenList.isEmpty()) {
                return "Keine Zutaten zum Hinzufügen in die Einkaufsliste vorhanden.";
            }
            for (Rezept_Zutat rezeptZutat : mengenList) {
                EinkaufslistenEintrag eintrag = existsInEintrag(rezeptZutat.getZutat());
                if (eintrag != null) {
                    double newMenge = eintrag.getMenge() + rezeptZutat.getMenge();
                    eintrag.setMenge(newMenge);
                    updateEintrag(eintrag);
                } else {
                    saveEintrag(rezeptZutat.getMenge(), rezeptZutat.getZutat());
                }
            }
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Prüft, ob eine Zutat bereits in einem Einkaufslisteneintrag existiert. Falls diese nicht existiert wird null zurückgegeben. Ansonsten wird der Eintrag zurückgegeben.
     * @param zutat
     * @return gibt den bereits existierenden Eintrag oder Null zurück
     * @author Léo Hérubel
     */
    private EinkaufslistenEintrag existsInEintrag(Zutat zutat) {
        List<EinkaufslistenEintrag> eintraege = getAllEintrag();

        for (EinkaufslistenEintrag eintrag : eintraege) {
            if (eintrag.getZutat().getId() == zutat.getId()) {
                return eintrag;
            }
        }
        return null;
    }


    public String updateEintrag(EinkaufslistenEintrag eintrag) {
        try {
            einkaufslistenRepository.save(eintrag);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    /**
     * Methode zum Löschen von bestehenden Einträgen
     */
    public String deleteEintrag(EinkaufslistenEintrag eintrag) {
        try {
            einkaufslistenRepository.delete(eintrag);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Methode zum Löschen von allen Einträgen
     */
    public String deleteAll() {
        try {
            einkaufslistenRepository.deleteAll();
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
