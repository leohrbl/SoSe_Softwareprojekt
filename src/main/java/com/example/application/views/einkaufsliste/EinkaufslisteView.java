package com.example.application.views.einkaufsliste;

import com.example.application.data.einkaufslisteneintrag.EinkaufslistenEintrag;
import com.example.application.data.einkaufslisteneintrag.EinkaufslistenService;
import com.example.application.views.components.DeleteDialog;
import com.example.application.views.components.MainLayout;
import com.example.application.views.drucken.DruckServiceEinkaufsliste;
import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Die Klasse zeigt und bearbeitet die Daten der Einkaufslisteneintrag Entität.
 * Die Klasse ermöglicht, durch den Zugriff auf die
 * EinkaufslistenService-Klasse,
 * das Löschen der gesamten Einkaufsliste und das Drucken von nicht abgewählten
 * Zutaten.
 *
 * @author Léo Hérubel
 * @see EinkaufslistenService
 *      hier printservice
 */
@CssImport(value = "./themes/rezeptbuch/einkaufsgridStyle.css", themeFor = "vaadin-grid")
@PageTitle("Einkaufsliste")
@Route(value = "einkaufsliste", layout = MainLayout.class)
public class EinkaufslisteView extends VerticalLayout {

    private final Grid<EinkaufslistenEintrag> einkaufsGrid;
    private final EinkaufslistenService einkaufslistenService;
    private final DeleteDialog deleteDialog;
    private final List<EinkaufslistenEintrag> displayedItems;

    private final DruckServiceEinkaufsliste druckservice;

    /**
     * Konstruktor zum Initialisieren der Instanzvariablen. Zudem wird das Grid
     * konfiguriert und die View erstellt.
     *
     * @param einkaufslistenService Datenbankservice der Einkaufsliste
     */
    public EinkaufslisteView(EinkaufslistenService einkaufslistenService) {
        this.einkaufslistenService = einkaufslistenService;
        this.displayedItems = einkaufslistenService.getAllEintrag();
        this.einkaufsGrid = new Grid<>(EinkaufslistenEintrag.class, false);
        this.deleteDialog = createDeleteDialog();
        this.druckservice = DruckServiceEinkaufsliste.getInstance();
        configureGrid();
        add(createView());
    }

