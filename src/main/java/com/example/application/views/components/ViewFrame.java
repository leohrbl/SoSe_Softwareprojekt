package com.example.application.views.components;

import com.example.application.views.rezept.display.RezeptView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Die Klasse dient als ein Container der RezeptView-Klassen. Die Klasse hat keine Funktion außer die Lesbarkeit der RezeptView-Klassen zu erleichtern und grundlegende CSS regeln anzuwenden.
 *
 * @author Léo Hérubel
 * @see RezeptView
 * @see com.example.application.views.rezept.create.RezeptCreateView
 * @see com.example.application.views.rezept.edit.RezeptEditView
 */
public class ViewFrame extends VerticalLayout implements HasStyle {

    private final Div header;
    private final Div content;
    private final Div footer;
    private final Div container;

    /**
     * Konstruktor initialisiert die den Container und die Container-Komponenten (Header, Content, Footer)
     */
    public ViewFrame() {

        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        header = new Div();
        content = new Div();
        footer = new Div();
        container = new Div();
        container.addClassName("viewframe-max-width");
        container.add(header, content, footer);
        this.add(container);
    }

    /**
     * Die Methode setzt den Header
     */
    public void setViewHeader(Component... components) {
        header.removeAll();
        header.add(components);
    }

    /**
     * Die Methode setzt den Content
     */
    public void setViewContent(Component... components) {
        content.removeAll();
        content.add(components);
    }

    /**
     * Die Methode setzt den Footer
     */
    public void setViewFooter(Component... components) {
        footer.removeAll();
        footer.add(components);
    }
}
