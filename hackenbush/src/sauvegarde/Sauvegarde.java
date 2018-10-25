package sauvegarde;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Sauvegarde {
	static String role1 = null;
	static String role2 = null;
	static String role3 = null;
	static String role4 = null;

	private static Element transforme(Document dom, Node node){
		if(node instanceof Circle){
			Circle c= (Circle) node;
			Element element= dom.createElement("Circle");
			element.setAttribute("centerX", String.valueOf(c.getCenterX()));
			element.setAttribute("centerY", String.valueOf(c.getCenterY()));
			element.setAttribute("radius", String.valueOf(c.getRadius()));
			element.setAttribute("fill", String.valueOf(c.getFill()));
			return element;
		}
		else if(node instanceof Line){
			Line l= (Line) node;
			Element element = dom.createElement("Line");
			element.setAttribute("startX", String.valueOf(l.getStartX()));
			element.setAttribute("startY", String.valueOf(l.getStartY()));
			element.setAttribute("endX", String.valueOf(l.getEndX()));
			element.setAttribute("endY", String.valueOf(l.getEndY()));
			element.setAttribute("stroke", String.valueOf(l.getStroke()));
			element.setAttribute("strokeWidth", String.valueOf(l.getStrokeWidth()));
			if(!l.getStroke().equals(Color.BLACK))
				element.setAttribute("onMouseClicked", "#supprime");

			return element;
		}
		return null;
	}

	public static void sauvegarde(String nomfic, AnchorPane pane){
		try {
			//builder
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			doc.appendChild(
					doc.createProcessingInstruction("import", "java.lang.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "java.net.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "java.util.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "javafx.scene.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "javafx.scene.control.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "javafx.scene.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "javafx.scene.layout.*"));
			doc.appendChild(
					doc.createProcessingInstruction("import", "javafx.scene.shape.*"));


			Element rootElement = doc.createElement("AnchorPane");
			rootElement.setAttribute("xmlns:fx", "http://javafx.com/fxml/1");
			rootElement.setAttribute("fx:id", "anchorpane");
			rootElement.setAttribute("fx:controller", "projetighackenbush.Ressource.Graphe");
			doc.appendChild(rootElement);

			for(Node node : pane.getChildren()){
				Element element = transforme(doc, node);
				if(element != null)
					rootElement.appendChild(element);
			}

			//enregistrement
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(
					new File("src/projetighackenbush/Ressource/"+nomfic));
			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException | TransformerException pce) {
			
		}
	}
}