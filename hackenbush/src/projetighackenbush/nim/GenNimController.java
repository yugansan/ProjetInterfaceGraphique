/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetighackenbush.nim;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import projetighackenbush.nim.Nim.Move;

/**
 * FXML Controller class
 *
 * @author yugan
 */
public class GenNimController implements Initializable {

    @FXML TextField numberOfHeaps;
    @FXML TextField maxSize;
    @FXML Button generate;
    @FXML Button rejouer;
    @FXML CheckBox isMisere;
    @FXML AnchorPane pane;
    @FXML Label notifJoueur;
    @FXML RadioButton vsAI;
    @FXML RadioButton vsPlayer;
    @FXML RadioButton aiVsAi;
    @FXML CheckBox isInteligentAI;
    //@FXML Button home;
    
    
    private Nim nim;
    private final int EPS = 50;
    private Random rand;
    private final int RADIUS_CIRCLE=8;
    private int []tabNim;
    private Circle [][]nimSommet;// contient les sommets du jeu
    private final Color colorPath = Color.GREEN;
    private Line sol;
    private boolean isJoueur1=false;
    private final String []messagevsAI ={"C'est à vous de jouer !", "C'est au tour de AI!","Vous avez gagné!!!","Vous avez perdu :("} ;
    private final String []messagevsPlayer ={"C'est Au tour de joueur 1 !", "C'est Au tour de joueur 2 !","Joueur 1 a gagné!!!","Joueur 2 a gagné !!!"} ;
    private final String []messageAivsAi ={"C'est au tour de AI 1 !", "C'est au tour de AI 2 !","AI 1 a gagné!!!","AI 2 a gagné !!!"} ;
    private String []message;
    private boolean isRealiser = false;
    private int modeJeuChoisi;
    
    
    //LIST ENLEVER DANS PILE
    private ArrayList<Shape> removedShape;
    private ArrayList<Line> edgesList; // MAP with index of delete
    
    //PAUSE TRANSITION
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rand = new Random();
        this.removedShape = new ArrayList<>();
        this.edgesList = new ArrayList<>();
        vsAI.setSelected(true);
        pause.setOnFinished(event ->
            Jouer()
        );
        //home.setGraphic(getImageView("icons/home.png"));
        notifJoueur.setTextFill(Color.DARKORANGE);
        notifJoueur.setFont(Font.font("Arial", 35));
        notifJoueur.setText("Après avoir remplie cliquer sur generate!");
        generate.setGraphic(getImageView("icons/play.png"));
        rejouer.setGraphic(getImageView("icons/replay2.png"));
        vsAI.setGraphic(getImageView("icons/playerVsAI2.png"));
        vsPlayer.setGraphic(getImageView("icons/playerVsPlayer.png"));
        aiVsAi.setGraphic(getImageView("icons/aiVsAi.png"));   
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
    
    @FXML
    private void handleGenerate(ActionEvent event){
        int nbTige = getValDuTextField(numberOfHeaps);
        int tailleMaxTige=getValDuTextField(maxSize);
        if(nbTige>0 && tailleMaxTige>0){
            numberOfHeaps.setDisable(true);
            maxSize.setDisable(true);
            generate.setDisable(true);
            boolean isMis=false;
            if(rand.nextInt(2)==1){
                this.isJoueur1=true;
            }else{
                this.isJoueur1=false;
            }
            if(isMisere.isSelected()){
                isMis =true;
            }
            isMisere.setDisable(true);
            isInteligentAI.setDisable(true);
            this.modeJeuChoisi = getModeDeJeu();
            vsAI.setDisable(true);
            vsPlayer.setDisable(true);
            aiVsAi.setDisable(true);
            generateSommets(isMis, nbTige, tailleMaxTige);
            generateArretes();
            afficheMessage(-1);
            if(modeJeuChoisi==2){
                passageDunJoueurALautre();
            }else if(modeJeuChoisi==0 && !isJoueur1){
                passageDunJoueurALautre();
            }
        }else{
            maxSize.setText("");
            numberOfHeaps.setText("");
        }
    }
    
