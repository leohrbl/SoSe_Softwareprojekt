package JUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.application.Application;
import com.example.application.data.einheit.Einheit;
import com.example.application.data.einheit.EinheitService;
import com.example.application.data.einheit.EinheitenRepository;
import com.example.application.data.zutat.Zutat;
import com.example.application.data.zutat.ZutatRepository;
import com.example.application.data.zutat.ZutatService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/**
 * JUnit-Test Klasse, testet die Klassen Zutat, ZutatRepository, ZutatService
 * 
 * @author Anna Karle
 * @see com.example.application.data.zutat.Zutat
 * @see com.example.application.data.zutat.ZutatRepository
 * @see com.example.application.data.zutat.ZutatService
 */
public class ZutatTest {

	@Autowired
	private ZutatRepository zutatRepository;

	private ZutatService service;
	private Einheit einheit;
	@Autowired
	private EinheitenRepository eRep;
	private EinheitService eServ;

	/**
	 * In der Methode setUp() werden ZutatService und EinheitService erstellt und
	 * eine neue Einheit gespeichert
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		this.service = new ZutatService(zutatRepository);
		this.eServ = new EinheitService(eRep);
		String nameEinheit = "mg";
		eServ.createEinheit(nameEinheit);
		this.einheit = eServ.findByName(nameEinheit);

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Methode testSaveZutat() testet die Methode saveZutat
	 * 
	 */

	@Test
	public void testSaveZutat() {

		String name = "Soda";
		service.saveZutat(name, einheit);

		assertNotNull("Das Zutat wurde erstellt", service.getZutatenByName(name));
	}

	/**
	 * Methode testZutatLoeschen() werden die Methoden getZutatenByName und
	 * deleteZutat aus der Klasse ZutatService getestet
	 * 
	 */
	@Test
	public void testZutatLoeschen() {

		String name = "Hefe";

		service.saveZutat(name, einheit);
		Zutat zutat = service.getZutatenByName(name);

		service.deleteZutat(zutat);
		assertNull("Zutat wurde gelöscht", service.getZutatenByName(name));
	}

	/**
	 * Methode testZutatUpdate() testet die Methode updateZutat aus der Klasse
	 * ZutatService
	 * 
	 */
	@Test
	public void testZutatUpdate() {

		String name = "Backpulver";
		service.saveZutat(name, einheit);
		Zutat zutat = service.getZutatenByName(name);
		zutat.setEinheit(new Einheit("mg"));
		service.updateZutat(zutat);
	}

	/**
	 * Methode testZutatenSuche() speichert neue Zutaten in der Datenbank, findet
	 * diese nach Suchwort
	 * und gibt in einer Liste zurück. Dadurch wird die Methode
	 * searchZutatenByFilterText getestet
	 */
	@Test
	public void testZutatenSuche() {

		String name1 = "Salz";
		String name2 = "Quark";
		String name3 = "Honig";
		service.saveZutat(name1, einheit);
		service.saveZutat(name2, einheit);
		service.saveZutat(name3, einheit);
		String searchword = "Salz";
		List<Zutat> zutaten = service.searchZutatenByFilterText(searchword);
		assertEquals("Ein Zutat Salz wird nach Suchwort gefunden", 1, zutaten.size());

	}

}
