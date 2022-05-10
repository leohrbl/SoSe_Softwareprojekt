package com.example.application.views.components;


import com.example.application.data.entity.Rezept;
import com.example.application.data.entity.Rezept_Zutat;
import com.example.application.data.entity.Zutat;
import com.example.application.data.service.ZutatService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;


public class AddZutatRow extends HorizontalLayout {

    private NumberField mengeField;
    private TextField einheitField;
    private ComboBox<Zutat> zutatAuswahl;

    private ZutatService zutatService;

    public AddZutatRow(ZutatService zutatService){
        this.zutatService = zutatService;
        configureMengeField();
        configureEinheitField();
        configureZutatComboBox();
    }

    private void configureZutatComboBox(){
        zutatAuswahl = new ComboBox<>();
        zutatAuswahl.setLabel("Zutat");
        zutatAuswahl.setRequired(true);
        zutatAuswahl.setAllowCustomValue(false);
        zutatAuswahl.addValueChangeListener( e -> {
           handleValueChange();
        });
        zutatAuswahl.addFocusListener(e->{
            zutatAuswahl.setItems(zutatService.getZutaten());
        });
        add(zutatAuswahl);
    }

    private void configureEinheitField(){
        einheitField = new TextField("Einheit");
        einheitField.setEnabled(false);
        add(einheitField);
    }

    private void handleValueChange() {
        if(zutatAuswahl.isEmpty()){
            einheitField.clear();
        }else{
            einheitField.setValue(zutatAuswahl.getValue().getEinheit().toString());
        }
    }

    private void configureMengeField(){
        mengeField = new NumberField("Menge");
        mengeField.setMin(1);
        mengeField.setMax(1000000);
        add(mengeField);
    }

    public boolean isFilled(){
        if(mengeField.isEmpty() && zutatAuswahl.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public Rezept_Zutat convertRowToRezeptZutat(){
        if(!isFilled()){
            Rezept_Zutat rezept_zutat = new Rezept_Zutat();
            rezept_zutat.setMenge(mengeField.getValue());
            return rezept_zutat;
        }else{
            return null;
        }
    }

    public Zutat getZutat(){
        return zutatAuswahl.getValue();
    }

    public double getMenge(){
        return mengeField.getValue();
    }

    public void setErrorZutat(){
    }


}
