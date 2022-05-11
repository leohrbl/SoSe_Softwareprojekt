package com.example.application.views.rezept.edit;

import com.example.application.data.entity.*;
import com.example.application.data.service.*;
import com.example.application.views.components.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.apache.commons.lang3.StringUtils;
import javax.swing.JFileChooser;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import com.example.application.views.components.ViewFrame;


/**
 * @author Joscha Cerny
 * die Klasse erstellt den View zum Bearbeiten eines Rezeptes.
 * Sie lädt die bestehden Daten des ausgewählten rezeptes und lässt diese verändern sowie entfernen und Hinzufügen.
 */
@PageTitle("Rezept bearbeiten")
@Route(value = "edit", layout = MainLayout.class)
public class RezeptEditView extends ViewFrame implements HasUrlParameter<String> {

    private static long rezeptId;
    Button fileUploadButton = new Button("Bild Hochladen");
    Button zutatHinzufuegenButton = new Button("Weitere Zutat anlegen");
    Button saveButton = new Button("Speichern");
    Button cancelButton = new Button("Abbrechen");
    Button neueZutatButton = new Button("Neue Zutat Erstellen");

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
    //Listen zum speichern aller werte welche nicht gelöscht worden sind
    List<String> remainingMengenWhenSavedList = new ArrayList<String>(); //Sobald gespeichert wird werden alle verbleibenden Mengen der Zutaten in dieser Liste gespeichert
    List<String> remainingNamenWhenSavedList = new ArrayList<String>(); //Sobald gespeichert wird werden alle verbleibenden Namen der Zutaten in dieser Liste gespeichert
    List<String> remainingEinheitenWhenSavedList = new ArrayList<String>(); //Sobald gespeichert wird werden alle verbleibenden Einheiten der Zutaten in dieser Liste gespeichert
    Dialog createNewZutatDialog = new Dialog(); //Dialog zum Erstellen neuer Zutaten
    VerticalLayout contentRezeptZutaten = new VerticalLayout();


