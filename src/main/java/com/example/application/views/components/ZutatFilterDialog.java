package com.example.application.views.components;

import com.example.application.data.entity.Zutat;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * Der ZutatFilterDialog wird genutzt, damit der Nutzer nach einer Zutat filtern kann. Der Dialog wird aus der RezeptuebersichtView gesteuert. Innerhalb des Dialoges kann der Nutzer den Filter entfernen
 * oder eine andere Zutat zum Filtern auswählen. Des Weiteren kann der Nutzer nach bestimmten Zutaten über ein Suchfeld suchen.
 *
 * @author Léo Hérubel
 * @see com.example.application.views.rezept.display.RezeptuebersichtView
 * @see ZutatService
 */
public class ZutatFilterDialog extends Dialog {

    private final TextField searchField;
    private final Grid<Zutat> grid;
    private final ZutatService zutatService;
    private final Button filterButton;
    private final Button removeFilterButton;

    /**
     * Der Konstruktor initialisiert die Instanzvariablen der Klasse. Zudem wird der Dialog im Konstruktor konfiguriert.
     *
     * @param zutatService Datenbankservice der Zutat Entität
     */
    public ZutatFilterDialog(ZutatService zutatService) {
        this.searchField = new TextField();
        this.grid = new Grid<>(Zutat.class, false);
        this.filterButton = new Button("Filtern");
        this.zutatService = zutatService;
        this.removeFilterButton = new Button("Filter entfernen");
        configureDialog();
    }

    /**
     * Methode zum Konfigurieren des Dialoges. Der Dialog besteht aus einem Footer, dem Content, und einem Header.
     */
    private void configureDialog() {
        configureFilterButton();
        configureRemoveFilterButton();
        configureSearchField();
        configureGrid();
        this.add(createHeaderLayout(), grid, createFooterLayout());
    }

    /**
     * Methode zum Erstellen des Headers des Dialoges. Der Header besteht aus dem Suchfeld und einem Abbrechen Button, welcher den Dialog schließt.
     *
     * @return Gibt das HeaderLayout zurück, damit es im Dialog hinzugefügt werden kann.
     */
    private HorizontalLayout createHeaderLayout() {
        HorizontalLayout header = new HorizontalLayout(searchField, createAbbrechenButton());
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1, searchField);
        header.setPadding(true);
        header.setSpacing(true);
        return header;
    }

    /**
     * Methode zum Erstellen des Footers, welcher aus dem FilterButton und dem RemoveFilterButton besteht.
     *
     * @return Gibt den Footer als HorizontalLayout zurück.
     */
    private HorizontalLayout createFooterLayout() {
        return new HorizontalLayout(filterButton, removeFilterButton);
    }

    /**
     * Methode zum Konfigurieren des FilterButtons. Die Funktionalität wird von der Klasse hinzugefügt, die den Dialog nutzt.
     */
    private void configureFilterButton() {
        filterButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    }

    /**
     * Methode zum Konfigurieren des RemoveFilterButtons. Die Funktionalität wird von der Klasse hinzugefügt, die den Dialog benutzt.
     */
    private void configureRemoveFilterButton() {
        removeFilterButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    /**
     * Methode zum Erstellen des AbbrechenButtons.
     *
     * @return Gibt den Button zurück, damit dieser in ein Layout hinzugefügt werden kann.
     */
    private Button createAbbrechenButton() {
        Button abbrechenButton = new Button(VaadinIcon.CLOSE.create());
        abbrechenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        abbrechenButton.addClickListener(e -> this.close());
        return abbrechenButton;
    }

    /**
     * Methode zum Konfigurieren des Suchfeldes.
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
     * Methode zum Konfigurieren des Grids, welches die Zutaten anzeigt.
     */
    private void configureGrid() {
        grid.addColumn(Zutat::getName).setHeader("Bezeichnung");
        grid.addColumn(Zutat::getEinheit).setHeader("Einheit");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        updateGrid();
    }

    /**
     * Methode zum Aktualisieren der Daten des Grids des Dialoges.
     */
    private void updateGrid() {
        if (searchField.isEmpty()) {
            grid.setItems(zutatService.getZutaten());
        } else {
            grid.setItems(zutatService.searchZutatenByFilterText(searchField.getValue()));
        }
    }

    /**
     * Methode zum Verarbeiten der veränderten Werte im Suchfeld.
     */
    private void handleSearch() {
        updateGrid();
    }


    public Button getFilterButton() {
        return this.filterButton;
    }

    public Button getRemoveFilterButton() {
        return this.removeFilterButton;
    }

    /**
     * Methode, mit der sich andere Klassen die aktuell selektierte Zutat aus dem Grid des Dialoges holen können.
     *
     * @return Gibt das Zutat-Objekt zurück
     */
    public Zutat getFilteredItem() {
        if (!grid.getSelectedItems().isEmpty()) {
            return grid.getSelectionModel().getFirstSelectedItem().get();
        } else {
            return null;
        }
    }

}
