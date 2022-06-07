package com.example.application.views.rezept.create;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.views.components.AddZutatDialog;
import com.example.application.views.components.AddZutatRow;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.ViewFrame;
import com.example.application.views.upload.UploadBild;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Die Klasse erzeugt die View für das Hinzufügen eines Rezeptes. Ein Rezept
 * kann mit einer Kategorie,
 * einem Titel, der Portionenanzahl, mindestens einer Zutat und einer
 * Zubereitungsbeschreibung angelegt werden.
 *
 * @author Lennart Rummel & Leo Herubel
 */

@PageTitle("Rezept erstellen")
@Route(value = "create", layout = MainLayout.class)
public class RezeptCreateView extends ViewFrame {

    private final List<AddZutatRow> zutatenRows = new LinkedList<>();
    private final VerticalLayout zutatenContainer = new VerticalLayout();
    private final Button neueZeileButton = new Button("Neue Zeile");
    private final Button neueZutatButton = new Button("Neue Zutat anlegen");
    private final Button speichernButton = new Button("Speichern");
    private TextField title;
    private Select<Kategorie> kategorie;
    private IntegerField portionenInput;
    private Image image;
    private TextArea zubereitung;
    private Button upload = new Button("Bild hochladen ...");
    private VerticalLayout vLayout;
    private VerticalLayout content;
    private byte[] byteArray;
    private byte[] initialByteArray;
    private UploadBild uploader;
    private final RezeptService rezeptService;
    private final RezeptZutatenService rezeptZutatenService;
    private final ZutatService zutatService;
    private final KategorieService kategorieService;
    private final EinheitService einheitService;

