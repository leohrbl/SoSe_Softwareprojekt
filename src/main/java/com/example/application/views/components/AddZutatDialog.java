package com.example.application.views.components;


import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class AddZutatDialog extends Dialog {

    private EinheitService einheitService;
    private ZutatService zutatService;

    public AddZutatDialog(EinheitService einheitService, ZutatService zutatService){
        this.einheitService = einheitService;
        this.zutatService = zutatService;
        add(new H5("Zutat hinzufügen"));

        TextField bezeichnung = new TextField("Bezeichnung");
        bezeichnung.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        ComboBox<Einheit> einheitAuswahl = new ComboBox<>();
        einheitAuswahl.setLabel("Einheit");
        einheitAuswahl.setPlaceholder("Einheit auswählen");
        einheitAuswahl.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        einheitAuswahl.setItems(einheitService.getEinheiten());

        add(new HorizontalLayout(bezeichnung, einheitAuswahl));


        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

            // Neue Zutat wird gespeichert.
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
        });
        add(new HorizontalLayout(speichern, abbrechen));
    }

}
