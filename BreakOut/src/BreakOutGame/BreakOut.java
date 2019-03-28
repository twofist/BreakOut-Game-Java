package BreakOutGame;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class BreakOut extends Application {
 
	private double cWidth = 300;
	private double cHeight = 250;
	private final int blockRows = 8;
	private final int blockColumns = 14;
	private Timeline TIMELINE;
	private Block blockList[][] = new Block[blockRows][blockColumns];
	private Ball ballList[] = new Ball[1];
	private Timer timer;
	private final double increaseSpeed = 0.01*(cWidth/cHeight);
	private FpsCounter fpsCounter;
	
	private Canvas CANVAS;
	private GraphicsContext CONTEXT;
	
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.getIcons().add(new Image("file:breakOutFavicon.png"));
        primaryStage.setTitle("BreakOut");
        StackPane pane = new StackPane();
        ResizableCanvas canvas = new ResizableCanvas();
        pane.widthProperty().addListener(evt -> resize(pane));
        pane.heightProperty().addListener(evt -> resize(pane));
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setOnMouseClicked((a) -> TIMELINE.play());
        pane.getChildren().add(canvas);
        primaryStage.setScene(new Scene(pane, Color.LIGHTBLUE));
        primaryStage.show();
        animationFrame(canvas, gc);
    }
    
    private void resize(StackPane pane) {
    	cWidth = pane.getWidth();
    	cHeight = pane.getHeight();
    	if(TIMELINE != null) {
    		TIMELINE.stop();
        	restartGame("resized");
    	}
    }
    
    private void animationFrame(Canvas canvas, GraphicsContext gc) {
    	final Duration dur = Duration.millis(1000/60);
    	final Timeline timeline = new Timeline();
    	CANVAS = canvas;
    	CONTEXT = gc;
    	TIMELINE = timeline;
    	TIMELINE.setCycleCount(Animation.INDEFINITE);
    	TIMELINE.setAutoReverse(false);
    	for(int ii = 0; ii < ballList.length; ii++) {
    		ballList[ii] = new Ball(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/canvas.getHeight(), Color.BLUE, canvas.getWidth()/25, canvas.getWidth()/25);
    	}
    	Paddle paddle = new Paddle(canvas.getWidth()/2, canvas.getHeight()/1.1, canvas.getWidth()/canvas.getHeight()*3, Color.RED, canvas.getWidth()/5, canvas.getHeight()/30, canvas);
    	createBlocks(canvas, blockRows, blockColumns);
    	fpsCounter = new FpsCounter(gc, canvas.getWidth(), 0, 20);
		fpsCounter.startCounting();
    	TIMELINE.getKeyFrames().add(new KeyFrame(dur,
    		new EventHandler<ActionEvent>() {
    		   @Override
    		   public void handle(ActionEvent args0) {
    			  clearCanvas(canvas);
    			  checkOnFocus(canvas, TIMELINE);
    			  fpsCounter.drawFps();
    			  
    			  for(Ball ball: ballList) {
    				  ball.drawBall(gc);
        			  ball.moveBall();
        			  ballCollideBorder(canvas, gc, ball);
    			  }
    			  
    			  paddle.drawPaddle(gc);
    			  paddle.movePaddle();
    			  paddleCollideBorder(canvas, paddle);
    			  
    			  for(Block[] row: blockList) {
    				  for(Block block: row) {
    					  block.drawBlock(gc);
    					  
    				  }
    			  }
    			  
    			  collisionDetectionPaddleBall(ballList, paddle);
    			  collisionDetectionBallBlock(blockList, ballList);
    		   }
    		})
    	 );
    	TIMELINE.play();
    }
    
    private void createBlocks(Canvas canvas, int columns, int rows) {
        double offsetw = 5*(canvas.getWidth()/canvas.getHeight());
        double offseth = 2*(canvas.getWidth()/canvas.getHeight());
        
        double w = canvas.getWidth()/23;
        double h = canvas.getHeight()/30;
        
        double startx = (canvas.getWidth()-(rows+columns*w+rows+columns*offsetw))/4;
        double starty = 20;
        Block list[][] = new Block[columns][rows];
        
        Color color = Color.rgb(randomNumBetween(0,255), randomNumBetween(0,255), randomNumBetween(0,255));
        
        for(int iic = 0; iic < columns; iic++){
            for(int iir = 0; iir < rows; iir++){
                double x = startx+(iir*offsetw+(iir*w));
                double y = starty+(iic*offseth+(iic*h));

                Block block = new Block(x,y,color,w,h);
                list[iic][iir] = block;
            }
        }
        
        blockList = list;
    }
    
    private void checkOnFocus(Canvas canvas, Timeline tl) {
    	if(!canvas.isFocused()) {
			tl.pause();
		}
    }
    
    private void clearCanvas(Canvas canvas) {
    	canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(), canvas.getHeight());
    }
    
    private void ballCollideBorder(Canvas canvas, GraphicsContext gc, Ball ball) {
    	if(ball.y < 0)
            ball.vely *= -1;
        if(ball.y > canvas.getHeight()) {
        	restartGame("lost");
        }
        if(ball.x > canvas.getWidth())
            ball.velx *= -1;
        if(ball.x < 0)
            ball.velx *= -1;
    }
    
    private void paddleCollideBorder(Canvas canvas, Paddle paddle) {
    	if(paddle.x+paddle.width > canvas.getWidth())
            paddle.x = canvas.getWidth()-paddle.width;
        if(paddle.x < 0)
            paddle.velx = 0;
    }
    
    private void collisionDetectionPaddleBall(Ball[] list, Paddle paddle) {
    	for(Ball ball: list) {
    		if(collisionBallRect(ball, paddle))
                ball.vely *= -1; 
    	}
    }
    
    private void collisionDetectionBallBlock(Block[][] list, Ball[] list2) {
    	for(Block[] row: list) {
			  for(Block block: row) {
				  for(Ball ball: list2) {
					  if(!block.destroyed && collisionBallRect(ball, block)){
		                    ball.vely *= -1;
		                    block.destroyBlock();
		                    ball.addDestroyedBlock(1);
		                    System.out.println(""+ball.destroyedBlocks+"-"+list.length*row.length);
		                    if(ball.destroyedBlocks == list.length*row.length) {
		                    	restartGame("won");
		                    }
		                    ball.increaseSpeed(increaseSpeed);
		                    changeColorTimer(list, ball);
		              }
				  }
			  }
		  }
    }
    
    private void restartGame(String message) {
    	System.out.println(message);
    	TIMELINE.stop();
    	TIMELINE = null;
    	animationFrame(CANVAS, CONTEXT);
    }
    
    private void changeColorTimer(Block[][] list, Ball ball) {
    	if(timer != null)
        	timer.cancel();
        Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for(Block[] row: list) {
					for(Block block: row) {
						int rgb[] = {(int) Math.round(block.color.getRed()*255),(int) Math.round(block.color.getGreen()*255),(int) Math.round(block.color.getBlue()*255)};
						for(int ii = 0; ii < rgb.length; ii++) {
							rgb[ii] = changeRGB(rgb[ii]);
						}
						Color color = Color.rgb(rgb[0], rgb[1], rgb[2]);
						block.setColor(color);
					}
				}
			}
		}, 0,(long) (1000-(ball.speed*10)));
    }
    
    private int changeRGB(int color) {
    	color += randomNumBetween(0,10);
    	
    	if(color > 255)
    		color = 0;
    	
    	return color;
    }
    
    private int randomNumBetween(int min, int max) {
    	return new Random().nextInt((max - min) + 1) + min;
    }

	private boolean collisionBallRect(Ball ball, Rectangle rect) {
		double x = Math.max(rect.x, Math.min(ball.x, rect.x+rect.width));
		double y = Math.max(rect.y, Math.min(ball.y, rect.y+rect.height));
		
		double distance = Math.sqrt((x - ball.x) * (x - ball.x) + (y - ball.y) * (y - ball.y));
		
		return distance < (ball.width+ball.height)/2;
	}
}
