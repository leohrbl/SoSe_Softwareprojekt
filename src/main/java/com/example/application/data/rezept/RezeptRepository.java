package com.example.application.data.rezept;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository der Entität Rezept. Auf die Methoden des JpaRepository wird
 * über eine Service-Klasse zugegriffen.
 *
 * @author Philipp Laupichler
 * @see Rezept
 * @see RezeptService
 */
@Repository
public interface RezeptRepository extends JpaRepository<Rezept, Long> {
    /**
     * Sucht anhand einer id nach dem Rezept und gibt dieses zurück
     *
     * @param id
     * @return Gibt ein Rezept zurück
     */
    Rezept findById(long id);

    /**
     * Sucht anhand des Titels nach dem Rezept und gibt dieses zurück
     *
     * @param Titel
     * @return Gibt ein Rezept zurück
     */
    Rezept findByTitel(String Titel);

    /**
     * Sucht anhand eines Strings Datensätze aus der Zutaten Tabelle, welche dem
     * Input String ohne Berücksichtigung der Groß- und Kleinschreibung ähneln.
     *
     * @param searchTerm
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben.
     * @author Léo Hérubel
     */
    @Query("select z from Rezept z " +
            "where lower(z.titel) like lower(concat('%', :searchTerm, '%')) ")
    List<Rezept> search(@Param("searchTerm") String searchTerm);

    /**
     * Löscht eine Rezept anhand einer id
     * 
     * @param id
     */
    void deleteById(long id);

    /**
     * Sucht nach allen vorhanden Rezepten und gibt diese sortiert zurück
     * 
     * @return Gibt eine Liste mit Rezepten zurück, die anhand der
     *         Kategorie.sequence_nr sortiert wurden
     */
    @Query(value = "Select * from Rezept INNER JOIN Kategorie ON Rezept.kategorie_id=Kategorie.id  order by Kategorie.sequence_nr asc", nativeQuery = true)
    List<Rezept> findAllByKategorieSequenceNr();

}
