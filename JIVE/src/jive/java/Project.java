package jive.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;

//TODO: Large BufferedImages use a lot of memory (7680 x 1200 is about 36mb), it might be better to store editing history as strings
//		or limit undo/redo to some number of actions.

/**
 * Project consists of methods to edit BufferedImage objects and manage editing projects.
 * 
 * Image editing functions are extended from the ImageEditor class.
 * All editing functions return a newly-edited BufferedImage object.
 * 
 * Edited images can be saved to file or converted to different raster file formats.
 * 
 * Undo and redo functionality can be implemented using the stateHistory and undoHistory
 * variables to store BufferedImage objects in memory until a new project is opened or
 * the ImageEditor instance is destroyed.
 * 
 * @author Devon Hunter
 * @author Craig Vandeventer
 * @author Casey Brown
 *
 */
public class Project extends ImageEditor
{
	private File imageFile;
	private Stack<BufferedImage> stateHistory;
	private Stack<BufferedImage> undoHistory;	
	private boolean hasUnsavedChanges;
	
	/**
	 * Initializes state history, undo history, and hasUnsavedChanges
	 * @param A File object of the image to create a project from
	 */
	public Project(File imageFile) throws IOException
	{
		super(ImageIO.read(imageFile));
		
		this.imageFile = imageFile;
		stateHistory = new Stack<BufferedImage>();
		undoHistory = new Stack<BufferedImage>();
		hasUnsavedChanges = false;
	}
	
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
	
	/**
	 * This method reverts the bufferedImage object to its previous state.
	 * @return A javaFX Image of the previous state
	 */
	public BufferedImage undo()
	{
		undoHistory.push(bufferedImage);
		bufferedImage = stateHistory.pop();
		return bufferedImage;
	}
	
	/**
	 * This method reverts the bufferedImage object to its previous state.
	 * @return A javaFX Image of the previous state
	 */
	public BufferedImage redo()
	{
		stateHistory.push(bufferedImage);
		bufferedImage = undoHistory.pop();
		return bufferedImage;
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
	 * Check if the project has unsaved changes
	 * @return true if it has unsaved changes, false otherwise
	 */
	public boolean hasUnsavedChanges()
	{
		return hasUnsavedChanges;
	}
	
	/**
	 * Get the name of the file of the current project
	 * @return The file name as a string
	 */
	public String getName()
	{
		return imageFile.getName();
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
