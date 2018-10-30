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
	
	public ImageEditor(BufferedImage image)
	{
		bufferedImage = image;
	}
	
	public BufferedImage rotateRight()
	{
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		AffineTransform rotateTransform = new AffineTransform();
				
		rotateTransform.translate(height / 2, width / 2);		//Images with a height and/or width not divisible by 2 are rounded down to the nearest even number
		rotateTransform.rotate(Math.PI / 2);
		rotateTransform.translate(width / -2, height / -2);
		
		AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
		bufferedImage = rotateOp.filter(bufferedImage, null);
		
		return bufferedImage;
	}
	
	public BufferedImage rotateLeft()
	{
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		
		AffineTransform rotateTransform = new AffineTransform();
		
		rotateTransform.translate(height / 2, width / 2);
		rotateTransform.rotate(Math.PI / -2);
		rotateTransform.translate(width / -2, height / -2);
		
		AffineTransformOp rotateOp = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
		bufferedImage = rotateOp.filter(bufferedImage, null);
		return bufferedImage;
	}
	
	public BufferedImage flipHorizontal()
	{
		AffineTransform flipTransform = AffineTransform.getScaleInstance(-1, 1);
		flipTransform.translate(-bufferedImage.getWidth(), 0);
		
		AffineTransformOp flipOp = new AffineTransformOp(flipTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = flipOp.filter(bufferedImage, null);
		return bufferedImage;
		
	}
	
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
