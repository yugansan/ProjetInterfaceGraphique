/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetighackenbush;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import projetighackenbush.Ressource.Graphe;
/**
 * FXML Controller class
 *
 * @author yugan
 */
public class JouerPageController implements Initializable {

	/**
	 * Initializes the controller class.
	 */
	Parent parent;
	@FXML AnchorPane anchorpane;
	@FXML BorderPane borderPane;
        @FXML Button home;
	HBox hbox= new HBox();
	Button joueur= new Button("Player vs Player");
	Button ai = new Button("Player vs AI");

	public void load() throws IOException{
		
		anchorpane.getChildren().add(parent);
	}
	
	@FXML
	private void handleOuvrir(ActionEvent e){
		try{
			FileChooser fileChooser = new FileChooser(); // file chooser
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("fxml", "*.fxml") // add extension *.lst
					); // add extension to file chooser
			//File f= new File(getClass().getResource("Ressource").toURI());
			
                        //fileChooser.setInitialDirectory(f);
			Window primaryStage =null;
			File file = fileChooser.showOpenDialog(primaryStage);
			FXMLLoader loader = new FXMLLoader();
			URL url = file.toURI().toURL();
			loader.setLocation(url);
			parent = loader.load();
			anchorpane.getChildren().clear();
			anchorpane.getChildren().add(hbox);
		}
		catch(IOException ex){
                    
		}
	}

	@FXML
	private void handleQuitter(ActionEvent e){
		System.exit(0);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		url = getClass().getResource("Ressource/szer.fxml");
		joueur.prefHeightProperty().bind(borderPane.heightProperty().subtract(30));
		joueur.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
		ai.prefHeightProperty().bind(borderPane.heightProperty().subtract(30));
		ai.prefWidthProperty().bind(borderPane.widthProperty().divide(2));
		home.setGraphic(getImageView("nim/icons/home.png"));
		hbox.getChildren().add(joueur);
		hbox.getChildren().add(ai);
		
		joueur.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				try {
					Graphe.ai = false;
					anchorpane.getChildren().clear();
					load();
				} catch (IOException e) {
					
				}
				
			}
		});
		
		ai.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					Graphe.ai = true;
					anchorpane.getChildren().clear();
					load();
				} catch (IOException e) {
					
				}
			}
		});
		
		anchorpane.getChildren().add(hbox);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Ressource/szer.fxml"));
		url = getClass().getResource("Ressource/szer.fxml");
		
		try {
			parent = loader.load();

		} catch (IOException e) {
			
		}
		
	} 
        
        
        @FXML
        private void handleHome(ActionEvent e){
            try {
                Stage stage = (Stage) anchorpane.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                
            }   
        }
        
        
        
        private ImageView getImageView(String url){
        ImageView iv1 = null;
        try{
        Image image = new Image(getClass().getResourceAsStream(url));
        iv1=new ImageView(image);
        iv1.setFitHeight(30);
        iv1.setFitWidth(30);
        }catch(Exception e){}
        return iv1;
    }

}