package jive.java;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class is the controller class for the JIVE GUI.
 * 
 * It is responsible for handling user input and coordinating image viewing and editing functions.
 * 
 * @author Devon Hunter
 * @author Casey Brown
 *
 */
public class GUI
{	
	final List<String> COMPATIBLE_FORMATS = Arrays.asList("*.jpg", "*.png", "*.bmp", "*.gif");
	
	Stage stage;
	ImageViewer imageViewer;
	CropSelector cropSelector;
	Project project;
	boolean redoAvailable = false;	//Redo is only available immediately after the undo function is used
	
	@FXML
	private AnchorPane mainPane;	
	@FXML
	private AnchorPane viewerPane;
	@FXML
	private StackPane functionPane;
	@FXML
	private HBox editingBox;
	@FXML
	private HBox cropBox;
	@FXML
	private HBox resizeBox;
	@FXML
	private HBox brightnessBox;
	@FXML
	private MenuItem saveAsItem;
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
	private Button editBrightnessButton;
	@FXML
	private Button confirmCropButton;
	@FXML
	private Button cancelCropButton;
	@FXML
	private Button confirmResizeButton;
	@FXML
	private Button cancelResizeButton;
	@FXML
	private Button confirmBrightnessButton;
	@FXML
	private Button cancelBrightnessButton;
	@FXML
	private Slider brightnessSlider;
	@FXML
	private Slider contrastSlider;
	@FXML
	private Slider resizeSlider;
	@FXML
	private Label nameLabel;
	@FXML
	private Label sizeLabel;
	@FXML
	private Label resizePercentLabel;
	@FXML
	private Label newDimensionsLabel;
	@FXML
	private Label brightnessLabel;
	@FXML
	private Label contrastLabel;
	
	public void initialize()
	{		
		imageViewer = new ImageViewer();
		viewerPane.getChildren().add(imageViewer);
		
		//Anchor the imageViewer node to the viewerPane to resize the imageViewer with the stage
		AnchorPane.setTopAnchor(imageViewer, 0.0);
		AnchorPane.setRightAnchor(imageViewer, 0.0);
		AnchorPane.setLeftAnchor(imageViewer, 0.0);
		AnchorPane.setBottomAnchor(imageViewer, 0.0);
		
		resizeSlider.valueProperty().addListener(resizeSliderListener);
		brightnessSlider.valueProperty().addListener(brightnessSliderListener);
		contrastSlider.valueProperty().addListener(contrastSliderListener);
		
		mainPane.setOnKeyPressed(event -> 
		{
			if (event.getCode() == KeyCode.O && event.isControlDown())
			{
				openFile();
			}
			if (event.getCode() == KeyCode.S && event.isControlDown())
			{
				if (project != null && project.hasUnsavedChanges())
					saveButtonAction();
			}
			if (event.getCode() == KeyCode.Z && event.isControlDown())
			{
				if (project != null && !project.stateHistoryIsEmpty())
					undoButtonAction();
			}
			if (event.getCode() == KeyCode.Y && event.isControlDown())
			{
				if (redoAvailable && !project.undoHistoryIsEmpty())
					redoButtonAction();
			}
		});
	}
		
	@FXML void openFileAction()
	{
		openFile();
	}
	
	@FXML void saveAsAction() 
	{
		String currFileExt = "*." + project.getFileExtension();
		FileChooser fileChooser = new FileChooser();
		
		//Filters appear in the order they are added, so the current extension is added first
		FileChooser.ExtensionFilter filter;
		filter = new FileChooser.ExtensionFilter(currFileExt, currFileExt);
		fileChooser.getExtensionFilters().add(filter);
		
		for (String ext : COMPATIBLE_FORMATS)
		{
			if (!ext.equals(currFileExt))
			{
				filter = new FileChooser.ExtensionFilter(ext, ext);
				fileChooser.getExtensionFilters().add(filter);
			}
		}
		
		fileChooser.setTitle("JIVE - Save As");
		fileChooser.setInitialFileName(project.getName());
		
		File savedFile = fileChooser.showSaveDialog(stage);
		
		if (savedFile != null)
		{	
			if (project.saveAs(savedFile))
				updateGUI();
			else
				createAlert("Error: could not save image");
		}		
	}
	
	@FXML void helpAction() 
	{
		String helpFile = getClass().getResource("/jive/resources/JIVE_User_Manual.html").toExternalForm();
		Stage helpStage = new Stage();
		helpStage.setResizable(false);
		helpStage.setTitle("JIVE - User Manual");
		StackPane pane = new StackPane();
		pane.setStyle("-fx-background-color:red");
		WebView webView = new WebView();
		pane.getChildren().add(webView);
		webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/jive/resources/WebViewManual.css").toString());
		webView.getEngine().load(helpFile);
		Scene root = new Scene(pane);
		helpStage.setScene(root);
		helpStage.getIcons().add(new Image(getClass().getResourceAsStream("/jive/resources/icons/JiveIcon.png")));
		helpStage.show();
	}
	
