package com.example.application.views.einstellungen;

import com.example.application.data.kategorie.Kategorie;
import com.example.application.data.kategorie.KategorieService;
import com.example.application.views.components.DeleteDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.dnd.GridDropLocation;
import com.vaadin.flow.component.grid.dnd.GridDropMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse erzeugt die Kategorien-View. Diese besteht aus der Überschrift,
 * einer Tabelle mit den bestehenden Kategorien
 * und den Hinzufügen- und Löschen-Button.
 *
 * @author Edwin Polle
 */

public class KategorieView extends VerticalLayout {
    private Grid<Kategorie> gridKategorie = null;
    private Dialog dialogKategorie;
    private DeleteDialog deleteDialogKategorie;
    private Kategorie draggedItem;
    private Button addKategorieButton;
    private Button removeKategorieButton;
    private KategorieService kategorieService;
    private GridListDataView<Kategorie> dataView;
    static List<Kategorie> kategorieList;

    /**
     * Der Konstruktor initialisiert die benötigten Variablen
     */
    public KategorieView() {
        this.gridKategorie = new Grid<>(Kategorie.class, false);
        this.dialogKategorie = new Dialog();

    }

    /**
     * Die Methode createView() erzeugt die View und gibt diese anschließend zurück.
     *
     * @param kategorieService
     * @return die erzeugte View wir zurückgegeben.
     */
    public VerticalLayout createView(KategorieService kategorieService) {
        this.kategorieList = new ArrayList<>(kategorieService.getKategorien());
        this.kategorieService = kategorieService;
        H3 title = getTitle();
        title.getStyle().set("margin", "0");

        configureButtonsKategorien();

        VerticalLayout verticalLayoutTitle = new VerticalLayout(title);
        VerticalLayout verticalLayoutKategorie = new VerticalLayout(configureGridKategorie(),
                new HorizontalLayout(addKategorieButton, removeKategorieButton));
        verticalLayoutKategorie.setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(verticalLayoutTitle);
        verticalLayout.add(verticalLayoutKategorie);

        setSizeFull();

        return verticalLayout;
    }

    /**
     * Die überschrift wird erzeugt und zurückgegeben.
     *
     * @return Überschrift wird zurückgegeben.
     */

    private H3 getTitle() {
        return new H3("Kategorieverwaltung");
    }

    /**
     * Die Buttons Hinzufügen und Löschen werden erzeugt.
     */
    private void configureButtonsKategorien() {
        this.addKategorieButton = new Button("Hinzufügen");
        addKategorieButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addKategorieButton.addClickListener(e -> addKategorieDialog().open());

        this.removeKategorieButton = new Button("Löschen");
        removeKategorieButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeKategorieButton.addClickListener(e -> removeKategorie());
    }

    /**
     * Der Dialog zum Hinzufügen einer Kategorie wird erzeugt und konfiguriert.
     *
     * @return Der Dialog wird zurückgegeben.
     */
    private Dialog addKategorieDialog() {
        dialogKategorie = new Dialog();
        dialogKategorie.add(new H5("Kategorie hinzufügen"));

        TextField kategorie = new TextField("Bezeichnung");
        kategorie.setRequired(true);
        kategorie.setMaxLength(12);
        kategorie.setErrorMessage("Gib eine Bezeichnung ein!");

        dialogKategorie.add(new HorizontalLayout(kategorie));

        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e -> dialogKategorie.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e -> {

            if (kategorie.getValue() != null) {
                kategorieService.saveKategorie(kategorie.getValue());
                dialogKategorie.close();
                Notification.show("Kategorie hinzugefügt: " + kategorie.getValue())
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("Die Kategorie '" + kategorie.getValue() + "' existiert bereits.")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            updateGridKategorie();
            UI.getCurrent().getPage().reload();
        });
        dialogKategorie.add(new HorizontalLayout(speichern, abbrechen));
        return dialogKategorie;
    }

