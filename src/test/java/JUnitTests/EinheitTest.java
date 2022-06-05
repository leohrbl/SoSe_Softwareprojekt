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
import com.example.application.data.entity.Einheit;
import com.example.application.data.repository.EinheitenRepository;
import com.example.application.data.service.EinheitService;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/**
 * JUnit-Test Klasse testet die Klassen Einheit, EinheitRepository und EinheitService
 * @author Anna Karle
 * @see com.example.application.data.entity.Einheit
 * @see com.example.application.data.repository.EinheitenRepository
 * @see com.example.application.data.service.EinheitService
 */

public class EinheitTest {
	
	@Autowired 
	private EinheitenRepository einheitenRepository;
    
    private EinheitService service ;
    
    /**In der Methode SetUp wird ein Einheitservice initialisiert.
     * 
     * @throws Exception
     */
	@Before
	public void setUp() throws Exception {

		service = new EinheitService(einheitenRepository);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * In der Methode testCreateEinheit() wird die Methode CreateEinheit getestet
	 */

	@Test
	public void testCreateEinheit() {
		String name = "G";
		service.createEinheit(name);
		assertNotNull("Die Einheit wurde erstellt", service.findByName(name));
		
		
	}
	/**Die Methode testLoeschenEinheit testet die Methode deleteEinheit
	 * 
	 */
	@Test
	public void testLoeschenEinheit() {
		String name = "Kg";
		service.createEinheit(name);
		service.deleteEinheit(service.findByName(name));
		assertNull("Die Einheit wurde gelöscht", service.findByName(name));
	}
	/** Methode testUpdateEinheit() testet die Methode updateEinheit()
	 * 
	 */
	@Test
	public void testUpdateEinheit() {
		String name = "Stück";
		service.createEinheit(name);
		Einheit einheit = service.findByName(name);
		String newEinheit = "ml";
		
		service.updateEinheit(newEinheit, einheit.getId());
		assertEquals("Die Einheit wurde geändert", newEinheit, service.findById(einheit.getId()).getEinheit());
	}
	
	/** In der Methode testListeEinheiten() werden neue Einheiten gespeichert und eine Liste mit allen Einheiten aus Datenbank 
	 *  zurückgegeben. Dadurch wird die Methode getEinheiten() getestet
	 * 
	 */
	@Test
	public void testListeEinheiten() {
		service.createEinheit("tl");
		service.createEinheit("El");
		service.createEinheit("mg");
		List <Einheit> einheiten = service.getEinheiten();
		assertNotNull("Liste mit Einheiten", einheiten);
	}
	
	
	

}
