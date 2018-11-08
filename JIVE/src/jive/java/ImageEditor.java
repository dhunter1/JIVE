package jive.java;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

//TODO: implement crop, resize, and metadata functions

/**
 * ImageEditor encompasses all image editing functions. 
 * The functions operate on the bufferedImage attribute and return edited BufferedImage objects
 * 
 * @author Devon Hunter
 * @author Craig Vandeventer
 * @author Casey Brown
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
				
		rotateTransform.translate(height / 2, width / 2);		//Images with a height and/or width not divisible by 2 are rounded down to the nearest even number
		rotateTransform.rotate(Math.PI / 2);
		rotateTransform.translate(width / -2, height / -2);
		
		BufferedImage newImage;
		
		if (imageType == BufferedImage.TYPE_BYTE_INDEXED)									//GIFs are converted to TYPE_INT_ARGB to preserve transparency and colors
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
	
	public BufferedImage crop(int x, int y)
	{
		//Need to calculate target width/height and call bufferedImage.getSubImage()
		return null;
	}
	
	public BufferedImage resize(double percentage)
	{
		return null;
	}
	
	public BufferedImage editMetadata()
	{
		return null;
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
