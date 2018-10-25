package projetighackenbush.Ressource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Graphe implements Initializable{
	@FXML AnchorPane anchorpane;
	ArrayList<Circle> listcercle;
	ArrayList<Line> listline;
	double sol = -1;
	int blueline=0;
	int redline=0;
	int greenline=0;
	boolean myturn= true;
	public static boolean ai= false;
	boolean end = false;
	Color mycolor =null;
	Text text= new Text(50,50,"");

	ArrayList<Circle> cerclevu;
	ArrayList<Line> lignevu;

	private void removeCascade(){
		for(Line l : lignevu){
			removeLine(l);
		}
		anchorpane.getChildren().removeAll(cerclevu);
		listcercle.removeAll(cerclevu);
		cerclevu.clear();
	}

	private boolean toucheSol(Circle c){
		if(cerclevu.contains(c))
			return false;
		if(c.getCenterY() >= sol)
			return true;
		cerclevu.add(c);
		double x=0;
		double y=0;
		boolean sortie= false;
		for(Line l : listline){
			boolean b= false;
			if(l.getStartX() == c.getCenterX() && l.getStartY() == c.getCenterY()){
				x = l.getEndX();
				y = l.getEndY();
				b = true;
				if(!lignevu.contains(l))
					lignevu.add(l);
			}
			else if(l.getEndX() == c.getCenterX() && l.getEndY() == c.getCenterY()){
				x = l.getStartX();
				y = l.getStartY();
				b=true;
				if(!lignevu.contains(l))
					lignevu.add(l);
			}
			if(b){
				for(Circle test : listcercle)
					if(test.getCenterX() == x && test.getCenterY() == y){
						if(toucheSol(test))
							sortie = true;
					}
			}
		}
		return sortie;

	}

	private void tourAutomatique(){
		Color col= null;
		int total=greenline;
		if(mycolor.equals(Color.BLUE)){
			total+=redline;
			col=Color.RED;
		}
		else if(mycolor.equals(Color.RED)){
			total+=blueline;
			col= Color.BLUE;
		}
		Random rand = new Random();
		int test = rand.nextInt(total)+1;
		for(Line l : listline){
			if(l.getStroke().equals(Color.GREEN) || l.getStroke().equals(col)){
				test--;
				if(test <=0){
					removeLine(l);
					for(Circle c : listcercle){
						if((c.getCenterX() == l.getEndX() && c.getCenterY() == l.getEndY())
								|| (c.getCenterX() == l.getStartX() && c.getCenterY() == l.getStartY())){
							cerclevu = new ArrayList<>();
							lignevu = new ArrayList<>();
							if(!toucheSol(c)){
								removeCascade();
								break;
							}
						}
					}
					break;
				}
			}
		}
		myturn = true;
		if(mycolor.equals(Color.BLUE))
			text.setText("tour de BLEU");
		else
			text.setText("tour de ROUGE");
		gagnant();
	}

	public void gagnant(){
		if (redline <= 0 && blueline <= 0 && greenline <=0){
			if((myturn && mycolor.equals(Color.RED)) 
					|| (!myturn && mycolor.equals(Color.BLUE)))
				text.setText("Bleu a gagné");
			else
				text.setText("Rouge a gagné");
			end = true;
		}
		else if(redline <= 0 && greenline <= 0){
			text.setText("Bleu a gagné");
			end= true;
		}
		else if(blueline <= 0 && greenline <= 0){
			text.setText("Rouge a gagné");
			end = true;
		}
	}
	public void removeLine(Line l){
		listline.remove(l);
		if(l.getStroke().equals(Color.BLUE))
			blueline--;
		else if(l.getStroke().equals(Color.RED))
			redline--;
		else if(l.getStroke().equals(Color.GREEN))
			greenline--;
		anchorpane.getChildren().remove(l);
	}

	@FXML
	private void supprime(MouseEvent event){
		Line l = (Line) event.getSource();
		if(l.getStroke().equals(Color.BLUE) && mycolor == null)
			mycolor=Color.BLUE;
		else if(l.getStroke().equals(Color.RED) && mycolor == null)
			mycolor = Color.RED;
		if(	!end && mycolor != null &&
				((myturn && mycolor.equals(l.getStroke()) 
				|| l.getStroke().equals(Color.GREEN))
						|| 
						(!myturn && !mycolor.equals(l.getStroke())
								|| l.getStroke().equals(Color.GREEN) && !ai))
				){
			removeLine(l);

			cerclevu= new ArrayList<>();
			lignevu = new ArrayList<>();
			for(Circle c : listcercle){
				if(
						c.getCenterX() == l.getEndX() && c.getCenterY() == l.getEndY()
						|| c.getCenterY() == l.getStartY() && c.getCenterX() == l.getStartX())
				{
					if(!toucheSol(c))
						break;
					else{
						cerclevu.clear();
						lignevu.clear();
					}
				}
			}
			removeCascade();

			
                        
			if(myturn){
				myturn= false;
				if(mycolor.equals(Color.BLUE))
					text.setText("tour de ROUGE");
				else
					text.setText("tour de BLEU");
			}
			else{
				myturn = true;
				if(mycolor.equals(Color.BLUE))
					text.setText("tour de BLEU");
				else
					text.setText("tour de ROUGE");
			}
			gagnant();
			if(ai)
				tourAutomatique();
		}

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text.setFont(new Font(40));
		text.setFill(Color.FUCHSIA);
		anchorpane.getChildren().add(text);
		listcercle = new ArrayList<>();
		listline = new ArrayList<>();
		for(Node node : anchorpane.getChildren()){
			if(node instanceof Circle){
				listcercle.add((Circle)node);
			}
			if(node instanceof Line){
				Line l= (Line)node;

				if(l.getStroke().equals(Color.BLACK)){
					sol = l.getEndY();
				}else{
					if(l.getStroke().equals(Color.BLUE))
						blueline++;
					else if(l.getStroke().equals(Color.RED))
						redline++;
					else if(l.getStroke().equals(Color.GREEN))
						greenline++;
					listline.add(l);
				}
			}
		}

	}

}