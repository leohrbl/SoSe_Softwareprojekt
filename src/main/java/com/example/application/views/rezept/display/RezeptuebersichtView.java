package com.example.application.views.rezept.display;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.RezeptCard;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;

/**
 * Die RezeptansichtView-Klasse ist die Main View der Applikation. Sie bietet neben der Anzeige von Rezept noch eine Filter- und eine Suchfunktion. Zudem kann die Klasse zu der RezeptCreateView und EinstellungenView navigieren.
 * @author Léo Hérubel
 * @see RezeptCard
 * @see RezeptView
 * @see RezeptService
 * @see ZutatService
 * @see RezeptZutatenService
 */
@PageTitle("Rezeptbuch")
@Route(value = "", layout = MainLayout.class)
public class RezeptuebersichtView extends VerticalLayout {

    private List<Rezept> displayedItems;
    private List<Rezept> filteredItemsByZutat;
    private FlexLayout cardLayout;
    private RezeptService rezeptService;
    private ZutatService zutatService;
    private RezeptZutatenService rezeptZutatenService;
    private TextField searchField = new TextField();
    private ComboBox<Zutat> zutatFilter = new ComboBox<>();
    private VerticalLayout mainLayout;

    /**
     * Der Konstruktor initialisiert die unterschiedlichen Services. Zudem werden alle Rezepte der Instanzvariable displayedItems zugewiesen und die View wird erstellt.
     * @param rezeptService
     * @param zutatService
     * @param rezeptZutatenService
     */
    public RezeptuebersichtView(RezeptService rezeptService, ZutatService zutatService, RezeptZutatenService rezeptZutatenService) {
        this.rezeptService = rezeptService;
        this.zutatService = zutatService;
        this.rezeptZutatenService = rezeptZutatenService;
        this.displayedItems = rezeptService.getAllRezepte();
        this.cardLayout = loadCards();
        this.filteredItemsByZutat = new LinkedList<>();
        configureSearchField();
        configureZutatComboBox();
        mainLayout = createView(cardLayout);
        add(mainLayout);

    }

    /**
     * Methode zum Erzeugen der View Komponente mit den Instanzvariablen searchField, zutatFilter und cardLayout. Die anderen Komponenten werden jeweils durch eine Funktion erzeugt.
     * @param cardLayout
     * @return gibt die View als VerticalLayout zum Anzeigen der GUI zurück.
     */
    private VerticalLayout createView(FlexLayout cardLayout) {
        return new VerticalLayout(new HorizontalLayout(searchField, zutatFilter, createEditKategorienBtn(), createPrintDisplayedRezepteBtn(), createAddRezeptBtn()), cardLayout);
    }

