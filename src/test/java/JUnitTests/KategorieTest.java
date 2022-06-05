package JUnitTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.application.Application;
import com.example.application.data.entity.Kategorie;
import com.example.application.data.repository.KategorieRepository;
import com.example.application.data.service.KategorieService;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/**
 * JUnitTest-Klasse, die Klassen Kategorie, KategorieRepository, KategorieService testet
 * @author Anna Karle
 * @see com.example.application.data.entity.Kategorie
 * @see com.example.application.data.repository.KategorieRepository
 * @see com.example.application.data.service.KategorieService
 *
 */
public class KategorieTest {
	
	@Autowired
	private KategorieRepository kategorieRepository;
	private KategorieService service;
	
	/** In der Methode setUp wird ein KategorieService erstellt
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		this.service = new KategorieService(kategorieRepository);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/** Methode testSaveKategorie() testet die Methode saveKategori aud der Klasse KategorieService
	 * 
	 */

	@Test
	public void testSaveKategorie() {
		String name = "Salate";
		service.saveKategorie(name);
		
		assertNotNull("Kategorie wurde gespeichert", service.getKategorieByName(name));
	}
	
	/** Methode testKategorieSaveUpdateDelete() testet die Methoden saveKategorie, getKategorieByName, updateKategorie 
	 *  und deleteKategorie aus der Klasse KategorieService
	 * 
	 */
	@Test
	public void testKategorieSaveUpdateDelete() {
		String name = "Pizza";
		service.saveKategorie(name);
		long id = service.getKategorieByName(name).getId();
		String newName = "Nudelngerichte";
		service.updateKategorie(newName, id);
		Kategorie kategorie = service.getKategorieByName(newName);
		assertNotNull("Der Name der gespeicherten Kategorie wurde geändert", kategorie);
		service.deleteKategorie(kategorie);
		assertNull("Die Kategorie wurde gelöscht.", service.getKategorieByName(newName));
		
	}
	
	/** Methode testKategorienliste testet die Methoden saveKategorie, updateKategorie und getKategorien() aus der Klasse KategorieService
	 *  Eine SequenceNr der letzt hinzugefügten Kategorie wird mit  Länge der Liste von gespeicherten in Datenbank Kategorien vergliechen und dadurch wird
	 *  die Methode getKategorien() getestet
	 */
	@Test
	public void testKategorienliste() {
		String name = "Pizza";
		service.saveKategorie(name);
		Kategorie kategorie = service.getKategorieByName(name);
		String newName = "Nudelngerichte";
		service.updateName(kategorie, newName);
		assertNotNull("Der Name der gespeicherten Kategorie wurde geändert", service.getKategorieByName(newName));
		
		List kategorien = service.getKategorien();
		assertNotNull ("Die Liste mit Kategorien ist nicht leer", kategorien);
		
		int soll = (int)service.getKategorieByName(newName).getSequenceNr() ;
		int ist = kategorien.size();
		assertEquals("Die Liste mit Kategorien wird komplett ausgegeben",soll, ist);
		
	}
	
	

}
