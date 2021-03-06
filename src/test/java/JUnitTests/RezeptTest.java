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
import com.example.application.data.zutat.Zutat;
import com.example.application.data.kategorie.Kategorie;
import com.example.application.data.kategorie.KategorieRepository;
import com.example.application.data.kategorie.KategorieService;
import com.example.application.data.rezept.Rezept;
import com.example.application.data.rezept.RezeptRepository;
import com.example.application.data.rezept.RezeptService;
import com.example.application.data.rezeptzutat.RezeptZutatenRepository;
import com.example.application.data.rezeptzutat.RezeptZutatenService;
import com.example.application.data.zutat.ZutatRepository;
import com.example.application.data.zutat.ZutatService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/**
 * JUnit-Test-Klasse testet die Methoden aus den Klassen RezeptService,Rezept
 * und RezeptRepository
 * 
 * @author Anna Karle
 * @see com.example.application.data.rezept.RezeptService
 * @see com.example.application.data.rezept.Rezept
 * @see com.example.application.data.rezept.RezeptRepository
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

	/**
	 * In der Methode setUp() werden Instanzvariablen service, kategorieService,
	 * rzService und zutatService
	 * initialisiert
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

	/**
	 * Methode testCreateRezept() erstellt ein Rezept mit allen Parameter, hinzuf??gt
	 * zum erstellten Rezept Zutaten,
	 * sucht ersteltes Rezept in Datenbank und l??scht dieses. Dadurch werden die
	 * Methoden createRezept,findByTitel,
	 * updateRezept, searchRezeptByFilterText und deleteRezept getestet
	 * 
	 */

	@Test
	public void testCreateRezept() {
		kategorieService.saveKategorie("Pizza");
		Kategorie kategorie = kategorieService.getKategorieByName("Pizza");
		String titel = "Pizza Vier K??se";
		try {
			this.bild = RezeptService.getBytesFromFile(
					"src/main/resources/META-INF/resources/images//SoSe_Softwareprojekt/src/main/resources/META-INF/resources/images/empty-plant.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.createRezept(bild, titel, "Zubereitung", 4, kategorie);
		Zutat zutat = zutatService.getZutatenByName("K??se");
		Zutat zutat2 = zutatService.getZutatenByName("Milch");

		Rezept rezept = service.findByTitel(titel);
		assertNotNull("Rezept wurde erstellt", rezept);
		rzService.createRezeptZutatenAndAddToSet(rezept, zutat, 4);
		rzService.createRezeptZutatenAndAddToSet(rezept, zutat2, 200);

		Rezept rezeptAlt = service.findByTitel(titel);
		service.updateRezept(rezeptAlt, rezept);
		assertNotSame(rezeptAlt, service.findByTitel(titel));

		List<Rezept> rezepte = service.searchRezepteByFilterText("Vier K??se");
		Rezept gefunden = rezepte.get(0);
		assertTrue(gefunden.toStringMitKategorie().equalsIgnoreCase(rezept.toStringMitKategorie()));

		service.deleteRezept(gefunden);
		assertNull("Das Rezept wurde gel??scht", service.findByTitel(titel));
	}

}
