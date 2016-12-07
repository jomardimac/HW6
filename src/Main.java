import java.awt.*;
import java.util.*;
import java.applet.*;
import java.net.*;
import java.awt.event.MouseEvent;
import javax.swing.event.*;
/*<applet code="Main" height=400 width=400></applet>*/


public class Main extends Applet implements Runnable
{

/* Configuration arguments. These should be initialized with the values read from the config.xml file*/					
    private int numBalls;
/*end of config arguments*/

    private int refreshrate = 15;	           //Refresh rate for the applet screen. Do not change this value. 
	private boolean isStopped = true;
    Font f = new Font ("Arial", Font.BOLD, 18);
	
	private Player player;			           //Player instance.		
	private Ball redball;                      //Ball instance. You need to replace this with an array of balls.
	private bounceball blueball;
	private Ball[] ball;
	Thread th;						           //The applet thread. 

	AudioClip shotnoise;	
	AudioClip hitnoise;		
	AudioClip outnoise;		
	  
    Cursor c;				
    private GameWindow gwindow;                 // Defines the borders of the applet screen. A ball is considered "out" when it moves out of these borders.
	private Image dbImage;
	private Graphics dbg;

	
	class HandleMouse extends MouseInputAdapter 
	{

    	public HandleMouse() 
    	{
            addMouseListener(this);
        }
		
    	public void mouseClicked(MouseEvent e) 
    	{

        	if (!isStopped) {
				if (redball.userHit (e.getX(), e.getY())) {
	        		hitnoise.play();

					redball.ballWasHit ();

	        	}
	        	if(blueball.userHit(e.getX(),e.getY())){
					hitnoise.play();
					blueball.ballWasHit();;
				}
				else {
					player.addClicks();
					shotnoise.play();
				}
			}
			else if (isStopped && e.getClickCount() == 2) {
				isStopped = false;
				init ();
			}

    	}

    	public void mouseReleased(MouseEvent e) 
    	{
           
    	}
        
    	public void RegisterHandler() 
    	{

    	}
    }
	
    /*initialize the game*/
	public void init ()
	{	
		c = new Cursor (Cursor.CROSSHAIR_CURSOR);
		this.setCursor (c);
		HandleMouse hm = new HandleMouse();	
				
        Color superblue = new Color (0, 0, 255);  
		setBackground (Color.black);
		setFont (f);

		if (getParameter ("refreshrate") != null) {
			refreshrate = Integer.parseInt(getParameter("refreshrate"));
		}
		else refreshrate = 15;

		
		hitnoise = getAudioClip (getCodeBase() , "gun.au");
		hitnoise.play();
		hitnoise.stop();
		shotnoise = getAudioClip (getCodeBase() , "miss.au");
		shotnoise.play();
		shotnoise.stop();
		outnoise = getAudioClip (getCodeBase() , "error.au");
		outnoise.play();
		outnoise.stop();

		
		player = new Player ();
		/* The parameters for the GameWindow constructor (x_leftout, x_rightout, y_upout, y_downout) 
		should be initialized with the values read from the config.xml file*/	
		gwindow = new GameWindow(10,370,45,370);
		/* The parameters for the Ball constructor (radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color) 
		should be initialized with the values read from the config.xml file. Note that the color value need to be converted from String to Color. */	
		redball = new Ball(10, 190, 250, 1, -1, 2, Color.red, outnoise, player, gwindow);
		blueball = new bounceball(12,190,150,1,1,3,Color.blue, 10, outnoise, player, gwindow);

	}
	
	/*start the applet thread and start animating*/
	public void start ()
	{		
		if (th==null){
			th = new Thread (this);
		}
		th.start ();
	}
	
	/*stop the thread*/
	public void stop ()
	{
		th=null;
	}

    
	public void run ()
	{	
		/*Lower this thread's priority so it won't infere with other processing going on*/
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        /*This is the animation loop. It continues until the user stops or closes the applet*/
		while (true) {
			if (!isStopped) {
				redball.move();
				blueball.move();
			}
            /*Display it*/
			repaint();
            
			try {
				
				Thread.sleep (refreshrate);
			}
			catch (InterruptedException ex) {
				
			}			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	
	public void paint (Graphics g)
	{
		/*if the game is still active draw the ball and display the player's score. If the game is active but stopped, ask player to double click to start the game*/ 
		if (!player.isGameOver()) {
			g.setColor (Color.yellow);
			
			g.drawString ("Score: " + player.getScore(), 10, 40);
			g.drawString ("You have: " + player.getLives() + "  Lives", 10, 60);
			g.drawString ("Total Clicks: " + player.getTotalClicks(), 10,80);



			redball.DrawBall(g);
			blueball.DrawBall(g);
			
			if (isStopped) {
				g.setColor (Color.yellow);
				g.drawString ("Doubleclick on Applet to start Game!", 40, 200);
			}
		}
		/*if the game is over (i.e., the ball is out) display player's score*/
		else {
			g.setColor (Color.yellow);

			
			g.drawString ("Game over!", 130, 100);
			g.drawString ("You scored " + player.getScore() + " Points!", 100, 140);
			g.drawString ("Hit Percentage: " + player.getPercentHit() + " %", 70, 160);
			g.drawString ("Miss Percentage: " + player.getPercentMiss() + " %", 70, 190);

			if (player.getScore() < 300) g.drawString ("Well, it could be better!", 100, 210);
			else if (player.getScore() < 600 && player.getScore() >= 300) g.drawString ("That was not so bad", 100, 210);
			else if (player.getScore() < 900 && player.getScore() >= 600) g.drawString ("That was really good", 100, 210);
			else if (player.getScore() < 1200 && player.getScore() >= 900) g.drawString ("You seem to be very good!", 90, 210);
			else if (player.getScore() < 1500 && player.getScore() >= 1200) g.drawString ("That was nearly perfect!", 90, 210);
			else if (player.getScore() >= 1500) g.drawString ("You are the Champion!",100, 200);

			g.drawString ("Doubleclick on the Applet, to play again!", 20, 240);

			isStopped = true;
			//player.resetTotalClicks(); //resets the total click to 0 just in case it isn't
		}
	}

	
	public void update (Graphics g)
	{
		
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		
		dbg.setColor (getForeground());
		paint (dbg);

		
		g.drawImage (dbImage, 0, 0, this);
	}
}

