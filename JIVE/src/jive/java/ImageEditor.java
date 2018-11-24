package jive.java;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

/**
 * ImageEditor encompasses all image editing functions. 
 * The functions operate on the bufferedImage attribute and return edited BufferedImage objects
 * 
 * @author Devon Hunter
 *
 */
public class ImageEditor
{
	protected BufferedImage bufferedImage;
	private int imageType;
	
	public ImageEditor(BufferedImage image)
	{
		bufferedImage = image;
		imageType = bufferedImage.getType();
	}
	
	/**
	 * Uses an AffineTransform to rotate a BufferedImage 90 degrees clockwise.
	 * This function converts GIFs to TYPE_INT_ARGB to preserve transparency
	 * @return A rotated BufferedImage
	 */
	public BufferedImage rotateRight()
	{
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		AffineTransform rotateTransform = new AffineTransform();
				
		rotateTransform.translate(height / 2, width / 2);	//Images with a height and/or width not divisible by 2 are rounded down to the nearest even number
		rotateTransform.rotate(Math.PI / 2);
		rotateTransform.translate(width / -2, height / -2);
		
		BufferedImage newImage;
		
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)	//Indexed images are converted to TYPE_INT_ARGB to preserve transparency and colors
			newImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
		else
			newImage = new BufferedImage(height, width, imageType);
		
		AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(bufferedImage, newImage);
		bufferedImage = newImage;
		newImage.flush();
		