    /**
     * Das Grid zum Anzeigen der Kategorien wird erzeugt und konfiguriert. Zudem
     * wird der Drag and Drop Modus aktiviert.
     *
     * @return Grid wird zurückgegeben.
     */
    private Grid configureGridKategorie() {
        dataView = gridKategorie.setItems(kategorieList);
        gridKategorie.setDropMode(GridDropMode.BETWEEN);
        gridKategorie.setRowsDraggable(true);
        gridKategorie.addColumn(Kategorie::getName).setHeader("Kategorien").setAutoWidth(true);
        gridKategorie.addColumn(Kategorie::getSequenceNr).setHeader(" Reihenfolge").setAutoWidth(true);
        gridKategorie.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        setClickListener();
        return gridKategorie;
    }

    /**
     * Der ClickListener für den Drag and Drop Modus wird hinzugefügt.
     */
    private void setClickListener() {

        gridKategorie.addDragStartListener(e -> draggedItem = e.getDraggedItems().get(0));

        gridKategorie.addDropListener(e -> {
            Kategorie zielKategorie = e.getDropTargetItem().orElse(null);
            GridDropLocation dropLocation = e.getDropLocation();

            boolean kategorieWasDroppedOntoItself = draggedItem.equals(zielKategorie);

            if (zielKategorie == null || kategorieWasDroppedOntoItself)
                return;

            dataView.removeItem(draggedItem);

            if (dropLocation == GridDropLocation.BELOW) {
                dataView.addItemAfter(draggedItem, zielKategorie);

            } else {
                dataView.addItemBefore(draggedItem, zielKategorie);

            }

            for (Kategorie element : kategorieList) {
                element.setSequenceNr(kategorieList.indexOf(element) + 1);
                kategorieService.updateSequenceNr(element);
            }

            dataView.refreshAll();
        });

        gridKategorie.addDragEndListener(e -> {
            draggedItem = null;
        });
    }

    /**
     * Der DeleteDialog zum Löschen einer Kategorie wird erzeugt. Anschließend wird
     * eine Kategorie zum Löschen übergeben.
     *
     * @param kategorie die zu löschende Kategorie wird übergeben.
     */
    public void configureDeleteDialogKategorie(Kategorie kategorie) {
        deleteDialogKategorie = new DeleteDialog("Kategorie", kategorie.getName(),
                "Sicher, das die Kategorie gelöscht werden soll?");
        deleteDialogKategorie.open();

        deleteDialogKategorie.getDeleteButton().addClickListener(e -> {
            if (kategorieService.deleteKategorie(kategorie) == true) {
                Notification.show("Kategorie gelöscht: " + kategorie.getName())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                reOrder();
            } else {
                Notification.show("Kategorie konnte nicht gelöscht werden, da diese noch mit Rezepten verknüpft ist!")
                        .addThemeVariants((NotificationVariant.LUMO_ERROR));
            }
            updateGridKategorie();
            deleteDialogKategorie.close();
            gridKategorie.getDataProvider().refreshAll();

        });
        deleteDialogKategorie.getCancelButton().addClickListener(e -> deleteDialogKategorie.close());
    }

    /**
     * Die Methode reOrder() ordnet den Kategorien nach einem Drag and Drop oder
     * Löschen die neue Reihenfolge Nummer zu.
     */
    public void reOrder() {
        kategorieList = kategorieService.getKategorien();
        for (Kategorie element : kategorieList) {
            element.setSequenceNr(kategorieList.indexOf(element) + 1);
            kategorieService.updateSequenceNr(element);
        }
    }

    /**
     * Die Methode removeKategorien() prüft, ob eine Kategorie ausgewählt wurde und
     * startet anschließend den DeleteDialog.
     */

    private void removeKategorie() {
        if (!gridKategorie.getSelectionModel().getSelectedItems().isEmpty()) {
            Kategorie kategorie = gridKategorie.getSelectionModel().getFirstSelectedItem().get();
            configureDeleteDialogKategorie(kategorie);
            dialogKategorie.close();
        } else {
            Notification.show("Löschen nicht möglich, da keine Kategorie ausgewählt wurde.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Die Methode updateGridKategorie aktualisiert das Grid.
     */
    private void updateGridKategorie() {
        gridKategorie.setItems(kategorieService.getKategorien());
    }
}
