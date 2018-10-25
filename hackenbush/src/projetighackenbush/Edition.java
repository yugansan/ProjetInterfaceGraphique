/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetighackenbush;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Button;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author yugan
 */
public class Edition extends Application {
	@FXML
	AnchorPane anchorPane;
	ArrayList<Circle> lcercle;
	Circle c;

	@Override
	public void start(Stage stage) throws Exception {
		anchorPane= FXMLLoader.load(getClass().getResource("EditionPage.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
		//Parent root = FXMLLoader.load(getClass().getResource("GridTest.fxml"));
		Scene scene = new Scene(anchorPane);
		stage.setScene(scene);
		getScreenSize(stage);
		StackPane stackpane = new StackPane();
		stackpane.getChildren().add(getBorderPane());
		stage.show();
		lcercle= new ArrayList<>();

		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				boolean b=true;
				for(Circle test : lcercle){
					if(Math.abs(test.getCenterX() - e.getX()) < 15
							&& Math.abs(test.getCenterY() - e.getY()) < 15
							)
						b=false;
				}
				if(b){
				Circle circle = new Circle(e.getX(), e.getY(), 5, Color.BLACK);
				circle.setVisible(true);
				lcercle.add(circle);
				circle.setOnMouseClicked(new EventHandler<Event>() {
					@Override
					public void handle(Event event) {
						if(c==null)
							c = (Circle) event.getSource();
						else {
							
							Circle circle= (Circle) event.getSource();
							if(circle != c){
								Line line= new Line(circle.getCenterX(), circle.getCenterY(),
										c.getCenterX(), c.getCenterY());

								line.setStrokeWidth(5);
								anchorPane.getChildren().add(line);
								c = null;
							}
						}

					}



				});
				anchorPane.getChildren().add(circle);

			}
			}
		});
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}


	private void getScreenSize(Stage stage){
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		//set Stage boundaries to visible bounds of the main screen
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth());
		stage.setHeight(primaryScreenBounds.getHeight());
	}

	private BorderPane getBorderPane(){
		BorderPane borderpane = new BorderPane();
		borderpane.setMinHeight(300);
		borderpane.setMaxWidth(300);
		return borderpane;
	}
}