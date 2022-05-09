package application;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainEntry extends Application {
	
	
    public static void main(String[] args) {
        launch(args); 
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
    	Parent LoadingSplash = FXMLLoader.load(getClass().getResource("LoadingSplashScreen.fxml"));
        Scene scene = new Scene(LoadingSplash, 600, 400);

        
    	Timeline Checker = new Timeline(
                new KeyFrame(Duration.millis(750), 
                new EventHandler<ActionEvent>() {
			   @Override
			   public void handle(ActionEvent event) {
				   boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
			        if(!is3DSupported) {
			           System.out.println("Sorry, 3D is not supported in JavaFX on this platform.");
			           try {
						scene.rootProperty().set(FXMLLoader.load(getClass().getResource("3DFalseSplash.fxml")));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        }else {
			        	try {
			        		scene.rootProperty().set(FXMLLoader.load(getClass().getResource("3DTrueSplash.fxml")));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			   }
			}));
    	Checker.setCycleCount(1);
    	Checker.play();
    	
    	
    	
    
        primaryStage.setTitle("3d Model Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
