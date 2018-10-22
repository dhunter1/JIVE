package jive.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

/**
 * ImageEditor consists of methods to edit BufferedImage objects and manage editing projects.
 * 
 * Image editing functions are extended from the ToolKit class.
 * All editing functions return a newly-edited BufferedImage object.
 * 
 * Edited images can be saved to file or converted to different raster file formats.
 * 
 * Undo and redo functionality can be implemented using the stateHistory and undoHistory
 * variables to store BufferedImage objects in memory until a new project is opened or
 * the ImageEditor instance is destroyed.
 * 
 * @authors Devon Hunter, Craig Vandeventer, Casey Brown
 *
 */
public class ImageEditor extends ToolKit
{
	private File imageFile;
	private BufferedImage bufferedImage;
	private Stack<BufferedImage> stateHistory = new Stack<BufferedImage>();
	private Stack<BufferedImage> undoHistory = new Stack<BufferedImage>();
	
	boolean hasUnsavedChanges = false;
	
	//TODO: implement this function
	public boolean save()
	{
		//write buffered image to file
		//hasUnsavedChanges = false;
		//if successful
		return true;
	}
	
	//TODO: implement this function
	public boolean saveAs(String fileName, String fileType)
	{
		//save buffered image as a specified file type
		//hasUnsavedChanges = false;
		//if successful
		return true;
	}
	
	/*
	 * This method reverts the bufferedImage object to its previous state.
	 * @return A javaFX Image of the previous state
	 */
	public BufferedImage undo()
	{
		undoHistory.push(bufferedImage);
		bufferedImage = stateHistory.pop();									//Revert bufferedImage to previous state
		return bufferedImage;
	}
	
	/*
	 * This method reverts the bufferedImage object to its previous state.
	 * @return A javaFX Image of the previous state
	 */
	public BufferedImage redo()
	{
		bufferedImage = undoHistory.pop();
		return bufferedImage;
	}
	
	/**
	 * Creates a new project with the supplied file.
	 * State and undo history is cleared and the BufferedImage object is updated to reflect the new file.
	 * @param file A File object containing the picture to be edited. Must be .jpg, .png, .bmp, or .gif.
	 * @return True if successful, false otherwise.
	 */
	public boolean newProject(File file)
	{
		imageFile = file;
		stateHistory.clear();
		undoHistory.clear();
		
		try
		{
			bufferedImage = ImageIO.read(imageFile);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	/**
	 * Pushes the ImageEditor's current BufferedImage object onto a stack of previous states.
	 * This method should be called before every editing function for undo/redo functionality.
	 */
	public void storeState()
	{
		stateHistory.push(bufferedImage);
	}
	
	/**
	 * Checks if there are BufferedImage objects in the undoHistory stack
	 * @return True if the stack is empty, false otherwise.
	 */
	public boolean undoHistoryIsEmpty()
	{
		return undoHistory.isEmpty();
	}
	
	/**
	 * Checks if there are BufferedImage objects in the stateHistory stack
	 * @return True if the stack is empty, false otherwise.
	 */
	public boolean stateHistoryIsEmpty()
	{
		return stateHistory.isEmpty();
	}
	
	/**
	 * Toggle the hasUnsavedChanges variable
	 * @param unsavedChanges
	 */
	public void setHasUnsavedChanges(boolean unsavedChanges)
	{
		hasUnsavedChanges = unsavedChanges;
	}
}
