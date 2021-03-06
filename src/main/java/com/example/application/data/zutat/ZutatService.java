package com.example.application.data.zutat;

import com.example.application.data.einheit.Einheit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Die Klasse ist die Service-Klasse der Entität Zutat und bietet neben
 * Grundlegenden CRUD Operationen 2 weitere Funktionen an. Es können Rezepte zu
 * einer Einheit_Id gesucht werden und es kann nach dem Attribut name der Zutat
 * gesucht werden.
 *
 * @author Léo Hérubel
 * @see Zutat
 * @see ZutatRepository
 */

@Service
public class ZutatService {

    private final ZutatRepository zutatRepository;

    /**
     * Konstruktor welcher die Instanzvariable zutatRepository initialisiert
     *
     * @param zutatRepository Repository wird als Instanzvariable der Klasse
     *                        initialisiert, damit man auf dessen Methoden zugreifen
     *                        kann.
     */
    @Autowired
    public ZutatService(ZutatRepository zutatRepository) {
        this.zutatRepository = zutatRepository;
    }

    /**
     * Erzeugt eine Liste von allen Zutaten aus der Datenbank und gibt diese zurück.
     *
     * @return gibt eine Liste (Java.Util.Collection) von allen Zutaten zurück,
     *         welche sich zum Zeitpunkt der Transaktion
     */
    public List<Zutat> getZutaten() {
        return zutatRepository.findAll();
    }

    /**
     * In dieser Methode kann anhand der Eingabe des Names und der Übergabe einer
     * bereits persistierten Einheit eine Zutat erzeugt und in der Datenbank
     * gespeichert werden.
     *
     * @param name    Name der Zutat
     * @param einheit Einheit, welche zu der Zutat gespeichert wird
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String saveZutat(String name, Einheit einheit) {
        try {
            Zutat zutat = new Zutat();
            zutat.setName(name.trim());
            zutat.setEinheit(einheit);
            zutatRepository.save(zutat);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * In dieser Methode kann eine bereits persistierte Zutat mit neuen Werten
     * überschrieben werden.
     *
     * @param zutat Zutat, welche aktualisiert wird
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String updateZutat(Zutat zutat) {
        try {
            zutatRepository.save(zutat);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Mit dieser Methode kann der Nutzer anhand von einem Eingabe-String Zutaten
     * finden.
     *
     * @param filterText
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben
     */
    public List<Zutat> searchZutatenByFilterText(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return null;
        } else {
            return zutatRepository.search(filterText.trim());
        }
    }

    /**
     * Methode zum Löschen einer bereits persistierten Zutat Entity.
     *
     * @param zutat Zutat, welche gelöscht werden soll
     * @return Gibt die Meldung "success" bei Erfolg oder die Fehlermeldung der
     *         Exception zurück
     */
    public String deleteZutat(Zutat zutat) {
        try {
            zutatRepository.delete(zutat);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Methode zum Anzeigen der Zutaten, welche zu einer Einheit gehören.
     *
     * @param id Es wird die Einheit anhand der Id des Fremdschlüssels gesucht.
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben
     */
    public List<Zutat> findZutatenByEinheitId(Long id) {
        return zutatRepository.findByEinheit_Id(id);
    }

    /**
     * @author Joscha Cerny
     *         Methode zum Zurückgeben von Einer Zutat Entity mit einem angegebenen
     *         Namen
     */
    public Zutat getZutatenByName(String name) {
        return zutatRepository.getZutatenByName(name.trim());
    }
}
