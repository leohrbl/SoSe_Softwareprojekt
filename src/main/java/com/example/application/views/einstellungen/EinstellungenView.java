package com.example.application.views.einstellungen;

import com.example.application.data.einheit.EinheitService;
import com.example.application.data.kategorie.KategorieService;
import com.example.application.views.components.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.*;

/**
 * Die Klasse erzeugt die Einstellung-View. Hierbei können die Einheiten und die
 * Kategorien verwaltet werden.
 * Zum Verwalten erhalten die "Einheiten" und "Kategorien" eigenständige Tabs
 * deren Inhalt in einem Container
 * dargestellt werden.
 * Zudem erhält jeder Tab einen * Hinzufügen- und Löschen-Button mit dem der
 * Nutzer die Entitäten bearbeiten kann.
 *
 * @author: Edwin Polle
 */

@PageTitle("Rezeptbuch")
@Route(value = "/einstellungen", layout = MainLayout.class)
public class EinstellungenView extends VerticalLayout {
    private final EinheitService einheitService;
    private final KategorieService kategorieService;
    private final EinheitView einheitV;
    private final KategorieView kategorieV;
    private final Div contentContainer;
    private final Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();
    private final Tabs tabs;

    /**
     * Konstruktor der EinstellungenView. Hier wird die Konfiguration der
     * EinstellungenView festgelegt. Dabei werden die Service-Klassen
     * der Einheiten und Kategorien initialisiert. Zudem werden die View-Klassen
     * initialisiert und dem Layout hinzugefügt.
     *
     * @param einheitService   der Einheit-Service wird übergeben und initialisiert,
     *                         um mit dem Backend zu kommunizieren.
     * @param kategorieService der Kategorie-Service wird übergeben und
     *                         initialisiert, um mit dem Backend zu kommunizieren.
     */

    public EinstellungenView(EinheitService einheitService, KategorieService kategorieService) {
        this.einheitService = einheitService;
        this.kategorieService = kategorieService;
        this.einheitV = new EinheitView();
        this.kategorieV = new KategorieView();
        this.contentContainer = new Div();
        this.tabs = new Tabs();
        contentContainer.setWidth("50%");
        createTab();
        createView();
        add(tabs, contentContainer);
    }

    /**
     * Die Methode createTab() konfiguriert die auswählbaren Tabs, und der
     * clickListener zum Anzeigen der Views wird hinzugefügt.
     */
    private void createTab() {
        tabComponentMap.put(new Tab("Kategorien"), kategorieV.createView(kategorieService));
        tabComponentMap.put(new Tab("Einheiten"), einheitV.einheitView(einheitService));
        tabs.add(tabComponentMap.keySet().toArray(new Tab[] {}));
        tabs.setSizeFull();
        tabs.addSelectedChangeListener(e -> {
            contentContainer.removeAll();
            contentContainer.add(tabComponentMap.get(e.getSelectedTab()));
        });
    }

    /**
     * Die Methode createView wird benötigt um, beim ersten Aufruf der
     * EinstellungenView den ContentContainer zu füllen.
     */
    private void createView() {
        contentContainer.add(tabComponentMap.get(tabs.getSelectedTab()));
    }
}