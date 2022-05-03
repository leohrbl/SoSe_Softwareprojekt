package com.example.application.views.zutatenmanager;

import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.DeleteDialog;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;




/**
 * Diese Klasse erzeugt die Zutatenmanager-View. Diese besteht aus einer Überschrift, einer Liste von Zutaten
 * mit zugeordneten Einheiten, einem Hinzufügen- und einem Löschen-Button. Mithilfe der View soll der Nutzer
 * Zutaten hinzufügen und löschen können. Eine Zutat ist immer einer Einheit zugeordnet.
 * @author: Lennart Rummel
 *
 */

@PageTitle("Rezeptbuch")
@Route(value = "/zutaten", layout = MainLayout.class)
public class ZutatenView extends VerticalLayout {

    Grid<Zutat> grid = new Grid<>(Zutat.class, false);
    Dialog dialog = new Dialog();
    Button addZutat = new Button("Hinzufügen");
    Button removeZutat = new Button("Löschen");
    DeleteDialog deleteDialog;
    ZutatService zutatService;
    EinheitService einheitService;


    /**
     * Konstruktor der ZutatenView-Klasse. Hier werden grundlegenden Konfigurationen festgelegt, wie die Intialisierung
     * der Services für Zutaten und Einheiten, das Konfigurieren der Click-Listener und die Componenten werden in einem
     * Layout angeordnet.
     *
     * @param zutatService der Zutaten-Service wird übergeben und initialisiert, um mit dem Backend zu kommunizieren.
     * @param einheitService der Einheit-Service wird übergeben und initialisiert, um mit dem Backend zu kommunizieren.
     */
    public ZutatenView(ZutatService zutatService, EinheitService einheitService){
        this.zutatService = zutatService;
        this.einheitService = einheitService;

        H3 seitenName = new H3("Zutatenverwaltung");
        seitenName.getStyle().set("margin", "0");

        configureButtons();


        VerticalLayout verticalLayout = new VerticalLayout(seitenName, configureGrid(), new HorizontalLayout(addZutat,removeZutat));
        add(verticalLayout);

    }

    /**
     * Hier werden die Buttons "Hinzufügen" und "Löschen" konfiguriert. Es wird das Theme angepasst und der
     * Click-Listener aufgerufen.
     * Beim Klick auf Hinzufügen wird mit addZutatDialog.open() aufgerufen und somit der Dialog gestartet.
     * Beim Klick auf Löschen wird die Methode removeZutaten() aufgerufen, um eine ausgewählt Zutat zu löschen.
     */
    public void configureButtons(){
        addZutat.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addZutat.addClickListener(e -> addZutatDialog().open());

        removeZutat.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeZutat.addClickListener(e -> removeZutaten());
    }

    /**
     * Das Dialogfenster zum Hinzufügen von Zutaten wird erstellt und konfiguriert.
     *
     * @return Das konfigurierte Dialogfenster wird zurückgegeben.
     */
    private Dialog addZutatDialog(){
        dialog = new Dialog();
        dialog.add(new H5("Zutat hinzufügen"));

        TextField bezeichnung = new TextField("Bezeichnung");
        bezeichnung.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        ComboBox<Einheit> einheitAuswahl = new ComboBox<>();
        einheitAuswahl.setLabel("Einheit");
        einheitAuswahl.setPlaceholder("Einheit auswählen");
        einheitAuswahl.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        einheitAuswahl.setItems(einheitService.getEinheiten());

        dialog.add(new HorizontalLayout(bezeichnung, einheitAuswahl));


        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> dialog.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

            // Neue Zutat wird gespeichert.
            if(!bezeichnung.isEmpty() && !einheitAuswahl.isEmpty()){
                if(zutatService.searchZutatenByFilterText(bezeichnung.getValue()).size() == 0){
                    dialog.close();
                    zutatService.saveZutat(bezeichnung.getValue(), einheitAuswahl.getValue());
                    Notification.show("Zutat hinzugefügt: " + bezeichnung.getValue() +" in "+ einheitAuswahl.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }else{
                    Notification.show("Die Zutat '"+ bezeichnung.getValue()+"' existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }else{
                Notification.show("Keine Zutat hinzugefügt. Es muss eine Bezeichnung und eine Einheit angegeben werden.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }



            updateGrid();
        });
        dialog.add(new HorizontalLayout(speichern, abbrechen));
        return dialog;
    }

    /**
     * Das Grid / Die Tabelle wird konfiguriert, indem die Spalten, das Theme und die Breite festgelegt werden.
     *
     * @return Das eingestellte Grid wird zurückgegeben.
     */
    public Grid configureGrid(){
        grid.addColumn(Zutat::getName).setHeader("Bezeichnung");
        grid.addColumn(Zutat::getEinheit).setHeader("Einheit");

        updateGrid();
        // Styling
        grid.setWidth("50%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return grid;
    }

    /**
     * Das Grid wird aktualisiert, indem alle Zutaten neu in das Grid geladen werden.
     */
    private void updateGrid(){
        grid.setItems(zutatService.getZutaten());
    }

    /**
     * Es wird geprüft, ob eine Zutat zum Löschen ausgewählt wurde. Sollte dies der Fall sein, wird die ausgewählte
     * Zutat bestimmt und die Methode configureDeleteDialog(Zutat) aufgerufen.
     */
    private void removeZutaten(){
        if(!grid.getSelectionModel().getSelectedItems().isEmpty()){
            Zutat zutat = grid.getSelectionModel().getFirstSelectedItem().get();
            configureDeleteDialog(zutat);
        }else{
            Notification.show("Löschen nicht möglich, da keine Zutat ausgewählt wurde.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Es wird eine DeleteDialog erzeugt, in dem bestätigt werden muss, dass die Zutat wirklich gelöscht werden soll.
     * Ebenfalls werden die Clicklistener des DeleteDialog konfiguriert.
     * @param zutat wird in der removeZutaten() Methode übergeben. Und enthält die ausgewählte Zutat.
     */
    public void configureDeleteDialog(Zutat zutat){
        deleteDialog = new DeleteDialog("Zutat ", zutat.getName(), "Sicher, dass du die Zutat wirklich löschen willst?");
        deleteDialog.open();
        deleteDialog.getDeleteButton().addClickListener( e -> {
            zutatService.deleteZutat(zutat);
            Notification.show("Zutat gelöscht: " + zutat.getName() + " in "+zutat.getEinheit().toString()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            deleteDialog.close();
            updateGrid();
        });
        deleteDialog.getCancelButton().addClickListener( e -> {
            deleteDialog.close();
        });
    }
}
