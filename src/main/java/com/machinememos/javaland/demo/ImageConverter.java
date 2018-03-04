package com.machinememos.javaland.demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public final class ImageConverter {
	
	private static final int INCEPTION_V3_IMAGE_INPUT_SIZE = 299;
	private static final String IMAGE_ENCODE_FORMAT_JPG = "jpg";
	
	
	public String toBase64EncodedPNG(String base64EncodedJPEG) {
		
		try {
			
			return convert(base64EncodedJPEG);
		
		} catch(IOException ex){
			throw new IllegalStateException("Image conversion failed.", ex);
		}
		
	}

	private String convert(String base64EncodedJPEG) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] jpegData = Base64.getDecoder().decode(base64EncodedJPEG);
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(jpegData));
		BufferedImage resizedImage = resizeImage(originalImage);
		ImageIO.write(resizedImage, IMAGE_ENCODE_FORMAT_JPG, byteArrayOutputStream);
		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

	private BufferedImage resizeImage(BufferedImage originalImage) {
		
		BufferedImage resizedImage = 
				new BufferedImage(INCEPTION_V3_IMAGE_INPUT_SIZE, INCEPTION_V3_IMAGE_INPUT_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, INCEPTION_V3_IMAGE_INPUT_SIZE, INCEPTION_V3_IMAGE_INPUT_SIZE, null);
		graphics.dispose();

		return resizedImage;
	}
	
}
