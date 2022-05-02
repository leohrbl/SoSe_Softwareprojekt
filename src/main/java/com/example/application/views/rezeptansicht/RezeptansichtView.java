package com.example.application.views.rezeptansicht;

import com.example.application.data.service.ZutatService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Rezeptbuch")
@Route(value = "", layout = MainLayout.class)
public class RezeptansichtView extends VerticalLayout {

    private TextField searchField = new TextField();
    private Button editKategorienBtn = new Button("Kategorien bearbeiten");
    private Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
    private Button addRezeptBtn = new Button(VaadinIcon.PLUS.create());

    public RezeptansichtView() {
        configureSearchField();
        configureButtons();
        Image image = new Image("/images/doener.png", "Doener");

        RezeptCard card = new RezeptCard("Döner Kebab mit Cocktailsoße", "Fast-Food", 23, image);
        add(createViewLayout(card));

    }

    private VerticalLayout createViewLayout(RezeptCard card){
        return new VerticalLayout(new HorizontalLayout(searchField, editKategorienBtn, printDisplayedRezepteBtn, addRezeptBtn), new HorizontalLayout(card));
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

    }

    // hier sollen die Liste (Rezept) die man aus dem Backend bekommt angezeigt werden

    private void displayRezepte(){

    }


    private Icon getIcon() {
        Icon icon = VaadinIcon.CUTLERY.create();
        icon.getStyle()
                .set("width", "45px")
                .set("height", "45px")
                .set("color", "var(--lumo-primary-color)");
        return icon;
    }

    private void handleCardClick(ComponentEvent e){
        Notification.show("Card Clicked");
    }


}
