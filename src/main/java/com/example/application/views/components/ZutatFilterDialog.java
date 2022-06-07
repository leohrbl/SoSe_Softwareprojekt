package com.example.application.views.components;

import com.example.application.data.zutat.Zutat;
import com.example.application.data.zutat.ZutatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.data.selection.MultiSelectionEvent;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Der ZutatFilterDialog wird genutzt, damit der Nutzer nach einer Zutat filtern
 * kann. Der Dialog wird aus der RezeptuebersichtView gesteuert. Innerhalb des
 * Dialoges kann der Nutzer den Filter entfernen
 * oder eine andere Zutat zum Filtern auswählen. Des Weiteren kann der Nutzer
 * nach bestimmten Zutaten über ein Suchfeld suchen.
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
    private List<Zutat> displayedItems;
    private Set<Zutat> selectedItems;
    private boolean gridDataLoad;

    /**
     * Der Konstruktor initialisiert die Instanzvariablen der Klasse. Zudem wird der
     * Dialog im Konstruktor konfiguriert.
     *
     * @param zutatService Datenbankservice der Zutat Entität
     */
    public ZutatFilterDialog(ZutatService zutatService) {
        this.searchField = new TextField();
        this.grid = new Grid<>(Zutat.class, false);
        this.filterButton = new Button("Filtern");
        this.zutatService = zutatService;
        this.removeFilterButton = new Button("Filter entfernen");
        this.selectedItems = new HashSet<>();
        this.gridDataLoad = false;
        configureDialog();
    }

    /**
     * Methode zum Konfigurieren des Dialoges. Der Dialog besteht aus einem Footer,
     * dem Content, und einem Header.
     */
    private void configureDialog() {
        configureFilterButton();
        configureRemoveFilterButton();
        configureSearchField();
        configureGrid();
        this.add(createHeaderLayout(), grid, createFooterLayout());
    }

    /**
     * Methode zum Erstellen des Headers des Dialoges. Der Header besteht aus dem
     * Suchfeld und einem Abbrechen Button, welcher den Dialog schließt.
     *
     * @return Gibt das HeaderLayout zurück, damit es im Dialog hinzugefügt werden
     *         kann.
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
     * Methode zum Erstellen des Footers, welcher aus dem FilterButton und dem
     * RemoveFilterButton besteht.
     *
     * @return Gibt den Footer als HorizontalLayout zurück.
     */
    private HorizontalLayout createFooterLayout() {
        HorizontalLayout footerLayout = new HorizontalLayout(filterButton, removeFilterButton);
        footerLayout.setWidthFull();
        footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return footerLayout;
    }

    /**
     * Methode zum Konfigurieren des FilterButtons. Die Funktionalität wird von der
     * Klasse hinzugefügt, die den Dialog nutzt.
     */
    private void configureFilterButton() {
        filterButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    }

    /**
     * Methode zum Konfigurieren des RemoveFilterButtons. Die Funktionalität wird
     * von der Klasse hinzugefügt, die den Dialog benutzt.
     */
    private void configureRemoveFilterButton() {
        removeFilterButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
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
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        MultiSelect<Grid<Zutat>, Zutat> multiSelect = grid.asMultiSelect();
        multiSelect.addSelectionListener(e -> handleSelectionChange(e));
        updateGrid();
    }

    /**
     * Methode, welche (sofern keine neue Ergebnisliste aus dem Backend durch die
     * Textsuche geladen wurde) das Hinzufügen oder Entfernen der selektierten
     * Zutaten delegiert.
     *
     * @param e Event, welches die Selection Changes verfolgt
     */
    private void handleSelectionChange(MultiSelectionEvent<Grid<Zutat>, Zutat> e) {
        if (!gridDataLoad) {
            removeSelectedItems(e);
            addSelectedItems(e);
        }
    }

    /**
     * Methode zum Entfernen der deselektierten Zutaten aus der Instanzvariable
     * selectedItems.
     *
     * @param e Event, welches die Selection Changes verfolgt
     */
    private void removeSelectedItems(MultiSelectionEvent<Grid<Zutat>, Zutat> e) {
        for (Zutat zutat : e.getRemovedSelection()) {
            if (!selectedItems.isEmpty()) {
                List<Zutat> toRemove = new LinkedList<>();
                for (Zutat selectedZutat : selectedItems) {
                    if (!zutat.getName().equals(selectedZutat.getName())) {
                        toRemove.add(zutat);
                    }
                }
                selectedItems.removeAll(toRemove);
            }
        }
    }

    /**
     * Methode zum Erstellen des AbbrechenButtons.
     *
     * @return Gibt den Button zurück, damit dieser in ein Layout hinzugefügt werden
     *         kann.
     */
    private Button createAbbrechenButton() {
        Button abbrechenButton = new Button(VaadinIcon.CLOSE.create());
        abbrechenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        abbrechenButton.addClickListener(e -> {
            this.close();
        });
        abbrechenButton.addClickListener(e -> this.close());
        return abbrechenButton;
    }

    /**
     * Methode zum Speichern der selektierten Zutaten in der Instanzvariable
     * selectedItems.
     *
     * @param e Event, welches die Selection Changes verfolgt
     */
    private void addSelectedItems(MultiSelectionEvent<Grid<Zutat>, Zutat> e) {
        for (Zutat zutat : e.getAddedSelection()) {
            if (selectedItems.isEmpty()) {
                selectedItems.add(zutat);
            } else {
                List<Zutat> toAdd = new LinkedList<>();
                for (Zutat selectedZutat : selectedItems) {
                    if (!zutat.getName().equals(selectedZutat.getName())) {
                        toAdd.add(zutat);
                    }
                }
                selectedItems.addAll(toAdd);
            }
        }
    }

    /**
     * Methode zum Aktualisieren der Daten des Grids des Dialoges.
     */
    private void updateGrid() {
        if (searchField.isEmpty()) {
            displayedItems = zutatService.getZutaten();
        } else {
            displayedItems = zutatService.searchZutatenByFilterText(searchField.getValue());
        }
        gridDataLoad = true;
        grid.setItems(displayedItems);
        selectItemsInGrid();
        gridDataLoad = false;
    }

    /**
     * Methode, welche die Zutaten, welche in der Instanzvariable selectedItems
     * vorhanden sind im Frontend aus der aktuellen Ergebnisliste selektiert.
     */
    private void selectItemsInGrid() {
        for (Zutat zutat : selectedItems) {
            for (Zutat displayedZutat : displayedItems) {
                if (zutat.getName().equals(displayedZutat.getName())) {
                    grid.select(displayedZutat);
                }
            }
        }
    }

    /**
     * Methode zum Verarbeiten der veränderten Werte im Suchfeld.
     */
    private void handleSearch() {
        updateGrid();
    }

    public Set<Zutat> getSelectedItems() {
        return selectedItems;
    }

    public Button getFilterButton() {
        return this.filterButton;
    }

    public Grid<Zutat> getGrid() {
        return grid;
    }

    public Button getRemoveFilterButton() {
        return this.removeFilterButton;
    }

    /**
     * Methode, mit der sich andere Klassen die aktuell selektierten Zutaten aus dem
     * Grid des Dialoges holen können.
     *
     * @return Gibt das Zutaten-Set zurück
     */
    public Set<Zutat> getFilteredItems() {
        if (!grid.getSelectedItems().isEmpty()) {
            return grid.getSelectionModel().getSelectedItems();
        } else {
            return null;
        }
    }

}
