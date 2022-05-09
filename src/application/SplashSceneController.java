package application;

import java.io.IOException;
import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class SplashSceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	public void openProgram(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("MainProgram.fxml"));
		stage = (Stage)((Node) e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
//		PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.setTranslateZ(-3.5);
//        camera.setTranslateY(-.5);
//        
//        
//        
//        Group model = loadModel(getClass().getResource("/application/resources/Scooter.obj"));
//        model.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
//
//
//        Group root = new Group(model);
//        
//
//        Scene scene = new Scene(root, 460, 350, true);
//        scene.setCamera(camera);
//		
//		stage = (Stage)((Node) e.getSource()).getScene().getWindow();
//		stage.setScene(scene);
//		stage.show();
		
		
		
		
	}
	
	
	
	public void closeProgram(ActionEvent e) throws IOException{
		Platform.exit();
	}
}
