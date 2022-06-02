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
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.server.StreamResource;

import java.awt.image.BufferedImage;

/**
 * 
 * @author Anna Karle
 *
 */

public class UploadBild extends Div {

	private Button uploadButton;

	private Image image;
	byte[] bytes;

	public UploadBild(RezeptEditView view, Button uploadButton, Image image, byte[] initalbytes) {
		this.image = image;
		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/gif");

		this.uploadButton = uploadButton;
		uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		upload.setUploadButton(uploadButton);
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

		upload.getElement().addEventListener("upload-abort", new DomEventListener() {

			@Override
			public void handleEvent(com.vaadin.flow.dom.DomEvent event) {
				view.setBytes(initalbytes);
				image.getElement().setAttribute("src", new StreamResource("",
						() -> new ByteArrayInputStream(initalbytes)));
			}
		});
		add(upload);

	}

	// private byte[] getImageAsByteArray() {
	// try {
	// return
	// IOUtils.toByteArray(this.image.getClass().getResourceAsStream(this.image.getSrc()));
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public UploadBild(RezeptCreateView view, Button uploadButton, Image rezeptBild, byte[] initalbytes) {
		image = rezeptBild;
		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);
		upload.setAcceptedFileTypes("image/jpeg", "image/gif");

		this.uploadButton = uploadButton;
		uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		upload.setUploadButton(uploadButton);
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

		upload.getElement().addEventListener("upload-abort", new DomEventListener() {

			@Override
			public void handleEvent(com.vaadin.flow.dom.DomEvent event) {
				view.setBytes(initalbytes);
				image.getElement().setAttribute("src", new StreamResource("",
						() -> new ByteArrayInputStream(initalbytes)));
			}
		});
		add(upload);
	}

}
