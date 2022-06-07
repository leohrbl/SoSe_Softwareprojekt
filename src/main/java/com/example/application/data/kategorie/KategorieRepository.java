package com.example.application.data.kategorie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KategorieRepository extends JpaRepository<Kategorie, Long> {
	/**
	 * Sucht Datensätze aus der Kategorie Tabelle anhand eines Strings ohne
	 * Berücksichtigung der Groß- und Kleinschreibung,
	 * 
	 * @param searchTerm
	 * @return Liste mit Kategorien, die dem Suchtext entsprechen
	 */
	@Query("select k from Kategorie k " +
			"where lower(k.name) like lower(concat('%', :searchTerm, '%')) ")
	List<Kategorie> search(@Param("searchTerm") String searchTerm);

	/**
	 * Sucht eine Kategorie anhand einer Reihenfolgenummer
	 * 
	 * @param sequenceNr
	 * @return Kategerie, die entsprechende Reihenfolgenummer hat
	 */
	@Query("select k from Kategorie k where k.sequenceNr = (:sequenceNr)")
	Kategorie searchBySequenceNr(@Param("sequenceNr") String sequenceNr);

	/**
	 * Sucht nach eine Kategorie anhand der Id
	 * 
	 * @param id
	 * @return Kategorie
	 */
	Kategorie findById(long id);

	/**
	 * Löscht eine Kategorie, das Suchkriterium ist der Name
	 * 
	 * @param name
	 * @return
	 */
	@Query("delete from Kategorie k where k.name =(:name)")
	void deleteByName(@Param("name") String name);

	Kategorie findByName(String name);

}
