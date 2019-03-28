package BreakOutGame;
import javafx.scene.paint.Color;

public class Rectangle {

	protected double x;
	protected double y;
	protected Color color;
	protected double width;
	protected double height;
	
	protected Rectangle(double x, double y, Color color, double width, double height) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.width = width;
		this.height = height;
	}
	
	protected void setColor(Color color) {
		this.color = color;
	}

}
