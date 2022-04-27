package com.example.application.views;

import com.example.application.data.entity.Zutat;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.LinkedList;
import java.util.List;

@PageTitle("Rezeptbuch")
@Route(value = "", layout = MainLayout.class)
public class RezeptansichtView extends VerticalLayout {


    public RezeptansichtView(ZutatService service) {
        add(new H1("Das hier ist die Rezeptansicht"));
        setSizeFull();

        List<Zutat> list = new LinkedList<Zutat>(service.getZutaten());

        add(new H1(list.get(0).getName()));

        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
