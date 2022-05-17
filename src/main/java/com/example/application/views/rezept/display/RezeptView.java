package com.example.application.views.rezept.display;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.service.EinkaufslistenService;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.ViewFrame;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@SuppressWarnings("unused")
@PageTitle("Rezept")
@Route(value = "display", layout = MainLayout.class)
public class RezeptView extends ViewFrame implements HasUrlParameter<String>, HasErrorParameter<NotFoundException> {

    private static long rezeptId;
    private RezeptService rezeptService;
    private Grid<Rezept_Zutat> zutatMengeGrid;
    private RezeptZutatenService rezeptZutatenService;
    private int previousPortionenInputValue;
    private EinkaufslistenService einkaufslistenService;
    private List<Rezept_Zutat> displayedItems;
    private IntegerField portionenInput = new IntegerField();


    public RezeptView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService, EinkaufslistenService einkaufslistenService) {
        this.rezeptService = rezeptService;
        this.zutatMengeGrid = new Grid<>(Rezept_Zutat.class, false);
        this.einkaufslistenService = einkaufslistenService;
        this.rezeptZutatenService = rezeptZutatenService;
        configureGrid();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            setRezeptID(Long.parseLong(parameter));
            Rezept rezept = rezeptService.findById(getRezeptID());
            createView(rezept);
        } catch (Exception e) {
            event.rerouteTo("");
        }
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo("");
        return HttpServletResponse.SC_NOT_FOUND;
    }

    private void createView(Rezept rezept) {
        configurePortionenInput(rezept);
        super.setViewHeader(createHeader(rezept));
        super.setViewContent(createContent(rezept));
        super.setViewFooter(createFooter());
        loadGridData(rezept);
    }

    private void configurePortionenInput(Rezept rezept) {
        portionenInput.setMin(1);
        portionenInput.setValue(rezept.getPortionen());
        portionenInput.setMax(20);
        portionenInput.setHasControls(true);
        portionenInput.addValueChangeListener(e -> calcMengen());
    }

    private HorizontalLayout createHeader(Rezept rezept) {
        setDefaultPortionenInputValue(rezept);
        Label title = new Label(rezept.getTitel());
        Label kategorie = new Label(rezept.getKategorie().getName());
        kategorie.setClassName("rezept-kategorie-view");
        HorizontalLayout header = new HorizontalLayout(kategorie, title, createEditButton(), createCloseButton());
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1, title);
        header.setPadding(true);
        header.setSpacing(true);
        return header;
    }

    private void setDefaultPortionenInputValue(Rezept rezept) {
        previousPortionenInputValue = rezept.getPortionen();
    }

    private VerticalLayout createContent(Rezept rezept) {
        Label zubereitung = new Label("Zubereitung:");
        Paragraph text = new Paragraph(rezept.getZubereitung());
        Image image = createImage(rezept);
        VerticalLayout content = new VerticalLayout(image, createPortionenAndZutatenLayout(), zubereitung, text);
        content.setPadding(true);
        return content;
    }

    private HorizontalLayout createFooter() {
        HorizontalLayout footer = new HorizontalLayout(createPrintBtn());
        footer.setPadding(true);
        footer.setSpacing(true);
        return footer;
    }

    private HorizontalLayout createPortionenLayout() {
        HorizontalLayout portionenLayout = new HorizontalLayout();
        Paragraph portionenText1 = new Paragraph("Zutaten für ");
        Paragraph portionenText2 = new Paragraph("Portionen");
        portionenLayout.add(portionenText1, portionenInput, portionenText2);
        portionenLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return portionenLayout;
    }

    private VerticalLayout createPortionenAndZutatenLayout() {
        VerticalLayout portionenAndZutatenLayout = new VerticalLayout();
        portionenAndZutatenLayout.setWidth("100%");
        portionenAndZutatenLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        portionenAndZutatenLayout.add(createPortionenLayout(), zutatMengeGrid, createAddToEinkaufslisteBtn());
        return portionenAndZutatenLayout;
    }

    public void configureGrid() {
        zutatMengeGrid.addColumn(Rezept_Zutat::getMenge).setHeader("Menge");
        zutatMengeGrid.addColumn(Rezept_Zutat::getEinheitFromZutat).setHeader("Einheit");
        zutatMengeGrid.addColumn(Rezept_Zutat::getEinheitFromZutat).setHeader("Zutat");
        zutatMengeGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        zutatMengeGrid.addClassName("rezept-view-grid");
    }

    private Image createImage(Rezept rezept) {
        Image image = rezept.getBild();
        image.setWidth("100%");
        image.setHeight("100%");
        image.addClassName("image");
        return image;
    }

    private Button createEditButton() {
        Button edit = new Button(new Icon(VaadinIcon.EDIT));
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        edit.addClickListener(e -> UI.getCurrent().navigate("edit/" + rezeptId));
        return edit;
    }

    private Button createCloseButton() {
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addClickListener(e -> UI.getCurrent().navigate(""));
        return close;
    }

    private Button createPrintBtn() {
        Button printBtn = new Button("Drucken", VaadinIcon.PRINT.create());
        printBtn.addClickListener(e -> printRezept());
        return printBtn;
    }

    private Button createAddToEinkaufslisteBtn() {
        Button addToEinkaufslisteBtn = new Button("Zur Einkaufsliste hinzufügen");
        addToEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        addToEinkaufslisteBtn.addClickListener(event -> addEintraege());
        return addToEinkaufslisteBtn;
    }

    private void addEintraege() {
        String response = einkaufslistenService.addEintraege(displayedItems);
        if (response.equals("success")) {
            Notification.show("Zutaten wurden erfolgreich in die Einkaufsliste hinzugefügt!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show(response).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void printRezept() {
        UI.getCurrent().navigate("/print/" + rezeptId);
    }

    private void calcMengen() {
        if (displayedItems.isEmpty()) {
            return;
        }
        int currentPortionen = portionenInput.getValue();

        for (Rezept_Zutat zutat : displayedItems) {
            double currentMenge = zutat.getMenge();
            double newMenge = (currentMenge / previousPortionenInputValue) * currentPortionen;
            zutat.setMenge(newMenge);
        }
        zutatMengeGrid.setItems(displayedItems);
        previousPortionenInputValue = currentPortionen;
    }

    private void loadGridData(Rezept rezept) {
        displayedItems = rezeptZutatenService.findAllByRezept(rezept);
        zutatMengeGrid.setItems(displayedItems);
    }

    private void setRezeptID(long rezeptID) {
        this.rezeptId = rezeptID;
    }

    private long getRezeptID() {
        return rezeptId;
    }
}
