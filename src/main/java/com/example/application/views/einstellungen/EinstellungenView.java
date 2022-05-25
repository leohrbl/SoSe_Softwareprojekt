package com.example.application.views.einstellungen;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.KategorieService;
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
 * Die Klasse erzeugt die Einstellung-View. Hierbei können die Einheiten und die Kategorien verwaltet werden.
 * Zum Verwalten erhalten die "Einheiten" und "Kategorien" eigenständige Tabs. Zudem erhält jeder Tab einen
 * Hinzufügen- und Löschen-Button mit dem der Nutzer die Entitäten bearbeiten kann.
 * @author: Edwin Polle
 */

@PageTitle("Rezeptbuch")
@Route(value = "/einstellungen", layout = MainLayout.class)
public class EinstellungenView extends VerticalLayout{

    private final EinheitService einheitService;
    private final KategorieService kategorieService;
    private final EinheitView einheitV;
    private final KategorieView kategorieV;

    private final Div contentContainer;
    private final Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

    private final Tabs tabs;
    public EinstellungenView(EinheitService einheitService, KategorieService kategorieService){
        this.einheitService = einheitService;
        this.kategorieService = kategorieService;
        this.einheitV = new EinheitView();
        this.kategorieV = new KategorieView();
        this.contentContainer = new Div();
        this.tabs = new Tabs();
        createTab();
        createView();
        add(tabs, contentContainer);
    }

    private void createTab(){
        tabComponentMap.put(new Tab("Kategorie"), kategorieV.createView(kategorieService));
        tabComponentMap.put(new Tab("Einheit"), einheitV.einheitView(einheitService));
        tabs.add(tabComponentMap.keySet().toArray(new Tab [] {}));
        tabs.addSelectedChangeListener(e ->{
            contentContainer.removeAll();
            contentContainer.add(tabComponentMap.get(e.getSelectedTab()));
        });
    }

    private void createView(){
        contentContainer.add(tabComponentMap.get(tabs.getSelectedTab()));
    }

}