package JUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
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
import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.entity.Zutat;
import com.example.application.data.repository.KategorieRepository;
import com.example.application.data.repository.RezeptRepository;
import com.example.application.data.repository.RezeptZutatenRepository;
import com.example.application.data.repository.ZutatRepository;
import com.example.application.data.service.KategorieService;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.data.service.ZutatService;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/** JUnit-Test-Klasse testet die Methoden aus den Klassen RezeptService,Rezept und RezeptRepository 
 * 
 * @author Anna Karle
 * @see com.example.application.data.service.RezeptService
 * @see com.example.application.data.entity.Rezept
 * @see com.example.application.data.repository.RezeptRepository
 *
 */
public class RezeptTest {
	
	@Autowired
	private RezeptRepository repository;
	private RezeptService service;
	@Autowired
	private KategorieRepository kategorieRepository;
	private KategorieService kategorieService;
	@Autowired
	private RezeptZutatenRepository rzRepository;
	private RezeptZutatenService rzService;
	@Autowired
	private ZutatRepository zutatRepository;
	private ZutatService zutatService;
    byte[] bild;
	
    /** In der Methode setUp() werden Instanzvariablen service, kategorieService, rzService und zutatService
     *  initialisiert
     * 
     * @throws Exception
     */
	

	@Before
	public void setUp() throws Exception {
		service = new RezeptService(repository);
		kategorieService = new KategorieService(kategorieRepository);
		rzService = new RezeptZutatenService(rzRepository, service);
		zutatService = new ZutatService(zutatRepository);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/** Methode testCreateRezept() erstellt ein Rezept mit allen Parameter, hinzufügt zum erstellten Rezept Zutaten,
	 * sucht ersteltes Rezept in Datenbank und löscht dieses. Dadurch werden die Methoden createRezept,findByTitel,
	 * updateRezept, searchRezeptByFilterText und deleteRezept getestet
	 * 
	 */

	@Test
	public void testCreateRezept() {
		kategorieService.saveKategorie("Pizza");
		Kategorie kategorie = kategorieService.getKategorieByName("Pizza");
		String titel = "Pizza Vier Käse";
		try {
			this.bild = RezeptService.getBytesFromFile("src/main/resources/META-INF/resources/images//SoSe_Softwareprojekt/src/main/resources/META-INF/resources/images/empty-plant.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.createRezept( bild, titel, "Zubereitung", 4, kategorie);
		Zutat zutat = zutatService.getZutatenByName("Käse");
		Zutat zutat2 = zutatService.getZutatenByName("Milch");
			
		Rezept rezept = service.findByTitel(titel);
		assertNotNull("Rezept wurde erstellt", rezept);
		rzService.createRezeptZutatenAndAddToSet(rezept, zutat, 4);
		rzService.createRezeptZutatenAndAddToSet(rezept, zutat2,200);
	
		Rezept rezeptAlt = service.findByTitel(titel);
		service.updateRezept(rezeptAlt, rezept);
		assertNotSame(rezeptAlt, service.findByTitel(titel) );
		
		List <Rezept> rezepte = service.searchRezepteByFilterText("Vier Käse");
		Rezept gefunden = rezepte.get(0);
		assertTrue( gefunden.toStringMitKategorie().equalsIgnoreCase(rezept.toStringMitKategorie()));
		
		service.deleteRezept(gefunden);
		assertNull("Das Rezept wurde gelöscht",service.findByTitel(titel));
	}

}
