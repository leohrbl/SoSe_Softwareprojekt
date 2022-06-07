package com.example.application.views.rezept.edit;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.views.components.DeleteDialog;
import com.example.application.views.components.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import javax.swing.JFileChooser;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import com.example.application.views.components.ViewFrame;
import com.example.application.views.upload.UploadBild;
import com.example.application.views.components.AddZutatDialog;
import com.example.application.views.components.DeleteDialog;

/**
 * @author Joscha Cerny
 *         die Klasse erstellt den View zum Bearbeiten eines Rezeptes.
 *         Sie lädt die bestehden Daten des ausgewählten rezeptes und lässt
 *         diese verändern sowie entfernen und Hinzufügen.
 */
@PageTitle("Rezept bearbeiten")
@Route(value = "edit", layout = MainLayout.class)
public class RezeptEditView extends ViewFrame implements HasUrlParameter<String> {

    private static long rezeptId;
    Button fileUploadButton = new Button("Bild hochladen");
    Button zutatHinzufuegenButton = new Button("Weitere Zutat anlegen");
    Button saveButton = new Button("Speichern");
    Button cancelButton = new Button("Abbrechen");
    Button neueZutatButton = new Button("Neue Zutat erstellen");
    Button rezeptDeleteButton = new Button("Rezept löschen");
    RezeptService rezeptService;
    private ZutatService zutatservice;
    private RezeptZutatenService rezeptZutatenService;
    private EinheitService einheitService;
    private KategorieService kategorieService;
    TextField rezeptNameTextField;
    TextArea zubereitungTextArea;
    IntegerField inputPortionenIntegerField;
    Image rezeptBild;
    TextField[] rezeptZutatMengeTextField = new TextField[40];
    TextField[] rezeptZutatEinheitTextField = new TextField[40];
    ComboBox[] rezeptZutatNameComboBox = new ComboBox[40];
    Button[] deleteButton = new Button[40];
    Integer identifierTextFieldCounter = 0;
    List<String> remainingMengenWhenSavedList = new ArrayList<String>(); // Sobald gespeichert wird werden alle
                                                                         // verbleibenden Mengen der Zutaten in dieser
                                                                         // Liste gespeichert
    List<String> remainingNamenWhenSavedList = new ArrayList<String>(); // Sobald gespeichert wird werden alle
                                                                        // verbleibenden Namen der Zutaten in dieser
                                                                        // Liste gespeichert
    List<String> remainingEinheitenWhenSavedList = new ArrayList<String>(); // Sobald gespeichert wird werden alle
                                                                            // verbleibenden Einheiten der Zutaten in
                                                                            // dieser Liste gespeichert
    Dialog createNewZutatDialog = new Dialog(); // Dialog zum Erstellen neuer Zutaten
    VerticalLayout contentRezeptZutaten = new VerticalLayout();
    ComboBox<Kategorie> auswahlKategorieComboBox = new ComboBox<>();
    VerticalLayout contentBild;
    UploadBild uploader;
    byte[] byteArray;
    byte[] initialByteArray;

