package com.example.application.views.rezeptansicht;

import com.example.application.views.MainLayout;
import com.example.application.views.ViewFrame;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("unused")
@PageTitle("Rezept")
@Route(value = "display", layout = MainLayout.class)
public class RezeptView extends ViewFrame implements HasUrlParameter<String>, HasErrorParameter<NotFoundException> {

    private static long rezeptID;

    public RezeptView() {

    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try{

            setRezeptID(Long.parseLong(parameter));
            Notification.show(String.valueOf(getRezeptID()));
            // die Abfrage ob es das Rezept gibt ansonsten kann man hier nicht nur die ID speichern sondern auch das Rezept als Enität :)

            createViewLayout(   );

            if(rezeptID == 0){
                event.rerouteTo("");
            }
        }catch(Exception e){
            event.rerouteTo("");
        }

    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo("");
        return HttpServletResponse.SC_NOT_FOUND;
    }

    private void createViewLayout(){
        Label title = new Label("Döner mit Cocktailsoße");

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
        Paragraph text = new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");

        Image image = new Image("/images/doener.png", "Doener");
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
        this.rezeptID = rezeptID;
    }

    public long getRezeptID() {
        return rezeptID;
    }



}
