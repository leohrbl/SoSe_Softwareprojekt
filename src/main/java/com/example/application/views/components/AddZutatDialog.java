package com.example.application.views.components;


import com.example.application.data.entity.Einheit;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Die Klasse AddZutatDialog erzeugt ein Objekt, welches von der Klasse Dialog erbt.
 * Erstellt wird die Zutaten hinzufügen Komponente. Es ist möglich eine Zutat mit einer zugeordneten Einheit hinzuzufügen.
 *
 * @author Lennart Rummel
 */

public class AddZutatDialog extends Dialog {

    private final EinheitService einheitService;
    private final ZutatService zutatService;
    private final Button speichern = new Button("Speichern");
    private final Button abbrechen = new Button("Abbrechen");
    private final TextField bezeichnung = new TextField("Bezeichnung");
    private final ComboBox<Einheit> einheitAuswahl = new ComboBox<>();


    /**
     * Der Konstruktor bekommt die beiden Serviceklassen EinheitService und ZutatService übergeben.
     * Wodurch das Dialogfenster auf die Zutat- und Einheitdaten zugreifen kann.
     *
     * @param einheitService
     * @param zutatService
     */
    public AddZutatDialog(EinheitService einheitService, ZutatService zutatService){
        this.einheitService = einheitService;
        this.zutatService = zutatService;
        createView();
    }

    /**
     * Die Methode erstellt die View und ruft Methoden auf, um die einzelnen Komponenten zu konfigurieren.
     */
    private void createView(){
        add(new H5("Zutat hinzufügen"));

        configureInputFields();
        add(new HorizontalLayout(bezeichnung, einheitAuswahl));

        configureButtons();
        add(new HorizontalLayout(speichern, abbrechen));
    }

    /**
     * Die Methode konfiguriert die Input-Felder für die Bezeichnung der Zutat und die Auswahl der Einheit.
     */
    private void configureInputFields() {
        bezeichnung.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung an!");

        einheitAuswahl.setLabel("Einheit");
        einheitAuswahl.setPlaceholder("Einheit auswählen");
        einheitAuswahl.setRequired(true);
        einheitAuswahl.setItems(einheitService.getEinheiten());
        einheitAuswahl.setErrorMessage("Gib eine Einheit an!");
    }

    /**
     *  In der Methode werden der Speichern-Button und der Abbrechen-Button konfiguriert.
     */
    private void configureButtons() {

        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        speichern.addClickListener(e ->{
            // Neue Zutat wird gespeichert.
            saveNewZutat();
        });
        speichern.addClickShortcut(Key.ENTER);

        abbrechen.addClickListener(e -> close());
        abbrechen.addClickShortcut(Key.ESCAPE);
    }

    /**
     * In der Methode wird die neue Zutat gespeichert. Vorher wird geprüft, ob eine Bezeichnung und eine Einheit
     * angegeben sind und bereits auch noch nicht vorhanden ist.
     */
    private void saveNewZutat(){
        if(!bezeichnung.isEmpty() && !einheitAuswahl.isEmpty()){
            if(zutatService.searchZutatenByFilterText(bezeichnung.getValue()).size() == 0){
                close();
                zutatService.saveZutat(bezeichnung.getValue(), einheitAuswahl.getValue());
                Notification.show("Zutat hinzugefügt: " + bezeichnung.getValue() +" in "+ einheitAuswahl.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }else{
                Notification.show("Die Zutat '"+ bezeichnung.getValue()+"' existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }else{
            Notification.show("Keine Zutat hinzugefügt. Es muss eine Bezeichnung und eine Einheit angegeben werden.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

}
