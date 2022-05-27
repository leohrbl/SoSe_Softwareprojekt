package com.example.application.views.einkaufsliste;

import com.example.application.data.entity.EinkaufslistenEintrag;
import com.example.application.data.service.EinkaufslistenService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.LoadMode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.vaadin.addon.componentexport.java.PdfFromComponent;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.persistence.criteria.Order;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


//@JsModule("html2pdf.bundle.min.js")
@Route(value = "printlist")
public class PrintView extends VerticalLayout {


    private Grid<EinkaufslistenEintrag> einkaufsGrid;

    private EinkaufslistenService einkaufslistenService;

    private H1 header;

    private Button print;

    TemplateEngine templateEngine;

    ServletContext servletContext;

    ServletContextTemplateResolver templateResolver;

    public PrintView( EinkaufslistenService einkaufslistenService){
        this.einkaufslistenService = einkaufslistenService;
        this.einkaufsGrid = new Grid<>(EinkaufslistenEintrag.class, false);
        //this.templateEngine = new TemplateEngine();
        //this.templateResolver = new ServletContextTemplateResolver(servletContext);
        configureGrid();
        createHeader();
        createButton();
        setId("element-to-print");

        add(print, header, einkaufsGrid);
        System.out.println("instanz erstellt");
        UI.getCurrent().getPage().addJavaScript("https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js");

        PdfFromComponent factory = new PdfFromComponent();



        /**
        OutputStream fileOutputStream = new FileOutputStream("Einkauflsliste.pdf");

        String html = UI.getCurrent().getElement().getOuterHTML();

        String vhtml = getElement().getOuterHTML();

        System.out.println(html);
        System.out.println(vhtml);

        HtmlConverter.convertToPdf(vhtml, fileOutputStream);
        fileOutputStream.close();

        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setTemplateMode(TemplateMode.HTML);
         */

        //Context context = new Context();

        //getElement().executeJs("");



        //generatePdfFromHtml(getElement().getOuterHTML());

        //String inhalt = String.valueOf(getElement().executeJs("document.documentElement.innerHTML;"));
        //System.out.println(inhalt);

        //generatePdfFromHtml("String");

        //getElement();












    }

    public void createPDF() throws IOException {
        PDDocument document = new PDDocument();

        PDPage page = new PDPage();
        document.addPage(page);
        document.save("C:\\PDF\\pdf.pdf");
        document.close();




    }



    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("to", "Baeldung");



        return templateEngine.process("thymeleaf_template", context);
    }

    public void generatePdfFromHtml(String html) throws IOException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
        System.out.println(outputFolder);
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocumentFromString(html);
        renderer.layout();

        renderer.createPDF(outputStream);

        outputStream.close();
    }

    @RequestMapping(path = "/pdf")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* Do Business Logic*/



        /* Create HTML using Thymeleaf template Engine */

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("orderEntry", "Hallo");
        String orderHtml = templateEngine.process("order", context);




        /* Setup Source and target I/O streams */

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        /*Setup converter properties. */
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:8080");

        /* Call convert method */
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);

        /* extract output as bytes */
        byte[] bytes = target.toByteArray();


        /* Send the response as downloadable PDF */

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

    public void createButton(){
        Icon icon = new Icon(VaadinIcon.PRINT);
        print = new Button(icon);
        print.setId("hidden");
        print.addClickListener(e -> {

            UI.getCurrent().getPage().executeJs("var element = document.body;\n" +
                    "html2pdf(element);");


        });
    }

    private void createHeader(){
        this.header = new H1("Ihre Einkaufsliste");
        this.header.setId("section-to-print");
    }

    private void configureGrid() {
        //einkaufsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getZutat).setHeader("Zutat").setAutoWidth(true);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getMenge).setHeader("Menge").setAutoWidth(true);
        einkaufsGrid.addColumn(EinkaufslistenEintrag::getEinheitByZutat).setHeader("Einheit").setAutoWidth(true);
        einkaufsGrid.setWidth("90%");
        einkaufsGrid.setAllRowsVisible(true);
        //einkaufsGrid.setVerticalScrollingEnabled(false);
        einkaufsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        einkaufsGrid.setId("section-to-print");
        setEintraege();
    }

    public void setEintraege(){
        einkaufsGrid.setItems(einkaufslistenService.getAllEintrag());

    }

}
