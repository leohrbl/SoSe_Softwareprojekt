package com.example.application.views.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ViewFrame extends VerticalLayout implements HasStyle {

    private String CLASS_NAME = "view-frame";

    private Div header;
    private Div content;
    private Div footer;
    private Div container;


    public ViewFrame() {

        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        header = new Div();
        header.setClassName(CLASS_NAME + "__header");

        content = new Div();
        content.setClassName(CLASS_NAME + "__content");

        footer = new Div();
        footer.setClassName(CLASS_NAME + "__footer");

        container = new Div();
        container.addClassName("viewframe-max-width");
        container.add(header, content, footer);
        this.add(container);
    }

    /**
     * Sets the header slot's components.
     */
    public void setViewHeader(Component... components) {
        header.removeAll();
        header.add(components);
    }

    /**
     * Sets the content slot's components.
     */
    public void setViewContent(Component... components) {
        content.removeAll();
        content.add(components);
    }

    /**
     * Sets the footer slot's components.
     */
    public void setViewFooter(Component... components) {
        footer.removeAll();
        footer.add(components);
    }


}
