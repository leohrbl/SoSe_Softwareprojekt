package com.example.application.views.components;

import com.example.application.data.rezeptzutat.Rezept_Zutat;
import com.example.application.data.zutat.Zutat;
import com.example.application.data.zutat.ZutatService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Diese Klasse erzeugt ein horizontales Layout, in dem Felder für die Menge,
 * die Zutat und die Einheit vorhanden sind.
 *
 * @author Lennart Rummel & Leo Herubel
 */

public class AddZutatRow extends HorizontalLayout {

    private final NumberField mengeField;
    private final TextField einheitField;
    private final ComboBox<Zutat> zutatAuswahl;

    private final ZutatService zutatService;

    /**
     * Im Konstruktor wird die Zutatenzeile erstellt und konfiguriert.
     *
     * @param zutatService
     */
    public AddZutatRow(ZutatService zutatService) {
        this.zutatService = zutatService;
        this.zutatAuswahl = new ComboBox<>();
        this.einheitField = new TextField("Einheit");
        this.mengeField = new NumberField("Menge");
        configureMengeField();
        configureEinheitField();
        configureZutatComboBox();
    }

    /**
     * Die Zutatenauswahl ComboBox wird konfiguriert.
     */
    private void configureZutatComboBox() {
        zutatAuswahl.setLabel("Zutat");
        zutatAuswahl.setRequired(true);
        zutatAuswahl.setAllowCustomValue(false);
        zutatAuswahl.addValueChangeListener(e -> {
            handleValueChange();
        });
        zutatAuswahl.addFocusListener(e -> {
            zutatAuswahl.setItems(zutatService.getZutaten());
        });
        add(zutatAuswahl);
    }

    /**
     * Das Einheiten-Feld wird konfiguriert.
     */
    private void configureEinheitField() {
        einheitField.setEnabled(false);
        add(einheitField);
    }

    /**
     * In dieser Methode wird der Fall behandelt, dass die eingegebene Zutat
     * geändert wird.
     * Dann wird ebenfalls die zugehörige Einheit aus dem Feld entfernt.
     */
    private void handleValueChange() {
        if (zutatAuswahl.isEmpty()) {
            einheitField.clear();
        } else {
            einheitField.setValue(zutatAuswahl.getValue().getEinheit().toString());
        }
    }

    /**
     * Hier wird das NumberField für die Menge konfiguriert und ein Minimum und
     * Maximum festgelegt.
     */
    private void configureMengeField() {
        mengeField.setMin(1);
        mengeField.setMax(1000000);
        add(mengeField);
    }

    /**
     * Diese Methode prüft, ob eine Zutatenzeile gefüllt sind.
     *
     * @return Gibt den Wahrheitswert zurück, ob die Felder der ZutatRow gefüllt
     *         sind.
     */
    public boolean isFilled() {
        if (mengeField.isEmpty() && zutatAuswahl.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * OUTDATED: Wird derzeit nicht genutzt.
     * 
     * @return
     */
    public Rezept_Zutat convertRowToRezeptZutat() {
        if (!isFilled()) {
            Rezept_Zutat rezept_zutat = new Rezept_Zutat();
            rezept_zutat.setMenge(mengeField.getValue());
            return rezept_zutat;
        } else {
            return null;
        }
    }

    /**
     * Es handelt sich um eine Getter-Methode, die die Zutat zurückgibt.
     *
     * @return Es wird das Zutatobjekt zurückgegeben.
     */
    public Zutat getZutat() {
        return zutatAuswahl.getValue();
    }

    /**
     * Es handelt sich um eine Getter-Methode, die die Menge zurückgibt.
     * 
     * @return
     */
    public double getMenge() {
        return mengeField.getValue();
    }

}
