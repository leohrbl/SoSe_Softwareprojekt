package com.example.application.views.rezept.create;

import com.example.application.data.entity.*;
import com.example.application.data.service.EinheitService;
import com.example.application.data.service.RezeptService;
import com.example.application.data.service.RezeptZutatenService;
import com.example.application.data.service.ZutatService;
import com.example.application.views.components.AddZutatDialog;
import com.example.application.views.components.AddZutatRow;
import com.example.application.views.components.MainLayout;
import com.example.application.views.components.ViewFrame;
import com.example.application.views.menge.MengeService;
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

import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Rezept erstellen")
@Route(value = "create", layout = MainLayout.class)
public class RezeptCreateView extends ViewFrame {

    private List<AddZutatRow> zutatenRows = new LinkedList<>();
    private VerticalLayout zutatenContainer = new VerticalLayout();
    private Button neueZeileButton = new Button("Neue Zeile");
    private Button neueZutatButton = new Button("Neue Zutat anlegen");
    private Button speichernButton = new Button("Speichern");
    private TextField title;
    private Select<Einheit> kategorie;
    private IntegerField portionenInput;
    private Image image;
    private TextArea zubereitung;

    private RezeptService rezeptService;
    private MengeService mengeService;
    private RezeptZutatenService rezeptZutatenService;
    private ZutatService zutatService;
    private EinheitService einheitService;

    public RezeptCreateView(RezeptService rezeptService, RezeptZutatenService rezeptZutatenService, ZutatService zutatService, EinheitService einheitService) {
        this.rezeptService = rezeptService;
        this.rezeptZutatenService = rezeptZutatenService;
        this.mengeService = new MengeService();
        this.zutatService = zutatService;
        this.einheitService = einheitService;

        configureButtons();
        createViewLayout();
    }

    private void createViewLayout(){
        title = new TextField("Titel");

        kategorie = new Select<>();
        kategorie.setLabel("Kategorie");
        kategorie.setItems(einheitService.getEinheiten());

        Paragraph portionen = new Paragraph("Zutaten für ");
        Paragraph portionen2 = new Paragraph("Portionen");
        portionenInput = new IntegerField();
        portionenInput.setMin(1);
        portionenInput.setValue(4);
        portionenInput.setMax(20);
        portionenInput.setHasControls(true);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setWidth("100%");
        vLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Button upload = new Button("Bild hochladen ...");
        vLayout.add(upload);

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



        HorizontalLayout btnLayout = new HorizontalLayout(neueZeileButton,neueZutatButton);

        //hier dann auch noch das grid mit den Zutaten/Mengen Objekten zu dem Rezept lul
        VerticalLayout content = new VerticalLayout(image, vLayout, zutatenContainer, btnLayout, zubereitung);
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

    public void addZutatZeile(){
        HorizontalLayout rowLayout = new HorizontalLayout();
        AddZutatRow row = new AddZutatRow(zutatService);
        zutatenRows.add(row);
        rowLayout.add(row,createDeleteRowButton());
        rowLayout.setAlignItems(Alignment.BASELINE);
        zutatenContainer.add(rowLayout);
    }

    private Button createDeleteRowButton(){
        Button delRowButton = new Button(VaadinIcon.MINUS.create());
        delRowButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delRowButton.addClickListener(e -> {
            deleteZutatRow(delRowButton);

        });
        return delRowButton;
    }

    private void deleteZutatRow(Button delRowButton) {
        Component parent = delRowButton.getParent().get();
        List<Component> componentList = parent.getChildren().collect(Collectors.toList());
        // Löschen der AddZutatRow Instanz in der Liste zutatenRows
        if(isNotLastRow() && isNotMaxRow()){
            for(Component component : componentList){
                if(component instanceof AddZutatRow){
                    AddZutatRow rowToDelete = null;
                    for(AddZutatRow row : zutatenRows){
                        if(component == row){
                            rowToDelete = row;
                        }
                    }
                    if(rowToDelete != null){
                        zutatenRows.remove(rowToDelete);
                    }
                }
            }
            zutatenContainer.remove(parent);
        }
    }




    public void configureButtons(){
        neueZeileButton.addClickListener(e ->{
            addZutatZeile();
        });
        neueZutatButton.addClickListener(e ->{
            AddZutatDialog addZutatDialog = new AddZutatDialog(einheitService,zutatService);
            addZutatDialog.open();
        });
        speichernButton.addClickListener(e->{
            rezeptSpeichern();
        });
    }

    public void rezeptSpeichern(){
        if(isValuesValid()){
            try {
                Rezept rezept = new Rezept(new Image(
                        image.getSrc(),
                        image.getAlt().toString()), title.getValue(), zubereitung.getValue(), portionenInput.getValue());
                Notification.show(rezept.toString());
                rezeptService.createRezept(rezept);
                for (AddZutatRow row : zutatenRows) {
                    rezeptZutatenService.createRezeptZutaten(rezept, row.getZutat(), row.getMenge());
                }
                Notification.show("Rezept '"+title.getValue()+"' gespeichert").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                clearAll();
            }catch(Exception e){
                e.printStackTrace();
                //Notification.show(e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        }

    }

    // TODO: Individuelle Fehlermeldungen
    private boolean isValuesValid() {
        if(kategorie.isEmpty() || title.isEmpty() || portionenInput.isEmpty() || zubereitung.isEmpty() || !isZutatRowsValid() || checkDuplicateZutaten()){
            Notification.show("Bitte Eingabewerte überprüfen!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private boolean isZutatRowsValid(){
        for(AddZutatRow row : zutatenRows){
            if(!row.isFilled()){
                return false;
            }
        }
        return true;
    }

    private boolean isNotLastRow(){
        if(zutatenRows.size() > 1) {
            return true;
        }else{
            Notification.show("Bitte geben Sie mindestens eine Zutat ein!").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
    }

    private boolean isNotMaxRow(){
        if(zutatenRows.size() <= 100) {
            return true;
        }else{
            Notification.show("Sie können nicht mehr als 100 Zutaten zu einem Rezept speichern").addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
    }


    private boolean checkDuplicateZutaten(){
        List<String> zutatNameList = new LinkedList<>();
        for(AddZutatRow row : zutatenRows){
            zutatNameList.add(row.getZutat().getName());
        }
        Set<String> zutatNameSet = new HashSet<>(zutatNameList);
        return zutatNameSet.size() < zutatNameList.size();

    }

    private void clearAll(){
        kategorie.clear();
        title.clear();
        portionenInput.clear();
        zutatenContainer.removeAll();
        zubereitung.clear();
        addZutatZeile();

    }

}
