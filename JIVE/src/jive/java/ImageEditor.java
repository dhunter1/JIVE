package jive.java;

import java.awt.image.BufferedImage;

//TODO: implement all the functions

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
		return null;
	}
	
	public BufferedImage rotateLeft()
	{
		return null;
	}
	
	public BufferedImage flipHorizontal()
	{
		return null;
	}
	
	public BufferedImage flipVertical()
	{
		return null;
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
