package com.example.application.views.upload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;


public class UploadBild extends Div {
	
	private Button uploadButton;
	private Image image = new Image();
	 byte[] bytes;
	
	public UploadBild(Button uploadButton) {
		MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        
        this.uploadButton = uploadButton;
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        upload.setUploadButton(uploadButton);
        upload.addSucceededListener(event -> {
        	
        	 String fileName = event.getFileName();
        	 InputStream fileData = buffer.getInputStream();
        	 try {
        		byte[] bytes = IOUtils.toByteArray(fileData);
				image.getElement().setAttribute("src", new StreamResource(fileName,
						() -> new ByteArrayInputStream(bytes)));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
        	
        	
        });
        
        add(upload);
        
	}
	
	
	private byte[] getImageAsByteArray() {
        try {
            return IOUtils.toByteArray(this.image.getClass().getResourceAsStream(this.image.getSrc()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	
	
	

    
}
