package com.example.application.data.generator;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.data.repository.CompanyRepository;
import com.example.application.data.repository.ContactRepository;
import com.example.application.data.repository.StatusRepository;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
            StatusRepository statusRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");
            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class,
                    LocalDateTime.now());
            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
            List<Company> companies = companyRepository.saveAll(companyGenerator.create(5, seed));

            List<Status> statuses = statusRepository
                    .saveAll(Stream.of("Imported lead", "Not contacted", "Contacted", "Customer", "Closed (lost)")
                            .map(Status::new).collect(Collectors.toList()));

            logger.info("... generating 50 Contact entities...");
            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
                    LocalDateTime.now());
            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
            contactGenerator.setData(Contact::setEmail, DataType.EMAIL);

            Random r = new Random(seed);
            List<Contact> contacts = contactGenerator.create(50, seed).stream().map(contact -> {
                contact.setCompany(companies.get(r.nextInt(companies.size())));
                contact.setStatus(statuses.get(r.nextInt(statuses.size())));
                return contact;
            }).collect(Collectors.toList());

            contactRepository.saveAll(contacts);

            logger.info("Generated demo data");
        };
    }

    /**
     * @author Philipp Laupichler
     * @param service
     * @return
     *         Die Methode erzeugt zwei Einheiten: ML und Wasser
     *         Diese werden mit dem Service in der Datenbank gespeichert
     */
    @Bean
    public CommandLineRunner createService(EinheitService service) {
        return args -> {
            service.createEinheit("ML");
            service.createEinheit("KG");
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Einheiten wurden durch Service Klasse erzeugt");
            logger.info(service.findById(1l).getEinheit());
            service.updateEinheit("L", 1l);
            logger.info(service.findById(1l).getEinheit());
        };
    }

    /**
     * Die Methode erzeugt Zutatenobjekte, ordnet diese Einheiten zu, welche in der
     * vorherigen Methode erstellt wurden, und testet die Methoden der Service
     * Klasse der Entität Zutat.
     * 
     * @author Léo Hérubel
     * @author Léo Hérubel
     * @param zutatService
     * @param einheitService
     * @see ZutatService
     * @see com.example.application.data.repository.ZutatRepository
     * @return
     */
    @Bean
    public CommandLineRunner loadZutaten(ZutatService zutatService, EinheitService einheitService) {
        return args -> {
            String name1 = "Milch";
            String name2 = "Wurst";
            String name3 = "Käse";
            List<Einheit> einheitList = new LinkedList<Einheit>(einheitService.getEinheiten());
            String response1 = zutatService.saveZutat(name1, einheitList.get(0));
            String response2 = zutatService.saveZutat(name2, einheitList.get(1));
            String response3 = zutatService.saveZutat(name3, einheitList.get(1));
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info(String.valueOf(einheitList.size()));
            logger.info(response1);
            logger.info(response2);
            List<Zutat> zutatList = new LinkedList<Zutat>(zutatService.getZutaten());
            logger.info(String.valueOf(zutatList.size()));
            logger.info(zutatService.deleteZutat(zutatList.get(1)));
            List<Zutat> newZutatList = new LinkedList<Zutat>(zutatService.findZutatenByEinheitId((long) 2));
            logger.info(String.valueOf(newZutatList.size()));
            logger.info(newZutatList.get(0).getName());
        };
    }

    /**
     * Nur zum Testen!!!
     * Erklärung folgt später
     * 
     * @param service
     * @param zutatService
     * @param rezeptZutatenService
     * @return
     */

    @Bean
    public CommandLineRunner loadRezept(RezeptService service, ZutatService zutatService,
            RezeptZutatenService rezeptZutatenService) {
        return args -> {

            Rezept rezept = new Rezept(new Image(
                    "https://media.gq-magazin.de/photos/5cf5323293d17014eb72ae59/1:1/w_1999,h_1999,c_limit/gesundheit-zuviel-essen.jpg",
                    "Essen"), "Test", "zubereitung", 2);
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info(rezept.toString());
            service.createRezept(rezept);
            for (Zutat string : zutatService.getZutaten()) {
                rezeptZutatenService.createRezeptZutaten(rezept, string, 2);
            }



            logger.info("Einheiten wurden durch Service Klasse erzeugt");
            logger.info(service.getAllRezepte().toString());
            logger.info(rezeptZutatenService.findAllByRezept(rezept).toString());
            for (Rezept_Zutat rezept_Zutat : rezeptZutatenService.findAllByRezept(rezept)) {
                rezept.addZutat(rezept_Zutat);
            }
            logger.info(rezeptZutatenService.findAllZutatenByRezept(rezept).toString());
            logger.info(rezeptZutatenService.findAllRezepteByZutat(zutatService.getZutaten().get(1)).toString());

            logger.info(service.findByTitel("Test").toString());
            logger.info("Test");
            logger.info(service.findByTitel("Test").getZutatenFromZutat().toString());
        };
    }

    /**
     * @author Joscha Cerny
     * @param service
     * @param zutatService
     * Sucht alle Zutaten aus der Liste Der Zutaten und erstellt für jede einen Eintrag in einer neu erstellten Tabelle einkaufslisten_eintrag
     *
     */
    @Bean
    public CommandLineRunner einkaufslistenEintrag(EinkaufslistenService service, ZutatService zutatService, EinheitService einheitService) {
        return args -> {
            Zutat newZutat = zutatService.getZutaten().get(0);
            service.saveEintrag(5, newZutat);
            service.saveEintrag(6, newZutat);
            service.saveEintrag(7, newZutat);
            service.deleteEintragByID(6);

        };

    }
}