    /**
     * Methode zum Konfigurieren des Grids. Es werden zudem die Daten das erste Mal
     * nach dem Aufruf im Konstruktor in das Grid geladen.
     */
    private void configureGrid() {
        einkaufsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getZutat).setHeader("Zutat").setAutoWidth(true);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getMengeString).setHeader("Menge").setAutoWidth(true);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getEinheitByZutat).setHeader("Einheit").setAutoWidth(true);
        einkaufsGrid.setWidth("50%");
        einkaufsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        einkaufsGrid.addThemeName("grid-selection-theme");
        // Initial Data load
        loadData();
    }

    /**
     * Methode zum Erzeugen der View mit bereits gefüllten Daten.
     *
     * @return gibt ein VerticalLayout zurück, damit das Layout mit der add Methode
     *         im Konstruktor im Frontend angezeigt werden kann.
     */
    private VerticalLayout createView() {
        return new VerticalLayout(createHeading(), einkaufsGrid,
                new HorizontalLayout(createPrintBtn(), createDeleteEinkaufslisteBtn()));
    }

    /**
     * Methode zum Erzeugen eines Löschen Buttons zum Löschen der Einkaufsliste.
     *
     * @return gibt den Delete Button zum Hinzufügen in ein Layout zurück.
     */
    private Button createDeleteEinkaufslisteBtn() {
        Button deleteEinkaufslisteBtn = new Button("Einkaufsliste löschen");
        deleteEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteEinkaufslisteBtn.addClickListener(e -> deleteDialog.open());
        return deleteEinkaufslisteBtn;
    }

    /**
     * Methode zum Erzeugen eines Drucken Buttons zum Drucken der Einkaufsliste.
     *
     * @return gibt den Drucken Button zum Hinzufügen in ein Layout zurück.
     */
    private Button createPrintEinkaufslisteBtn() {
        Button printEinkaufslisteBtn = new Button("Drucken", VaadinIcon.PRINT.create());
        printEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        // printEinkaufslisteBtn.addClickListener(e -> printEinkaufsliste());
        return printEinkaufslisteBtn;
    }

    /**
     * Methode zum Erzeugen der Überschrift in der RezeptView.
     *
     * @return gibt einen H3 zum Hinzufügen in ein Layout zurück.
     */
    private H3 createHeading() {
        H3 heading = new H3("Einkaufsliste");
        heading.getStyle().set("margin", "0");
        return heading;
    }

    /**
     * Methode zum Erzeugen des Delete Dialogs. Der Delete dient dazu, dass der User
     * seine Entscheidung zum Löschen der Einkaufsliste überprüfen kann.
     *
     * @return gibt einen DeleteDialog zurück. Dieser wird zur weiteren Verwendung
     *         der Instanzvariable deleteDialog zugewiesen.
     */
    private DeleteDialog createDeleteDialog() {
        DeleteDialog deleteDialog = new DeleteDialog("Einkaufsliste", "Einkaufsliste",
                "Sicher, dass du die Einkaufsliste löschen möchtest?");
        deleteDialog.getDeleteButton().addClickListener(e -> deleteEinkaufsliste());
        deleteDialog.getCancelButton().addClickListener(e -> deleteDialog.close());
        return deleteDialog;
    }

    /**
     * Diese Methode setzt die Items in dem Grid anhand der Variable displayedItems.
     * Nachdem die displayedItems Variable manipuliert wurde, wird diese Methode zum
     * Anzeigen der neuen Daten aufgerufen.
     */
    private void loadData() {
        einkaufsGrid.setItems((displayedItems));
    }

    /**
     * Die Methode delegiert den DeleteDialog und prüft die Entscheidung des Users,
     * die Einkaufsliste löschen zu wollen. Zudem wird der Serviceklasse mitgeteilt,
     * dass die Operation deleteAll() durchgeführt werden soll.
     * Bei erfolgreicher Ausführung wird dem User eine Benachrichtigung angezeigt.
     * Ansonsten wird eine Fehlermeldung in der Nutzeroberfläche ausgegeben.
     */
    private void deleteEinkaufsliste() {
        if (displayedItems.isEmpty()) {
            Notification.show("Keine Einkaufslisteneinträge vorhanden")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            deleteDialog.close();
            return;
        }
        String response = einkaufslistenService.deleteAll();
        if (response.equals("success")) {
            displayedItems.clear();
            einkaufsGrid.setItems(displayedItems);
            Notification.show("Einkaufsliste gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show("Ein Fehler ist aufgetreten").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        deleteDialog.close();
    }

    /**
     * Methode zum Drucken der aktuellen Daten in der EinkaufslisteView.
     */
    private List printEinkaufsliste(){
        List<EinkaufslistenEintrag> printList = getNotSelectedItems();
        return printList;
    }

    /**
     * In dem Grid lassen sich unterschiedliche Felder selektieren. Eine oder
     * mehrere Reihen werden selektiert. Da nur die nicht selektierten Daten
     * ausgedruckt werden sollen,
     * sucht diese Methode die nicht selektierten Komponenten der displayedItems.
     *
     * @return Es wird eine Liste von dem Container-Objekt Menge zurückgegeben.
     *         Diese Daten können anschließend ausgedruckt werden.
     */
    private List<EinkaufslistenEintrag> getNotSelectedItems() {
        if (displayedItems.isEmpty()) {
            return null;
        }else if(einkaufsGrid.getSelectedItems().isEmpty()){
            return displayedItems;
        }else {
            List<EinkaufslistenEintrag> printList = new ArrayList<>(displayedItems);
            List<EinkaufslistenEintrag> gridSelectedItems = new ArrayList<>(einkaufsGrid.getSelectedItems());

            for (EinkaufslistenEintrag eintrag : gridSelectedItems){
                printList.remove(eintrag);
            }

            return printList;
        }
    }

    /**
     * Die Methode createPrintBtn() erzeugt einen Button um die Einkaufsliste als PDF zu generieren.
     * Der ClickListener verweist direkt auf die erzeugte PDF.
     * @author Edwin Polle
     */

    private Button createPrintBtn() {
        Button printEinkaufslisteBtn = new Button(VaadinIcon.PRINT.create());
        printEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        printEinkaufslisteBtn.addClickListener(e -> {
            if(getNotSelectedItems() == null || getNotSelectedItems().isEmpty()) {
                Notification.show("Keine Einkaufslisteneinträge zum Drucken vorhanden.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }else{
                StreamResource resource = generateEinkaufsliste();
                final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry()
                        .registerResource(resource);
                UI.getCurrent().getPage().open(registration.getResourceUri().toString(), "Einkaufsliste drucken");

            }
        });
        return printEinkaufslisteBtn;

    }

    /**
     * Die Methode generateEinkaufsliste() erzeugt ein ByteArray um die PDF zu erzeugen und gibt diese wieder.
     * @author Edwin Polle
     * @return resource
     */

    private StreamResource generateEinkaufsliste() {
        byte[] byteArray = druckservice.createPDF(getNotSelectedItems());

        StreamResource resource = new StreamResource("Einkaufsliste drucken", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {
                return new ByteArrayInputStream(byteArray);
            }
        });
        resource.setContentType("application/pdf");
        return resource;
    }

}