    /**
     * Konstruktor des EditViews, hier werden alle benötigten Services Initiert
     */
    public RezeptEditView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService, EinheitService einheitService
            , ZutatService zutatservice, KategorieService kategorieService)
    {
        this.rezeptService = rezeptService;
        this.zutatservice = zutatservice;
        this.rezeptZutatenService = rezeptZutatenService;
        this.einheitService = einheitService;
        this.kategorieService = kategorieService;
    }

    /**
     * Die Methode nimmt den Parameter aus der Url entgegen damit der View weiß welches Rezept bearbeitet werden soll
     * Er setzt den Parameter in die RezeptID und führt die Methode createView und Configure Buttons Inital aus
     */
    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try{
            setRezeptID(Long.parseLong(parameter));
            Rezept rezept = rezeptService.findById(getRezeptID());
            createView(rezept);
            configureButtons(rezept);
        }catch(Exception e){
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
     * Die Methode ruft das Initiale Layout des Views auf und ruft ebenfalls die InitalWerte des Ausgewöhlten rezepts aus
     */
    public void createView(Rezept rezept)
    {
        configureInitialLayout(rezept);
        createList(rezept);
    }

    /**
     * In dieser Methode wird das Initiale Layout des EditViews gebaut und geladen.
     */
    public void configureInitialLayout(Rezept rezept)
    {
        //SeitenTitel und Textfeld mit name
        H3 seitenName = new H3("Rezept bearbeiten");
        rezeptNameTextField = new TextField("RezeptName", rezept.getTitel(),"");

        //Drop down für die Kategorie in arbeit, wird fertig sobald Katergorie da ist
        ComboBox<Kategorie> auswahlKategorie = new ComboBox<>();
        auswahlKategorie.setLabel("Kategorie");
        auswahlKategorie.setItems(kategorieService.getKategorien());

        //Bild laden
        rezeptBild = rezept.getBild();
        rezeptBild.setWidth("35%");
        rezeptBild.setHeight("35%");
        rezeptBild.addClassName("image");

        //fürs angeben der Portionen
        Paragraph portionenText1 = new Paragraph("Zutaten für ");
        Paragraph portionenText2 = new Paragraph("Portionen");
        inputPortionenIntegerField = new IntegerField();
        inputPortionenIntegerField.setMin(1);
        inputPortionenIntegerField.setValue(rezept.getPortionen());
        inputPortionenIntegerField.setMax(20);
        inputPortionenIntegerField.setHasControls(true);

        //Text feld für die Zubereitung
        String zubereitungString = rezept.getZubereitung();
        zubereitungTextArea = new TextArea("Zubereitung",zubereitungString,"");
        zubereitungTextArea.setWidth("50%");
        zubereitungTextArea.setHeight("40%");
        VerticalLayout zubereitungLayout = new VerticalLayout(zubereitungTextArea);
        zubereitungLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        zubereitungLayout.setPadding(true);
        zubereitungLayout.setSpacing(true);

        HorizontalLayout contentHeader = new HorizontalLayout(seitenName, auswahlKategorie, rezeptNameTextField);
        contentHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        contentHeader.setPadding(true);
        contentHeader.setSpacing(true);

        VerticalLayout contentBild = new VerticalLayout(rezeptBild, fileUploadButton);
        contentBild.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        contentBild.setPadding(true);

        HorizontalLayout contentPortion = new HorizontalLayout(portionenText1, inputPortionenIntegerField, portionenText2);
        contentPortion.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        contentPortion.setPadding(true);

        contentRezeptZutaten.setPadding(true);
        neueZutatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout zutatHinzufuegenButtonLayout = new VerticalLayout(zutatHinzufuegenButton, neueZutatButton);
        zutatHinzufuegenButtonLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        zutatHinzufuegenButtonLayout.setPadding(true);

        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout contentFooter = new HorizontalLayout(cancelButton,saveButton);
        contentHeader.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        contentHeader.setPadding(true);
        contentHeader.setSpacing(true);

        setViewHeader(contentHeader);
        setViewContent(contentBild, contentPortion, contentRezeptZutaten, zutatHinzufuegenButtonLayout, zubereitungLayout);
        setViewFooter(contentFooter);
    }

    /**
     * KDie Methode gibt allen Buttons ihre Funktion und wird am Anfang aufgerufen
     */
    private void configureButtons(Rezept rezept){
        //Buttons
        fileUploadButton.addClickListener(event -> fileUpload());
        zutatHinzufuegenButton.addClickListener(event -> createNewZutat(rezept));
        saveButton.addClickListener(event -> save(rezept));
        cancelButton.addClickListener(event -> cancel());
        neueZutatButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        neueZutatButton.addClickListener(e -> createNewZutatDialog().open());
    }

    /**
     * Die Methode erstellt die Initalen Zuataten des Rezepts und gibt ihnen ihre InitalWerte
     * Des weiteren wird auch hier ein deleteButton für jede Zeile erstellt
     */
    private void createList(Rezept rezept)
    {
        for(int i = 0; i < rezeptZutatenService.findAllByRezept(rezept).size(); i++)
        {
            //Textfelder mit Initalwert erstellen
            rezeptZutatMengeTextField[identifierTextFieldCounter]= new TextField("", "1", "");
            rezeptZutatEinheitTextField[identifierTextFieldCounter] = new TextField("", rezeptZutatenService.findAllByRezept(rezept).get(i).getZutat().getEinheit().getEinheit(), "");
            rezeptZutatEinheitTextField[identifierTextFieldCounter].setReadOnly(true);

            rezeptZutatNameComboBox[identifierTextFieldCounter] = new ComboBox("", zutatservice.getZutaten());
            rezeptZutatNameComboBox[identifierTextFieldCounter].setValue(rezeptZutatenService.findAllByRezept(rezept).get(i).getZutat().getName());
            rezeptZutatNameComboBox[identifierTextFieldCounter].setAllowCustomValue(false);
            //Setzen der Einheit auf die Jeweilige Zutat
            rezeptZutatNameComboBox[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
            Integer tabIndexRezeptEinheitComboBox = rezeptZutatNameComboBox[identifierTextFieldCounter].getTabIndex();
            rezeptZutatNameComboBox[identifierTextFieldCounter].addValueChangeListener(event -> rezeptZutatEinheitTextField[tabIndexRezeptEinheitComboBox].setValue
                    (zutatservice.getZutatenByName(rezeptZutatNameComboBox[tabIndexRezeptEinheitComboBox].getValue().toString()).getEinheit().getEinheit()));

            //Button fürs löschen anlegen
            deleteButton[identifierTextFieldCounter] = new Button("-");
            deleteButton[identifierTextFieldCounter].addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
            deleteButton[identifierTextFieldCounter].addClickListener(event -> deleteByNumber(event.getSource().getTabIndex(),rezept));

            //Textfelder ins layout einbauen
            HorizontalLayout newZutatLayout = new HorizontalLayout(rezeptZutatMengeTextField[identifierTextFieldCounter], rezeptZutatEinheitTextField[identifierTextFieldCounter],
                    rezeptZutatNameComboBox[identifierTextFieldCounter], deleteButton[identifierTextFieldCounter]);
            contentRezeptZutaten.add(newZutatLayout);
            identifierTextFieldCounter++;
        }
    }

    /**
     * Die Methode erstellt eine neue Zeile an Textfeld und Comboboxen für die
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatEinheitComboBox
     * @param rezeptZutatNameComboBox
     * und erstellt auch den delete Button für die Zeile
     * der Delete Button ruft die Methode deleteByNumber auf und gibt ihr den Wert mit den sie braucht um die
     * Zeile ausfindig zu machen. Dieser ist event.getSource().getTabIndex() und gibt einen Integer wert zurück
     * welcher die Zeilennummer darstellt
     */
    public void createNewZutat(Rezept rezept)
    {
        //Textfelder für die neue Zutat
        rezeptZutatMengeTextField[identifierTextFieldCounter] = new TextField("","1","");
        rezeptZutatEinheitTextField[identifierTextFieldCounter] = new TextField("", "", "");
        rezeptZutatEinheitTextField[identifierTextFieldCounter].setReadOnly(true);
        rezeptZutatNameComboBox[identifierTextFieldCounter] = new ComboBox("",zutatservice.getZutaten());
        rezeptZutatNameComboBox[identifierTextFieldCounter].setAllowCustomValue(false);

        //Setzen der Einheit auf die Jeweilige Zutat
        rezeptZutatNameComboBox[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
        Integer tabIndexRezeptEinheitComboBox = rezeptZutatNameComboBox[identifierTextFieldCounter].getTabIndex();
        rezeptZutatNameComboBox[identifierTextFieldCounter].addValueChangeListener(event -> rezeptZutatEinheitTextField[tabIndexRezeptEinheitComboBox].setValue
                (zutatservice.getZutatenByName(rezeptZutatNameComboBox[tabIndexRezeptEinheitComboBox].getValue().toString()).getEinheit().getEinheit()));

        //Button fürs löschen anlegen
        deleteButton[identifierTextFieldCounter] = new Button("-");
        deleteButton[identifierTextFieldCounter].addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton[identifierTextFieldCounter].setTabIndex(identifierTextFieldCounter);
        deleteButton[identifierTextFieldCounter].addClickListener(event -> deleteByNumber(event.getSource().getTabIndex(),rezept));

        HorizontalLayout newZutatLayout = new HorizontalLayout(rezeptZutatMengeTextField[identifierTextFieldCounter], rezeptZutatEinheitTextField[identifierTextFieldCounter],
                rezeptZutatNameComboBox[identifierTextFieldCounter], deleteButton[identifierTextFieldCounter]);
        contentRezeptZutaten.add(newZutatLayout);

        //setzen des Counters für die wiedererkennung
        identifierTextFieldCounter++;
    }

    /**
     * Die Methode nimmt Alle zum Zeitpunkt des Drückens des Speichern Buttons entgegen und erstellt damit ein neues Rezept
     * @param newRezept
     * Dann wird im Rezeptservice die Methode Update aufgerufen welche ein altes und neues Rezept entgegen nimmt
     * und alle Daten des alten rezepts auf die Daten des neuen Rezepts setzt und dann im RezeptReposity speichert
     * Dann Navigiert die Methode zurück zur Rezeptansicht
     *
     * Die Überprüfung auf nicht gesetze werte ist in Arbeit
     */
    public void save(Rezept rezept)
    {
        checkEintragZutat();
        if(checkEintragMenge() == true && checkEintragZutat() == true)
        {
            //speichern aller Rezept werte
            Image newImage = this.rezeptBild;
            String newTitel = rezeptNameTextField.getValue();
            String newZubereitung = zubereitungTextArea.getValue();
            Integer newPortionen = inputPortionenIntegerField.getValue();
            List<Rezept_Zutat> newRezeptZutatenList = createRezeptZutaten(rezept);

            //Erstellen von neuem Rezept Entity
            Rezept newRezept = new Rezept(new Image(this.rezeptBild.getSrc(), "Essen"), newTitel, newZubereitung, newPortionen);
            Set<Rezept_Zutat> newSet = new HashSet<>(newRezeptZutatenList);
            newRezept.setZutaten(newSet);

            //Rezept updaten und zurück zum Menü
            rezeptService.updateRezept(rezept, newRezept);
            returnToRezeptCard();
        }
        else
        {
            if(checkEintragMenge() == false && checkEintragZutat() == false)
            {
                Notification.show("Mengen und Zutaten Inkorrekt, Spast").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            if(checkEintragMenge() == false)
            {
                Notification.show("Bitte Mengen überprüfen und Korrekt ausfüllen").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            if(checkEintragZutat() == false)
            {
                Notification.show("Bitte Zutaten auf Doppelung und Korrektheit Überprüfen").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }

    }
    /**
     * Wenn der Delete button einer TextField Zeile genutzt wird, löscht die Methode alle Elemente Diser:
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatEinheitComboBox
     * @param rezeptZutatNameComboBox
     * anhand des
     * @param selectedCounter welcher vom Button selber mitgegeben wird
     * Des weiteren werden die Dateninhalte der Menge dieser Zeile auf NULL gesetzt damit sie später als nicht mehr beständig identifiezuert und
     * gelöscht werden können.
     */
    public void deleteByNumber(Integer selectedCounter, Rezept rezept)
    {
        //entfernen der Textfelder nach dem selected counter
        HorizontalLayout deletedLayout = new HorizontalLayout(rezeptZutatMengeTextField[selectedCounter], rezeptZutatEinheitTextField[selectedCounter], rezeptZutatNameComboBox[selectedCounter], deleteButton[selectedCounter]);
        remove(deletedLayout);

        //Setzt die Rezeptmenge einer gelöschten eintrags auf null damit abgefragt werden kann ob der eintrag noch existiert und gespeichert werden muss
        rezeptZutatMengeTextField[selectedCounter] = null;

    }
    /**
     * Methode die für das auswählen eines neuen Bildes genutz werden Soll
     * IST NOCH IN ARBEIT
     */
    private void fileUpload()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
    }

    /**
     * Methode die Aufgerufen wird wenn der Abbruch Button betätigt wird, Führt zurück in die RezeptAnsicht
     */
    public void cancel()
    {
        returnToRezeptCard();
    }

    /**
     * Die Methode nimmt die Listen welche von dem MethodenAufruf
     * @param setNewListen()
     * erstellt werden
     * @param remainingMengenWhenSavedList
     * @param remainingNamenWhenSavedList
     * @param remainingEinheitenWhenSavedList
     * und erstellt anhand von ihnen alle neuen RezeptZutaten in einer Schleife.
     * Des weiteren gibt sie eine neue Liste mit den neu erstellten RezeptZutaten zurück.
     */
    public List<Rezept_Zutat> createRezeptZutaten(Rezept rezept)
    {
        //Alle neuen ListenWerte für Name Einheit und Menge setzen
        setNewListen();
        List<Rezept_Zutat> newRezeptZutatenList = new LinkedList<>();

        for(int i = 0; i < remainingMengenWhenSavedList.size(); i++)
        {
            //Erstellen neuer Zutat für das Rezept
            Zutat newZutat = new Zutat();

            //Setzen der Werte der neuen Zutat und erstellen der neuen Einheit
            String zutatName = remainingNamenWhenSavedList.get(i);

            //Anlegen neuer Menge und initialisieren mit emtpy wert aus textfeld
            int newMenge = Integer.parseInt(remainingMengenWhenSavedList.get(i));
            rezeptZutatenService.createRezeptZutaten(rezept, zutatservice.getZutatenByName(zutatName), newMenge);
            Rezept_Zutat newRezeptZutat = new Rezept_Zutat(rezept, zutatservice.getZutatenByName(zutatName), newMenge);
            newRezeptZutatenList.add(newRezeptZutat);
        }
        return newRezeptZutatenList;
    }

    /**
     * Die Methode überprüft welche Textfelder und Comboboxen der
     * @param rezeptZutatMengeTextField
     * @param rezeptZutatNameComboBox
     * @param rezeptZutatEinheitComboBox
     * Arrays Noch übrig sind.
     * Alle Jemals erstellten (Auch Initial) Felder dieser Art werden mit einem
     * @param identifierTextFieldCounter identifiziert und dann geprüft ob ihr wert NULL ist.
     * Falls ja Werden in den Neuen Listen
     * @param remainingMengenWhenSavedList
     * @param remainingNamenWhenSavedList
     * @param remainingEinheitenWhenSavedList
     * die nicht gelöschten Einträge gespeichert
     */
    public void setNewListen()
    {
        //for schleife die Jeden textfeld wert überprüft ob er gelöscht wurde und wenn nein dann speichern in Liste
        for(int i = 0; i < identifierTextFieldCounter; i++)
        {
            if(rezeptZutatMengeTextField[i] != null)
            {
                remainingMengenWhenSavedList.add(rezeptZutatMengeTextField[i].getValue());
                remainingNamenWhenSavedList.add(rezeptZutatNameComboBox[i].getValue().toString());
                remainingEinheitenWhenSavedList.add(rezeptZutatEinheitTextField[i].getValue().toString());
            }
        }
    }

    /**
     * Die Methode Navigiert aus dem EditView wieder zurück in die Rezeptansicht
     */
    public void returnToRezeptCard()
    {
        UI.getCurrent().navigate("display");
    }

    /**
     * Die Methode überprüft ob alle Mengen Felder ausgefüllt und Korrekt ausgefüllt sind
     * Falls ja gibt sie ein Entsprechenden Wert True zurück
     */
    public boolean checkEintragMenge()
    {
        boolean allMengenfilled = true;
        List<String> newMengenlistForCheck = new ArrayList<String>();
        for(int i = 0; i < identifierTextFieldCounter; i++)
        {
            if(rezeptZutatMengeTextField[i] != null)
            {
                newMengenlistForCheck.add(rezeptZutatMengeTextField[i].getValue());
            }
        }
        for(int i = 0; i < newMengenlistForCheck.size(); i++)
        {
            if(newMengenlistForCheck.get(i) == null || newMengenlistForCheck.get(i) == "" || StringUtils.isNumeric(newMengenlistForCheck.get(i)) == false)
            {
                allMengenfilled = false;
            }
        }
        return allMengenfilled;
    }

    /**
     * Die Methode überprüft ob alle Zutaten felder auf einzigartigkeit
     * und auf die Ausfüllung
     * Falls ja gibt sie ein Entsprechenden Wert True zurück
     */
    public boolean checkEintragZutat()
    {
        boolean allZutatenChecked = true;
        List<String> newZutatenListForCheck = new ArrayList<String>();

        for(int i = 0; i < identifierTextFieldCounter; i++)
        {
            if(rezeptZutatMengeTextField[i] != null)
            {
                //Wenn die ZutatenCombobox nicht ausgefüllt ist
                if(rezeptZutatNameComboBox[i].getValue() == null)
                {
                    return false;
                }
                newZutatenListForCheck.add(rezeptZutatNameComboBox[i].getValue().toString());
            }
        }

        for(int i = 0; i < newZutatenListForCheck.size(); i++)
        {
            Integer counter = 0;
            for(int z = 0; z < newZutatenListForCheck.size(); z++)
            {
                //Counter einen Hoch setzen wenn ein Element aus der Liste dem Anderen gleicht um auf Doppelung zu prüfen
                if(newZutatenListForCheck.get(i).equals(newZutatenListForCheck.get(z)))
                {
                    counter++;
                }
                //Da der Listeneintrag auch mit sich selbst verglichen wird, wird der counter immer um 1 hoch gesetze
                //Deswegen wird überprüft ob der counter größer als 1 ist also in der Liste 1 Element mit
                //mehreren Elementen gleichgestellt war
                if(counter > 1)
                {
                    return false;
                }
            }
        }
        return allZutatenChecked;
    }



    /**
     * Methode zum Erstellen neuer Zutaten.
     * Leicht abgeändert aber ursprünglich geschrieben im
     * @ZutatenView von
     * @author Lennard Rummel
     */
    public Dialog createNewZutatDialog()
    {
        createNewZutatDialog = new Dialog();
        createNewZutatDialog.add(new H5("Zutat hinzufügen"));

        TextField bezeichnung = new TextField("Bezeichnung");
        bezeichnung.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        ComboBox<Einheit> einheitAuswahl = new ComboBox<>();
        einheitAuswahl.setLabel("Einheit");
        einheitAuswahl.setPlaceholder("Einheit auswählen");
        einheitAuswahl.setRequired(true);
        bezeichnung.setErrorMessage("Gib eine Bezeichnung und eine Einheit!");

        einheitAuswahl.setItems(einheitService.getEinheiten());
        createNewZutatDialog.add(new HorizontalLayout(bezeichnung, einheitAuswahl));

        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> createNewZutatDialog.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

            // Neue Zutat wird gespeichert.
            if(!bezeichnung.isEmpty() && !einheitAuswahl.isEmpty()){
                if(zutatservice.searchZutatenByFilterText(bezeichnung.getValue()).size() == 0){
                    createNewZutatDialog.close();
                    zutatservice.saveZutat(bezeichnung.getValue(), einheitAuswahl.getValue());
                    Notification.show("Zutat hinzugefügt: " + bezeichnung.getValue() +" in "+ einheitAuswahl.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }else{
                    Notification.show("Die Zutat '"+ bezeichnung.getValue()+"' existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }else{
                Notification.show("Keine Zutat hinzugefügt. Es muss eine Bezeichnung und eine Einheit angegeben werden.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        createNewZutatDialog.add(new HorizontalLayout(speichern, abbrechen));
        return createNewZutatDialog;
    }
}
