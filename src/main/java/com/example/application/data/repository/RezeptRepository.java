package com.example.application.data.repository;

import java.util.List;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.RezeptService;

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
     * Sucht anhand eines Strings Datensätze aus der Zutaten Tabelle, welche dem Input String ohne Berücksichtigung der Groß- und Kleinschreibung ähneln.
     *
     * @param searchTerm
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben.
     * @author Léo Hérubel
     */
    @Query("select z from Rezept z " +
            "where lower(z.titel) like lower(concat('%', :searchTerm, '%')) ")
    List<Rezept> search(@Param("searchTerm") String searchTerm);

    void deleteById(long id);
}
