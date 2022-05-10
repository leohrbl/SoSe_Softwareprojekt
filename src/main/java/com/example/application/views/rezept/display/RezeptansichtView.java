package com.example.application.views.rezept.display;

import com.example.application.data.entity.Rezept;
import com.example.application.data.service.RezeptService;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.RezeptCard;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Rezeptbuch")
@Route(value = "", layout = MainLayout.class)
public class RezeptansichtView extends VerticalLayout {

    private TextField searchField = new TextField();
    private Button editKategorienBtn = new Button("Kategorien bearbeiten");
    private Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
    private Button addRezeptBtn = new Button(VaadinIcon.PLUS.create());
    private RezeptService rezeptService;

    public RezeptansichtView(RezeptService rezeptService) {
        this.rezeptService = rezeptService;
        configureSearchField();
        configureButtons();
        add(createViewLayout(loadCards()));

    }

    private VerticalLayout createViewLayout(HorizontalLayout cardLayout){
        return new VerticalLayout(new HorizontalLayout(searchField, editKategorienBtn, printDisplayedRezepteBtn, addRezeptBtn), cardLayout);
    }

    private void configureSearchField() {
        searchField.getElement().setAttribute("aria-label", "search");
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
    }

    private void configureButtons() {
        editKategorienBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        printDisplayedRezepteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRezeptBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        editKategorienBtn.addClickListener(e -> editKategorien());
        printDisplayedRezepteBtn.addClickListener(e -> printErgebnisliste());
        addRezeptBtn.addClickListener(e -> addRezept());
    }



    private void editKategorien(){

    }

    private void printErgebnisliste() {

    }

    private void addRezept(){
        UI.getCurrent().navigate("create");
    }

    private HorizontalLayout loadCards (){
        List<Rezept> rezeptList = rezeptService.getAllRezepte();
        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.setWidth("100%");
        for(Rezept rezept : rezeptList){
            RezeptCard card = new RezeptCard(rezept.getTitel(), "Indisch", rezept.getId(), rezept.getBild());
            cardLayout.add(card);
        }
        return cardLayout;
    }
}