	@FXML void saveButtonAction() 
	{
		if (project.save())
			updateGUI();
		else
			createAlert("Error: could not save image");
	}
	
	@FXML void undoButtonAction() 
	{
		BufferedImage newImage = project.undo();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		
		if (project.stateHistoryIsEmpty())
			project.setHasUnsavedChanges(false);
		else
			project.setHasUnsavedChanges(true);
		
		redoAvailable = true;
		updateGUI();
	}
	
	@FXML void redoButtonAction() 
	{
		BufferedImage newImage = project.redo();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = true;
		updateGUI();
	}
	
	@FXML void rotateRightAction() 
	{
		project.storeState();
		BufferedImage newImage = project.rotateRight();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
	}
	
	@FXML void rotateLeftAction() 
	{
		project.storeState();
		BufferedImage newImage = project.rotateLeft();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
	}
	
	@FXML void flipHorizontalAction() 
	{
		project.storeState();
		BufferedImage newImage = project.flipHorizontal();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
	}
	
	@FXML void flipVerticalAction() 
	{
		project.storeState();
		BufferedImage newImage = project.flipVertical();
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
	}
	
	@FXML void cropAction() 
	{
		cropBox.toFront();
		cropSelector = new CropSelector(imageViewer, imageViewer.imageView, confirmCropButton);
	}
	
	@FXML void confirmCropAction()
	{
		project.storeState();
		int x = cropSelector.getCropX();
		int y = cropSelector.getCropY();
		int width = cropSelector.getCropWidth();
		int height = cropSelector.getCropHeight();
		BufferedImage newImage = project.crop(x, y, width, height);
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
		cropSelector.remove();
		editingBox.toFront();
	}
	
	@FXML void cancelCropAction()
	{
		editingBox.toFront();
		cropSelector.remove();
	}
	
	@FXML void resizeAction() 
	{
		resizeSlider.setValue(resizeSlider.getMax());
		confirmResizeButton.setDisable(true);
		resizeBox.toFront();
	}
	
	@FXML void confirmResizeAction()
	{
		project.storeState();
		double percentage = Math.round(resizeSlider.getValue());
		double scaleFactor = percentage / 100;
		BufferedImage newImage = project.resize(scaleFactor);
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
		editingBox.toFront();
	}
	
	@FXML void cancelResizeAction()
	{
		editingBox.toFront();
	}
	
	@FXML void editBrightnessAction() 
	{
		brightnessSlider.setValue(100);
		contrastSlider.setValue(100);
		brightnessBox.toFront();
	}
	
	@FXML void confirmBrightnessAction()
	{
		project.storeState();
		double brightness = brightnessSlider.getValue() - 100;
		double contrast = contrastSlider.getValue() / 100;
		BufferedImage newImage = project.adjustBrightnessContrast(brightness, contrast);
		imageViewer.update(SwingFXUtils.toFXImage(newImage, null));
		project.setHasUnsavedChanges(true);
		redoAvailable = false;
		updateGUI();
		editingBox.toFront();
	}
	
	@FXML void cancelBrightnessAction()
	{
		editingBox.toFront();
		imageViewer.update(SwingFXUtils.toFXImage(project.getImage(), null));
	}
		
	/*
	 * This function allows the user to select an image file for viewing and editing
	 */
	private void openFile()
	{
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", COMPATIBLE_FORMATS);
		fileChooser.getExtensionFilters().add(filter);
		
		for (String ext : COMPATIBLE_FORMATS)
		{
			filter = new FileChooser.ExtensionFilter(ext, ext);
			fileChooser.getExtensionFilters().add(filter);
		}
		
		fileChooser.setTitle("JIVE - Open an Image");
		File imageFile = fileChooser.showOpenDialog(stage);
				
		if (imageFile != null)
		{
			Image image;
			
			//Users can enter non-image files manually, so additional validation is done here
			String fileName = imageFile.getName();
			int extensionIndex = fileName.lastIndexOf(".");
			String extension = fileName.substring(extensionIndex + 1);
			
			if (!(COMPATIBLE_FORMATS.contains("*." + extension)))
			{
				createAlert("JIVE does not support ." + extension + " files.");
				return;
			}
			
			try
			{
				image = new Image(imageFile.toURI().toURL().toExternalForm());
				imageViewer.update(image);
				project = new Project(imageFile);				
				updateGUI();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				imageViewer.update(null);
				project = null;
				createAlert("Error: could not read image file.");
				return;				
			}
			
			editingBox.toFront();
			redoAvailable = false;
			saveAsItem.setDisable(false);
			rotateRightButton.setDisable(false);
			rotateLeftButton.setDisable(false);
			flipHorizontalButton.setDisable(false);
			flipVerticalButton.setDisable(false);
			cropButton.setDisable(false);
			resizeButton.setDisable(false);
			editBrightnessButton.setDisable(false);
			
			if (cropSelector != null)
				cropSelector.remove();
		}
	}
	
