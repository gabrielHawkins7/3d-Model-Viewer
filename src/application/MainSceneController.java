package application;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.filechooser.FileSystemView;

import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;




import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainSceneController {
	
	@FXML
	private VBox DirectoryBox; 
	
	@FXML
	private Accordion DirectoryCollection;
	
	@FXML
	private BorderPane mainWindow;
	
	private double anchorX, anchorY;
	private double anchorAngleX = 0;
	private double anchorAngleY = 0;
	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	
	
	ArrayList<String> directoryFiles = new ArrayList<>();
	
	public void DirectoryChooser(ActionEvent e) throws IOException{
		//System.out.println("Hello");
		DirectoryChooser directoryChooser = new DirectoryChooser();
		DataHandler.CurrentDirectory = directoryChooser.showDialog(((MenuItem)e.getTarget()).getParentPopup().getScene().getWindow());
				while(DataHandler.CurrentDirectory != null) {
			
			fillDirectory(DataHandler.CurrentDirectory);
			return;
		}
		
		
		
	}
	
	 public void createScene(URL f) {

	        PerspectiveCamera camera = new PerspectiveCamera(true);
	        camera.setTranslateZ(-3.5);
	        camera.setTranslateY(-.5);
	        
	        
	        
	        Group model = loadModel(f);
	        model.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));


	        Group root = new Group(model);
	        
	        
	        
	        SubScene subscene = new SubScene(root, 460, 350, true, SceneAntialiasing.BALANCED);
	        subscene.setCamera(camera);
	        mainWindow.setCenter(subscene);
	        initMouseControl(root,subscene);

	        
	    }
	 
	 
	 private Group loadModel(URL url) {
	        Group modelRoot = new Group();
	        String fileName = url.getFile().toString();
	        String fileType = fileName.substring(fileName.length() - 4, fileName.length());

	        System.out.println(fileType);
	        if(fileType.equals(".obj")){
		        ObjModelImporter importer = new ObjModelImporter();
		        importer.read(url);
		        for (MeshView view : importer.getImport()) {
		            modelRoot.getChildren().add(view);
		        }
		        importer.close();
		        return modelRoot;
	        }
	        
	        
	        
	        if(fileType.equals(".stl")){
	        	StlMeshImporter importer = new StlMeshImporter();
		        importer.read(url);
		        MeshView view = new MeshView(importer.getImport());
		        modelRoot.getChildren().add(view);
		        importer.close();

		        return modelRoot;
	        }
	        return null;
	        
	        
	    }
	
	
	public void fillDirectory(File dir) throws IOException {
		TitledPane root = new TitledPane();
		root.setText(dir.getName() + "/");
		
		VBox dirBox = new VBox();
		dirBox.setSpacing(0);
		TitledPane root2 = new TitledPane();

		root.setContent(dirBox);
		DirectoryCollection.getPanes().add(root);
		DirectoryCollection.setExpandedPane(root);
		
		
		
		addFiles(dir,root,dirBox);		
	}
	
	public TitledPane addFiles(File dir, TitledPane current, VBox vCurrent){
	    

	    if (!dir.isDirectory()){
	    	
	    	if(!checkFile(dir)) {
	    		return current;
	    	}
	    	
	    	Label fileLabel = new Label(dir.getName());	
			fileLabel.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        System.out.println("mouse click detected! " + mouseEvent.getSource());
			        System.out.println(dir.toURI().toString());
			        //createScene(s.toURI().toString());
			        try {
						createScene(dir.toURI().toURL());
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			});
			
			vCurrent.getChildren().add(fileLabel);
			
	        return current;
	    }
	    TitledPane NewCurrent = new TitledPane();
		NewCurrent.setText(dir.getName() + "/");
		VBox newVCurrent = new VBox();
		NewCurrent.setContent(newVCurrent);
		vCurrent.getChildren().add(NewCurrent);
		for (File file : dir.listFiles())
	        addFiles(file, NewCurrent, newVCurrent);
	    return current;
	}
	
	public boolean checkFile(File x) {
		String S = x.getPath();
		String FE = S.substring(S.length() - 4, S.length());
		
		if(FE.equals(".obj") || FE.equals(".stl")) {
			return true;
		}
		
		return false;
	}
	
	
	private void initMouseControl(Group group, SubScene scene) {
 	   Rotate xRotate;
 	   Rotate yRotate;
 	   
 	   group.getTransforms().addAll(
 	       xRotate = new Rotate(0, Rotate.X_AXIS),
 	       yRotate = new Rotate(0, Rotate.Y_AXIS)
 	   );
 	   xRotate.angleProperty().bind(angleX);
 	   yRotate.angleProperty().bind(angleY);

 	 
 	   scene.setOnMousePressed(event -> {
 	     anchorX = event.getSceneX();
 	     anchorY = event.getSceneY();

 	     anchorAngleX = angleX.get();
 	     anchorAngleY = angleY.get();
 	     scene.requestFocus();
 	     
 	   });
 	 
 	   scene.setOnMouseDragged(event -> {
 	     angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
 	     angleY.set(anchorAngleY + anchorX - event.getSceneX());
 	     scene.requestFocus();
 	   });
 	   
 	  scene.getScene().setOnKeyPressed(e -> {
 		  double inc = 0.05;
          if (e.getCode() == KeyCode.W) {
              scene.getCamera().setTranslateZ(scene.getCamera().getTranslateZ() + inc);
          }
          if (e.getCode() == KeyCode.S) {
              scene.getCamera().setTranslateZ(scene.getCamera().getTranslateZ() - inc);
          }
          if (e.getCode() == KeyCode.A) {
              scene.getCamera().setTranslateX(scene.getCamera().getTranslateX() - inc);
          }
          if (e.getCode() == KeyCode.D) {
              scene.getCamera().setTranslateX(scene.getCamera().getTranslateX() + inc);
          }
          if (e.getCode() == KeyCode.Q) {
              scene.getCamera().setTranslateY(scene.getCamera().getTranslateY() + inc);
          }
          if (e.getCode() == KeyCode.E) {
              scene.getCamera().setTranslateY(scene.getCamera().getTranslateY() - inc);
          }
          
  	     scene.requestFocus();

      });
 	   
 	  scene.addEventHandler(ScrollEvent.SCROLL, event -> {
 	    double delta = event.getDeltaY();
 	    group.translateZProperty().set(group.getTranslateZ() + (delta / 4));
 	});
 	   
 }

}