		return bufferedImage;
	}
	
	/**
	 * Uses an AffineTransform to rotate a BufferedImage 90 degrees counter-clockwise.
	 * This function converts GIFs to TYPE_INT_ARGB to preserve transparency
	 * @return A rotated BufferedImage
	 */
	public BufferedImage rotateLeft()
	{
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		AffineTransform rotateTransform = new AffineTransform();
		
		rotateTransform.translate(height / 2, width / 2);
		rotateTransform.rotate(Math.PI / -2);
		rotateTransform.translate(width / -2, height / -2);
		
		BufferedImage newImage;
		
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)
			newImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);
		else
			newImage = new BufferedImage(height, width, imageType);
		
		AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(bufferedImage, newImage);
		bufferedImage = newImage;
		newImage.flush();
		return bufferedImage;
	}
	
	/**
	 * Mirrors the bufferedImage horizontally
	 * @return A flipped BufferedImage
	 */
	public BufferedImage flipHorizontal()
	{
		AffineTransform flipTransform = AffineTransform.getScaleInstance(-1, 1);
		flipTransform.translate(-bufferedImage.getWidth(), 0);
		
		AffineTransformOp flipOp = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = flipOp.filter(bufferedImage, null);
		return bufferedImage;
		
	}
	
	/**
	 * Mirrors the bufferedImage vertically
	 * @return A flipped BufferedImage
	 */
	public BufferedImage flipVertical()
	{
		AffineTransform flipTransform = AffineTransform.getScaleInstance(1, -1);
		flipTransform.translate(0, -bufferedImage.getHeight());
		
		AffineTransformOp flipOp = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = flipOp.filter(bufferedImage, null);
		return bufferedImage;
	}

	/**
	 * Crops the bufferedImage using the specified coordinates and dimensions
	 * 
	 * @param x - The X coordinate of the upper-left corner of the crop area
	 * @param y - The Y coordinate of the upper-left corner of the crop area
	 * @param width - The width of the crop area
	 * @param height - The height of the crop area
	 * @return A cropped BufferedImage
	 */
	public BufferedImage crop(int x, int y, int width, int height)
	{
		bufferedImage = bufferedImage.getSubimage(x, y, width, height);
		return bufferedImage;
	}
	
	/**
	 * Resizes the bufferedImage by the given factor
	 * @param scaleFactor - The percent to scale by, between 0.0 and 1.0
	 * @return A resized BufferedImage
	 */
	public BufferedImage resize(double scaleFactor)
	{
		int newWidth = (int) (bufferedImage.getWidth() * scaleFactor);
		int newHeight = (int) (bufferedImage.getHeight() * scaleFactor);
		
		AffineTransform scaleTransform = new AffineTransform();
		scaleTransform.scale(scaleFactor, scaleFactor);
		
		BufferedImage newImage;
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)
			newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		else
			newImage = new BufferedImage(newWidth, newHeight, imageType);
		
		AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		scaleOp.filter(bufferedImage, newImage);
		bufferedImage = newImage;
		newImage.flush();
		return bufferedImage;
	}
	
	/*
	 * Adjusts the brightness and contrast of a BufferedImage using RescaleOp.
	 * RescaleOp uses a version of the following algorithm to adjust brightness and contrast:
	 * 
	 * newPixelColor = scaleFactor(currentPixelColor) + offset
	 * 
	 * scaleFactor affects the contrast and offset affects the brightness.
	 * 
	 * @param brightnessAdjustment - the offset to be applied to each pixel (-100.0 to 100)
	 * @param contrastAdjustment - the value to scale the pixel by (0.0 to 2.0)
	 * @return a BufferedImage with the appropriate brightness and contrast adjustments
	 */	
	public BufferedImage adjustBrightnessContrast(double brightnessAdjustment, double contrastAdjustment)
	{			
		//RescaleOp doesn't support images with indexed color
		//Images with indexed color (PNGs and GIFs, typically) are converted to ARGB
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)
		{
			BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			newImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
			bufferedImage.flush();
			bufferedImage = newImage;
		}
		
		RescaleOp brightnessOp;
		float offset = (float) brightnessAdjustment;
		float scaleFactor = (float) contrastAdjustment;
		
		if (bufferedImage.getColorModel().hasAlpha())
		{
			float[] scaleFactors = {scaleFactor, scaleFactor, scaleFactor, scaleFactor};
			float[] offsets = {offset, offset, offset, 0};	//Alpha channel is not adjusted
			brightnessOp = new RescaleOp(scaleFactors, offsets, null);
		}
		else
		{
			brightnessOp = new RescaleOp(scaleFactor, offset, null);
		}
				
		bufferedImage = brightnessOp.filter(bufferedImage, null);
		
		return bufferedImage;
	}
	
	/**
	 * Same as adjustBrightnessContrast(), but adjustments are not saved to the bufferedImage attribute
	 * @param brightnessAdjustment
	 * @param contrastAdjustment
	 * @return A BufferedImage with the appropriate adjustments
	 */
	public BufferedImage previewBrightnessContrast(double brightnessAdjustment, double contrastAdjustment)
	{			
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)
		{
			BufferedImage newImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			newImage.createGraphics().drawImage(bufferedImage, 0, 0, null);
			bufferedImage.flush();
			bufferedImage = newImage;
		}
		
		RescaleOp rescaleOp;
		float offset = (float) brightnessAdjustment;
		float scaleFactor = (float) contrastAdjustment;
		
		if (bufferedImage.getColorModel().hasAlpha())
		{
			float[] scaleFactors = {scaleFactor, scaleFactor, scaleFactor, scaleFactor};
			float[] offsets = {offset, offset, offset, 0};
			rescaleOp = new RescaleOp(scaleFactors, offsets, null);
		}
		else
		{
			rescaleOp = new RescaleOp(scaleFactor, offset, null);
		}
				
		BufferedImage preview = rescaleOp.filter(bufferedImage, null);
		
		return preview;
	}
	
	/**
	 * @return a reference to the bufferedImage attribute
	 */
	public BufferedImage getImage()
	{
		return bufferedImage;
	}
	
	/**
	 * @return The height of the class's BufferedImage attribute
	 */
	public int getHeight()
	{
		return bufferedImage.getHeight();
	}
	
	/**
	 * @return The width of the class's BufferedImage attribute
	 */
	public int getWidth()
	{
		return bufferedImage.getWidth();
	}
}
