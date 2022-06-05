package JUnitTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.application.Application;
import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.EinkaufslistenEintrag;
import com.example.application.data.entity.Zutat;
import com.example.application.data.repository.EinheitenRepository;
import com.example.application.data.repository.EinkaufslistenRepository;
import com.example.application.data.repository.ZutatRepository;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.EinkaufslistenService;
import com.example.application.data.service.ZutatService;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
/** JUnit-Test-Klasse testet die Klassen EinkaufslistenEintrag, EinkaufslistenRepository und 
 *  EinkaufslistenService
 *
 *  @author Anna Karle
 *  @see com.example.application.data.service.EinkaufslistenService
 *  @see com.example.application.data.entity.EinkaufslistenEintrag
 *  @see com.example.application.data.repository.EinkaufslistenRepository
 *
 */
public class EinkaufslistenTest {
	@Autowired
	private EinkaufslistenRepository repository;
	private EinkaufslistenService service;
	@Autowired
	private ZutatRepository zutatRepository;
	private ZutatService zutatService;
	@Autowired
	private EinheitenRepository einheitRepository;
	private EinheitService einheitService;
	private Einheit einheit;
	
	/** In der Methode SetUp () werden die Variablen service, zutatService, einheitService und einheit
	 *  initialisiert
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		this.service = new EinkaufslistenService(repository);
		this.zutatService = new ZutatService(zutatRepository);
		this.einheitService = new EinheitService(einheitRepository);
		einheitService.createEinheit("tl");
		einheit = einheitService.findByName("tl");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/** Methode testEinkaufslistenEintrag() testet die Methode saveEintrag, updateEintrag, findByEintragsID und 
	 *  deleteEintrag aus der Klasse EinkaufslistenService
	 * 
	 */

	@Test
	public void testEinkaufslistenEintrag() {
		
		String zutatName = "Backpulver";
		zutatService.saveZutat(zutatName, einheit);
		Zutat zutat = zutatService.getZutatenByName(zutatName);
		String save = service.saveEintrag(1, zutat);
		assertEquals("success", save);
		
		EinkaufslistenEintrag eintrag = new EinkaufslistenEintrag();
		eintrag.setMenge(2);
		eintrag.setZutat(zutat);
		String update = service.updateEintrag(eintrag);
		assertEquals("success", update );
		
		long id = eintrag.getId();
		EinkaufslistenEintrag found = service.findByEintragsID(id);
		assertTrue(eintrag.getZutat().getName().equals(found.getZutat().getName()));
		
		service.deleteEintrag(found);
		assertNull(service.findByEintragsID(id));
		
	}
	
	

}
