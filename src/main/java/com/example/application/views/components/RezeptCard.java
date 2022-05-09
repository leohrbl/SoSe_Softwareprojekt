package com.example.application.views.components;

import com.example.application.views.rezept.display.RezeptView;
import com.example.application.views.rezept.display.RezeptuebersichtView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse RezeptCard ist eine Custom Vaadin Component. Sie ist für das Anzeigen der Daten eines Rezeptes in der RezeptansichtView verantwortlich. Zudem kann die Karte durch einen
 * eigenen ClickListener zu der erweiterten Ansicht des Rezeptes der Karte navigieren. Die Klasse wird in die RezeptansichtView angebunden.
 * @author Léo Hérubel
 * @see RezeptView
 * @see RezeptuebersichtView
 */
public class RezeptCard extends VerticalLayout {

    private long rezeptId;

    /**
     * Konstruktor weist die Daten des Rezeptes den Methoden zu. Zudem wird die Id des Rezeptes in der Komponente gespeichert und es wird die Komponente anhand der übergebenen Daten erzeugt.
     * @param titel
     * @param kategorie
     * @param rezeptId
     * @param image
     */
    public RezeptCard(String titel, String kategorie, long rezeptId, Image image) {
        this.rezeptId = rezeptId;
        createCard(createHeader(titel, kategorie), createImageLayout(image));
    }

    /**
     * Anhand des Titels und der Kategorie wird der Header der Karte erzeugt.
     * @param titel
     * @param kategorie
     * @return
     */
    private HorizontalLayout createHeader(String titel, String kategorie) {
        HorizontalLayout headingLayout = new HorizontalLayout(createTitelLayout(titel), createKategorieLayout(kategorie));
        headingLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        headingLayout.addClassName("card-heading");
        headingLayout.setPadding(false);
        headingLayout.setHeight("30%");
        headingLayout.setWidthFull();
        return headingLayout;
    }

    /**
     * Anhand eines übergebenen Bildes erzeugt die Methode Layout, welches das Bild anzeigt und zu dem Header angrenzt.
     * @param image
     * @return
     */
    private HorizontalLayout createImageLayout(Image image) {
        HorizontalLayout imageLayout = new HorizontalLayout(createImage(image));
        imageLayout.setHeight("70%");
        imageLayout.setPadding(false);
        imageLayout.setMargin(false);
        imageLayout.setWidth("100%");
        return imageLayout;
    }

    /**
     * Die Methode erzeugt anhand des Titels ein Layout, welches in der Header-Funktion hinzugefügt wird.
     * @param titel
     * @return gibt das Layout zum Hinzufügen in die Header-Funktion zurück
     */
    private HorizontalLayout createTitelLayout(String titel) {
        String trimTitel = trimTitel(titel);
        HorizontalLayout titelLayout = new HorizontalLayout(createTitel(trimTitel));
        titelLayout.setWidth("70%");
        titelLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return titelLayout;
    }

    /**
     * Da die Schrift im Header nicht responsive ist, muss der eingegebene Titel (Sofern > 26 Zeichen) gekürzt werden.
     * @param titel
     * @return gibt den ggf. gekürzten Titel zurück.
     */
    private String trimTitel(String titel) {
        if (titel.length() >= 26) {
            return titel.substring(0, 25);
        }
        return titel;
    }

    /**
     * Die Methode erzeugt anhand der Kategorie ein Layout, welches in der Header-Funktion hinzugefügt wird.
     * @param kategorie
     * @return gibt das Layout zum Hinzufügen in die Header-Funktion zurück.
     */
    private HorizontalLayout createKategorieLayout(String kategorie) {
        HorizontalLayout kategorieLayout = new HorizontalLayout(createKategorie(new Span(kategorie)));
        kategorieLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kategorieLayout.setWidth("30%");
        return kategorieLayout;
    }

    /**
     * Funktion zum Erstellen und stylen eines H1 Elementes, welches in der Funktion createKategorieLayout hinzugefügt wird.
     * @param kategorie
     * @return gibt einen H1 zum Hinzufügen in ein Layout zurück.
     */
    private H1 createKategorie(Span kategorie) {
        H1 kategorieH1 = new H1(kategorie);
        kategorieH1.addClassName("card-category");
        kategorieH1.addClassNames("card-text", "card-category");
        return kategorieH1;
    }

    public long getRezeptId() {
        return this.rezeptId;
    }

    /**
     * Funktion definiert Layout regeln, damit das Bild in die Parent Komponente, also das ImageLayout, passt.
     * @param image
     * @return das Image wird an die Funktion createImageLayout zurückgegeben.
     */
    private Image createImage(Image image) {
        Image cardImage = image;
        cardImage.setWidth("100%");
        cardImage.setHeight("100%");
        cardImage.addClassName("image");
        return cardImage;
    }

    /**
     * Funktion zum Erzeugen der Karte anhand des Headers und des ImageLayouts
     * @param header
     * @param imageLayout
     */
    private void createCard(HorizontalLayout header, HorizontalLayout imageLayout) {
        customizeCard();
        addClickListener();
        this.add(header, imageLayout);
    }

    /**
     * Funktion zur Definition von Layout Regelungen der Karte.
     */
    private void customizeCard() {
        this.setWidth("400px");
        this.setHeight("300px");
        this.addClassName("card-border");
        this.setId("card");
        this.setSpacing(false);
    }

    /**
     * Funktion zum Erstellen und stylen eines H1 Elementes, welches in der Funktion createTitelLayout hinzugefügt wird.
     * @param titel
     * @return gibt einen H1 zum Hinzufügen in ein Layout zurück.
     */
    private H1 createTitel(String titel) {
        H1 titelH1 = new H1(titel);
        titelH1.addClassName("card-text");
        return titelH1;
    }

    /**
     * Funktion zum Hinzufügen des Click Listeners der Karte. Der ClickListener navigiert anschließend mit dem URl-Parameter, welcher die Instanzvariable rezeptId ist, zur RezeptView.
     */
    private void addClickListener() {
        this.addClickListener(event -> {
            UI.getCurrent().navigate("display/" + rezeptId);
        });
    }
}
