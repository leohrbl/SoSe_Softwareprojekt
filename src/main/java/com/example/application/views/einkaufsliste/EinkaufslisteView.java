package com.example.application.views.einkaufsliste;

import com.example.application.data.entity.Zutat;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Einkaufsliste")
@Route(value = "/einkaufsliste", layout = MainLayout.class)
public class EinkaufslisteView extends VerticalLayout {

    private ZutatService zutatService;
    private EinheitService einheitService;
    private Button deleteEinkaufslisteBtn = new Button("Einkaufsliste löschen");
    private Button printEinkaufslisteBtn = new Button("Drucken", VaadinIcon.PRINT.create());
    //Grid<Object> zutatGrid = new Grid<Object>(Object.class, false);
    private H3 heading = new H3("Einkaufsliste");

    public EinkaufslisteView(ZutatService zutatService, EinheitService einheitService){
        this.zutatService = zutatService;
        this.einheitService = einheitService;
        styleHeading();
        //configureGrid();
        configureButtons();
        add(createViewLayout());

    }


    private void styleHeading(){
        heading.getStyle().set("margin", "0");
    }

    private void configureGrid(){
        /*
        zutatGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        zutatGrid.addColumn("name").setHeader("Name").setAutoWidth(true);
        zutatGrid.addColumn("einheit").setHeader("Einheit").setAutoWidth(true);
        zutatGrid.addColumn("menge").setHeader("Menge").setAutoWidth(true);
        zutatGrid.setWidth("50%");
        zutatGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
         */
    }

    private VerticalLayout createViewLayout(){
         return new VerticalLayout(heading, new HorizontalLayout(printEinkaufslisteBtn, deleteEinkaufslisteBtn));
    }

    private void configureButtons() {
        printEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printEinkaufslisteBtn.addClickListener(e -> printEinkaufsliste());

        deleteEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteEinkaufslisteBtn.addClickListener(e -> deleteEinkaufsliste());
    }

    private void updateGrid(){
        //zutatGrid.setItems(zutatService.getZutaten());
    }

    private void deleteEinkaufsliste() {

    }

    private void printEinkaufsliste(){

    }
}
