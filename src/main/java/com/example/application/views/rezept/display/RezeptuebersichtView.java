package com.example.application.views.rezept.display;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.DruckserviceRezept;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.RezeptCard;
import com.example.application.views.components.ZutatFilterDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;

import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Die RezeptansichtView-Klasse ist die Main View der Applikation. Sie bietet
 * neben der Anzeige von Rezept noch eine Filter- und eine Suchfunktion. Zudem
 * kann die Klasse zu der RezeptCreateView und EinstellungenView navigieren.
 *
 * @author Léo Hérubel
 * @see RezeptCard
 * @see RezeptView
 * @see RezeptService
 * @see ZutatFilterDialog
 * @see DruckserviceRezept
 */
@PageTitle("Rezeptbuch")
@Route(value = "", layout = MainLayout.class)
public class RezeptuebersichtView extends VerticalLayout {

    private List<Rezept> displayedItems;
    private List<Rezept> filteredItemsByZutaten;
    private FlexLayout cardLayout;
    private final RezeptService rezeptService;
    private final RezeptZutatenService rezeptZutatenService;
    private final TextField searchField;
    private final Button zutatFilterButton;
    private final ZutatFilterDialog zutatFilterDialog;
    private final VerticalLayout mainLayout;
    private boolean isFilterActive;
    private final DruckserviceRezept druckservice = DruckserviceRezept.getInstance();

    /**
     * Der Konstruktor initialisiert die unterschiedlichen Services. Zudem werden
     * alle Rezepte der Instanzvariable displayedItems zugewiesen und die View wird
     * erstellt.
     *
     * @param rezeptService        Datenbankservice für Rezepte
     * @param zutatService         Datenbankservice für Zutaten
     * @param rezeptZutatenService Datenbankservice für Rezept_Zutat Entitäten
     */
    public RezeptuebersichtView(RezeptService rezeptService, ZutatService zutatService,
            RezeptZutatenService rezeptZutatenService) {
        this.rezeptService = rezeptService;
        this.rezeptZutatenService = rezeptZutatenService;
        this.displayedItems = rezeptService.getAllRezepte();
        Collections.sort(displayedItems);
        this.cardLayout = loadCards();
        this.filteredItemsByZutaten = new LinkedList<>();
        this.zutatFilterDialog = new ZutatFilterDialog(zutatService);
        this.isFilterActive = false;
        this.searchField = new TextField();
        this.zutatFilterButton = new Button("Filtern", VaadinIcon.FILTER.create());
        configureZutatFilterButton();
        configureZutatFilterDialog();
        configureSearchField();
        mainLayout = createView(cardLayout);
        add(mainLayout);
    }

    /**
     * Methode zum Erzeugen der View Komponente mit den Instanzvariablen
     * searchField, zutatFilter und cardLayout. Die anderen Komponenten werden
     * jeweils durch eine Funktion erzeugt.
     *
     * @param cardLayout Gibt das CardLayout zurück, indem die RezeptCards enthalten
     *                   sind.
     * @return gibt die View als VerticalLayout zum Anzeigen der GUI zurück.
     */
    private VerticalLayout createView(FlexLayout cardLayout) {
        return new VerticalLayout(new HorizontalLayout(searchField, zutatFilterButton, createEditKategorienBtn(),
                createPrintDisplayedRezepteBtn(), createAddRezeptBtn()), cardLayout);
    }

    /**
     * Methode zum Konfigurieren der Instanzvariable searchField zum Suchen von
     * Rezepten.
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
     * Methode zum Konfigurieren des ZutatFilterDialogs. Für die Buttons des
     * Dialoges werden ClickListener hinzugefügt, damit diese Klasse die
     * Informationen aus dem FilterDialog verarbeiten kann.
     */
    private void configureZutatFilterDialog() {
        zutatFilterDialog.getFilterButton().addClickListener(e -> handleZutatFilterButtonClick());
        zutatFilterDialog.getRemoveFilterButton().addClickListener(e -> handleZutatFilterRemoveButtonClick());
    }

    /**
     * Methode zum Verarbeiten vom Click des Filter-Buttons. Die Informationen aus
     * dem ZutatFilterDialog werden mit der aktiven oder inaktiven Suchfunktion
     * verknüpft
     * und die gefilterten Rezepte werden verarbeitet.
     */
    private void handleZutatFilterButtonClick() {
        if (zutatFilterDialog.getFilteredItems() == null) {
            Notification.show("Keine Zutat zum Filtern ausgewählt!").addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            isFilterActive = true;
            zutatFilterButton.removeThemeVariants(ButtonVariant.LUMO_CONTRAST);
            zutatFilterButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            handleZutatFilter(zutatFilterDialog.getFilteredItems());
            zutatFilterDialog.close();
        }
    }

    /**
     * Methode, welche nach einem Click vom RemoveFilterButton den aktiven Filter
     * aus der View entfernt.
     */
    private void handleZutatFilterRemoveButtonClick() {
        isFilterActive = false;
        zutatFilterButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        zutatFilterButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        zutatFilterDialog.close();
        zutatFilterDialog.getGrid().deselectAll();
        zutatFilterDialog.getSelectedItems().clear();
        handleSearch();
    }

    /**
     * Methode zum Erstellen und Konfigurieren des ZutatFilterButtons der
     * RezeptuebersichtView.
     */
    private void configureZutatFilterButton() {
        zutatFilterButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        zutatFilterButton.addClickListener(e -> zutatFilterDialog.open());
    }