	/**
	 * This function updates the GUI's labels and buttons as appropriate
	 * It should be called after any change is made to the GUI, Project, or ImageViewer
	 */
	private void updateGUI()
	{
		sizeLabel.setText(project.getWidth() + " x " + project.getHeight());
		newDimensionsLabel.setText(project.getWidth() + " x " + project.getHeight());
				
		if (project.hasUnsavedChanges())
		{
			saveButton.setDisable(false);
			nameLabel.setText(project.getName() + "*");
		}
		else
		{
			saveButton.setDisable(true);
			nameLabel.setText(project.getName());
		}
		
		if (project.stateHistoryIsEmpty())
			undoButton.setDisable(true);
		else
			undoButton.setDisable(false);
		
		if (!redoAvailable)
			project.clearUndoHistory();
			
		if (project.undoHistoryIsEmpty())
			redoButton.setDisable(true);
		else
			redoButton.setDisable(false);
	}
	
	/**
	 * Shows an alert in the GUI
	 * @param message The text to be shown in the alert
	 */
	private void createAlert(String message)
	{
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.setHeaderText(null);
		alert.setTitle("JIVE - Error");
		GaussianBlur blur = new GaussianBlur(5);
		mainPane.setEffect(blur);
		alert.showAndWait();
		mainPane.setEffect(null);
	}
	
	/**
	 * This method is used to set a reference to the stage from the Main class.
	 * It also defines the application's behavior when the stage is closed.
	 */
	public void setStage(Stage stage)
	{
		this.stage = stage;
		
		stage.setOnCloseRequest(event ->
		{
			if (project != null && project.hasUnsavedChanges())
			{
				ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
				
				Alert alert = new Alert(AlertType.CONFIRMATION, 
						"Do you want to save changes to " + project.getName() + "?",
						yesButton, noButton);
				
				alert.setHeaderText(null);
				alert.setTitle("JIVE - Save Changes");
				
				GaussianBlur blur = new GaussianBlur(7);
				mainPane.setEffect(blur);
				
				Optional<ButtonType> response = alert.showAndWait();
				
				if (response.get() == yesButton)
					project.save();
			}
		});
	}
	
	/**
	 * resizeSliderListener updates the resizePercentLabel and the newDimensionsLabel
	 * according to the value of the slider. It also enables or disables the
	 * confirmResizeButton as appropriate.
	 */
	ChangeListener<Number> resizeSliderListener = (observable, oldValue, newValue) -> 
	{
		double sliderValue = Math.round(resizeSlider.getValue());
		double scale = sliderValue / 100;
		
		int newWidth = (int) (project.getWidth() * scale);
		int newHeight = (int) (project.getHeight() * scale);
		
		resizePercentLabel.setText(String.valueOf((int) sliderValue) + "%");
		newDimensionsLabel.setText(newWidth + " x " + newHeight);
		
		if (newWidth < 1 || newHeight < 1 || scale == 1)
			confirmResizeButton.setDisable(true);
		else
			confirmResizeButton.setDisable(false);
	};
	
	/**
	 * This listener updates the brightnessSlider label and previews brightness changes.
	 * The slider min and max values are 0 and 200, respectively. 100 is subtracted from
	 * the value to effectively make the adjustment between -100 and 100.
	 */
	ChangeListener<Number> brightnessSliderListener = (observable, oldValue, newValue) ->
	{
		double brightnessValue = brightnessSlider.getValue() - 100;
		double contrastValue = contrastSlider.getValue() / 100;
		brightnessLabel.setText(String.valueOf((int) brightnessValue));
		
		BufferedImage previewImage = project.previewBrightnessContrast(brightnessValue, contrastValue);
		imageViewer.update(SwingFXUtils.toFXImage(previewImage, null));
	};
	
	/**
	 * This listener updates the contrastSlider label and previews contrast changes.
	 * The adjustment value is divided by 100 to calculate the appropriate
	 * value to pass to adjustBrightnessContrast()
	 */
	ChangeListener<Number> contrastSliderListener = (observable, oldValue, newValue) ->
	{
		double contrastValue = contrastSlider.getValue() / 100;
		double brightnessValue = brightnessSlider.getValue() - 100;
		contrastLabel.setText(String.valueOf((int) (contrastValue * 100 - 100)));
				
		BufferedImage previewImage = project.previewBrightnessContrast(brightnessValue, contrastValue);
		imageViewer.update(SwingFXUtils.toFXImage(previewImage, null));
	};
}