package com.example.application.data.repository;

import java.util.List;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.RezeptZutatenService;

import org.springframework.data.jpa.repository.JpaRepository;

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
}
