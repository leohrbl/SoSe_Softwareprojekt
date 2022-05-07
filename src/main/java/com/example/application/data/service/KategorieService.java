package com.example.application.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.data.entity.Kategorie;
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
			Kategorie kategorie = new Kategorie();
			kategorie.setName(name);
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
		kategorieRepository.findById(id).setName(newName);
		return "success";
	}
	
	

}
