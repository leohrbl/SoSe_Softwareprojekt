package com.example.application.views.zutat;

import com.example.application.data.einheit.EinheitService;
import com.example.application.data.zutat.Zutat;
import com.example.application.data.zutat.ZutatService;
import com.example.application.views.components.AddZutatDialog;
import com.example.application.views.components.DeleteDialog;
import com.example.application.views.components.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Diese Klasse erzeugt die Zutatenmanager-View. Diese besteht aus einer
 * Überschrift, einer Liste von Zutaten
 * mit zugeordneten Einheiten, einem Hinzufügen- und einem Löschen-Button.
 * Mithilfe der View soll der Nutzer
 * Zutaten hinzufügen und löschen können. Eine Zutat ist immer einer Einheit
 * zugeordnet.
 * 
 * @author: Lennart Rummel
 *
 */

@PageTitle("Rezeptbuch")
@Route(value = "/zutaten", layout = MainLayout.class)
public class ZutatenView extends VerticalLayout {

    private final Grid<Zutat> grid = new Grid<>(Zutat.class, false);
    private final Dialog dialog = new Dialog();
    private final Button addZutat = new Button("Hinzufügen");
    private final Button removeZutat = new Button("Löschen");
    private final ZutatService zutatService;
    private final EinheitService einheitService;

    /**
     * Konstruktor der ZutatenView-Klasse. Hier werden grundlegenden Konfigurationen
     * festgelegt, wie die Intialisierung
     * der Services für Zutaten und Einheiten, das Konfigurieren der Click-Listener
     * und die Componenten werden in einem
     * Layout angeordnet.
     *
     * @param zutatService   der Zutaten-Service wird übergeben und initialisiert,
     *                       um mit dem Backend zu kommunizieren.
     * @param einheitService der Einheit-Service wird übergeben und initialisiert,
     *                       um mit dem Backend zu kommunizieren.
     */
    public ZutatenView(ZutatService zutatService, EinheitService einheitService) {
        this.zutatService = zutatService;
        this.einheitService = einheitService;

        H3 seitenName = new H3("Zutatenverwaltung");
        seitenName.getStyle().set("margin", "0");

        configureButtons();

        VerticalLayout verticalLayout = new VerticalLayout(seitenName, configureGrid(),
                new HorizontalLayout(addZutat, removeZutat));
        add(verticalLayout);

    }

    /**
     * Hier werden die Buttons "Hinzufügen" und "Löschen" konfiguriert. Es wird das
     * Theme angepasst und der
     * Click-Listener aufgerufen.
     * Beim Klick auf Hinzufügen wird mit addZutatDialog.open() aufgerufen und somit
     * der Dialog gestartet.
     * Beim Klick auf Löschen wird die Methode removeZutaten() aufgerufen, um eine
     * ausgewählt Zutat zu löschen.
     */
    private void configureButtons() {
        addZutat.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addZutat.addClickListener(e -> {
            AddZutatDialog addZutatDialog = new AddZutatDialog(einheitService, zutatService);
            addZutatDialog.open();
            addZutatDialog.addDetachListener(ev -> {
                updateGrid();
            });
        });

        removeZutat.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeZutat.addClickListener(e -> removeZutaten());
    }

    /**
     * Das Grid / Die Tabelle wird konfiguriert, indem die Spalten, das Theme und
     * die Breite festgelegt werden.
     * 
     * @return Das eingestellte Grid wird zurückgegeben.
     */
    private Grid configureGrid() {
        grid.addColumn(Zutat::getName).setHeader("Bezeichnung");
        grid.addColumn(Zutat::getEinheit).setHeader("Einheit");

        updateGrid();
        // Styling
        grid.setWidth("50%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return grid;
    }

    /**
     * Das Grid wird aktualisiert, indem alle Zutaten neu in das Grid geladen
     * werden.
     */
    private void updateGrid() {
        grid.setItems(zutatService.getZutaten());
    }

    /**
     * Es wird geprüft, ob eine Zutat zum Löschen ausgewählt wurde. Sollte dies der
     * Fall sein, wird die ausgewählte
     * Zutat bestimmt und die Methode configureDeleteDialog(Zutat) aufgerufen.
     */
    private void removeZutaten() {
        if (!grid.getSelectionModel().getSelectedItems().isEmpty()) {
            Zutat zutat = grid.getSelectionModel().getFirstSelectedItem().get();
            configureDeleteDialog(zutat);
        } else {
            Notification.show("Löschen nicht möglich, da keine Zutat ausgewählt wurde.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Es wird eine DeleteDialog erzeugt, in dem bestätigt werden muss, dass die
     * Zutat wirklich gelöscht werden soll.
     * Ebenfalls werden die Clicklistener des DeleteDialog konfiguriert.
     * 
     * @param zutat wird in der removeZutaten() Methode übergeben. Und enthält die
     *              ausgewählte Zutat.
     */
    private void configureDeleteDialog(Zutat zutat) {
        DeleteDialog deleteDialog = new DeleteDialog("Zutat ", zutat.getName(),
                "Sicher, dass die Zutat gelöscht werden soll? Sie wird aus allen Rezepten entfernt!");
        deleteDialog.open();
        deleteDialog.getDeleteButton().addClickListener(e -> {
            zutatService.deleteZutat(zutat);
            Notification.show("Zutat gelöscht: " + zutat.getName() + " in " + zutat.getEinheit().toString())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            deleteDialog.close();
            updateGrid();
        });
        deleteDialog.getCancelButton().addClickListener(e -> {
            deleteDialog.close();
        });
    }
}
