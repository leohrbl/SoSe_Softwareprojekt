package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse DeleteDialog erzeugt ein Dialogfenster, was zum Löschen von Zutaten, Kategorien und Einheiten verwendet
 * werden kann. Die Funktionalität der enthaltenen Buttons muss jedoch in den aufrufenden Klassen implementiert werden.
 * @author Lennart Rummel
 */
public class DeleteDialog extends Dialog {

    H5 title;
    Button deleteButton;
    Button cancelButton;
    Label textfield;
    String delName;

    /**
     * Der DeleteDialog wird erzeugt.
     *
     * @param title ist der Titel also z.B. Zutat, Einheit, Kategorie...
     * @param delName ist der Name der Zutat, Einheit, Kategorie...
     * @param text ist der Infotext unter dem Titel.
     */
    public DeleteDialog(String title, String delName, String text){
        this.title = new H5(title + " '"+ delName+"'"+ " löschen?");
        this.title.getStyle().set("margin-top", "0");
        this.delName = delName;
        this.textfield = new Label(text);

        configureButtons();
        buildDialog();
    }

    /**
     * Der Dialog wird zusammengebaut einem Layout zugeordnet.
     */
    private void buildDialog() {
        add(this.title,this.textfield);
        add(new HorizontalLayout(this.deleteButton, this.cancelButton));
    }

    /**
     * Die Buttons innerhalb des Dialogfensters werden benannt und ihnen wird ein Theme zugeordnet.
     */
    private void configureButtons(){
        this.deleteButton = new Button("Löschen");
        this.cancelButton = new Button("Abbrechen");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}
