package com.example.application.views.rezeptansicht;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RezeptCard extends VerticalLayout{

    private H1 titel;
    private String titel2;
    private H1 kategorie;
    private Image image;
    private long rezeptId;
    private HorizontalLayout kategorieLayout;
    private HorizontalLayout titelLayout;
    private HorizontalLayout headingLayout;
    private HorizontalLayout imageLayout;

    public RezeptCard(String titel, String kategorie, long rezeptId, Image image) {

        this.titel = new H1(titel);
        this.titel2 = titel;
        this.kategorie = new H1(new Span(kategorie));
        this.rezeptId = rezeptId;
        this.image = image;
        this.kategorieLayout = new HorizontalLayout(this.kategorie);
        this.titelLayout = new HorizontalLayout(this.titel);
        this.headingLayout = new HorizontalLayout(titelLayout, kategorieLayout);
        imageLayout = new HorizontalLayout(image);
        addClickListener();
        createCardLayout();

    }

    public long getRezeptId() {
        return this.rezeptId;
    }

    private void createCardLayout() {
        configureRezeptCard();
        configureHeaderComponents();
        configureHeaderComponentLayout();
        configureHeaderLayout();
        configureImage();
        configureImageLayout();
        this.add(headingLayout, imageLayout);
    }

    private void configureRezeptCard (){
        this.setWidth("400px");
        this.setHeight("300px");
        this.addClassName("card-border");
        this.setId("card");
        this.setSpacing(false);
    }

    private void configureHeaderComponents(){
        kategorie.addClassName("card-category");
        kategorie.addClassNames("card-text", "card-category");
        titel.addClassName("card-text");
    }

    private void configureHeaderComponentLayout() {
        kategorieLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        kategorieLayout.setWidth("30%");
        titelLayout.setWidth("70%");
        titelLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
    }

    private void configureHeaderLayout() {
        headingLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        headingLayout.addClassName("card-heading");
        headingLayout.setPadding(false);
        headingLayout.setHeight("30%");
        headingLayout.setWidthFull();
    }

    private void configureImage() {
        image.setWidth("100%");
        image.setHeight("100%");
        image.addClassName("image");
    }

    private void configureImageLayout() {
        imageLayout.setHeight("70%");
        imageLayout.setPadding(false);
        imageLayout.setMargin(false);
        imageLayout.setWidth("100%");
    }

    private void addClickListener(){
        this.addClickListener(event -> {
            UI.getCurrent().navigate("display/" + rezeptId);
        });
    }

}
