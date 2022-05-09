package com.example.application.views.einstellungen;


import com.example.application.data.entity.Einheit;
import com.example.application.data.service.EinheitService;
<<<<<<< HEAD:src/main/java/com/example/application/views/einstellungen/EinstellungenView.java
import com.example.application.views.components.MainLayout;
=======
import com.example.application.views.DeleteDialog;
import com.example.application.views.MainLayout;
>>>>>>> 025cf38beadae712ba7af8a8fedd8a2b54bfbcc0:src/main/java/com/example/application/views/einstellungenView/EinstellungenView.java
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.LinkedHashMap;
import java.util.Map;

@PageTitle("Rezeptbuch")
@Route(value = "/einstellungen", layout = MainLayout.class)
public class EinstellungenView extends VerticalLayout{

    Grid<Einheit> gridEinheit = new Grid<>(Einheit.class, false);
    Grid<Einheit> gridKategorie = new Grid<>(Einheit.class, false);
    Dialog dialog = new Dialog();
    Button addEinheit = new Button("Hinzufügen");
    Button removeEinheit = new Button("Löschen");
    Button addKategorie = new Button("Hinzufügen");
    Button removeKategorie = new Button("Löschen");
    EinheitService einheitService;

    DeleteDialog deleteDialog;
    private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();
    public EinstellungenView(EinheitService einheitService){
        this.einheitService = einheitService;
        tabComponentMap.put(new Tab("Einheit"), einheitView());
        tabComponentMap.put(new Tab("Kategorie"), kategorieView());
        Tabs tabs = new Tabs(tabComponentMap.keySet().toArray(new Tab [] {}));

        Div contentContainer = new Div();

        contentContainer.setWidth("50%");

        add(tabs, contentContainer);



        /**
        einheit.getElement().addEventListener("click", e -> {
            //verticalLayoutmain.removeAll();
            verticalLayoutmain.add(einheitView());
            updateGridEinheit();

        } );
        */

        /**
        kategorie.getElement().addEventListener("click", e -> {
            verticalLayoutmain.add(kategorieView());
            updateGridKategorie();

        } );
         */

        tabs.addSelectedChangeListener(e ->{
            contentContainer.removeAll();
            contentContainer.add(tabComponentMap.get(e.getSelectedTab()));
        });
        contentContainer.add(tabComponentMap.get(tabs.getSelectedTab()));







        /**

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        verticalLayout.add(verticalLayoutUeberschrift);
        horizontalLayout.add(verticalLayoutEinheit);
        horizontalLayout.add(verticalLayoutKategorie);

        verticalLayout.add(horizontalLayout);

        add(verticalLayout);
        */
    }

    private VerticalLayout einheitView(){

        H3 ueberschrift = new H3("Einheitenverwaltung");
        ueberschrift.getStyle().set("margin", "0");

        configureButtonsEinheit();
        VerticalLayout verticalLayoutUeberschrift = new VerticalLayout(ueberschrift);
        VerticalLayout verticalLayoutEinheit = new VerticalLayout(configureGridEinheit(), new HorizontalLayout(addEinheit,removeEinheit));
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(verticalLayoutUeberschrift);
        verticalLayout.add(verticalLayoutEinheit);
        return verticalLayout;
    }

    private VerticalLayout kategorieView(){

        H3 ueberschrift = new H3("Kategorienverwaltung");
        ueberschrift.getStyle().set("margin", "0");

        configureButtonsKategorien();

        VerticalLayout verticalLayoutUeberschrift = new VerticalLayout(ueberschrift);
        VerticalLayout verticalLayoutKategorie = new VerticalLayout(configureGridKategorie(), new HorizontalLayout(addKategorie,removeKategorie));
        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(verticalLayoutUeberschrift);
        verticalLayout.add(verticalLayoutKategorie);

        return verticalLayout;

    }

