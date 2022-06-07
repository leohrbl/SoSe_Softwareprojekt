package com.example.application.views.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.example.application.views.rezept.create.RezeptCreateView;
import com.example.application.views.rezept.edit.RezeptEditView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.server.StreamResource;

import elemental.json.Json;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Anna Karle & Philipp Laupichler
 *         Upload Component für RezeptEditView und RezeptCreateView
 *
 */

public class UploadBild extends Div {

	private Button uploadButton;

	private Image image;
	byte[] bytes;
	final Paragraph hint = new Paragraph("Maximale Datei Größe: 5 MB");
	Upload upload;

	/**
	 * Konstruktor für Rezepteditview
	 * 
	 * @param view         RezeptEditView
	 * @param uploadButton Uploadbutton, der in der View implementiert wurde
	 * @param image        Image, dass sich in der View befindet
	 * @param initalbytes  Bytes, die zum Start gespeichert wurden
	 */
	public UploadBild(RezeptEditView view, Button uploadButton, Image image, byte[] initalbytes) {

		MemoryBuffer buffer = new MemoryBuffer();
		upload = new Upload(buffer);

		configureUpload(uploadButton, image, upload);

		/**
		 * SuccededListener
		 * Das Bild wird eingelesen und als Buffered Image eingelesen, danach als
		 * *jpg* in einem Byte Array gespeichert und die Bytes der View werden neu
		 * gesetzt. Des Weiteren wird das Bild in der View geupdatet
		 */
		upload.addSucceededListener(event -> {

			String fileName = event.getFileName();

			try {
				// The image can be jpg png or gif, but we store it always as jpg file in this
				// example
				BufferedImage inputImage = ImageIO.read(buffer.getInputStream());
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "jpg", pngContent);
				bytes = (pngContent.toByteArray());
				view.setBytes(bytes);
				image.getElement().setAttribute("src", new StreamResource(fileName,
						() -> new ByteArrayInputStream(bytes)));

			} catch (IOException e) {

				e.printStackTrace();
			}

		});
		upload.addFileRejectedListener(event -> {
			rejectedNotification();
		});
		upload.addFailedListener(event -> {
			failedNotification();
		});
		/**
		 * EventListener, falls das hochgeladene Bild entfernt wird, die Bytes und das
		 * Image in der View werden auf die inital Werte zurückgesetzt
		 */
		upload.getElement().addEventListener("upload-abort", new DomEventListener() {

			@Override
			public void handleEvent(com.vaadin.flow.dom.DomEvent event) {
				view.setBytes(initalbytes);
				image.getElement().setAttribute("src", new StreamResource("",
						() -> new ByteArrayInputStream(initalbytes)));
			}
		});
		add(hint, upload);

	}

	/*
	 * Notification, falls das Bild, welches versucht wurden hochzuladen, zu groß
	 * ist
	 */
	private void rejectedNotification() {
		Notification.show("Das Bild konnte nicht hochgeladen werden, bitte Größe prüfen!")
				.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	/*
	 * Notification, falls der Upload fehlgeschlagen ist
	 */
	private void failedNotification() {
		Notification.show("Es ist ein Fehler aufgetreten, bitte erneut versuchen")
				.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}

	/**
	 * Konstruktor für Rezepteditview
	 * 
	 * @param view         RezeptCreateView
	 * @param uploadButton Uploadbutton, der in der View implementiert wurde
	 * @param image        Image, dass sich in der View befindet
	 * @param initalbytes  Bytes, die zum Start gespeichert wurden
	 */
	public UploadBild(RezeptCreateView view, Button uploadButton, Image rezeptBild, byte[] initalbytes) {

		MemoryBuffer buffer = new MemoryBuffer();
		upload = new Upload(buffer);
		configureUpload(uploadButton, rezeptBild, upload);
		/**
		 * SuccededListener
		 * Das Bild wird eingelesen und als Buffered Image eingelesen, danach als
		 * *jpg* in einem Byte Array gespeichert und die Bytes der View werden neu
		 * gesetzt. Des Weiteren wird das Bild in der View geupdatet
		 */
		upload.addSucceededListener(event -> {

			String fileName = event.getFileName();

			try {
				BufferedImage inputImage = ImageIO.read(buffer.getInputStream());
				ByteArrayOutputStream pngContent = new ByteArrayOutputStream();
				ImageIO.write(inputImage, "jpg", pngContent);
				bytes = (pngContent.toByteArray());
				view.setBytes(bytes);
				image.getElement().setAttribute("src", new StreamResource(fileName,
						() -> new ByteArrayInputStream(bytes)));
			} catch (IOException e) {

				e.printStackTrace();
			}

		});

		upload.addFileRejectedListener(event -> {
			rejectedNotification();
		});
		upload.addFailedListener(event -> {
			rejectedNotification();
		});
		/**
		 * EventListener, falls das hochgeladene Bild entfernt wird, die Bytes und das
		 * Image in der View werden auf die inital Werte zurückgesetzt
		 */
		upload.getElement().addEventListener("upload-abort", new DomEventListener() {

			@Override
			public void handleEvent(com.vaadin.flow.dom.DomEvent event) {
				view.setBytes(initalbytes);
				image.getElement().setAttribute("src", new StreamResource("",
						() -> new ByteArrayInputStream(initalbytes)));
			}
		});
		add(hint, upload);
	}

	/**
	 * Der Upload wird konfiguriert, es werden die akzeptierten Dateitypen, das
	 * Label, der Button und die maximale Dateigröße gesetzt
	 * 
	 * @param uploadButton UploadButton aus der View
	 * @param rezeptBild   Das Bild, welches vor dem Bild in der View zu sehen war
	 * @param upload       Uploader Vaadin
	 */
	private void configureUpload(Button uploadButton, Image rezeptBild, Upload upload) {
		image = rezeptBild;
		upload.setAcceptedFileTypes("image/jpeg", "image/gif");
		this.uploadButton = uploadButton;
		uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		upload.setUploadButton(uploadButton);
		upload.setDropLabel(new Label("Datei einfügen"));
		upload.setMaxFileSize(5000000);

	}

	/**
	 * Methode, die das Bild zurücksetzt und die Liste der hochgeladene Bilder leert
	 * 
	 * @param initalbytes Bytes, von dem Bild, welches anfangs zu sehen war
	 */
	public void clear(byte[] initalbytes) {
		image.getElement().setAttribute("src", new StreamResource("",
				() -> new ByteArrayInputStream(initalbytes)));
		this.upload.getElement().setPropertyJson("files", Json.createArray());
	}
}
