package jive.java;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class UserManual
{
	String manual = getClass().getResource("/jive/resources/jiveUserManual.html").toExternalForm();
	Stage stage = new Stage();
	StackPane pane = new StackPane();
	WebView webView = new WebView();
	Scene root = new Scene(pane);
	
	public UserManual()
	{
		stage.setResizable(false);
		stage.setTitle("JIVE - User Manual");
		stage.setScene(root);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/jive/resources/icons/JiveIcon.png")));		
		webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/jive/resources/usermanual.css").toString());
		webView.getEngine().load(manual);
		pane.getChildren().add(webView);
	}
	
	public void openUserManual()
	{
		if (stage.isShowing())
		{
			stage.setIconified(false);
			stage.toFront();
		}
		else
			stage.show();
	}
}
