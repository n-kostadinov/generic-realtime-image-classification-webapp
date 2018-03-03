package com.machinememos.javaland.demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public final class ImageConverter {
	
	private static final int IMG_SIZE = 299;
	private static final String IMAGE_ENCODE_FORMAT_PNG = "png";
	
	public String toBase64EncodedPNG(String base64EncodedJPEG) throws MalformedURLException, IOException{
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] jpegData = Base64.getDecoder().decode(base64EncodedJPEG);
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(jpegData));
		BufferedImage resizedImage = resizeImage(originalImage, BufferedImage.TYPE_INT_ARGB, IMG_SIZE, IMG_SIZE);
		ImageIO.write(resizedImage, IMAGE_ENCODE_FORMAT_PNG, byteArrayOutputStream);
		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		
		
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int type, int newWidth, int newHeight) {
		
		BufferedImage resizedImage = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		graphics.dispose();

		return resizedImage;
	}
	
}
