package BreakOutGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class Ball {

	protected double x;
	protected double y;
	protected double speed;
	protected Color color;
	protected double width;
	protected double height;
	protected double velx;
	protected double vely;
	protected int destroyedBlocks = 0;
	
	protected Ball(double x, double y, double speed, Color color, double width, double height) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.color = color;
		this.width = width;
		this.height = height;
		this.velx = speed;
		this.vely = -speed;
	}

	protected void moveBall() {
		this.x += this.velx*speed;
		this.y += this.vely*speed;
	}
	
	protected void drawBall(GraphicsContext gc) {
		gc.setFill(this.color);
		gc.fillArc(x, y, width, height, 0, 360, ArcType.ROUND);
	}

	protected void increaseSpeed(double speed) {
		this.speed+=speed;
	}
	
	protected void addDestroyedBlock(int amount) {
		destroyedBlocks += amount;
	}
}
