package com.example.application.views.einstellungen;

import com.example.application.data.einheit.Einheit;
import com.example.application.data.einheit.EinheitService;
import com.example.application.views.components.DeleteDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import java.util.ArrayList;
import java.util.List;

public class EinheitView extends VerticalLayout {
    Grid<Einheit> gridEinheit = new Grid<>(Einheit.class, false);
    Dialog dialogEinheit = new Dialog();
    Button addEinheit = new Button("Hinzufügen");
    Button removeEinheit = new Button("Löschen");
    DeleteDialog deleteDialogEinheit;
    EinheitService einheitService;

    public EinheitView() {

    }

    public VerticalLayout einheitView(EinheitService einheitService) {
        this.einheitService = einheitService;
        H3 ueberschrift = new H3("Einheitenverwaltung");
        ueberschrift.getStyle().set("margin", "0");

        configureButtonsEinheit();
        VerticalLayout verticalLayoutUeberschrift = new VerticalLayout(ueberschrift);
        VerticalLayout verticalLayoutEinheit = new VerticalLayout(configureGridEinheit(),
                new HorizontalLayout(addEinheit, removeEinheit));
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(verticalLayoutUeberschrift);
        verticalLayout.add(verticalLayoutEinheit);

        return verticalLayout;
    }

    private void configureButtonsEinheit() {
        addEinheit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEinheit.addClickListener(e -> addEinheitDialog().open());

        removeEinheit.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeEinheit.addClickListener(e -> removeEinheit());
    }

    private Dialog addEinheitDialog() {
        dialogEinheit = new Dialog();
        dialogEinheit.add(new H5("Einheit hinzufügen"));

        TextField einheit = new TextField("Bezeichnung");
        einheit.setRequiredIndicatorVisible(true);
        einheit.setMaxLength(8);

        dialogEinheit.add(new HorizontalLayout(einheit));

        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e -> dialogEinheit.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e -> {
            int valueLength = einheit.getValue().replaceAll(" ", "").length();

            if(einheit.isEmpty() || valueLength == 0){
                Notification.show("Bitte geben Sie eine Einheit ein!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else if(einheitService.searchEinheitByBezeichnung(einheit.getValue()) != null){
                Notification.show("Die Einheit existiert bereits!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {
                einheitService.createEinheit(einheit.getValue());
                Notification.show("Einheit hinzugefügt: " + einheit.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialogEinheit.close();
                updateGridEinheit();
            }
        });

        dialogEinheit.add(new HorizontalLayout(speichern, abbrechen));
        return dialogEinheit;
    }

    private Grid configureGridEinheit() {
        List<Einheit> einheit = new ArrayList<>(einheitService.getEinheiten());

        gridEinheit.addColumn(Einheit::getEinheit).setHeader("Einheiten").setSortable(true);
        updateGridEinheit();

        gridEinheit.setWidthFull();
        gridEinheit.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return gridEinheit;
    }

    private void updateGridEinheit() {
        gridEinheit.setItems(einheitService.getEinheiten());
    }

    private void removeEinheit() {
        if (!gridEinheit.getSelectionModel().getSelectedItems().isEmpty()) {
            Einheit einheit = gridEinheit.getSelectionModel().getFirstSelectedItem().get();
            configuteDeleteDialogEinheit(einheit);
            dialogEinheit.close();
        } else {
            Notification.show("Löschen nicht möglich, da keine Einheit ausgewählt wurde.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public void configuteDeleteDialogEinheit(Einheit einheit) {
        deleteDialogEinheit = new DeleteDialog("Einheit", einheit.getEinheit(),
                "Sicher, das die Einheit gelöscht werden soll?");
        deleteDialogEinheit.open();
        deleteDialogEinheit.getDeleteButton().addClickListener(e -> {
            einheitService.deleteEinheit(einheit);
            Notification.show("Einheit gelöscht: " + einheit.getEinheit())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            updateGridEinheit();
            deleteDialogEinheit.close();
        });
        deleteDialogEinheit.getCancelButton().addClickListener(e -> {
            deleteDialogEinheit.close();
        });
    }
}
