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
import com.example.application.data.rezeptzutat.Rezept_Zutat;
import com.example.application.data.zutat.Zutat;
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
 * JUnitTest-Klasse, die Klassen RezeptZutat, RezeptZutatenRepository und
 * RezeptZutatenService testet
 * 
 * @author Anna Karle
 * @see com.example.application.data.rezeptzutat.RezeptZutatenService
 * @see com.example.application.data.entity.Rezept_Zutat
 * @see com.example.application.data.rezeptzutat.RezeptZutatenRepository
 */
public class RezeptZutatTest {

	@Autowired
	RezeptZutatenRepository repository;
	RezeptZutatenService service;
	@Autowired
	RezeptRepository rezeptRepository;
	RezeptService rezeptService;
	@Autowired
	ZutatRepository zutatRepository;
	ZutatService zutatService;

	/**
	 * In Methode setUp() werden die Variablen RezeptService rezeptService,
	 * RezeptZutatenService service
	 * und ZutatService zutatService initialisiert
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		this.rezeptService = new RezeptService(rezeptRepository);
		this.service = new RezeptZutatenService(repository, rezeptService);
		this.zutatService = new ZutatService(zutatRepository);

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Methode testCreateRezeptZutaten() testet die Methoden createRezeptZutaten und
	 * findAllByRezept
	 * aus der Klasse RezeptZutatenService. Ein neues Objekt Rezept_Zutat wird
	 * erstellt und in einer Liste
	 * zurückgegeben
	 * 
	 */

	@Test
	public void testCreateRezeptZutaten() {

		Rezept rezept = new Rezept();
		rezeptService.createRezept(rezept);
		Zutat zutat = zutatService.getZutatenByName("Milch");
		service.createRezeptZutaten(rezept, zutat, 200);
		List<Rezept_Zutat> listeRZ = service.findAllByRezept(rezept);

		assertNotNull("RezeptZutat Objekt wurde erfolgreich erstellt und zum Rezept hinzugefügt", listeRZ.get(0));

	}

	/**
	 * Methode testRezeptZutatFinden() testet die Methoden
	 * createRezeptZutatenAndAddToSet, findAllZutatenByRezept
	 * und deleteAllByRezept aus der Klasse RezeptZutatenService
	 * 
	 */
	@Test
	public void testRezeptZutatFinden() {
		Rezept rezept = new Rezept();
		rezeptService.createRezept(rezept);
		Zutat zutat = zutatService.getZutatenByName("Milch");
		service.createRezeptZutatenAndAddToSet(rezept, zutat, 300);
		List<Zutat> zutatenAusRezept = service.findAllZutatenByRezept(rezept);

		assertEquals("Zutat aus dem erstellten Objekt RezeptZutat wurde erfolgreich zum Rezept hinzugefügt",
				zutat.getId(), zutatenAusRezept.get(0).getId());

		Zutat zutat2 = zutatService.getZutatenByName("Käse");
		service.createRezeptZutatenAndAddToSet(rezept, zutat2, 3);
		zutatenAusRezept = service.findAllZutatenByRezept(rezept);
		assertEquals("Zweites Zutat aus dem erstellten Objekt RezeptZutat wurde erfolgreich zum Rezept hinzugefügt",
				zutat2.getId(), zutatenAusRezept.get(1).getId());

		service.deleteAllByRezept(rezept);

		assertTrue("Alle Zutaten wurden aus dem Rezept gelöscht", service.findAllZutatenByRezept(rezept).isEmpty());
	}

}
