package com.example.application.data.repository;

import com.example.application.data.entity.Einheit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Philipp Laupichler
 * @param
 * @return
 *         EinheitenRepository basiert auf dem JPARepository, welches schon
 *         viele Methoden implementiert hat
 * 
 */
public interface EinheitenRepository extends JpaRepository<Einheit, Long> {
    /**
     * Sucht nach einer Einheit, anhand des Namens
     * 
     * @param name
     * @return Einheit
     */
    Einheit findByEinheit(String name);

    /**
     * Löscht eine Einheit, das Suchkriterium ist der Name
     * 
     * @param name
     */
    void deleteByEinheit(String name);
}