    /**
     * Konstruktor des EditViews, hier werden alle benötigten Services Initiert
     */
    public RezeptEditView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService,
            EinheitService einheitService, ZutatService zutatservice, KategorieService kategorieService) {
        this.rezeptService = rezeptService;
        this.zutatservice = zutatservice;
        this.rezeptZutatenService = rezeptZutatenService;
        this.einheitService = einheitService;
        this.kategorieService = kategorieService;

    }

    /**
     * Die Methode nimmt den Parameter aus der Url entgegen damit der View weiß
     * welches Rezept bearbeitet werden soll
     * Er setzt den Parameter in die RezeptID und führt die Methode createView und
     * Configure Buttons Inital aus
     */
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            setRezeptID(Long.parseLong(parameter));
            Rezept rezept = rezeptService.findById(getRezeptID());
            createView(rezept);
            configureButtons(rezept);
            fileUpload();
        } catch (Exception e) {
            event.rerouteTo("");
        }
    }

    /**
     * setter für die RezeptID
     */
    private void setRezeptID(long rezeptID) {
        this.rezeptId = rezeptID;
    }

    /**
     * getter für die RezeptID
     */
    private long getRezeptID() {
        return rezeptId;
    }

    /**
     * Die Methode ruft das Initiale Layout des Views auf und ruft ebenfalls die
     * InitalWerte des Ausgewöhlten rezepts aus
     */
    public void createView(Rezept rezept) {
        configureInitialLayout(rezept);
        createList(rezept);

    }

    /**
     * In dieser Methode wird das Initiale Layout des EditViews gebaut und geladen.
     */
    public void configureInitialLayout(Rezept rezept) {

        // HEADER
        // Seitenname
        H3 seitenName = new H3("Rezept bearbeiten");
        seitenName.setWidth("100%");
        seitenName.getElement().getStyle().set("color", "blue");

        // Exit Button
        Button exitButton = new Button(new Icon(VaadinIcon.CLOSE));
        exitButton.addClickListener(e -> cancel());
        exitButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout header = new HorizontalLayout(seitenName, exitButton);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        // Layouts Einfügen Header
        setViewHeader(header);

        // CONTENT
        // Rezept Name und Kategorie + filler
        rezeptNameTextField = new TextField("RezeptName", rezept.getTitel(), "");
        rezeptNameTextField.setWidth("40%");

        auswahlKategorieComboBox.setLabel("Kategorie");
        auswahlKategorieComboBox.setItems(kategorieService.getKategorien());
        auswahlKategorieComboBox.setValue(rezept.getKategorie());
        auswahlKategorieComboBox.setAllowCustomValue(false);

        VerticalLayout textFieldLayout = new VerticalLayout(rezeptNameTextField);
        textFieldLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        textFieldLayout.setPadding(true);

        VerticalLayout auswahlFieldLayout = new VerticalLayout(auswahlKategorieComboBox);
        auswahlFieldLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        auswahlFieldLayout.setPadding(true);

        // Bild und Hochladen Button
        rezeptBild = rezeptService.generateImage(rezept);
        rezeptBild.setWidth("60%");
        rezeptBild.setHeight("60%");
        rezeptBild.addClassName("image");
        initialByteArray = rezept.getBild();

        contentBild = new VerticalLayout(rezeptBild, fileUploadButton);
        contentBild.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        contentBild.setPadding(true);

        // Portionen
        Paragraph portionenText1 = new Paragraph("Zutaten für ");
        Paragraph portionenText2 = new Paragraph("Portionen");
        inputPortionenIntegerField = new IntegerField();
        inputPortionenIntegerField.setMin(1);
        inputPortionenIntegerField.setValue(rezept.getPortionen());
        inputPortionenIntegerField.setMax(20);
        inputPortionenIntegerField.setHasControls(true);

        HorizontalLayout contentPortionHorizontal = new HorizontalLayout(portionenText1, inputPortionenIntegerField,
                portionenText2);
        VerticalLayout contentPortionVertical = new VerticalLayout(contentPortionHorizontal);
        contentPortionVertical.setAlignItems(Alignment.CENTER);

        // ZutatenListe
        neueZutatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout zutatenListeLayout = new HorizontalLayout(contentRezeptZutaten);
        VerticalLayout zutatenListeLayoutVertical = new VerticalLayout(zutatenListeLayout);
        zutatenListeLayoutVertical.setAlignItems(Alignment.CENTER);

        HorizontalLayout zutatButtonsLayout = new HorizontalLayout(zutatHinzufuegenButton, neueZutatButton);
        VerticalLayout zutatButtonsLayoutVertical = new VerticalLayout(zutatButtonsLayout);
        zutatButtonsLayoutVertical.setAlignItems(Alignment.CENTER);

        // TextArea
        String zubereitungString = rezept.getZubereitung();
        zubereitungTextArea = new TextArea("Zubereitung", zubereitungString, "");
        zubereitungTextArea.setWidth("70%");
        zubereitungTextArea.setHeight("40%");
        VerticalLayout textAreaLayout = new VerticalLayout(zubereitungTextArea);
        textAreaLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        // Layouts Einfügen Content
        setViewContent(textFieldLayout, auswahlFieldLayout, contentBild, contentPortionVertical,
                zutatenListeLayoutVertical, zutatButtonsLayoutVertical, textAreaLayout);

        // FOOTER
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        rezeptDeleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout contentFooter = new HorizontalLayout(cancelButton, saveButton, rezeptDeleteButton);
        VerticalLayout contentFooterVertical = new VerticalLayout(contentFooter);
        contentFooterVertical.setAlignItems(Alignment.CENTER);

        // Layouts Einfügen Footer
        setViewFooter(contentFooterVertical);
    }

    /**
     * KDie Methode gibt allen Buttons ihre Funktion und wird am Anfang aufgerufen
     */
    private void configureButtons(Rezept rezept) {

        zutatHinzufuegenButton.addClickListener(event -> createNewZutat(rezept));
        saveButton.addClickListener(event -> save(rezept));
        cancelButton.addClickListener(event -> cancel());
        neueZutatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        neueZutatButton.addClickListener(e -> createNewZutatDialog());
        rezeptDeleteButton.addClickListener(e -> deleteRezept(rezept));
    }

    /**
     * Die Methode erstellt die Initalen Zuataten des Rezepts und gibt ihnen ihre
     * InitalWerte
     * Des weiteren wird auch hier ein deleteButton für jede Zeile erstellt
     */
    private void createList(Rezept rezept) {
        for (int i = 0; i < rezeptZutatenService.findAllByRezept(rezept).size(); i++) {
            // Textfelder mit Initalwert erstellen

            rezeptZutatMengeTextField[identifierTextFieldCounter] = new TextField("",
                    Double.toString(rezeptZutatenService.findAllByRezept(rezept).get(i).getMenge()), "");
            rezeptZutatEinheitTextField[identifierTextFieldCounter] = new TextField("",
                    rezeptZutatenService.findAllByRezept(rezept).get(i).getZutat().getEinheit().getEinheit(), "");
            rezeptZutatEinheitTextField[identifierTextFieldCounter].setReadOnly(true);

            rezeptZutatNameComboBox[identifierTextFieldCounter] = new ComboBox("", zutatservice.getZutaten());
            rezeptZutatNameComboBox[identifierTextFieldCounter]
                    .setValue(rezeptZutatenService.findAllByRezept(rezept).get(i).getZutat().getName());
            rezeptZutatNameComboBox[identifierTextFieldCounter].setAllowCustomValue(false);
            // Setzen der Einheit auf die Jeweilige Zutat
            rezeptZutatNameComboBox[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
            Integer tabIndexRezeptEinheitComboBox = rezeptZutatNameComboBox[identifierTextFieldCounter].getTabIndex();
            rezeptZutatNameComboBox[identifierTextFieldCounter].addValueChangeListener(
                    event -> rezeptZutatEinheitTextField[tabIndexRezeptEinheitComboBox].setValue(zutatservice
                            .getZutatenByName(
                                    rezeptZutatNameComboBox[tabIndexRezeptEinheitComboBox].getValue().toString())
                            .getEinheit().getEinheit()));

            // Button fürs löschen anlegen
            deleteButton[identifierTextFieldCounter] = new Button("-");
            deleteButton[identifierTextFieldCounter].addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
            deleteButton[identifierTextFieldCounter]
                    .addClickListener(event -> deleteByNumber(event.getSource().getTabIndex(), rezept));

            // Textfelder ins layout einbauen
            HorizontalLayout newZutatLayout = new HorizontalLayout(
                    rezeptZutatMengeTextField[identifierTextFieldCounter],
                    rezeptZutatEinheitTextField[identifierTextFieldCounter],
                    rezeptZutatNameComboBox[identifierTextFieldCounter], deleteButton[identifierTextFieldCounter]);
            contentRezeptZutaten.add(newZutatLayout);
            identifierTextFieldCounter++;
        }
    }

    /**
     * Die Methode erstellt eine neue Zeile an Textfeld und Comboboxen für die
     * 
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatEinheitComboBox
     * @param rezeptZutatNameComboBox
     *                                   und erstellt auch den delete Button für die
     *                                   Zeile
     *                                   der Delete Button ruft die Methode
     *                                   deleteByNumber auf und gibt ihr den Wert
     *                                   mit den sie braucht um die
     *                                   Zeile ausfindig zu machen. Dieser ist
     *                                   event.getSource().getTabIndex() und gibt
     *                                   einen Integer wert zurück
     *                                   welcher die Zeilennummer darstellt
     */
    public void createNewZutat(Rezept rezept) {
        // Textfelder für die neue Zutat
        rezeptZutatMengeTextField[identifierTextFieldCounter] = new TextField("", "1", "");
        rezeptZutatEinheitTextField[identifierTextFieldCounter] = new TextField("", "", "");
        rezeptZutatEinheitTextField[identifierTextFieldCounter].setReadOnly(true);
        rezeptZutatNameComboBox[identifierTextFieldCounter] = new ComboBox("", zutatservice.getZutaten());
        rezeptZutatNameComboBox[identifierTextFieldCounter].setAllowCustomValue(false);

        // Setzen der Einheit auf die Jeweilige Zutat
        rezeptZutatNameComboBox[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
        Integer tabIndexRezeptEinheitComboBox = rezeptZutatNameComboBox[identifierTextFieldCounter].getTabIndex();
        rezeptZutatNameComboBox[identifierTextFieldCounter].addValueChangeListener(
                event -> rezeptZutatEinheitTextField[tabIndexRezeptEinheitComboBox].setValue(zutatservice
                        .getZutatenByName(rezeptZutatNameComboBox[tabIndexRezeptEinheitComboBox].getValue().toString())
                        .getEinheit().getEinheit()));

        // Button fürs löschen anlegen
        deleteButton[identifierTextFieldCounter] = new Button("-");
        deleteButton[identifierTextFieldCounter].addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
        deleteButton[identifierTextFieldCounter]
                .addClickListener(event -> deleteByNumber(event.getSource().getTabIndex(), rezept));

        HorizontalLayout newZutatLayout = new HorizontalLayout(rezeptZutatMengeTextField[identifierTextFieldCounter],
                rezeptZutatEinheitTextField[identifierTextFieldCounter],
                rezeptZutatNameComboBox[identifierTextFieldCounter], deleteButton[identifierTextFieldCounter]);
        contentRezeptZutaten.add(newZutatLayout);

        // setzen des Counters für die wiedererkennung
        identifierTextFieldCounter++;
    }

    /**
     * Die Methode nimmt Alle zum Zeitpunkt des Drückens des Speichern Buttons
     * entgegen und erstellt damit ein neues Rezept
     * 
     * @param newRezept
     *                  Dann wird im Rezeptservice die Methode Update aufgerufen
     *                  welche ein altes und neues Rezept entgegen nimmt
     *                  und alle Daten des alten rezepts auf die Daten des neuen
     *                  Rezepts setzt und dann im RezeptReposity speichert
     *                  Dann Navigiert die Methode zurück zur Rezeptansicht
     *
     *                  Die Überprüfung auf nicht gesetze werte ist in Arbeit
     */
    public void save(Rezept rezept) {
        checkEintragZutat();
        if (checkEintragMenge() == true && checkEintragZutat() == true && checkAnzahlRezeptZutaten() == true) {
            // speichern aller Rezept werte
            if (byteArray == null)
                byteArray = initialByteArray;
            Kategorie newKategorie = auswahlKategorieComboBox.getValue();
            String newTitel = rezeptNameTextField.getValue().trim();
            String newZubereitung = zubereitungTextArea.getValue().trim();
            Integer newPortionen = inputPortionenIntegerField.getValue();
            List<Rezept_Zutat> newRezeptZutatenList = createRezeptZutaten(rezept);

            // Erstellen von neuem Rezept Entity
            Rezept newRezept = new Rezept(byteArray,
                    newTitel, newZubereitung,
                    newPortionen);
            Set<Rezept_Zutat> newSet = new HashSet<>(newRezeptZutatenList);
            newRezept.setZutaten(newSet);
            newRezept.setKategorie(newKategorie);

            // Rezept updaten und zurück zum Menü
            rezeptService.updateRezept(rezept, newRezept);
            returnToRezeptCard();
            Notification.show("Rezept: " + newRezept.getTitel() + " erfolgreich Gespeichert!")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            if (checkEintragMenge() == false && checkEintragZutat() == false) {
                Notification.show("Mengen und Zutaten inkorrekt").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            if (checkEintragMenge() == false) {
                Notification.show("Bitte Mengen überprüfen und korrekt ausfüllen")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            if (checkEintragZutat() == false) {
                Notification.show("Bitte Zutaten auf Doppelung und Korrektheit Überprüfen")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            if(checkAnzahlRezeptZutaten() == false){
                Notification.show("Bitte nicht die Maximale RezeptZutatenMenge von 40 überschreiten")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        }
    }

    /**
     * Wenn der Delete button einer TextField Zeile genutzt wird, löscht die Methode
     * alle Elemente Diser:
     * 
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatEinheitComboBox
     * @param rezeptZutatNameComboBox
     *                                   anhand des
     * @param selectedCounter            welcher vom Button selber mitgegeben wird
     *                                   Des weiteren werden die Dateninhalte der
     *                                   Menge dieser Zeile auf NULL gesetzt damit
     *                                   sie später als nicht mehr beständig
     *                                   identifiezuert und
     *                                   gelöscht werden können.
     */

    public void deleteByNumber(Integer selectedCounter, Rezept rezept) {
        // entfernen der Textfelder nach dem selected counter
        HorizontalLayout deletedLayout = new HorizontalLayout(rezeptZutatMengeTextField[selectedCounter],
                rezeptZutatEinheitTextField[selectedCounter], rezeptZutatNameComboBox[selectedCounter],
                deleteButton[selectedCounter]);
        remove(deletedLayout);

        // Setzt die Rezeptmenge einer gelöschten eintrags auf null damit abgefragt
        // werden kann ob der eintrag noch existiert und gespeichert werden muss
        rezeptZutatMengeTextField[selectedCounter] = null;

    }

    /**
     * Methode die für das auswählen eines neuen Bildes genutz werden Soll
     * IST NOCH IN ARBEIT
     */

    /**
     * Methode die Aufgerufen wird wenn der Abbruch Button betätigt wird, Führt
     * zurück in die RezeptAnsicht
     */
    public void cancel() {
        returnToRezeptCard();
        Notification.show("Rezept Bearbeitung wurde abgebrochen und die Werte wurden zurückgesetz!")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Die Methode nimmt die Listen welche von dem MethodenAufruf
     * 
     * @param setNewListen()
     *                                        erstellt werden
     * @param remainingMengenWhenSavedList
     * @param remainingNamenWhenSavedList
     * @param remainingEinheitenWhenSavedList
     *                                        und erstellt anhand von ihnen alle
     *                                        neuen RezeptZutaten in einer Schleife.
     *                                        Des weiteren gibt sie eine neue Liste
     *                                        mit den neu erstellten RezeptZutaten
     *                                        zurück.
     */
    public List<Rezept_Zutat> createRezeptZutaten(Rezept rezept) {
        // Alle neuen ListenWerte für Name Einheit und Menge setzen
        setNewListen();
        List<Rezept_Zutat> newRezeptZutatenList = new LinkedList<>();

        for (int i = 0; i < remainingMengenWhenSavedList.size(); i++) {
            // Erstellen neuer Zutat für das Rezept
            Zutat newZutat = new Zutat();

            // Setzen der Werte der neuen Zutat und erstellen der neuen Einheit
            String zutatName = remainingNamenWhenSavedList.get(i);

            // Anlegen neuer Menge und initialisieren mit emtpy wert aus textfeld
            double newMenge = Double.parseDouble(remainingMengenWhenSavedList.get(i));
            rezeptZutatenService.createRezeptZutaten(rezept, zutatservice.getZutatenByName(zutatName), newMenge);
            Rezept_Zutat newRezeptZutat = new Rezept_Zutat(rezept, zutatservice.getZutatenByName(zutatName), newMenge);
            newRezeptZutatenList.add(newRezeptZutat);
        }
        return newRezeptZutatenList;
    }

    /**
     * Die Methode überprüft welche Textfelder und Comboboxen der
     * 
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatNameComboBox
     * @param rezeptZutatEinheitComboBox
     *                                        Arrays Noch übrig sind.
     *                                        Alle Jemals erstellten (Auch Initial)
     *                                        Felder dieser Art werden mit einem
     * @param identifierTextFieldCounter      identifiziert und dann geprüft ob ihr
     *                                        wert NULL ist.
     *                                        Falls ja Werden in den Neuen Listen
     * @param remainingMengenWhenSavedList
     * @param remainingNamenWhenSavedList
     * @param remainingEinheitenWhenSavedList
     *                                        die nicht gelöschten Einträge
     *                                        gespeichert
     */
    public void setNewListen() {
        // for schleife die Jeden textfeld wert überprüft ob er gelöscht wurde und wenn
        // nein dann speichern in Liste
        for (int i = 0; i < identifierTextFieldCounter; i++) {
            if (rezeptZutatMengeTextField[i] != null) {
                remainingMengenWhenSavedList.add(rezeptZutatMengeTextField[i].getValue());
                remainingNamenWhenSavedList.add(rezeptZutatNameComboBox[i].getValue().toString());
                remainingEinheitenWhenSavedList.add(rezeptZutatEinheitTextField[i].getValue().toString());
            }
        }
    }

    /**
     * Die Methode Navigiert aus dem EditView wieder zurück in die Rezeptansicht
     */
    public void returnToRezeptCard() {
        UI.getCurrent().navigate("display/" + rezeptId);
    }

    /**
     * Die Methode überprüft ob alle Mengen Felder ausgefüllt und Korrekt ausgefüllt
     * sind
     * Falls ja gibt sie ein Entsprechenden Wert True zurück
     */

    public boolean checkEintragMenge() {
        boolean allMengenChecked = true;
        List<String> newMengenlistForCheck = new ArrayList<String>();
        for (int i = 0; i < identifierTextFieldCounter; i++) {
            if (rezeptZutatMengeTextField[i] != null) {
                newMengenlistForCheck.add(rezeptZutatMengeTextField[i].getValue());
            }
        }
        for (int i = 0; i < newMengenlistForCheck.size(); i++) {
            if (newMengenlistForCheck.get(i) == null || newMengenlistForCheck.get(i) == ""
                    || newMengenlistForCheck.get(i).contains(",")) {
                allMengenChecked = false;
            }
            try {
                double doubleChecker = Double.parseDouble(newMengenlistForCheck.get(i));
                if (Double.parseDouble(newMengenlistForCheck.get(i)) <= 0) {
                    allMengenChecked = false;
                }
            } catch (NumberFormatException e) {
                allMengenChecked = false;
            }

        }
        return allMengenChecked;
    }

    /**
     * Die Methode überprüft ob die maximale anzahl an Rezeptzutaten eingehalten wird
     * Falls ja gibt sie ein Entsprechenden Wert True zurück
     */
    public boolean checkAnzahlRezeptZutaten() {
        List<String> newMengenlistForCheck = new ArrayList<String>();
        for (int i = 0; i < identifierTextFieldCounter; i++) {
            if (rezeptZutatMengeTextField[i] != null) {
                newMengenlistForCheck.add(rezeptZutatMengeTextField[i].getValue());
            }
        }
        if(newMengenlistForCheck.size() >= 40)
        {
            return false;
        }
        return true;
    }

    /**
     * Die Methode überprüft ob alle Zutaten felder auf einzigartigkeit
     * und auf die Ausfüllung
     * Falls ja gibt sie ein Entsprechenden Wert True zurück
     */

    public boolean checkEintragZutat() {
        boolean allZutatenChecked = true;
        List<String> newZutatenListForCheck = new ArrayList<String>();

        for (int i = 0; i < identifierTextFieldCounter; i++) {
            if (rezeptZutatMengeTextField[i] != null) {
                // Wenn die ZutatenCombobox nicht ausgefüllt ist
                if (rezeptZutatNameComboBox[i].getValue() == null) {
                    return false;
                }
                newZutatenListForCheck.add(rezeptZutatNameComboBox[i].getValue().toString());
            }
        }

        for (int i = 0; i < newZutatenListForCheck.size(); i++) {
            Integer counter = 0;
            for (int z = 0; z < newZutatenListForCheck.size(); z++) {
                // Counter einen Hoch setzen wenn ein Element aus der Liste dem Anderen gleicht
                // um auf Doppelung zu prüfen
                if (newZutatenListForCheck.get(i).equals(newZutatenListForCheck.get(z))) {
                    counter++;
                }
                // Da der Listeneintrag auch mit sich selbst verglichen wird, wird der counter
                // immer um 1 hoch gesetze
                // Deswegen wird überprüft ob der counter größer als 1 ist also in der Liste 1
                // Element mit
                // mehreren Elementen gleichgestellt war
                if (counter > 1) {
                    return false;
                }
            }
        }
        return allZutatenChecked;
    }

    /**
     * Methode zum Erstellen neuer Zutaten.
     * Leicht abgeändert aber ursprünglich geschrieben im
     * 
     * @ZutatenView von
     * @author Lennard Rummel
     */
    public void createNewZutatDialog() {
        AddZutatDialog addZutatDialog = new AddZutatDialog(einheitService, zutatservice);
        addZutatDialog.open();
    }

    /**
     * Methode zum Löschen eines Rezepts, öffnet den Delete Dialog von
     * @Lennard Rummel und löscht das rezept nach bestätigen
     * Kann abgebrochen werden, falls nicht wird Notification gezeigt und
     * zur RezeptCard zurückgekehrt
     */
    public void deleteRezept(Rezept rezept) {
        DeleteDialog newDeletedialog = new DeleteDialog("Rezept: ",rezept.getTitel(),"Wirklich löschen?");
        newDeletedialog.open();
        newDeletedialog.getCancelButton().addClickListener( e -> {newDeletedialog.close();});
        newDeletedialog.getDeleteButton().addClickListener( e -> {
            rezeptService.delete(rezept.getId());
            returnToRezeptAnsicht();
            Notification.show("Rezept wurde gelöscht").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            newDeletedialog.close();
        });
    }

    /**
     * Methode die in die Rezept card zurück führt
     * nicht die gleiche wie oben!
     */
    public void returnToRezeptAnsicht() {
        UI.getCurrent().navigate("display");
    }

    /**Die Methode estellt und fügt einen BildUploader ins Layout hinzu, der das Bild-Hochladen ermöglicht
     * Das bild wird auch ins byteArray umgewandelt
     * @author Anna Karle
     */
    private void fileUpload() {
        uploader = new UploadBild(this, fileUploadButton, rezeptBild, initialByteArray);
        contentBild.add(uploader);
        byteArray = rezeptService.findById(rezeptId).getBild();

    }

    public void setBytes(byte[] bytes) {
        this.byteArray = bytes;
    }

}