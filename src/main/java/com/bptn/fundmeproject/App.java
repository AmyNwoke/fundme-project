package com.bptn.fundmeproject;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.bptn.fundmeproject_01_modelling.User;
import com.bptn.fundmeproject_01_modelling.UserManager;

/**
 * JavaFX App
 */
public class App extends Application {
public static User loggedInUser;
	
	private static Scene scene;
	private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
    	mainStage = stage;
        scene = new Scene(loadFXML("signup"));
        mainStage.setScene(scene);
        mainStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
    	
    	           
            UserManager.loadUsersFromFile("userData.csv");
            
                         
        launch();
    }

}
	
	

