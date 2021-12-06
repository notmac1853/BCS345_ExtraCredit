package com.mycompany.guiextracredit;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


//This GUI application creates a small circle that can be dragged along a larger one
//The circle also changes color as it is dragged, depending on its X and Y coordinates
public class App extends Application {
    
    private double varY;
    private double anchorY;
    private double varX;
    private double anchorX;
    private Label label;
    double centerX = 0.0;
    double centerY = 0.0;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 300, 300);

        //creating the circle that the small circle will travel on
        Circle bigCircle = new Circle(150, 150, 100);
        bigCircle.setFill(null);
        bigCircle.setStroke(Color.BLACK);

        //the small circle we can click and drag
        Circle smallCircle = new Circle(250, 150, 10);
        smallCircle.setFill(Color.RED);
        
        //create the event for the mouse being clicked
        smallCircle.setOnMousePressed((MouseEvent event) -> {
            varY = smallCircle.getCenterY();
            //vertical position of the event relative to origin of the scene
            anchorY = event.getSceneY();
            varX = smallCircle.getCenterX();
            //horizontal position of the event relative to origin of the scene
            anchorX = event.getSceneX();
            
        });
        
        label = new Label("HSB color value will display here");
        label.setLayoutX(0);
        label.setLayoutY(0);
        
        //To get the circle to stay on the track, I used the Point2D API as suggested on a StackOverflow post.
        //This event uses vectors to update the location of the circle
        smallCircle.setOnMouseDragged((MouseEvent event) -> {
            //get the center of the big cricle
            Point2D bigCenter = new Point2D(bigCircle.getCenterX(), bigCircle.getCenterY());
            //get the coordinates of the mouse
            Point2D mouse = new Point2D(event.getX(), event.getY());
            //from the center to the mouse
            Point2D centerToMouse = mouse.subtract(bigCenter);
            //from the center of the big circle to the new point position
            Point2D centerToNewPoint = centerToMouse.normalize().multiply(bigCircle.getRadius());
            Point2D newPoint = centerToNewPoint.add(bigCenter);
            //update the postion of the small circle
            smallCircle.setCenterX(newPoint.getX());
            smallCircle.setCenterY(newPoint.getY());
            
            //Using the position of the small circle to generate values for 
            //an HSB (Hue, Saturation, Brightness) color code
            centerX = smallCircle.getCenterX();
            centerY = smallCircle.getCenterY();
            //convert to ints
            int xInt = (int)centerX;
            int yInt = (int)centerY;
            //Controls the hue(color)
            int hueVal = (yInt + xInt)/2;
            //Convert it back to a double because we need double values for the saturation and value(brightness)
            double doubleHueValue = (double)hueVal;
            //create a saturation and brightness value and scale it up to make the difference more visible
            double finalSatValue = (doubleHueValue/1000) * 3;
            double finalDarkValue = (doubleHueValue/1000) * 4.3; 
            
            //set the precision for the HSB display, otherwise it can overflow
            String satPrecision = String.format("%.2f", finalSatValue);
            String darkPrecision = String.format("%.2f", finalDarkValue);
            
            //set the color of the circle to change
            smallCircle.setFill(Color.hsb(hueVal, finalSatValue, finalDarkValue));
            
            //create a label to display the HSB values in real time
            label.setText("The current HSB color value is: " + hueVal + ", " + satPrecision + ", " + darkPrecision);

        });

        pane.getChildren().addAll(bigCircle, smallCircle, label);

        stage.setTitle("BCS345 Extra Credit");
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch();
    }

}