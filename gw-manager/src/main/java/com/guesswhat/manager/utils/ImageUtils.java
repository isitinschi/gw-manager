package com.guesswhat.manager.utils;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.guesswhat.manager.dto.ImageType;

public class ImageUtils {

	public static BufferedImage scaleImage(BufferedImage bufferedImage, ImageType imageType) {
		double scaleFactor = 1;
		switch (imageType) {
			case XXHDPI:	return bufferedImage;
			case XHDPI:		scaleFactor = 1 / 1.5;break;
			case HDPI:		scaleFactor = 1 / 2.0;break;
			case MDPI:		scaleFactor = 1 / 3.0;break;
			case LDPI:		scaleFactor = 1 / 4.0;break;
			default:   		return null;
		}
		
		AffineTransform tx = new AffineTransform();
		tx.scale(scaleFactor, scaleFactor);

		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BILINEAR);
		return op.filter(bufferedImage, null);
	}
	
	public static byte [] convertImageToByteArray(BufferedImage bufferedImage) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", os);
		} catch (IOException e) {
			MessageDialog.showErrorDialog("write", "image");
			return null;
		}
		
		return os.toByteArray();
	}
	
}