    /**
     * Methode zum Erzeugen des Buttons zum editieren der Kategorien.
     *
     * @return gibt den Button zum Hinzufügen in ein Layout zurück.
     */
    private Button createEditKategorienBtn() {
        Button editKategorienBtn = new Button("Kategorien bearbeiten");
        editKategorienBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editKategorienBtn.addClickListener(e -> editKategorien());
        return editKategorienBtn;
    }

    /**
     * Methode zum Erzeugen des Drucken-Buttons
     *
     * @return Gibt den PrintButton zurück, damit dieser in ein Layout eingefügt
     *         werden kann.
     */
    private Button createPrintDisplayedRezepteBtn() {
        Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
        printDisplayedRezepteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        printDisplayedRezepteBtn.addClickListener(e -> {
            if (displayedItems.isEmpty()) {
                Notification.show("Keine Rezepte zum Drucken verfügbar!")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            StreamResource resource = generatePDF();

            if (resource == null) {
                Notification.show("PDF konnte nicht erstellt werden!")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry()
                    .registerResource(resource);

            UI.getCurrent().getPage().open(registration.getResourceUri().toString(), "Rezeptliste drucken");
        });
        return printDisplayedRezepteBtn;
    }

    /**
     * Methode, die eine PDF erzeugt und diese als StreamResource zurückgibt
     * 
     * @author Philipp Laupichler
     * @return StreamResource mit PDF
     */
    private StreamResource generatePDF() {
        byte[] byteArray = druckservice.createRezeptByte(this.displayedItems);

        StreamResource resource = new StreamResource("Rezeptliste drucken", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {

                return new ByteArrayInputStream(byteArray);

            }
        });
        resource.setContentType("application/pdf");
        return resource;
    }

    /**
     * Methode zum Erzeugen des Create-Buttons
     *
     * @return gibt den Button zum Hinzufügen ein einem Layout zurück
     */
    private Button createAddRezeptBtn() {
        Button addRezeptBtn = new Button(VaadinIcon.PLUS.create());
        addRezeptBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRezeptBtn.addClickListener(e -> addRezept());
        return addRezeptBtn;
    }

    /**
     * Methode zum Erzeugen eines gefüllten Layouts mit RezeptCard Komponenten
     * anhand der aktuellen Datensätze von Rezepten.
     *
     * @return gibt ein Layout zum Ersetzen des bereits existierenden cardLayouts
     *         zurück.
     * @see RezeptCard
     */
    private FlexLayout loadCards() {
        FlexLayout cardLayout = new FlexLayout();
        cardLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        for (Rezept rezept : displayedItems) {
            RezeptCard card = new RezeptCard(rezept.getTitel(), rezept.getKategorie().getName(), rezept.getId(),
                    rezeptService.generateImage(rezept));
            cardLayout.add(card);
        }
        return cardLayout;
    }

    /**
     * Methode zum Updaten des CardLayouts anhand der loadCards() Methode. Das
     * aktuelle cardLayout wird im mainLayout durch ein neues cardLayout ersetzt.
     */
    private void updateCardLayout() {
        FlexLayout oldLayout = cardLayout;
        cardLayout = loadCards();
        mainLayout.replace(oldLayout, cardLayout);
    }

    /**
     * Methode zum Filtern von Rezepten anhand einer Zutat. Sofern kein Suchergebnis
     * besteht, werden alle gefilterten Rezepte angezeigt. Ansonsten wird die
     * handleSearch() Methode aufgerufen,
     * damit diese die Kombination der Suchtext-Ergebnisliste und den gefilterten
     * Rezepten verarbeiten kann.
     */
    private void handleZutatFilter(Set<Zutat> filteredZutaten) {
        if (isSearching()) {
            filteredItemsByZutaten = rezeptZutatenService.findAllRezepteByZutaten(filteredZutaten);
            handleSearch();
        } else {
            filteredItemsByZutaten = rezeptZutatenService.findAllRezepteByZutaten(filteredZutaten);
            displayedItems = filteredItemsByZutaten;
        }
        Collections.sort(displayedItems);
        updateCardLayout();
    }

    /**
     * Methode welche zurückgibt, ob nach einem Rezept in der View gesucht wurde.
     *
     * @return gibt einen Boolean zurück. Ist die eine Suche aktiv, ist der Wert =
     *         true
     */
    private boolean isSearching() {
        return !searchField.getValue().isEmpty();
    }

    /**
     * Methode zum Suchen nach Rezepten. Ist der Zutat-Filter aktiv, wird die
     * Kombination der gefilterten Zutaten und der Ergebnisliste des Suchtexts in
     * der View hinzugefügt.
     */
    private void handleSearch() {
        String value = searchField.getValue();

        if (!isSearching() && !isFilterActive) {
            displayedItems = rezeptService.getAllRezepte();
        } else if (!isSearching() && isFilterActive) {
            displayedItems = filteredItemsByZutaten;
        } else if (isFilterActive && isSearching()) {
            displayedItems = rezeptService.getRezepteByFilterAndSearchText(value, filteredItemsByZutaten);
        } else {
            displayedItems = rezeptService.searchRezepteByFilterText(value);
        }
        Collections.sort(displayedItems);
        updateCardLayout();
    }

    /**
     * Methode zum Navigieren zu den Einstellungen, in denen die Kategorien
     * bearbeitet werden können.
     */
    private void editKategorien() {
        UI.getCurrent().navigate("/einstellungen");
    }

    /**
     * Methode zum Navigieren zu der View, in der der Nutzer Rezepte erstellen kann.
     */
    private void addRezept() {
        UI.getCurrent().navigate("/create");
    }

}