    private void configureButtonsEinheit(){
        addEinheit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEinheit.addClickListener(e -> addEinheitDialog().open());

        removeEinheit.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeEinheit.addClickListener(e -> removeEinheit());
    }

    private void configureButtonsKategorien(){
        addKategorie.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //addKategorie.addClickListener(e -> addKategorieDialog().open());

        removeKategorie.addThemeVariants(ButtonVariant.LUMO_ERROR);
       //removeKategorie.addClickListener(e -> removeKategorie());
    }


    private Dialog addEinheitDialog(){
        dialog = new Dialog();
        dialog.add(new H5("Einheit hinzufügen"));

        TextField einheit = new TextField("Bezeichnung");
        einheit.setRequired(true);
        einheit.setErrorMessage("Gib eine Bezeichnung ein!");

        dialog.add(new HorizontalLayout(einheit));


        Button speichern = new Button("Speichern");
        speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button abbrechen = new Button("Abbrechen", e-> dialog.close());
        abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

        speichern.addClickListener(e ->{

            if(einheit.getValue() != null){
                einheitService.createEinheit(einheit.getValue());
                dialog.close();
                Notification.show("Einheit hinzugefügt: " + einheit.getValue()).addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }else{
                    Notification.show("Die Einheit '"+ einheit.getValue() + "' existiert bereits.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

            updateGridEinheit();
        }
        );
        dialog.add(new HorizontalLayout(speichern, abbrechen));
        return dialog;
    }

    private Grid configureGridEinheit(){
        gridEinheit.addColumn(Einheit::getEinheit).setHeader("Einheit").setSortable(true);

        updateGridEinheit();
        // Styling
        gridEinheit.setWidthFull();
        gridEinheit.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return gridEinheit;
    }

    private Grid configureGridKategorie(){
        gridKategorie.addColumn(Einheit::getEinheit).setHeader("Kategorie").setSortable(true);

        updateGridKategorie();
        // Styling
        gridEinheit.setWidthFull();
        gridKategorie.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return gridKategorie;
    }


    private void updateGridEinheit(){
        gridEinheit.setItems(einheitService.getEinheiten());
    }

    private void updateGridKategorie(){
        gridKategorie.setItems(einheitService.getEinheiten());
    }


    private void removeEinheit(){
        if(!gridEinheit.getSelectionModel().getSelectedItems().isEmpty()){
            Einheit einheit = gridEinheit.getSelectionModel().getFirstSelectedItem().get();
            configuteDeleteDialogEinheit(einheit);
            dialog.close();

        }else{
            Notification.show("Löschen nicht möglich, da keine Einheit ausgewählt wurde.").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    public void configuteDeleteDialogEinheit(Einheit einheit){
        deleteDialog = new DeleteDialog("Einheit",einheit.getEinheit(), "Sicher, das die Einheit gelöscht werden soll?" );
        deleteDialog.open();
        deleteDialog.getDeleteButton().addClickListener( e -> {
            einheitService.deleteEinheit(einheit);
            Notification.show("Einheit gelöscht: " + einheit.getEinheit()).addThemeVariants(NotificationVariant.LUMO_ERROR);
           updateGridEinheit();
           deleteDialog.close();
        });
        deleteDialog.getCancelButton().addClickListener( e -> {
            deleteDialog.close();
        });
    }
    /**
    public void configuteDeleteDialogKategorie(Kategorie kategorie){
        deleteDialog = new DeleteDialog("Kategorie",kategorie.getKategorie(), "Sicher, das die Kategorie gelöscht werden soll?" );
        deleteDialog.open();
        deleteDialog.getDeleteButton().addClickListener( e -> {
            einheitService.deleteKategorie(kategorie);
            Notification.show("Kaategorie gelöscht: " + kategorie.getKategorie()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            updateGridKategorie();
            deleteDialog.close();
        });
        deleteDialog.getCancelButton().addClickListener( e -> {
            deleteDialog.close();
        });
    }
    */
}







