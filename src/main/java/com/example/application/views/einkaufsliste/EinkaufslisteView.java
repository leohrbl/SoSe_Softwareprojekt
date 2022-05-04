package com.example.application.views.einkaufsliste;

import com.example.application.data.service.EinheitService;
import com.example.application.data.service.EinkaufslistenService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.DeleteDialog;
import com.example.application.views.MainLayout;
import com.example.application.views.rezeptansicht.Menge;
import com.example.application.views.rezeptansicht.MengeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@PageTitle("Einkaufsliste")
@Route(value = "einkaufsliste", layout = MainLayout.class)
public class EinkaufslisteView extends VerticalLayout {

    private ZutatService zutatService;
    private EinheitService einheitService;
    private Button deleteEinkaufslisteBtn = new Button("Einkaufsliste löschen");
    private Button printEinkaufslisteBtn = new Button("Drucken", VaadinIcon.PRINT.create());
    Grid<Menge> einkaufsGrid = new Grid<Menge>(Menge.class, false);
    private H3 heading = new H3("Einkaufsliste");
    private DeleteDialog deleteDialog;
    private EinkaufslistenService einkaufslistenService;
    private MengeService mengeService;
    private List<Menge> displayedItems;

    public EinkaufslisteView(EinkaufslistenService einkaufslistenService){
        this.einkaufslistenService = einkaufslistenService;
        this.mengeService = new MengeService();
        this.displayedItems = mengeService.getMengenEinkaufsliste(einkaufslistenService.getAllEintrag());
        deleteDialog = new DeleteDialog("Einkaufsliste", "Einkaufsliste", "Sicher, dass du die Einkaufsliste löschen möchtest?");
        configureDeleteDialog();
        styleHeading();
        configureGrid();
        configureButtons();
        add(createViewLayout());

    }


    private void styleHeading(){
        heading.getStyle().set("margin", "0");
    }

    private void configureGrid(){
        einkaufsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        einkaufsGrid.addColumn(Menge::getZutat).setHeader("Zutat").setAutoWidth(true);
        einkaufsGrid.addColumn(Menge::getMenge).setHeader("Menge").setAutoWidth(true);
        einkaufsGrid.addColumn(Menge::getEinheit).setHeader("Einheit").setAutoWidth(true);
        einkaufsGrid.setWidth("50%");
        einkaufsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        loadData();

    }

    private VerticalLayout createViewLayout(){
         return new VerticalLayout(heading, einkaufsGrid ,new HorizontalLayout(printEinkaufslisteBtn, deleteEinkaufslisteBtn));
    }

    private void configureButtons() {
        printEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printEinkaufslisteBtn.addClickListener(e -> printEinkaufsliste());

        deleteEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteEinkaufslisteBtn.addClickListener(e -> deleteDialog.open());
    }

    private void loadData() {
        einkaufsGrid.setItems((displayedItems));
    }

    private void configureDeleteDialog(){
        deleteDialog.getDeleteButton().addClickListener(e -> {
            deleteEinkaufsliste();
        });
        deleteDialog.getCancelButton().addClickListener(e -> {
            deleteDialog.close();
        });
    }


    private void deleteEinkaufsliste() {

        if(displayedItems.isEmpty()){
            Notification.show("Keine Einkaufslisteneinträge vorhanden").addThemeVariants(NotificationVariant.LUMO_ERROR);
            deleteDialog.close();
            return;
        }
        String response = einkaufslistenService.deleteAll();
        if(response == "success"){
            displayedItems.clear();
            einkaufsGrid.setItems(displayedItems);
            Notification.show("Einkaufsliste gelöscht!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }else{
            Notification.show("Ein Fehler ist aufgetreten").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        deleteDialog.close();
    }

    private void printEinkaufsliste() {

        if(displayedItems.isEmpty()) {
            Notification.show("Keine Einkaufslisteneinträge vorhanden").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        List<Menge> printList = getNotSelectedItems();
        // hier dann die übergabe an den Printservice
    }

    private List<Menge> getNotSelectedItems() {

        if(displayedItems.isEmpty()) {
            Notification.show("Keine Einkaufslisteneinträge vorhanden").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return null;
        }

        List<Menge> mengenList = new LinkedList<Menge>();
        Set<Menge> gridSelectedItems = einkaufsGrid.getSelectedItems();

        for(Menge menge: gridSelectedItems) {
            if(!displayedItems.contains(menge)) {
                mengenList.add(menge);
            }
        }
        return mengenList;
    }
}
