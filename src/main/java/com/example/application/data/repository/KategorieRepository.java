package com.example.application.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.data.entity.Kategorie;


public interface KategorieRepository extends JpaRepository<Kategorie, UUID> {
	
	/**
	 * Sucht nach eine Kategorie anhand des Namens
	 * @param name
	 * @return Kategorie
	 * 
	 */
	Kategorie findByKategorie(String name);
	
	/**
	 * Sucht nach eine Kategorie anhand der Id
	 * @param id
	 * @return Kategorie
	 */
	Kategorie findById (long id);
	
	/**
	 * Löscht eine Kategorie, das Suchkriterium ist der Name
	 * @param name
	 */
	
	void deleteByKategorie(String name);

}
