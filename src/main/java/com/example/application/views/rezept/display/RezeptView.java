package com.example.application.views.rezept.display;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.service.EinkaufslistenService;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.views.DruckserviceRezept;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

/**
 * Die Klasse RezeptView ist für das erweiterte Anzeigen der Informationen eines
 * Rezeptes verantwortlich. Über einen URL-Parameter kann diese Klasse von
 * anderen Views aufgerufen werden. Der Url Parameter ist die Id des Rezeptes
 * welches angezeigt
 * werden soll. Abgesehen vom Anzeigen der Informationen zu einem Rezept kann
 * der Nutzer die Zutaten des Rezeptes für beliebige Portionen zur Einkaufsliste
 * hinzufügen. Ergänzend kann der User aus dieser View das Rezept ausdrucken.
 *
 * @author Léo Hérubel
 * @see EinkaufslistenService
 * @see DruckserviceRezept
 * @see RezeptService
 * @see RezeptZutatenService
 */
@PageTitle("Rezept")
@Route(value = "display", layout = MainLayout.class)
public class RezeptView extends ViewFrame implements HasUrlParameter<String>, HasErrorParameter<NotFoundException> {

    private static long rezeptId;
    private final RezeptService rezeptService;
    private final Grid<Rezept_Zutat> zutatMengeGrid;
    private final RezeptZutatenService rezeptZutatenService;
    private int previousPortionenInputValue;
    private final EinkaufslistenService einkaufslistenService;
    private List<Rezept_Zutat> displayedItems;
    private final IntegerField portionenInput;
    private final DruckserviceRezept druckservice;

