package com.example.application.views.einstellungen;

import com.example.application.data.entity.Kategorie;
import com.example.application.data.service.KategorieService;
import com.example.application.views.components.DeleteDialog;
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

public class KategorieView extends VerticalLayout {
    private final Grid<Kategorie> gridKategorie;
    private Dialog dialogKategorie;
    private  DeleteDialog deleteDialogKategorie;
    private Kategorie draggedItem;
    private Button addKategorieButton;
    private Button removeKategorieButton;
    private KategorieService kategorieService;


    List<Kategorie> kategorieList;

    public KategorieView(){
        this.gridKategorie = new Grid<>(Kategorie.class, false);
        this.dialogKategorie = new Dialog();
    }

    public VerticalLayout createView(KategorieService kategorieService){
        this.kategorieService = kategorieService;
        this.kategorieList = new ArrayList<>(kategorieService.getKategorien());
        H3 ueberschrift = new H3("Kategorienverwaltung");
        ueberschrift.getStyle().set("margin", "0");

        configureButtonsKategorien();

        VerticalLayout verticalLayoutUeberschrift = new VerticalLayout(ueberschrift);
        VerticalLayout verticalLayoutKategorie = new VerticalLayout(configureGridKategorie(), new HorizontalLayout(addKategorieButton, removeKategorieButton));
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(verticalLayoutUeberschrift);
        verticalLayout.add(verticalLayoutKategorie);

        return verticalLayout;
    }

    private void configureButtonsKategorien(){
        this.addKategorieButton  = new Button("Hinzufügen");
        addKategorieButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addKategorieButton.addClickListener(e -> addKategorieDialog().open());

        this.removeKategorieButton = new Button("Löschen");
        removeKategorieButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeKategorieButton.addClickListener(e -> removeKategorie());
    }

    private Dialog addKategorieDialog(){
        dialogKategorie = new Dialog();
        dialogKategorie.add(new H5("Kategorie hinzufügen"));

        TextField kategorie = new TextField("Bezeichnung");
        kategorie.setRequired(true);
        kategorie.setErrorMessage("Gib eine Bezeichnung ein!");

        dialogKategorie.add(new HorizontalLayout(kategorie));

        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> dialogKategorie.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

                    if(kategorie.getValue() != null){
                        kategorieService.saveKategorie(kategorie.getValue());
                        dialogKategorie.close();
                        Notification.show("Kategorie hinzugefügt: " + kategorie.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }else{
                        Notification.show("Die Kategorie '"+ kategorie.getValue() + "' existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }

                    updateGridKategorie();
                }
        );
        dialogKategorie.add(new HorizontalLayout(speichern, abbrechen));
        return dialogKategorie;
    }

    private Grid configureGridKategorie(){

        updateGridKategorie();

        GridListDataView<Kategorie> dataView = gridKategorie.setItems(kategorieList);

        gridKategorie.setDropMode(GridDropMode.BETWEEN);
        gridKategorie.setRowsDraggable(true);
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

            for (Kategorie element: kategorieList){
                element.setSequenceNr(kategorieList.indexOf(element));
                kategorieService.updateSequenceNr(element);
            }

            dataView.refreshAll();

        });

        gridKategorie.addDragEndListener(e -> draggedItem = null);

        gridKategorie.addColumn(Kategorie::getName).setHeader("Kategorie");
        gridKategorie.addColumn(Kategorie::getSequenceNr).setHeader(" Reihenfolge");
        gridKategorie.setWidthFull();
        gridKategorie.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        return gridKategorie;
    }
    public void configureDeleteDialogKategorie(Kategorie kategorie){
        deleteDialogKategorie = new DeleteDialog("Kategorie",kategorie.getName(), "Sicher, das die Kategorie gelöscht werden soll?" );
        deleteDialogKategorie.open();

        deleteDialogKategorie.getDeleteButton().addClickListener( e -> {
            kategorieService.deleteKategorie(kategorie);
            Notification.show("Kategorie gelöscht: " + kategorie.getName()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            updateGridKategorie();
            deleteDialogKategorie.close();
        });
        deleteDialogKategorie.getCancelButton().addClickListener( e -> deleteDialogKategorie.close());
    }

    private void removeKategorie(){
        if(!gridKategorie.getSelectionModel().getSelectedItems().isEmpty()){
            Kategorie kategorie = gridKategorie.getSelectionModel().getFirstSelectedItem().get();
            configureDeleteDialogKategorie(kategorie);
            dialogKategorie.close();

            for (Kategorie element: kategorieList){
                element.setSequenceNr(kategorieList.indexOf(element));
                kategorieService.updateSequenceNr(element);
            }

            updateGridKategorie();

        }else{
            Notification.show("Löschen nicht möglich, da keine Kategorie ausgewählt wurde.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
    private void updateGridKategorie(){
        gridKategorie.setItems(kategorieService.getKategorien());}

}
