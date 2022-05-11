package com.example.application.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * Entität Kategorie mit drei Instansvariablen: name als String, id als long und SequenceNr (Reihenfolgenummer) Außerdem Konstruktoren, Getter und Setter
 * @author Anna Karle
 * @see com.example.application.data.repository.KategoerieRepository
 * @see com.example.application.data.service.KategorieService
 */
@Entity
public class Kategorie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	@Column
	private long sequenceNr;
	
	public Kategorie () {
		
	}
	
	public Kategorie(String name) {
		this.name = name;
	}
	
	public Kategorie(String name, long sequenceNr) {
		this.name = name;
		this.sequenceNr = sequenceNr;
	}

	public Long getId() {
		return id;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public long getSequenceNr() {
		return sequenceNr;
	}

	public void setSequenceNr(long sequenceNr) {
		this.sequenceNr = sequenceNr;
	}
	
	@Override
	public String toString() {
		return this.sequenceNr + " " + this.name;
	}
	
	
	
	
	
	
	
	

}