    private int getModeDeJeu(){
        int mode =0;
        if(vsAI.isSelected()){
            mode =0;
            message = messagevsAI;
        }else if(vsPlayer.isSelected()){
            mode =1;
            message = messagevsPlayer;
        }else if(aiVsAi.isSelected()){
            mode = 2;
            message = messageAivsAi;
        }
        return mode;
    }
    
    @FXML
    private void handleRejouer(ActionEvent event){
        try{
            maxSize.setText("");
            numberOfHeaps.setText("");
            numberOfHeaps.setDisable(false);
            maxSize.setDisable(false);
            generate.setDisable(false);
            isMisere.setDisable(false);
            isMisere.setSelected(false);
            vsAI.setDisable(false);
            vsPlayer.setDisable(false);
            aiVsAi.setDisable(false);
            vsAI.setSelected(true);
            vsPlayer.setSelected(false);
            aiVsAi.setSelected(false);
            isInteligentAI.setSelected(false);
            isInteligentAI.setDisable(false);
            notifJoueur.setText("Après avoir remplie cliquer sur generate!");

            pane.getChildren().removeAll(removedShape);
            pane.getChildren().removeAll(edgesList);
            for(int i=0; i<nimSommet.length;i++){
                for (int j = 0; j < nimSommet[i].length; j++) {
                    pane.getChildren().removeAll(nimSommet[i][j]);
                }
            }
            removedShape.clear();
            edgesList.clear();
        }catch (Exception e){}   
    }
    
    
    @FXML
	private void handleQuitter(ActionEvent e){
		System.exit(0);
    }
         
    
    /**
     * 
     * @param textf : textfield passed on parameter
     * @return return the value entered on textfield 0 if error
     */
    private int getValDuTextField(TextField textf){
        String donee = textf.getText();
        if(donee != null){
            try{
                Integer i = Integer.parseInt(donee);
                return i;
            }catch(NumberFormatException exception){}   
        }
        return 0;
    }
    
    /**
     * 
     * @param estMisere : boolean saying if misere or not 
     * @param nbTige : number of heaps
     * @param tailleMaxTige : max size of a heap
     */
    private void generateSommets(boolean estMisere,int nbTige,int tailleMaxTige){
        this.nim = new Nim(estMisere);
        Circle cir ;
        this.tabNim = nim.generateRandomNim(nbTige, tailleMaxTige);
        this.sol = new Line(0, pane.getHeight()-50, pane.getWidth() ,pane.getHeight()-50);
        sol.setStrokeWidth(RADIUS_CIRCLE);
        sol.setStroke(Color.BLACK);
        pane.getChildren().add(sol);
        double startX = sol.getStartX()+20;
        double endX = sol.getEndX();
        double tailleSol = Math.abs(endX-startX);
        double espaceEntreTige = tailleSol/nbTige;
        double tailleYDispo = Math.abs(sol.getStartY()-(pane.getScaleX()+EPS));
        double espHautMax ;
        this.nimSommet = new Circle[tabNim.length][tailleMaxTige+1];
        for (int i = 0; i < tabNim.length; i++) {
            espHautMax = tailleYDispo/(tabNim[i]+1);
            for (int j = 0; j < tabNim[i]+1; j++) {
                
                double cirX = startX+(espaceEntreTige*i);
                double cirY = sol.getStartY()-(espHautMax*j);
                
                if(j==0){
                    cir = new Circle(cirX,cirY, RADIUS_CIRCLE);
                }else{
                    int ranNum = rand.nextInt(4);
                    switch (ranNum) {
                        case 0:
                            cir = new Circle(cirX+getRandomBetween(espaceEntreTige),cirY +getRandomBetween(espHautMax), RADIUS_CIRCLE);
                            break;
                        case 1:
                            cir = new Circle(cirX-getRandomBetween(espaceEntreTige),cirY -getRandomBetween(espHautMax), RADIUS_CIRCLE);
                            break;
                        case 2:
                            cir = new Circle(cirX+getRandomBetween(espaceEntreTige),cirY -getRandomBetween(espHautMax), RADIUS_CIRCLE);
                            break;
                        case 3:
                            cir = new Circle(cirX-getRandomBetween(espaceEntreTige),cirY +getRandomBetween(espHautMax), RADIUS_CIRCLE);
                            break;
                        default:
                            cir = new Circle(cirX+getRandomBetween(espaceEntreTige),cirY +getRandomBetween(espHautMax), RADIUS_CIRCLE);
                            break;
                    }
                }
                cir.setStroke(Color.CHARTREUSE);
                cir.setFill(Color.WHITE);
                pane.getChildren().add(cir);
                this.nimSommet[i][j]=cir;
            }
        } 
    }
    