    /**
     * Der Konstruktor bekommt die benötigten Serviceklassen übergeben und weist
     * diese den Instanzvariablen zu.
     * Darauffolgend werden die Methoden aufgerufen, also die Buttons konfiguriert
     * und die View erzeugt.
     *
     * @param rezeptService
     * @param rezeptZutatenService
     * @param zutatService
     * @param kategorieService
     * @param einheitService
     */
    public RezeptCreateView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService,
            ZutatService zutatService, KategorieService kategorieService, EinheitService einheitService) {
        this.rezeptService = rezeptService;
        this.rezeptZutatenService = rezeptZutatenService;
        this.zutatService = zutatService;
        this.kategorieService = kategorieService;
        this.einheitService = einheitService;

        configureButtons();
        createViewLayout();
        bildUploader();

    }

    /**
     * Die Klasse createViewLayout erzeugt die View der Rezeptansicht. Die einzelnen
     * Elemente werden hier
     * konfiguriert und dem entsprechenden Layout zugeordnet.
     */
    private void createViewLayout() {
        title = new TextField("Titel");

        kategorie = new Select<>();
        kategorie.setLabel("Kategorie");
        kategorie.setItems(kategorieService.getKategorien());

        Paragraph portionen = new Paragraph("Zutaten für ");
        Paragraph portionen2 = new Paragraph("Portionen");
        portionenInput = new IntegerField();
        portionenInput.setMin(1);
        portionenInput.setValue(4);
        portionenInput.setMax(20);
        portionenInput.setHasControls(true);

        vLayout = new VerticalLayout();
        vLayout.setWidth("100%");
        vLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout portionenLayout = new HorizontalLayout(portionen, portionenInput, portionen2);
        portionenLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        zutatenContainer.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        zutatenContainer.setWidth("100%");
        vLayout.add(portionenLayout, zutatenContainer);

        HorizontalLayout header = new HorizontalLayout(kategorie, title);
        header.setAlignItems(Alignment.BASELINE);
        header.setFlexGrow(1, title);
        header.setPadding(true);
        header.setSpacing(true);

        setViewHeader(header);
        // Content
        zubereitung = new TextArea("Zubereitung");
        zubereitung.setWidthFull();

        image = new Image("images/image-placeholder.png", "Placeholder");
        image.setWidth("100%");
        image.setHeight("100%");
        image.addClassName("image");
        try {
            initialByteArray = RezeptService
                    .getBytesFromFile("src/main/resources/META-INF/resources/images/image-placeholder.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //

        HorizontalLayout btnLayout = new HorizontalLayout(neueZeileButton, neueZutatButton);

        // hier dann auch noch das grid mit den Zutaten/Mengen Objekten zu dem Rezept
        // lul
        content = new VerticalLayout(this.image, vLayout, zutatenContainer, btnLayout, zubereitung);
        content.setPadding(true);

        setViewContent(content);

        // Footer

        HorizontalLayout footer = new HorizontalLayout(speichernButton);
        speichernButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        footer.setPadding(true);
        footer.setSpacing(true);

        setViewFooter(footer);

        addZutatZeile();

    }

    public void setBytes(byte[] bytes) {
        this.byteArray = bytes;
    }

    /**
     * Die Methode addZutatZeile fügt eine neue Zeile zum Hinzufügen von Zutaten mit
     * Menge und Einheit hinzu.
     * Das Erzeugen der einzelnen Vaadin Komponenten ist in der Klasse AddZutatRow
     * ausgelagert.
     * Daher werden Objekte der Klasse AddZutatRow erzeugt und dem Layout
     * hinzugefügt.
     *
     * @see AddZutatRow
     */
    public void addZutatZeile() {
        HorizontalLayout rowLayout = new HorizontalLayout();
        AddZutatRow row = new AddZutatRow(zutatService);
        zutatenRows.add(row);
        rowLayout.add(row, createDeleteRowButton());
        rowLayout.setAlignItems(Alignment.BASELINE);
        zutatenContainer.add(rowLayout);
    }

    /**
     * Die Methode createDeleteRowButton erzeugt einen Button,
     * mit dem die einzelnen AddZutatRow Elemente aus dem Layout entfernt werden
     * können.
     * Dazu mehr in der Methode deleteZutatRow.
     *
     * @return Der erzeugte Löschen-Button wird zurückgegeben.
     */
    private Button createDeleteRowButton() {
        Button delRowButton = new Button(VaadinIcon.MINUS.create());
        delRowButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delRowButton.addClickListener(e -> {
            deleteZutatRow(delRowButton);

        });
        return delRowButton;
    }

    /**
     * Die Methode ermöglicht das Löschen der Zutat Zeile, die mithilfe der Klasse
     * AddZutatRow erzeugt wird.
     * Dazu muss das darüberstehende Layout gelöscht werden (Parent), da der
     * Löschen-Button der Zeile ebenfalls in
     * dem Layout enthalten ist und auch gelöscht werden soll.
     *
     * @param delRowButton
     */
    private void deleteZutatRow(Button delRowButton) {
        Component parent = delRowButton.getParent().get();
        List<Component> componentList = parent.getChildren().collect(Collectors.toList());
        // Löschen der AddZutatRow Instanz in der Liste zutatenRows
        if (isNotLastRow() && isNotMaxRow()) {
            for (Component component : componentList) {
                if (component instanceof AddZutatRow) {
                    AddZutatRow rowToDelete = null;
                    for (AddZutatRow row : zutatenRows) {
                        if (component == row) {
                            rowToDelete = row;
                        }
                    }
                    if (rowToDelete != null) {
                        zutatenRows.remove(rowToDelete);
                    }
                }
            }
            zutatenContainer.remove(parent);
        }
    }

    /**
     * Die Buttons "Neue Zeile", "Neue Zutat" und "Soeichern" werden mit dem
     * Clicklistener konfiguriert.
     */
    public void configureButtons() {
        neueZeileButton.addClickListener(e -> {
            addZutatZeile();
        });
        neueZutatButton.addClickListener(e -> {
            AddZutatDialog addZutatDialog = new AddZutatDialog(einheitService, zutatService);
            addZutatDialog.open();
        });
        speichernButton.addClickListener(e -> {
            rezeptSpeichern();
        });
    }

    /**
     * In dieser Methode wird das Rezept gespeichert.
     * Dazu werden das Bild, der Titel, die Zubereitung, die Portionenanzahl und die
     * Kategorie.
     * Die Objekte der Klasse AddZutatRow, die in der Liste zutatenRows
     * zwischengespeichert sind, werden iteriert
     * und anschließend ebenfalls dem Rezept zugeordnet.
     * Sollte das Rezept bereits existieren wird abgebrochen.
     *
     */
    public void rezeptSpeichern() {
        if (isValuesValid()) {
            try {
                if (byteArray == null) {
                    byteArray = initialByteArray;
                }
                Rezept rezept = rezeptService.createRezept(byteArray, title.getValue(),
                        zubereitung.getValue(),
                        portionenInput.getValue(), kategorie.getValue());
                if (rezept == null) {
                    Notification.show("Rezept '" + title.getValue() + "' gibt es schon")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
                for (AddZutatRow row : zutatenRows) {
                    rezeptZutatenService.createRezeptZutatenAndAddToSet(rezept, row.getZutat(),
                            row.getMenge());
                }
                Notification.show("Rezept '" + title.getValue() + "' gespeichert")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                clearAll();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Die Methode prüft, ob die Werte gespeichert werden können.
     * Die Kategorie, der Titel, die Portionenanzahl und die Zubereitung dürfen
     * nicht leer sein.
     * Des Weiteren müssen die Werte der ZutatRows geprüft werden, damit diese nicht
     * leer sind
     * und nicht mehrfach auftreten.
     *
     * @return Es wird der Wahrheitswert zurückgegeben, wenn die Daten nicht so
     *         vorliegen,
     *         dass diese gespeichert werden können.
     */
    // TODO: Individuelle Fehlermeldungen
    private boolean isValuesValid() {
        if (kategorie.isEmpty() || !titleValid() || portionenInput.isEmpty() || zubereitung.isEmpty()
                || !isZutatRowsValid() || checkDuplicateZutaten()) {
            Notification.show(getErrorString()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Es wird geprüft, ob die Eingaben in der Zutatenzeile valide sind.
     *
     * @return Es wird der Wahrheitswert zurückgegeben, ob die Eingaben in der
     *         ZutatZeile valide sind.
     */
    private boolean isZutatRowsValid() {
        for (AddZutatRow row : zutatenRows) {
            if (!row.isFilled()) {
                return false;
            }
            if (row.getMenge() < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Diese Methode ist für das Löschen der Zutatzeilen relevant, da hier geprüft
     * wird,
     * ob es sich nicht um die letzte Zeile handelt.
     *
     * @return Es wird der Wahrheitswert zurückgegeben, ob es sich nicht um die
     *         letzte Zeile handelt.
     */
    private boolean isNotLastRow() {
        if (zutatenRows.size() > 1) {
            return true;
        } else {
            Notification.show("Bitte geben Sie mindestens eine Zutat ein!")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
    }

    /**
     * Die Methode prüft, ob das Limit der Zutaten für ein Rezept bereits erreicht
     * ist.
     *
     * @return Gibt den Wahrheitswert zurück, ob die maximale Anzahl an Zutaten für
     *         ein Rezept bereits vorliegt.
     */

    private boolean isNotMaxRow() {
        if (zutatenRows.size() <= 30) {
            return true;
        } else {
            Notification.show("Sie können nicht mehr als 100 Zutaten zu einem Rezept speichern")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
    }

    /**
     * In dieser Methode wird geprüft, ob in den Zutatenzeile Duplikate vorliegen.
     * Da eine Zutat in einem Rezept maximal ein mal vorkommen darf.
     *
     * @return Gibt den Wahrheitswert zurück, ob Duplikate vorhanden sind.
     */
    private boolean checkDuplicateZutaten() {
        List<String> zutatNameList = new LinkedList<>();
        for (AddZutatRow row : zutatenRows) {
            if (row.isFilled()) {
                zutatNameList.add(row.getZutat().getName());
            }
        }
        Set<String> zutatNameSet = new HashSet<>(zutatNameList);
        return zutatNameSet.size() < zutatNameList.size();
    }

    /**
     * Alle Listen und Felder werden geleert und anschließend eine neue ZutatenZeile
     * hinzugefügt.
     */
    private void clearAll() {
        kategorie.clear();
        title.clear();
        portionenInput.clear();
        zutatenContainer.removeAll();
        zubereitung.clear();
        zutatenRows.clear();
        addZutatZeile();
        clearUpload();

    }

    private void clearUpload() {
        uploader.clear(this.initialByteArray);
        this.byteArray = initialByteArray;
    }

    /**
     * Die Methode erzeugt einen String und gibt diesen zurück. Der String enthält
     * welche Eingabefelder vom
     * Anwender geprüft werden sollen.
     *
     * @return Es wird ein String zurückgegeben, der enthält welche Eingabefelder
     *         gepüft werden müssen.
     */

    private String getErrorString() {
        String error = "Bitte prüfe: ";

        if (kategorie.isEmpty()) {
            error += " [Kategorie]";
        }
        if (!titleValid()) {
            error += " [Titel]";
        }
        if (portionenInput.isEmpty()) {
            error += " [Portionen]";
        }
        if (zubereitung.isEmpty()) {
            error += " [Zubereitung]";
        }

        if (!isZutatRowsValid()) {
            error += " [Zutaten-Zeile nicht valide]";
        }

        if (checkDuplicateZutaten()) {
            error += " [Zutaten Duplikate vorhanden]";
        }

        return error;
    }

    /**
     * @author Anna Karle
     */

    private void bildUploader() {

        uploader = new UploadBild(this, upload, image, initialByteArray);
        vLayout.add(uploader);

    }

    /**
     * Die Methode prüft, ob der Titel valide ist. Der Titel darf nicht leer und
     * auch nicht nur Leerzeichen enthalten.
     * 
     * @return Der Wahrheitswert, ob der Titel valide ist.
     */
    public boolean titleValid() {
        if (title.isEmpty() || title.getValue().trim().length() == 0) {
            return false;
        }
        return true;
    }

}
