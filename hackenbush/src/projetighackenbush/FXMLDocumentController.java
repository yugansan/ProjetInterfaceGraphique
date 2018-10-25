/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetighackenbush;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author yugan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button jouer;
    @FXML
    private Button editer;
    @FXML private Button nim;

    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root ;
        if(event.getSource() == editer ){
            stage = (Stage) editer.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("EditionPage.fxml"));
        }else if(event.getSource() == nim){
            stage = (Stage) jouer.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("nim/genNim.fxml"));

        }
        else{
            stage = (Stage) jouer.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("JouerPage.fxml"));

        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jouer.setMinHeight(50);
        jouer.setMinWidth(100);
        jouer.setStyle("-fx-cursor: hand; -fx-background-radius: 10px; -fx-font-family: Arial; -fx-font-size: 25px; -fx-font-weight: bold;");
        editer.setMinHeight(50);
        editer.setMinWidth(100);
        editer.setStyle("-fx-cursor: hand; -fx-background-radius: 10px; -fx-font-family: Arial; -fx-font-size: 25px; -fx-font-weight: bold;");
        nim.setMinHeight(50);
        nim.setMinWidth(100);
        nim.setStyle("-fx-cursor: hand; -fx-background-radius: 10px; -fx-font-family: Arial; -fx-font-size: 25px; -fx-font-weight: bold;");
    }    
    
}
