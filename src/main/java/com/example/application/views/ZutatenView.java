package com.example.application.views;

import com.example.application.data.entity.Einheit;
import com.example.application.data.entity.Zutat;
import com.example.application.data.repository.ZutatRepository;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.ClickEvent;
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
 * Diese Klasse erzeugt die Zutatenmanager-View.
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
    ZutatService zutatService;
    EinheitService einheitService;


    /**
     *
     * @param zutatService Übergibt
     * @param einheitService
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


    public void configureButtons(){
        addZutat.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addZutat.addClickListener(e -> addZutatDialog().open());

        removeZutat.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeZutat.addClickListener(e -> removeZutaten());
    }


    private Dialog addZutatDialog(){
        dialog = new Dialog();
        dialog.add(new H5("Zutat hinzufügen"));

        TextField bezeichnung = new TextField("Bezeichnung");
        bezeichnung.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung ein!");

        ComboBox<Einheit> einheitAuswahl = new ComboBox<>();
        einheitAuswahl.setLabel("Einheit");
        einheitAuswahl.setPlaceholder("Einheit auswählen");
        einheitAuswahl.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Einheit ein!");

        einheitAuswahl.setItems(einheitService.getEinheiten()); //TODO: Hier wird eine Collection übergeben

        dialog.add(new HorizontalLayout(bezeichnung, einheitAuswahl));


        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> dialog.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

            //TODO: Neue Zutat wird gespeichert!!!!
            if(bezeichnung.getValue() != null && einheitAuswahl.getValue() != null){
                dialog.close();
                zutatService.saveZutat(bezeichnung.getValue(), einheitAuswahl.getValue());
                Notification.show("Zutat hinzugefügt: " + bezeichnung.getValue() +" in "+ einheitAuswahl.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }else{
                Notification.show("Keine Zutat hinzugefügt. Es muss eine Bezeichnung und eine Einheit angegeben werden.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }



            updateGrid();
        });
        dialog.add(new HorizontalLayout(speichern, abbrechen));
        return dialog;
    }

    public Grid configureGrid(){
       // grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Zutat::getName).setHeader("Bezeichnung");
        grid.addColumn(Zutat::getEinheit).setHeader("Einheit");

        updateGrid();
        // Styling
        grid.setWidth("50%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return grid;
    }


    private void updateGrid(){
        grid.setItems(zutatService.getZutaten());
    }

    private void removeZutaten(){
        if(grid.getSelectionModel().getSelectedItems().isEmpty() == false){ //TODO: Abfragen ob Auswahl
            Zutat zutat = grid.getSelectionModel().getFirstSelectedItem().get();
          //  deleteDialog(zutat).open();
            zutatService.deleteZutat(zutat);
            Notification.show("Zutat gelöscht: " + zutat.getName() + " in "+zutat.getEinheit().toString()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            updateGrid();
        }else{
            Notification.show("Löschen nicht möglich, da keine Zutat ausgewählt wurde.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private Dialog deleteDialog(Zutat zutat){
        boolean isConfirmed = false;
        Dialog delDialog = new Dialog();


        Label infoText = new Label("Wirklich löschen?");

        Button deleteButton = new Button("Löschen");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Abbrechen", e -> {
            delDialog.close();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        delDialog.add(new VerticalLayout(new H5("Zutat '"+zutat.getName()+"' löschen?"),infoText, new HorizontalLayout(deleteButton, cancelButton)));


        return delDialog;
    }
}
