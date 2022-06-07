package com.example.application.data.einheit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Philipp Laupichler
 * @see Einheit
 * @see EinheitService
 * 
 */
public interface EinheitenRepository extends JpaRepository<Einheit, Long> {
    /**
     * Sucht nach einer Einheit, anhand des Namens
     * 
     * @param name Name der Einheit, nach der gesucht wird
     * @return Einheit
     */
    Einheit findByEinheit(String name);

    /**
     * Sucht nach einer Einheit, anhand der Id
     * 
     * @param id Id der Einheit ,nach der gesucht wird
     * @return Einheit
     */
    Einheit findById(long id);

    /**
     * Löscht eine Einheit, das Suchkriterium ist der Name
     * 
     * @param name Name der Einheit, nach der gesucht wird
     */
    void deleteByEinheit(String name);
}
