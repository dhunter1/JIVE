package jive.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//TODO: Enable save button automatically if there are unsaved changes

/**
 * This class is the controller class for the JIVE GUI.
 * 
 * It is responsible for handling user input and coordinating ImageViewer and ImageEditor functions.
 * 
 * @authors Devon Hunter, Craig Vandeventer, Casey Brown
 *
 */
public class GUI
{	
	final List<String> COMPATIBLE_FORMATS = Arrays.asList("jpg", "png", "bmp", "gif");
	
	Stage stage;
	ImageViewer imageViewer;
	ImageEditor imageEditor;
	
	@FXML
	private AnchorPane mainPane;
	
	@FXML
	private AnchorPane viewerPane;
	
	@FXML
	private Button saveButton;
	
	@FXML
	private Button undoButton;
	
	@FXML
	private Button redoButton;
	
	@FXML
	private Button rotateRightButton;
	
	@FXML
	private Button rotateLeftButton;
	
	@FXML
	private Button flipHorizontalButton;
	
	@FXML
	private Button flipVerticalButton;
	
	@FXML
	private Button cropButton;
	
	@FXML
	private Button resizeButton;
	
	@FXML
	private Button editMetadataButton;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label sizeLabel;
	
	public void initialize()
	{		
		imageEditor = new ImageEditor();
		imageViewer = new ImageViewer();
		viewerPane.getChildren().add(imageViewer);
		
		AnchorPane.setTopAnchor(imageViewer, 0.0);			//Anchor the imageViewer node to the viewerPane to resize the imageViewer with the stage
		AnchorPane.setRightAnchor(imageViewer, 0.0);
		AnchorPane.setLeftAnchor(imageViewer, 0.0);
		AnchorPane.setBottomAnchor(imageViewer, 0.0);
	}
		
	@FXML void openFileAction()
	{
		openFile();
	}
	
	//TODO:
	@FXML void saveAsAction() 
	{
		//Not sure how to do this
		//Need to get file name and type from user
		//then call imageEditor.saveAs(fileName, fileType)?
	}
	
	//TODO:
	@FXML void helpAction() 
	{
		//Need to decide how to display user manual
		//Either in-application or maybe html file?
	}
	
	@FXML void saveButtonAction() 
	{
		imageEditor.save();
		saveButton.setDisable(true);
	}
	
	@FXML void undoButtonAction() 
	{
		BufferedImage image = imageEditor.undo();
		imageViewer.update(SwingFXUtils.toFXImage(image, null));
		
		if (imageEditor.stateHistoryIsEmpty())
		{
			undoButton.setDisable(true);
			saveButton.setDisable(true);
			imageEditor.setHasUnsavedChanges(false);
		}
	}
	
	@FXML void redoButtonAction() 
	{
		BufferedImage image = imageEditor.undo();
		imageViewer.update(SwingFXUtils.toFXImage(image, null));
		imageEditor.setHasUnsavedChanges(true);
		
		if (imageEditor.undoHistoryIsEmpty())
			redoButton.setDisable(true);
	}
	
	//TODO:
	@FXML void rotateRightAction() 
	{
		
	}
	
	//TODO:
	@FXML void rotateLeftAction() 
	{
		
	}
	
	//TODO:
	@FXML void flipHorizontalAction() 
	{
		
	}
	
	//TODO:
	@FXML void flipVerticalAction() 
	{
		
	}
	
	//TODO:
	@FXML void cropAction() 
	{
		
	}
	
	//TODO:
	@FXML void resizeAction() 
	{
		
	}
	
	//TODO:
	@FXML void editMetadataAction() 
	{
		
	}
		
	/*
	 * This method opens a user-selected image file for viewing and editing
	 */
	public void openFile()
	{
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.bmp", "*.gif");
		fileChooser.getExtensionFilters().add(filter);
		File imageFile = fileChooser.showOpenDialog(stage);
				
		if (imageFile != null)
		{
			Image image;
			
			String fileName = imageFile.getName();																//Users can enter non-image files manually, so additional validation is done here
			int extensionIndex = fileName.lastIndexOf(".");
			String extension = fileName.substring(extensionIndex + 1);
			
			if (!(COMPATIBLE_FORMATS.contains(extension)))
			{
				Alert alert = new Alert(AlertType.ERROR, "JIVE does not support ." + extension + " files.");	//This could be improved to look more modern
				alert.setHeaderText(null);
				GaussianBlur blur = new GaussianBlur(5);
				mainPane.setEffect(blur);
				alert.showAndWait();
				mainPane.setEffect(null);
				return;
			}
			
			try
			{
				image = new Image(imageFile.toURI().toURL().toExternalForm());
				imageViewer.update(image);
				imageEditor.newProject(imageFile);																//Should handle failure of this graphically
				nameLabel.setText(imageFile.getName());
				sizeLabel.setText((int)image.getWidth() + " x " + (int)image.getHeight());
			} 
			catch (MalformedURLException e)
			{
				//TODO: should handle exception graphically also
				e.printStackTrace();
			}
			
			rotateRightButton.setDisable(false);
			rotateLeftButton.setDisable(false);
			flipHorizontalButton.setDisable(false);
			flipVerticalButton.setDisable(false);
			cropButton.setDisable(false);
			resizeButton.setDisable(false);
			editMetadataButton.setDisable(false);
		}
	}
	
	/*
	 * This function is used to set a reference to the stage from the Main class
	 */
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
}
