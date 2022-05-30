package com.example.application.data.generator;

import java.util.LinkedList;
import java.util.List;

import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.KategorieService;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    /**
     * @param service
     * @return Die Methode erzeugt zwei Einheiten: ML und Wasser
     *         Diese werden mit dem Service in der Datenbank gespeichert
     * @author Philipp Laupichler
     */
    @Bean
    public CommandLineRunner createService(EinheitService service) {
        return args -> {
            service.createEinheit("ML");
            service.createEinheit("KG");
            service.createEinheit("L");
            service.createEinheit("G");
            service.createEinheit("Flasche");
            service.createEinheit("Stück");
            service.createEinheit("Scheibe");
            service.createEinheit("Prise");
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Einheiten wurden durch Service Klasse erzeugt");
        };
    }

    /**
     * @author Edwin Polle
     * @param kategorieService
     * @return
     */

    @Bean
    public CommandLineRunner createKategorie(KategorieService kategorieService) {
        return args -> {
            kategorieService.saveKategorie("Abendessen");
            kategorieService.saveKategorie("Nachtisch");
            kategorieService.saveKategorie("Grillen");
            kategorieService.saveKategorie("Frühstück");
            kategorieService.saveKategorie("Snack");
            kategorieService.saveKategorie("Brot");
            kategorieService.saveKategorie("Dessert");
            kategorieService.saveKategorie("Backen");
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info("Kategorien wurden durch Service Klasse erzeugt");
        };
    }

    /**
     * Die Methode erzeugt Zutatenobjekte, ordnet diese Einheiten zu, welche in der
     * vorherigen Methode erstellt wurden, und testet die Methoden der Service
     * Klasse der Entität Zutat.
     *
     * @param zutatService
     * @param einheitService
     * @return
     * @author Léo Hérubel
     * @author Léo Hérubel
     * @see ZutatService
     * @see com.example.application.data.repository.ZutatRepository
     */
    @Bean
    public CommandLineRunner loadZutaten(ZutatService zutatService, EinheitService einheitService) {
        return args -> {
            String name1 = "Milch";
            String name2 = "Wurst";
            String name3 = "Käse";
            String name4 = "Salat";
            String name5 = "Hackfleisch";
            String name6 = "Nudeln";
            String name7 = "Brot";
            String name8 = "Reis";
            String name9 = "Tomate";
            String name10 = "Joghurt";
            String name11 = "Butter";
            String name12 = "Korn";

            String quark = "Magerquark";
            String schmand = "Schmand";
            String limmettensafe = "Limettensaft";
            String zwiebel = "Zwiebel";
            String kräuter = "Kräuter";
            String sahne = "Sahne";
            String wasser = "Wasser";
            String schinken = "Schinken";
            String nudeln = "Nudeln";

            List<Einheit> einheitList = new LinkedList<Einheit>(einheitService.getEinheiten());
            String response1 = zutatService.saveZutat(name1, einheitList.get(0));
            String response2 = zutatService.saveZutat(name2, einheitList.get(1));
            String response3 = zutatService.saveZutat(name3, einheitList.get(6));
            zutatService.saveZutat(name4, einheitList.get(1));
            zutatService.saveZutat(name5, einheitList.get(3));
            zutatService.saveZutat(name6, einheitList.get(3));
            zutatService.saveZutat(name7, einheitList.get(6));
            zutatService.saveZutat(name8, einheitList.get(3));
            zutatService.saveZutat(name9, einheitList.get(5));
            zutatService.saveZutat(name10, einheitList.get(0));
            zutatService.saveZutat(name11, einheitList.get(3));
            zutatService.saveZutat(name11, einheitList.get(4));

            zutatService.saveZutat(quark, einheitService.findByName("G"));
            zutatService.saveZutat(schmand, einheitService.findByName("G"));
            zutatService.saveZutat(limmettensafe, einheitService.findByName("ML"));
            zutatService.saveZutat(zwiebel, einheitService.findByName("G"));
            zutatService.saveZutat(kräuter, einheitService.findByName("G"));
            zutatService.saveZutat(sahne, einheitService.findByName("ML"));
            zutatService.saveZutat(wasser, einheitService.findByName("ML"));
            zutatService.saveZutat(schinken, einheitService.findByName("G"));
            zutatService.saveZutat(nudeln, einheitService.findByName("G"));

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
            RezeptZutatenService rezeptZutatenService, KategorieService kategorieService) {
        return args -> {

            Rezept rezept = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/1367011241524338/bilder/1027677/crop-600x400/der-weltbeste-kraeuterquark.jpg",
                    "Essen"), "Der Weltbeste Kräuterquark",
                    "Joghurt, Quark und Schmand gut miteinander verrühren. Die Limette auf der Arbeitsfläche hin und her rollen damit sie mehr Saft gibt, auspressen und den Saft zum Quark geben. Zwiebel, Knoblauch und die Kräuter dazu geben und alles gut durchrühren. Mit Salz, Pfeffer und Paprika oder Chili abschmecken.\nDen Quark für mindestens 1 Stunde zum Durchziehen in den Kühlschrank stellen. Vor dem Servieren nochmal verrühren und abschmecken.\nDen Quark gibt es bei uns traditionell zu Pellkartoffeln, er schmeckt aber auch lecker auf Brot, passt zum Grillen, zum Dippen von Rohkost und als abrundender Klecks auf warme Speisen wie Ratatouille.",
                    2);
            Logger logger = LoggerFactory.getLogger(getClass());
            logger.info(rezept.toString());
            rezept.setKategorie(kategorieService.getKategorien().get(4));
            service.createRezept(rezept);
            rezeptZutatenService.createRezeptZutaten(rezept, zutatService.getZutatenByName("Magerquark"), 1000);
            rezeptZutatenService.createRezeptZutaten(rezept, zutatService.getZutatenByName("Schmand"), 100);
            rezeptZutatenService.createRezeptZutaten(rezept, zutatService.getZutatenByName("Limettensaft"), 50);
            rezeptZutatenService.createRezeptZutaten(rezept, zutatService.getZutatenByName("Zwiebel"), 100);
            rezeptZutatenService.createRezeptZutaten(rezept, zutatService.getZutatenByName("Kräuter"), 10);
            Rezept rezept2 = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/3208091477471041/bilder/1278063/crop-600x400/optimierter-nudel-schinken-auflauf.jpg",
                    "Essen"), "Nudeln-Schinken-Auflauf",
                    "Den Ofen auf 230 Grad Ober-Unterhitze vorheizen. Das Tomatenmark mit etwas Sahne in der Auflaufform verrühren. Die restliche Sahne hinzufügen und großzügig würzen. Die zerkleinerten Zwiebelstückchen, die rohen Nudeln und den Schinken hineingeben. Das Wasser hinzufügen, so dass die Nudeln gerade so bedeckt sind, und alles gut vermengen. Den Käse darüberstreuen. Den Auflauf in den Ofen schieben und ca. 35 Minuten backen lassen.\n\nAnmerkung: Es gibt viele Rezepte für einen Nudel-Schinken-Auflauf. Da mir diese aber alle zu kompliziert sind, hier mein optimierter Auflauf. Das besondere ist der gute Geschmack bei sehr einfacher und schneller Zubereitung. Am Ende muss lediglich eine Auflaufform abgewaschen werden, die Zutaten kann man leicht passend und auf Vorrat kaufen und man braucht keine Gemüsebrühe mit vielen Zusatzstoffen. Von der Arbeit nach Hause kommen, schnell den Ofen anschmeißen, 10 Minuten Arbeit, Timer stellen und kurze Zeit später das fertige Essen genießen. ",
                    2);
            rezept2.setKategorie(kategorieService.getKategorien().get(4));
            service.createRezept(rezept2);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Zwiebel"), 100);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Kräuter"), 10);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Sahne"), 200);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Wasser"), 400);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Nudeln"), 250);
            rezeptZutatenService.createRezeptZutaten(rezept2, zutatService.getZutatenByName("Schinken"), 125);

            Rezept rezept3 = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/3104701463388573/bilder/916289/crop-600x400/super-einfaches-grillbrot-mit-joghurt.jpg",
                    "Essen"), "Super einfaches Grillbrot mit Joghurt", "zubereitung", 2);
            rezept3.setKategorie(kategorieService.getKategorien().get(3));
            service.createRezept(rezept3);
            for (Zutat string : zutatService.getZutaten()) {
                rezeptZutatenService.createRezeptZutaten(rezept3, string, 2);
            }
            Rezept rezept4 = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/1406731245497012/bilder/745155/crop-600x400/spaghetti-carbonara.jpg",
                    "Essen"), "Spaghetti Carbonara",
                    "Die Spaghetti wie gewohnt im Salzwasser kochen.\nDen Schinken oder Speck klein schneiden, in wenig Margarine anbraten, Knoblauch dazu. Schlagsahne oder Cremefine einrühren und die einzelnen Schmelzkäseecken langsam hineinschmelzen lassen.\nAlles auf kleiner Flamme, bei öfterem Umrühren einmal kurz aufköcheln lassen. Sollte die Soße zu dünnflüssig sein, mit etwas Saucenbinder binden.\n Wenn man möchte, kann man noch Petersilie dazugeben. Mit den Nudeln mischen und servieren. ",
                    2);
            rezept4.setKategorie(kategorieService.getKategorien().get(2));
            service.createRezept(rezept4);
            for (Zutat string : zutatService.getZutaten()) {
                rezeptZutatenService.createRezeptZutaten(rezept4, string, 2);
            }
            Rezept rezept5 = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/1060321211349230/bilder/261021/crop-600x400/torte-der-faulenzer.jpg",
                    "Essen"), "Torte 'Der Faulenzer'",
                    "Die Kekse müssen zuerst zerkleinert werden.\n einer Schüssel mischt man dann die zerkleinerten Kekse mit Milchmädchen und den Nüssen. Danach stellt man mit dieser Masse auf einem Teller eine beliebige Form her, bestreut diese mit Puderzucker und stellt sie bis zum Verzehr im Kühlschrank kühl.\nTipp: Man kann auch die Torte z. B. mit Schokoladenglasur beziehen.\nDie Torte ist sehr lecker und lässt sich einfach vorbereiten.",
                    2);
            rezept5.setKategorie(kategorieService.getKategorien().get(4));
            service.createRezept(rezept5);
            for (Zutat string : zutatService.getZutaten()) {
                rezeptZutatenService.createRezeptZutaten(rezept5, string, 2);
            }
            Rezept rezept6 = new Rezept(new Image(
                    "https://img.chefkoch-cdn.de/rezepte/3807881579963868/bilder/1271758/crop-600x400/superschnelle-nutella-cookies.jpg",
                    "Essen"), "Superschnelle Nutella-Cookies",
                    "Den Backofen auf 140 °C Ober-/Unterhitze vorheizen und ein Blech mit Backpapier auslegen.\nAlle Zutaten, bis auf die Smarties, in einer Schüssel vermengen und zu einem gleichmäßigen Teig kneten. Aus dem Teig 25 gleich große Bällchen formen und diese platt drücken. Die Smarties in die geformten Kekse drücken.\nDie Cookies 20 Minuten backen.\nNach dem Abkühlen zum Kaffee oder einem kalten Glas Milch genießen. ",
                    2);
            rezept6.setKategorie(kategorieService.getKategorien().get(4));
            service.createRezept(rezept6);
            for (Zutat string : zutatService.getZutaten()) {
                rezeptZutatenService.createRezeptZutaten(rezept6, string, 2);
            }

            logger.info("Einheiten wurden durch Service Klasse erzeugt");
            logger.info(service.getAllRezepte().toString());
            logger.info(rezeptZutatenService.findAllByRezept(rezept).toString());
            for (Rezept_Zutat rezept_Zutat : rezeptZutatenService.findAllByRezept(rezept)) {
                rezept.addZutat(rezept_Zutat);
            }
            logger.info(rezeptZutatenService.findAllZutatenByRezept(rezept).toString());
            logger.info(rezeptZutatenService.findAllRezepteByZutat(zutatService.getZutaten().get(1)).toString());
            System.out.println(service.findAllbyKategorie());
            // logger.info(service.findByTitel("Test").toString());
            // logger.info("Test");
            // logger.info(service.findByTitel("Test").getZutatenFromZutat().toString());
        };
    }

    /**
     * @author Joscha Cerny
     * @param service
     * @param zutatService
     *                     Sucht alle Zutaten aus der Liste Der Zutaten und erstellt
     *                     für jede einen Eintrag in einer neu erstellten Tabelle
     *                     einkaufslisten_eintrag
     *
     */
    /*
     * @Bean
     * public CommandLineRunner einkaufslistenEintrag(EinkaufslistenService service,
     * ZutatService zutatService, EinheitService einheitService) {
     * return args -> {
     * Zutat newZutat = zutatService.getZutaten().get(0);
     * service.saveEintrag(5, newZutat);
     * service.saveEintrag(6, newZutat);
     * service.saveEintrag(7, newZutat);
     * service.deleteEintragByID(6);
     * 
     * 
     * };
     * }
     */
    /**
     * @Bean
     *       public CommandLineRunner loadKategorien(KategorieService service) {
     *       return args -> {
     *       service.saveKategorie("Vorspeisen");
     *       service.saveKategorie("Nachspeisen");
     *       service.saveKategorie("Getränke");
     * 
     *       };
     *       }
     */
}