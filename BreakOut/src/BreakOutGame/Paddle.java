package BreakOutGame;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class Paddle extends Rectangle{

	private boolean[] keys = {false, false};
	protected double speed;
	protected double velx;
	protected double vely;
	protected Canvas canvas;
	
	protected Paddle(double x, double y, double speed, Color color, double width, double height, Canvas canvas) {
		super(x,y,color,width,height);
		this.speed = speed;
		this.canvas = canvas;
		addKeyEvents();
	}

	protected void drawPaddle(GraphicsContext gc) {
		gc.setFill(this.color);
		gc.fillRect(x, y, width, height);
	}
	
	protected void movePaddle() {
		if(keys[0]) this.velx = this.speed;
		if(keys[1]) this.velx = -this.speed;
		
		this.x += this.velx;
	}
	
	private void addKeyEvents() {
		canvas.setFocusTraversable(true);
		this.canvas.setOnKeyPressed(new EventHandler <KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				switch(e.getCode()) {
            	case RIGHT:
            		keys[0] = true;
            		break;
            	case LEFT:
            		keys[1] = true;
            		break;
				default:
					break;
				}
			}
		});
		
		this.canvas.setOnKeyReleased(new EventHandler <KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				switch(e.getCode()) {
            	case RIGHT:
            		keys[0] = false;
            		break;
            	case LEFT:
            		keys[1] = false;
            		break;
				default:
					break;
				}
			}
		});
	}
}
