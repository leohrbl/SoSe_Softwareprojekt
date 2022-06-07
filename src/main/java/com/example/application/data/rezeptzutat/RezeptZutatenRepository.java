package com.example.application.data.rezeptzutat;

import java.util.List;

import javax.transaction.Transactional;

import com.example.application.data.zutat.Zutat;
import com.example.application.data.rezept.Rezept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

/**
 * Repository der Entität Rezept_Zutat. Auf die Methoden des JpaRepository wird
 * über eine Service-Klasse zugegriffen.
 * 
 * @author Philipp Laupichler
 * @see Zutat
 * @see Rezept
 * @see Rezept_Zutat
 * @see RezeptZutatenService
 */
public interface RezeptZutatenRepository extends JpaRepository<Rezept_Zutat, Long> {

    /**
     * Sucht anhand eines Rezeptes alle Datensätze
     * 
     * @param rezept
     * @return Das Suchergebnis, ist eine Liste mit allen Datensätzen
     */
    List<Rezept_Zutat> findAllByRezept(Rezept rezept);

    /**
     * Sucht anhand einer Zutat alle Datensätze
     * 
     * @param zutat
     * @return Das Suchergebnis, ist eine Liste mit allen Datensätzen
     */
    List<Rezept_Zutat> findAllByZutat(Zutat zutat);

    /**
     * Löscht Rezept_Zutaten Datensätze anhand eines Rezeptes
     * 
     * @param rezept Rezept, dass gelöscht werden soll
     */
    @Modifying
    @Transactional
    void deleteAllByRezept(Rezept rezept);
}
