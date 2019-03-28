package BreakOutGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block extends Rectangle {
	
	protected boolean destroyed = false;

	protected Block(double x, double y, Color color, double width, double height) {
		super(x,y,color,width,height);
	}

	protected void drawBlock(GraphicsContext gc){
		if(!destroyed) {
			gc.setFill(this.color);
			gc.fillRect(x, y, width, height);
		}
	}
	
	protected void destroyBlock() {
		this.destroyed = true;
	}
}
