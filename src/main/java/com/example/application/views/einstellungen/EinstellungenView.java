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

    EinheitService einheitService;
    KategorieService kategorieService;
    EinheitView einheitV = new EinheitView();
    KategorieView kategorieV = new KategorieView();
    final private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

    public EinstellungenView(EinheitService einheitService, KategorieService kategorieService){
        this.einheitService = einheitService;
        this.kategorieService = kategorieService;
        tabComponentMap.put(new Tab("Einheit"), einheitV.einheitView(einheitService));
        tabComponentMap.put(new Tab("Kategorie"), kategorieV.kategorieView(kategorieService));
        Tabs tabs = new Tabs(tabComponentMap.keySet().toArray(new Tab [] {}));


        Div contentContainer = new Div();


        add(tabs, contentContainer);



        tabs.addSelectedChangeListener(e ->{
            contentContainer.removeAll();
            contentContainer.add(tabComponentMap.get(e.getSelectedTab()));
        });
        contentContainer.add(tabComponentMap.get(tabs.getSelectedTab()));
    }
}