    /**
     * Der Konstruktor initialisiert die Instanzvariablen. Zudem wird das Grid
     * konfiguriert. Die View wird aber erst in der setParameter() Methode erstellt,
     * da dort erst auf das Rezept in der Datenbank zugegriffen werden kann.
     *
     * @param rezeptService         Datenbankservice für Rezepte
     * @param rezeptZutatenService  Datenbankservice für Rezept_Zutaten
     * @param einkaufslistenService Datenbankservice für die Einkaufsliste
     */
    public RezeptView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService,
            EinkaufslistenService einkaufslistenService) {
        this.rezeptService = rezeptService;
        this.zutatMengeGrid = new Grid<>(Rezept_Zutat.class, false);
        this.einkaufslistenService = einkaufslistenService;
        this.rezeptZutatenService = rezeptZutatenService;
        this.portionenInput = new IntegerField();
        this.druckservice = DruckserviceRezept.getInstance();
        configureGrid();
    }

    /**
     * Die Methode ist der Startpunkt der View und verarbeitet den URL-Parameter.
     * Sofern der Parameter eine valide Id eines Rezeptes ist, wird die View
     * erstellt.
     *
     * @param event     Event bevor der HTML Code gerendert wird
     * @param parameter URL Parameter (RezeptId)
     */
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

    /**
     * Falls kein Parameter abgefangen wird, wird zur RezeptuebersichtView
     * navigiert.
     *
     * @param event     Event bevor der HTML Code gerendert wird
     * @param parameter URL Parameter (Keine valide Id)
     * @return gibt eine Http Response zurück, damit das Frontend weiterhin
     *         kompiliert werden kann.
     */
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo("");
        return HttpServletResponse.SC_NOT_FOUND;
    }

    /**
     * Methode zum Erstellen der gesamten View
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter geladen wurde.
     */
    private void createView(Rezept rezept) {
        configurePortionenInput(rezept);
        super.setViewHeader(createHeader(rezept));
        super.setViewContent(createContent(rezept));
        super.setViewFooter(createFooter());
        loadGridData(rezept);
    }

    /**
     * Methode zum Konfigurieren der Instanzvariable portionenInput
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter geladen wurde.
     */
    private void configurePortionenInput(Rezept rezept) {
        portionenInput.setMin(1);
        portionenInput.setValue(rezept.getPortionen());
        portionenInput.setMax(20);
        portionenInput.setHasControls(true);
        portionenInput.addValueChangeListener(e -> calcMengen());
    }

    /**
     * Methode zum Erstellen der Header-Komponente der View.
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter hervorgeht
     * @return gibt ein HorizontalLayout zum Hinzufügen in der View zurück
     */
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

    /**
     * Methode zum Setzen des Parameters previousPortionenInputValue auf die
     * Portionen vom Rezept. Die Variable wird zur Umrechnung der Mengen von Zutaten
     * auf Grundlage der Portionen genutzt.
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter hervorgeht
     */
    private void setDefaultPortionenInputValue(Rezept rezept) {
        previousPortionenInputValue = rezept.getPortionen();
    }

    /**
     * Methode zum Erstellen des Contents der View. Der Content besteht aus dem
     * Bild, den Zutaten und dem Zubereitungstext.
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter hervorgeht.
     * @return Gibt den gesamten Content als VerticalLayout zurück
     */
    private VerticalLayout createContent(Rezept rezept) {
        Label zubereitung = new Label("Zubereitung:");
        Paragraph text = new Paragraph(rezept.getZubereitung());
        Image image = createImage(rezept);
        VerticalLayout content = new VerticalLayout(image, createPortionenAndZutatenLayout(), zubereitung, text);
        content.setPadding(true);
        return content;
    }

    /**
     * Methode zum Erstellen des Footers der View. Der Footer besteht aus einem
     * Druck Button.
     *
     * @return Gibt den Footer als HorizontalLayout zurück
     */
    private HorizontalLayout createFooter() {
        HorizontalLayout footer = new HorizontalLayout(createPrintBtn());
        footer.setPadding(true);
        footer.setSpacing(true);
        return footer;
    }

    /**
     * Methode zum Erstellen des PortionenLayouts. Das Layout besitzt den
     * PortionenInput für die Umrechnung der Zutaten und dazu zwei Labels.
     *
     * @return Gibt das PortionenLayout als HorizontalLayout zurück
     */
    private HorizontalLayout createPortionenLayout() {
        HorizontalLayout portionenLayout = new HorizontalLayout();
        Paragraph portionenText1 = new Paragraph("Zutaten für ");
        Paragraph portionenText2 = new Paragraph("Portionen");
        portionenLayout.add(portionenText1, portionenInput, portionenText2);
        portionenLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return portionenLayout;
    }

    /**
     * Methode zum Erstellen des Grids für die Zutaten des Rezeptes. Das Layout
     * besteht aus dem PortionenLayout als Überschrift der Tabelle, dem Grid und aus
     * dem Button zum Hinzufügen der Zutaten in die Einkaufsliste.
     *
     * @return Gibt das PortionenAndZutatenLayout als VerticalLayout zurück
     */
    private VerticalLayout createPortionenAndZutatenLayout() {
        VerticalLayout portionenAndZutatenLayout = new VerticalLayout();
        portionenAndZutatenLayout.setWidth("100%");
        portionenAndZutatenLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        portionenAndZutatenLayout.add(createPortionenLayout(), zutatMengeGrid, createAddToEinkaufslisteBtn());
        return portionenAndZutatenLayout;
    }

    /**
     * Methode zum Konfigurieren des Zutat-Grids.
     */
    public void configureGrid() {
        zutatMengeGrid.addColumn(Rezept_Zutat::getMengeString).setHeader("Menge");
        zutatMengeGrid.addColumn(Rezept_Zutat::getEinheitFromZutat).setHeader("Einheit");
        zutatMengeGrid.addColumn(Rezept_Zutat::getZutat).setHeader("Zutat");
        zutatMengeGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        zutatMengeGrid.addClassName("rezept-view-grid");
    }

    /**
     * Methode zum Erstellen des Bildes, welches aus dem Rezept hervorgeht.
     *
     * @param rezept Rezept, welches aus dem validen URL Parametern hervorgeht.
     * @return Gibt das Bild zurück, damit es einem Layout hinzugefügt werden kann.
     */
    private Image createImage(Rezept rezept) {
        Image image = rezeptService.generateImage(rezept);
        image.setWidth("100%");
        image.setHeight("100%");
        image.addClassName("image");
        return image;
    }

    /**
     * Methode zum Erstellen des Edit Rezept Buttons, welcher bei einem Click mit
     * der rezeptId als URL Parameter zur RezeptEditView navigiert.
     *
     * @return Gibt den CreateButton zurück, damit dieser einem Layout hinzugefügt
     *         werden kann.
     */
    private Button createEditButton() {
        Button edit = new Button(new Icon(VaadinIcon.EDIT));
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        edit.addClickListener(e -> UI.getCurrent().navigate("edit/" + rezeptId));
        return edit;
    }

    /**
     * Methode zum Erstellen des CloseButtons, welcher bei einem Click zurück zur
     * RezeptuebersichtView navigiert.
     *
     * @return Gibt den EditButton zurück, damit dieser in ein Layout eingefügt
     *         werden kann.
     */
    private Button createCloseButton() {
        Button close = new Button(new Icon(VaadinIcon.CLOSE));
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addClickListener(e -> UI.getCurrent().navigate(""));
        return close;
    }

    /**
     * Es wird ein Anchor erzeugt, der in einem Button gemapped wird. Dieser Anchor
     * verweist auf das Dokument "Rezept.pdf", welches erstellt wird, wenn der
     * Button gedrückt wird.
     *
     * @return Gibt den PrintButton zurück, damit dieser in ein Layout eingefügt
     *         werden kann.
     * @author Phillip Laupichler
     */
    private Button createPrintBtn() {
        Button printDisplayedRezepteBtn = new Button(VaadinIcon.PRINT.create());
        printDisplayedRezepteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        printDisplayedRezepteBtn.addClickListener(e -> {

            StreamResource resource = generateRezept();
            if (resource == null) {
                Notification.show("PDF konnte nicht erzeugt werden!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry()
                    .registerResource(resource);
            UI.getCurrent().getPage().open(registration.getResourceUri().toString(), "Rezept drucken");
        });
        return printDisplayedRezepteBtn;

    }

    /**
     * Methode zum Erstellen des EinkaufslisteButtons.
     *
     * @return Gibt den Button zurück, damit dieser in ein Layout hinzugefügt werden
     *         kann.
     */
    private Button createAddToEinkaufslisteBtn() {
        Button addToEinkaufslisteBtn = new Button("Zur Einkaufsliste hinzufügen");
        addToEinkaufslisteBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        addToEinkaufslisteBtn.addClickListener(event -> addEintraege());
        return addToEinkaufslisteBtn;
    }

    /**
     * Nachdem der AddToEinkaufslisteBtn angeklickt wurde, wird die der
     * Datenbankservice von der Einkaufsliste aufgerufen, um die aktuell angezeigten
     * Rezept_Zutat Objekte zu der Einkaufsliste hinzuzufügen.
     */
    private void addEintraege() {
        String response = einkaufslistenService.addEintraege(displayedItems);
        if (response.equals("success")) {
            Notification.show("Zutaten wurden erfolgreich in die Einkaufsliste hinzugefügt!")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show(response).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Methode zum Erzeugen der PDF, diese wird als StreamResource zurückgegeben
     * 
     */
    private StreamResource generateRezept() {
        byte[] byteArray = druckservice.createRezeptByteArray(this.rezeptService.findById(rezeptId), displayedItems,
                portionenInput.getValue());

        StreamResource resource = new StreamResource("Rezept", new InputStreamFactory() {
            @Override
            public InputStream createInputStream() {

                return new ByteArrayInputStream(byteArray);

            }
        });
        resource.setContentType("application/pdf");
        return resource;
    }

    /**
     * Methode zum Umrechnen der Mengen von Zutaten, nachdem sich der Value vom
     * PortionenInput verändert hat.
     */
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

    /**
     * Methode zum Laden der Rezept_Zutat Objekte in das Grid.
     *
     * @param rezept Rezept, welches aus dem validen URL Parameter hervorgeht.
     */
    private void loadGridData(Rezept rezept) {
        displayedItems = rezeptZutatenService.findAllByRezept(rezept);
        zutatMengeGrid.setItems(displayedItems);
    }

    private void setRezeptID(long rezeptID) {
        rezeptId = rezeptID;
    }

    private long getRezeptID() {
        return rezeptId;
    }
}
