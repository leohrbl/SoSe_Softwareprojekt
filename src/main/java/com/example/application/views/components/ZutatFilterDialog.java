package com.example.application.views.components;

import com.example.application.data.entity.Zutat;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;


public class ZutatFilterDialog extends Dialog{

    private TextField searchField;
    private Grid<Zutat> grid;
    private ZutatService zutatService;
    private Button filterButton;
    private Button removeFilterButton;

    public ZutatFilterDialog(ZutatService zutatService){
        this.searchField = new TextField();
        this.grid = new Grid<>(Zutat.class, false);
        this.filterButton = new Button("Filtern");
        this.zutatService = zutatService;
        this.removeFilterButton = new Button("Filter entfernen");
        createDialog();
    }

    private void createDialog(){
        configureFilterButton();
        configureRemoveFilterButton();
        configureSearchField();
        configureGrid();
        this.add(createHeaderLayout(), grid, createFooterLayout());
    }

    private HorizontalLayout createHeaderLayout(){
        HorizontalLayout header = new HorizontalLayout(searchField, createAbbrechenButton());
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1, searchField);
        header.setPadding(true);
        header.setSpacing(true);
        return header;
    }

    private HorizontalLayout createFooterLayout(){
        return new HorizontalLayout(filterButton, removeFilterButton);
    }

    private void configureFilterButton(){
        filterButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    }

    private void configureRemoveFilterButton(){
        removeFilterButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    private Button createAbbrechenButton(){
        Button abbrechenButton = new Button(VaadinIcon.CLOSE.create());
        abbrechenButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        abbrechenButton.addClickListener(e -> {
            this.close();
        });
        return abbrechenButton;
    }

    private void configureSearchField() {
        searchField.getElement().setAttribute("aria-label", "search");
        searchField.setPlaceholder("Search");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> handleSearch());
    }

    private void configureGrid() {
        grid.addColumn(Zutat::getName).setHeader("Bezeichnung");
        grid.addColumn(Zutat::getEinheit).setHeader("Einheit");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        updateGrid();
    }

    private void updateGrid(){
        if(searchField.isEmpty()){
            grid.setItems(zutatService.getZutaten());
        }else{
            grid.setItems(zutatService.searchZutatenByFilterText(searchField.getValue()));
        }
    }

    private void handleSearch(){
        updateGrid();
    }

    public Button getFilterButton(){
        return this.filterButton;
    }

    public Button getRemoveFilterButton(){
        return this.removeFilterButton;
    }

    public  Zutat getFilteredItem (){
        if(!grid.getSelectedItems().isEmpty()){
            return grid.getSelectionModel().getFirstSelectedItem().get();
        }else{
            return null;
        }
    }

}
