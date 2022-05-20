package com.example.application.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Kategorie;
import com.example.application.data.entity.Rezept;
import com.example.application.data.repository.KategorieRepository;

/**
 * Die Klasse ist die Service-Klasse der Entität Kategorie.
 * @author Anna Karle
 * @see Kategorie
 * @see KategorieRepository 
 */
@Service
public class KategorieService {
	
	private final KategorieRepository kategorieRepository;
	
	
	public KategorieService(KategorieRepository kategorieRepository) {
		this.kategorieRepository = kategorieRepository;
	}
	
	
	public List <Kategorie> getKategorien (){
		return kategorieRepository.findAll();
	}
	
	public String saveKategorie(String name) {
		try {
			long sequenceNr = kategorieRepository.count();
			Kategorie kategorie = new Kategorie(name, sequenceNr);
			kategorieRepository.save(kategorie);
			return "success";
		}catch(Exception e){
			return e.getMessage();			
		}
	}
	
	public String deleteKategorie(Kategorie kategorie) {
		try {
			kategorieRepository.delete(kategorie);
			return "success";
		}catch(Exception e) {
			return e.getMessage();
		}
	}
	
	public String updateKategorie(String newName, long id) {
		if (kategorieRepository.findById(id) == null ) {
			return "Kategorie existiert nicht";
		}		
		Kategorie kategorie = kategorieRepository.findById(id);
	    kategorie.setName(newName);
	    kategorieRepository.save(kategorie);
		return "success";
	}

	
	public void updateSequenceNr(Kategorie kategorie){
		kategorieRepository.save(kategorie);
	}
	
	 public void updateName(Kategorie kategorie, String newName){
		kategorie.setName(newName);
		kategorieRepository.save(kategorie);	    	
	 }


	 public Kategorie getKategorieByName(String name){
		return kategorieRepository.findByName(name);
	}
	

}
