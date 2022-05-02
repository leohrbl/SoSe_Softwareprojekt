package com.example.application.views.rezeptansicht;

import com.example.application.data.entity.Rezept;
import com.example.application.data.service.RezeptService;
import com.example.application.views.MainLayout;
import com.example.application.views.ViewFrame;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
@PageTitle("Rezept")
@Route(value = "display", layout = MainLayout.class)
public class RezeptView extends ViewFrame implements HasUrlParameter<String>, HasErrorParameter<NotFoundException> {

    private static long rezeptId;
    private RezeptService rezeptService;

    public RezeptView(RezeptService rezeptService) {
        this.rezeptService = rezeptService;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try{
            //setRezeptID(Long.parseLong(parameter));
            //Rezept rezept = rezeptService.findByTitel(parameter);
            //createViewLayout(rezept);
            /*
            if(rezeptId == 0){
                event.rerouteTo("");
            }
             */
        }catch(Exception e){
            event.rerouteTo("");
        }

    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo("");
        return HttpServletResponse.SC_NOT_FOUND;
    }

    private void createViewLayout(Rezept rezept){
        Label title = new Label(rezept.getTitel());

        Label kategorie = new Label("Fast-Food");
        kategorie.setClassName("rezept-kategorie-view");

        Paragraph portionen = new Paragraph("Zutaten für ");
        Paragraph portionen2 = new Paragraph("Portionen");
        IntegerField portionenInput = new IntegerField();
        portionenInput.setMin(1);
        portionenInput.setValue(1);
        portionenInput.setMax(20);
        portionenInput.setHasControls(true);

        VerticalLayout vLayout = new VerticalLayout();
        vLayout.setWidth("100%");
        vLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout portionenLayout = new HorizontalLayout(portionen, portionenInput, portionen2);
        portionenLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        vLayout.add(portionenLayout);


        Button print = new Button(new Icon(VaadinIcon.EDIT));
        Button external = new Button(new Icon(VaadinIcon.CLOSE));
        external.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout header = new HorizontalLayout(kategorie, title, print, external);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1, title);
        header.setPadding(true);
        header.setSpacing(true);

        setViewHeader(header);
        // Content
        Label zubereitung = new Label("Zubereitung:");
        Paragraph text = new Paragraph(rezept.getZubereitung());

        Image image = rezept.getBild();
        image.setWidth("100%");
        image.setHeight("100%");
        image.addClassName("image");

        //hier dann auch noch das grid mit den Zutaten/Mengen Objekten zu dem Rezept lul
        VerticalLayout content = new VerticalLayout(image, vLayout, zubereitung, text);
        content.setPadding(true);


        setViewContent(content);


        // Footer
        Button printBtn = new Button("Drucken" ,VaadinIcon.PRINT.create());

        HorizontalLayout footer = new HorizontalLayout(printBtn);
        footer.setPadding(true);
        footer.setSpacing(true);

        setViewFooter(footer);

    }

    public void setRezeptID(long rezeptID) {
        this.rezeptId = rezeptID;
    }

    public long getRezeptID() {
        return rezeptId;
    }



}
