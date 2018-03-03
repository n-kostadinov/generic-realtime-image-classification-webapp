package com.machinememos.javaland.demo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.BoundedInputStream;

public final class ImageRetriever {
	
	private static final int IMG_SIZE = 299;
	private static final long MAX_FILE_SIZE = 4 * 1024 * 1024;

	public static byte[] getPNGBytes(String imageUrl) throws MalformedURLException, IOException{
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try (InputStream inputStream = new BoundedInputStream(new URL(imageUrl).openStream(), MAX_FILE_SIZE)) {
			
			BufferedImage originalImage = ImageIO.read(inputStream);
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			BufferedImage resizedImage = resizeImage(originalImage, type, IMG_SIZE, IMG_SIZE);
			ImageIO.write(resizedImage, "png", baos);

		}
		return baos.toByteArray();
	}

	private static BufferedImage resizeImage(BufferedImage originalImage, int type, int newWidth, int newHeight) {

		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();

		return resizedImage;
	}
	
}
