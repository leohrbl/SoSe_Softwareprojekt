package com.example.application.data.repository;

import com.example.application.data.entity.Zutat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Repository Klasse der Entität Zutat. Auf die Methoden des JpaRepository wird über eine Service-Klasse zugegriffen.
 * @author Léo Hérubel
 * @see Zutat
 * @see com.example.application.data.service.ZutatService
 */
public interface ZutatRepository extends JpaRepository<Zutat, UUID> {

    /**
     * Sucht anhand eines Strings Datensätze aus der Zutaten Tabelle, welche dem String ohne Berücksichtigung der Groß- und Kleinschreibung ähneln.
     * @param searchTerm
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben
     */
    @Query("select z from Zutat z " +
            "where lower(z.name) like lower(concat('%', :searchTerm, '%')) ")
    List<Zutat> search(@Param("searchTerm") String searchTerm);

    /**
     * Sucht anhand der ID des Fremdschlüssels der Einheit alle Datensätze von Zutaten.
     * @param id
     * @return Das Suchergebnis wird als Liste (Java.Util.Collection) zurückgegeben
     */
    @Query("select z from Zutat z where z.einheit.id = :id")
    List<Zutat> findByEinheit_Id(@Param("id") Long id);
}