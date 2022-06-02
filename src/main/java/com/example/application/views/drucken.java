package com.example.application.views;

import java.util.Set;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.service.RezeptService;
import com.example.application.views.components.ViewFrame;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Drucken")
@Route(value = "print")
public class drucken extends ViewFrame implements HasUrlParameter<String> {

    Grid<Rezept> gridRezept = new Grid<>(Rezept.class, false);
    private Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
    RezeptService service;

    public drucken(RezeptService service) {
        this.service = service;
    }

    private void createFrame(Rezept rezept) {
        /*
         * // A user interface for a (trivial) data model from which
         * // the PDF is generated.
         * final TextField name = new TextField("Name");
         * name.setValue("Slartibartfast");
         * 
         * // This has to be clicked first to create the stream resource
         * final Button ok = new Button("OK");
         * 
         * // This actually opens the stream resource
         * final Button print = new Button("Open PDF");
         * print.setEnabled(false);
         * 
         * ok.addClickListener((ClickEvent<Button> ClickEvent) -> {
         * StreamSource source = new MyPdfSource((String) name.getValue());
         * 
         * // Create the stream resource and give it a file name
         * String filename = "pdf_printing_example.pdf";
         * StreamResource resource = new StreamResource(source, filename);
         * 
         * // These settings are not usually necessary. MIME type
         * // is detected automatically from the file name, but
         * // setting it explicitly may be necessary if the file
         * // suffix is not ".pdf".
         * resource.setMIMEType("application/pdf");
         * resource.getStream().setParameter(
         * "Content-Disposition",
         * "attachment; filename=" + filename);
         * 
         * // Extend the print button with an opener
         * // for the PDF resource
         * BrowserWindowOpener opener = new BrowserWindowOpener(resource);
         * opener.extend(print);
         * 
         * name.setEnabled(false);
         * ok.setEnabled(false);
         * print.setEnabled(true);
         * 
         * });
         * 
         * layout.addComponent(name);
         * layout.addComponent(ok);
         * layout.addComponent(print);
         */
        HorizontalLayout mainLayout = new HorizontalLayout();
        // Zwei Horizontale Layout
        VerticalLayout verticalLayout = new VerticalLayout();

        H1 title = new H1(rezept.getTitel());
        Label kategorie = new Label("Fast-Food");
        kategorie.getStyle().set("font-family", "Arial");

        Paragraph textare = new Paragraph(rezept.getZubereitung());
        verticalLayout.add(title, kategorie, textare);

        VerticalLayout verticalLayout2 = new VerticalLayout();

        // Image image = rezept.getBild();
        // image.setMaxWidth("100%");
        // image.setMaxHeight("50%");
        // image.getStyle().set("border-radius", "10px");
        Paragraph portionen = new Paragraph("Zutaten für " + rezept.getPortionen() + " Portionen:");
        portionen.getStyle().set("font-weigth", "bold");
        Grid<Rezept_Zutat> grid = new Grid<>(Rezept_Zutat.class, false);
        Set<Rezept_Zutat> rezept_zutatSet = getRezept(rezept);
        grid.addColumn(Rezept_Zutat::getMenge).setHeader("Menge");
        grid.addColumn(Rezept_Zutat::getEinheitFromZutat).setHeader("Einheit");
        grid.addColumn(Rezept_Zutat::getZutat).setHeader("Zutat");
        grid.getStyle().set("border-style", "none");
        grid.setItems(rezept_zutatSet);

        // verticalLayout2.add(image, portionen, grid);

        mainLayout.add(verticalLayout, verticalLayout2);
        mainLayout.setPadding(true);

        setViewContent(mainLayout);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(printDisplayedRezepteBtn);
        printDisplayedRezepteBtn.addClickListener((ClickEvent<Button> ClickEvent) -> {
            UI.getCurrent().getPage().executeJs("print();");

        });
        setViewHeader(headerLayout);
    }

    private Set<Rezept_Zutat> getRezept(Rezept rezept) {
        return rezept.getZutatenFromRezept_Zutaten();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {

            Rezept rezept = service.findById(Long.parseLong(parameter));
            createFrame(rezept);

        } catch (Exception e) {
            event.rerouteTo("");
        }

    }
}
