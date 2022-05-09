package com.example.application.views.components;

import com.example.application.views.einstellungen.EinstellungenView;
import com.example.application.views.rezept.display.RezeptuebersichtView;
import com.example.application.views.zutat.ZutatenView;
import com.example.application.views.einkaufsliste.EinkaufslisteView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * Diese Klasse erzeugt eine waagerechte Header Komponente, welche in jeder View Ansicht vorhanden ist. Die Header Komponente kann mithilfe eines Toggle Button die Navigationsbar aktivieren und deaktivieren.
 * Zusätzlich erzeugt diese Klasse eine senkrechte "Navigationsbar", welche sich in der rechten Hälfte der Applikation befindet. Mit der Navigationsbar kann der User zwischen unterschiedlichen Views der Applikation navigieren.
 *
 * @author Léo Hérubel
 * @see RezeptuebersichtView
 */
public class MainLayout extends AppLayout {

    /**
     * Konstruktor erzeugt die beiden Hauptkomponenten der Klasse.
     */
    public MainLayout() {
        createHeader();
        createDrawer();
    }

    /**
     * Erzeugt senkrechte Navigationsbar
     */
    private void createDrawer() {
        RouterLink mainView = new RouterLink("Rezeptansicht", RezeptuebersichtView.class);
        mainView.addClassNames("mx-m", "text-m");
        RouterLink einkaufsliste = new RouterLink("Einkaufsliste", EinkaufslisteView.class);
        einkaufsliste.addClassNames("mx-m", "text-m");
        RouterLink zutaten = new RouterLink("Zutaten", ZutatenView.class);
        zutaten.addClassNames("mx-m", "text-m");
        RouterLink einstellungen = new RouterLink("Einstellungen", EinstellungenView.class);
        einstellungen.addClassNames("mx-m", "text-m");

        VerticalLayout layout = new VerticalLayout(mainView, einkaufsliste, zutaten, einstellungen);
        layout.addClassName("px-m");
        addToDrawer(layout);

    }

    /**
     * Erzeugt waagerechte Header Komponenten
     */
    private void createHeader() {
        H1 logo = new H1("Rezeptbuch");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);
    }
}