    private void generateArretes(){
        for (int i = 0; i < this.nimSommet.length; i++) {
            for (int j = 1; j < this.nimSommet[i].length; j++) {
                if(nimSommet[i][j]==null){
                    j=this.nimSommet[i].length;
                }else{
                    Line line = new Line(nimSommet[i][j-1].getCenterX(), nimSommet[i][j-1].getCenterY(),nimSommet[i][j].getCenterX(),nimSommet[i][j].getCenterY());
                    line.setStrokeWidth(RADIUS_CIRCLE);
                    line.setStroke(colorPath);
                    this.edgesList.add(line);
                    pane.getChildren().add(line);
                    line.toBack();
                    FadeTransition ft = new FadeTransition(Duration.millis(3000), line);
                                ft.setFromValue(3);
                                ft.setToValue(0.8);
                                ft.setCycleCount(Timeline.INDEFINITE);
                                ft.setAutoReverse(true);
                                ft.play();
                    if(modeJeuChoisi!=2){
                        line.setOnMouseClicked(handleLine);
                    }
                }    
            } 
        }
    }
    
    /**
     * 
     * @param move :c action performed
     * @return : delete all elements not connected to the "sol"
     */
    private void removeUp(Nim.Move move){
        int size = move.getSize();
        int index =move.getIndex();
        if( (size>=0 && size<this.nimSommet[0].length-1) && index>=0 && index<this.nimSommet.length){
            ArrayList<Shape> listeShapeAsupprimer = new ArrayList<>();
            if(nimSommet[index][size]!=null){ //sommet correspondant a move
                Circle circle1=null,circle2 = null,circleDebut=nimSommet[index][size];
                for(int i = size+1; i<this.nimSommet[index].length; i++){
                     circle1=nimSommet[index][i-1];//sommet bas
                     circle2 = nimSommet[index][i];//sommet haut
                     if(circle1==null || circle2==null){
                         i=this.nimSommet[index].length;
                     }else{
                        Line l=null;
                        for(int k=0;k<this.edgesList.size();k++){
                            l = this.edgesList.get(k);
                            if( (l.getStartX()==circle2.getCenterX() && l.getStartX()==circle2.getCenterY()) ||
                                    (l.getEndX()==circle2.getCenterX() && l.getEndY()==circle2.getCenterY())){
                                removedShape.add(l);
                                pane.getChildren().remove(l);
                                if( (circle1.getCenterX() != circleDebut.getCenterX() && circle1.getCenterY() != circleDebut.getCenterY() ) ||
                                        (circle1.getCenterY()==sol.getStartY()) ){
                                    removedShape.add(circle1);
                                    pane.getChildren().remove(circle1);
                                }
                                this.edgesList.remove(k);
                                //STOCKER DANS UN ARRAY ET SUPPRIMER APRES VERIFIVCATION A LA FIN
                            }
                            if(l.getEndX()==circle2.getCenterX() && l.getEndY()==circle2.getCenterY()){
                                removedShape.add(circle2);
                                pane.getChildren().remove(circle2);
                            }
                        }
                    }
                }
                if(circle1 != null && !removedShape.contains(circle1)){
                    removedShape.add(circle1);
                    pane.getChildren().remove(circle1);
                }
                if(circle2 != null && !removedShape.contains(circle2)){
                    removedShape.add(circle2);
                    pane.getChildren().remove(circle2);
                }
                   
            }    
        }
    }
    
    
    private EventHandler<MouseEvent> handleLine
    = (MouseEvent e) -> {
        afficheMessage(-1);
        Line line = (Line) e.getSource();
        if(line!=null){
            int []res = getIndexSizeOfElement(line.getStartX(), line.getStartY());
            if(res[0]>=0 && res[1]>=0){
                if(isJoueur1){
                    Move move =new Nim.Move(res[0], res[1]);
                    nim.applyMove(move, tabNim);
                    removeUp(move);
                    isRealiser = true;
                }
                else if( (!isJoueur1) && (modeJeuChoisi==1)){
                    Move move =new Nim.Move(res[0], res[1]);
                    nim.applyMove(move, tabNim);
                    removeUp(move);
                    isRealiser = true;
                }
                if(!isRealiser){
                    pause.play();
                }
                passageDunJoueurALautre();
            }
        }
    };
    
    
    private int[] getIndexSizeOfElement(double posX, double posY){
        int []res = {-1,-1};
        Circle cir;
        for (int i = 0; i < nimSommet.length; i++) {
            for (int j = 0; j < nimSommet[i].length; j++) {
                cir = nimSommet[i][j];
                if(cir!=null){
                    if(cir.getCenterX()==posX && cir.getCenterY()==posY){
                        res[0]=i;
                        res[1]=j;
                    }
                }
            }
        }
        return res;
    }
    
    
    private void passageDunJoueurALautre(){
        if(this.nim.isFinished(tabNim)){
            if(isJoueur1){
                afficheMessage(2);
            }else{
                afficheMessage(3);
            }
            return;
        }
        if(modeJeuChoisi==0){
            this.isJoueur1=!isJoueur1;
        }else{
            if(isJoueur1 && this.isRealiser){
                this.isJoueur1=!isJoueur1;
                this.isRealiser =false;
            }else if((!isJoueur1) && this.isRealiser){
                this.isJoueur1=!isJoueur1;
                this.isRealiser =false;
            }
        }
        afficheMessage(-1);
        if(!isJoueur1 && modeJeuChoisi == 0){
            pause.play();
        }else if(modeJeuChoisi==2){
            pause.play();
        }
    }
    
    
    private void afficheMessage(int i){
        if(i==-1){
            if(this.isJoueur1){
                this.notifJoueur.setText(message[0]);
                this.notifJoueur.setTextFill(Color.BLUE);
            }else{
                this.notifJoueur.setText(message[1]);
                this.notifJoueur.setTextFill(Color.RED);
            }
        }else{
            this.notifJoueur.setTextFill(Color.DARKORANGE);
            if(i>=0 && i< message.length){
                if(i==2 || i==3){
                    this.notifJoueur.setText(message[i]);
                    FadeTransition ft = new FadeTransition(Duration.millis(300), notifJoueur);
                                ft.setFromValue(3);
                                ft.setToValue(0.5);
                                ft.setCycleCount(10);
                                ft.setAutoReverse(true);
                                ft.play();
                }else{
                    this.notifJoueur.setText(message[i]);
                }
            }else{
                 this.notifJoueur.setText("");
            }
        }
    }
    
    private double getRandomBetween(double nb){
        return rand.nextDouble()*(nb/3);
    }
    

    private void Jouer(){
        Move move = Move.EMPTY;
        if(!nim.isFinished(tabNim)) {
            if(!isJoueur1){
                if(isInteligentAI.isSelected()){
                    move = nim.randomMove(tabNim);
                }else{
                    move= nim.winningMove(tabNim);
                }
                this.isRealiser=true;
            }else if(isJoueur1 && (modeJeuChoisi==2)){
                if(isInteligentAI.isSelected()){
                    move = nim.randomMove(tabNim);
                }else{
                    move= nim.winningMove(tabNim);
                }
                this.isRealiser=true;
            }
            nim.applyMove(move, tabNim);
            removeUp(move);
        }
        passageDunJoueurALautre();
    }
    
}
