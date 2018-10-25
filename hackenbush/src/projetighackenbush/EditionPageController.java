/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetighackenbush;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author yugan
 */
public class EditionPageController implements Initializable {

	/**
	 * Initializes the controller class.
	 */
	@FXML BorderPane borderPane;
	@FXML AnchorPane anchorPane;
	@FXML Button create;
	@FXML Button clean;
	@FXML Button delete;
	@FXML Button bleu;
	@FXML Button red;
	@FXML Button green;
	@FXML MenuItem sauvegarder;
	@FXML Line sol;
        @FXML Label saveName;
        @FXML TextField saveNameT;
        @FXML Button save;
        @FXML Button home;

	Circle link=null;
	ArrayList<Circle> listCercle;
	ArrayList<Line> listLine;
	boolean createControl= true;
	boolean deleteControl= false;
	boolean moveControl= false;

	//line color
	private Color colorLine = Color.GREEN;

	@FXML
	private void handleMove(ActionEvent event) throws IOException{
		if(link != null)
			link.setFill(Color.BLACK);
		link = null;
		deleteControl = false;
		createControl = false;
		moveControl = true;
		delete.setDisable(true);
		create.setDisable(true);
	}

	@FXML
	private void handleDelete(ActionEvent event) throws IOException {
		if(link != null)
			link.setFill(Color.BLACK);
		link=null;
		deleteControl = true;
		createControl = false;
		delete.setDisable(true);
		create.setDisable(false);

	}

	@FXML
	private void handleCreate(ActionEvent event) throws IOException {
		if(link != null)
			link.setFill(Color.BLACK);
		link=null;
		createControl= true;
		deleteControl = false;
		create.setDisable(true);
		delete.setDisable(false);
	}

	@FXML
	private void handleClean(ActionEvent event) throws IOException {
		link=null;
		anchorPane.getChildren().removeAll(listCercle);
		listCercle.clear();
		anchorPane.getChildren().removeAll(listLine);
		listLine.clear();
	}
        
        @FXML
        private void handleSave(ActionEvent e){
            String donne = saveNameT.getText();
            donne = donne.trim();
            if(donne!=null && !donne.isEmpty()){
                sauvegarde.Sauvegarde.sauvegarde(donne+".fxml", anchorPane);
            }else{
                saveNameT.setText("");
            }
        }
        
        @FXML
        private void handleHome(ActionEvent e){
            try {
                Stage stage = (Stage) create.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                
            }   
        }

	private EventHandler<MouseEvent> handleLine
	= (MouseEvent e) -> {
		Line line = (Line) e.getSource();
		if(deleteControl){
			anchorPane.getChildren().remove(line);
			listLine.remove(line);
		}
	};

	private EventHandler<MouseEvent> handlePoint
	= (MouseEvent e) -> {
		Circle circle= (Circle) e.getSource();
		if(createControl && !moveControl){
			if(link == null){
				link = circle;
				link.setFill(Color.MAROON);
			}
			else if(link == e.getSource()){
				link.setFill(Color.BLACK);
				link = null;
			}
			else{
				Line line= new Line(circle.getCenterX(), circle.getCenterY(),
						link.getCenterX(), link.getCenterY());
				line.setStrokeWidth(8);

				line.startXProperty().bind(circle.centerXProperty());
				line.startYProperty().bind(circle.centerYProperty());
				line.endXProperty().bind(link.centerXProperty());
				line.endYProperty().bind(link.centerYProperty());

				anchorPane.getChildren().add(line);
				line.toBack();
				line.setStroke(colorLine);//color line
				line.addEventFilter(MouseEvent.MOUSE_CLICKED , handleLine);
				listLine.add(line);
			}
		}
		else if(deleteControl && !moveControl){
			double x= circle.getCenterX();
			double y = circle.getCenterY();
			ArrayList<Line> removeListLine= new ArrayList<Line>();
			for(Line line : listLine){
				if((line.getEndX() == x && line.getEndY() == y)
						|| (line.getStartX() == x && line.getStartY() == y)){
					anchorPane.getChildren().remove(line);
					removeListLine.add(line);
				}
			}
			listLine.removeAll(removeListLine);
			anchorPane.getChildren().remove(circle);
			listCercle.remove(circle);

		}
		else{
			moveControl = false;
		}

	};

	private EventHandler<MouseEvent> handlePane
	= (MouseEvent e) -> {
		if(createControl){
			boolean b=true;
			double y= sol.getEndY();
			if(e.getY() < y)
				y= e.getY();
			for(Circle test : listCercle){
				if(Math.abs(test.getCenterX() - e.getX()) < 15
						&& Math.abs(test.getCenterY() - y) < 15
						)
					b=false;
			}
			if(b){
				Circle c= new Circle(e.getX(), y, 8);
				anchorPane.getChildren().add(c);
				c.setOnMouseDragged(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if((event.getX() > 0) && (event.getX() < anchorPane.getWidth()))
							c.setCenterX(event.getX());
						if((event.getY() > 0) && (event.getY() < sol.getEndY()))
							c.setCenterY(event.getY());	
						if(event.getY() >= sol.getEndY())
							c.setCenterY(sol.getEndY());
						moveControl = true;
					}
				});
				c.addEventFilter(MouseEvent.MOUSE_CLICKED, handlePoint);
				listCercle.add(c);
			}
		}
	};

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		sol.endXProperty().bind(anchorPane.widthProperty());
		sol.endYProperty().bind(anchorPane.heightProperty().multiply(0.9));
		sol.startYProperty().bind(anchorPane.heightProperty().multiply(0.9));
		listCercle = new ArrayList<>();
		listLine = new ArrayList<>();
		create.setDisable(true);
		anchorPane.addEventFilter(MouseEvent.MOUSE_CLICKED, handlePane);
		anchorPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));    
                save.setGraphic(getImageView("nim/icons/save.png"));
                saveNameT.setFont(Font.font("Arial", 20));
                saveName.setFont(Font.font("Arial", 20));
                
                bleu.setGraphic(getImageView("nim/icons/bleu.png"));
                green.setGraphic(getImageView("nim/icons/green.png"));
                red.setGraphic(getImageView("nim/icons/red.png"));
                home.setGraphic(getImageView("nim/icons/home.png"));
        }  


	@FXML
	private void handleColor(ActionEvent event) throws IOException{
		if(bleu.isArmed()){
			colorLine = Color.BLUE;
		}
		else if(red.isArmed()){
			colorLine = Color.RED;
		}

		if(green.isArmed()){
			colorLine = Color.GREEN;
		}

	}

	@FXML
	private void handleQuitter (ActionEvent event) throws IOException{
		System.exit(0);
	}

	@FXML
	private void handleSauvegarde (ActionEvent event) throws IOException{
		FileChooser fileChooser = new FileChooser(); // file chooser
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("fxml", "*.fxml") // add extension *.lst
				); // add extension to file chooser
		Window primaryStage =null;
		File file = fileChooser.showSaveDialog(primaryStage);

		sauvegarde.Sauvegarde.sauvegarde(file.getName(), anchorPane);
	}

	@FXML
	private void handleOuvrir(ActionEvent event) throws IOException{
		try{
		FileChooser fileChooser = new FileChooser(); // file chooser
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("fxml", "*.fxml") // add extension *.lst
				); // add extension to file chooser
		File f= new File(getClass().getResource("Ressource").toURI());
		fileChooser.setInitialDirectory(f);
		Window primaryStage =null;
		File file = fileChooser.showOpenDialog(primaryStage);
		URL url = null;
		url = file.toURI().toURL();
		}
		catch(Exception e){
			
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