    /**
     * Methode zum Konfigurieren der Instanzvariable searchField zum Suchen von Rezepten.
     */
    private void configureSearchField() {
        searchField.getElement().setAttribute("aria-label", "search");
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> handleSearch());
    }

    /**
     * Methode zum Konfigurieren der zutatFilter Komponente zum Filtern von Rezepten anhand von Zutaten.
     */
    private void configureZutatComboBox() {
        zutatFilter.setPlaceholder("Zutat filtern");
        zutatFilter.setRequired(false);
        zutatFilter.setAllowCustomValue(false);
        zutatFilter.setClearButtonVisible(true);
        zutatFilter.setItems(zutatService.getZutaten());
        zutatFilter.addValueChangeListener(event -> handleZutatFilter());
    }

    /**
     * Methode zum Erzeugen des Buttons zum editieren der Kategorien.
     * @return gibt den Button zum Hinzufügen ein einem Layout zurück
     */
    private Button createEditKategorienBtn() {
        Button editKategorienBtn = new Button("Kategorien bearbeiten");
        editKategorienBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editKategorienBtn.addClickListener(e -> editKategorien());
        return editKategorienBtn;
    }

    /**
     * Methode zum Erzeugen des Drucken-Buttons
     * @return gibt den Button zum Hinzufügen ein einem Layout zurück
     */
    private Button createPrintDisplayedRezepteBtn() {
        Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
        printDisplayedRezepteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printDisplayedRezepteBtn.addClickListener(e -> printErgebnisliste());
        return printDisplayedRezepteBtn;
    }

    /**
     * Methode zum Erzeugen des Create-Buttons
     * @return gibt den Button zum Hinzufügen ein einem Layout zurück
     */
    private Button createAddRezeptBtn() {
        Button addRezeptBtn = new Button(VaadinIcon.PLUS.create());
        addRezeptBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRezeptBtn.addClickListener(e -> addRezept());
        return addRezeptBtn;
    }

    /**
     * Methode zum Erzeugen eines gefüllten Layouts mit RezeptCard Komponenten anhand der aktuellen Datensätze von Rezepten.
     * @see RezeptCard
     * @return gibt ein Layout zum Ersetzen des bereits existierenden cardLayouts zurück.
     */
    private FlexLayout loadCards() {
        FlexLayout cardLayout = new FlexLayout();
        cardLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        for (Rezept rezept : displayedItems) {
            RezeptCard card = new RezeptCard(rezept.getTitel(), "Indisch", rezept.getId(), rezept.getBild());
            cardLayout.add(card);
        }
        return cardLayout;
    }

    /**
     * Methode zum Updaten des CardLayouts anhand der loadCards() Methode. Das aktuelle cardLayout wird im mainLayout durch ein neues cardLayout ersetzt.
     */
    private void updateCardLayout() {
        FlexLayout oldLayout = cardLayout;
        cardLayout = loadCards();
        mainLayout.replace(oldLayout, cardLayout);
    }

    /**
     * Methode zum Filtern von Rezepten anhand einer Zutat. Sofern kein Suchergebnis besteht, werden alle gefilterten Rezepte angezeigt. Ansonsten wird die handleSearch() Methode aufgerufen,
     * damit diese die Kombination der Suchtext-Ergebnisliste und den gefilterten Rezepten anzeigen kann.
     */
    private void handleZutatFilter() {
        if (zutatFilter.getValue() == null && !isSearching()) {
            displayedItems = rezeptService.getAllRezepte();
        } else if (zutatFilter.getValue() == null || zutatFilter.getValue() != null && isSearching()) {
            filteredItemsByZutat = rezeptZutatenService.findAllRezepteByZutat(zutatFilter.getValue());
            handleSearch();
        } else {
            filteredItemsByZutat = rezeptZutatenService.findAllRezepteByZutat(zutatFilter.getValue());
            displayedItems = filteredItemsByZutat;
        }
        updateCardLayout();
    }

    /**
     * Methode welche zurückgibt, ob nach einer Zutat in der View gefiltert wurde.
     * @return gibt einen Boolean zurück. Ist der Filter aktiv, ist der Wert = true
     */
    private boolean isFilterActive() {
        if (zutatFilter.getValue() != null) {
            return true;
        }
        return false;
    }

    /**
     * Methode welche zurückgibt, ob nach einem Rezept in der View gesucht wurde.
     * @return gibt einen Boolean zurück. Ist die eine Suche aktiv, ist der Wert = true
     */
    private boolean isSearching() {
        if (!searchField.getValue().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * Methode zum Suchen nach Rezepten. Ist der Zutat-Filter aktiv, wird die Kombination der gefilterten Zutaten und der Ergebnisliste des Suchtexts in der View hinzugefügt.
     */
    private void handleSearch() {
        String value = searchField.getValue();

        if (value.isEmpty() && !isFilterActive()) {
            displayedItems = rezeptService.getAllRezepte();
        } else if (value.isEmpty() && isFilterActive()) {
            return;
        } else if (isFilterActive()) {
            displayedItems = rezeptService.getRezeptByFilterAndSearchText(value, filteredItemsByZutat);
            updateCardLayout();
            return;
        } else {
            displayedItems = rezeptService.searchRezeptByFilterText(value);
        }
        updateCardLayout();
    }

    /**
     * Methode zum Navigieren zu den Einstellungen, in denen die Kategorien bearbeitet werden können.
     */
    private void editKategorien() {
        UI.getCurrent().navigate("/einstellungen");
    }

    /**
     * Methode zum Drucken der aktuell angezeigten Rezepte.
     */
    private void printErgebnisliste() {

    }

    /**
     * Methode zum Navigieren zu der View, in der der Nutzer Rezepte erstellen kann.
     */
    private void addRezept() {
        UI.getCurrent().navigate("/create");
    }

}
