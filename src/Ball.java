import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;

public class Ball
{
    /*Properties of the basic ball. These are initialized in the constructor using the values read from the config.xml file*/
	protected  int pos_x;
	protected int pos_y;
	protected int radius;
	protected int first_x;
	protected int first_y;
	protected int x_speed;
	protected int y_speed;
	protected int maxspeed;
	protected Color color;
	protected AudioClip outSound;
	
    protected GameWindow gameW;
	protected Player player;
	
	/*constructor*/
	public Ball () {};
	public Ball (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW)
	{	
		this.radius = radius;

		pos_x = initXpos;
		pos_y = initYpos;

		first_x = initXpos;
		first_y = initYpos;

		x_speed = speedX;
		y_speed = speedY;

		maxspeed = maxBallSpeed;

		this.color = color;

		this.outSound = outSound;

		this.player = player;
		this.gameW = gameW;

	}

	/*update ball's location based on it's speed*/
	public void move ()
	{
		pos_x += x_speed; //this will move the position in the x axis
		pos_y += y_speed; //this will move the position in the y axis
		isOut(); //should see if the ball is out of the window

	}

	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit () {

		resetBallPosition();
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */	
	public boolean userHit (int mouse_x, int mouse_y)
	{

		double x = mouse_x - pos_x; //will be used to check if it is hit
		double y = mouse_y - pos_y; //will be used y coordinate to check if ball is hit

		double distance = Math.sqrt ((x*x) + (y*y)); //algorithm that mouse did hit the ball

		//if hit:
		if (distance-this.radius < (int)(player.scoreConstant)) {

			//add the score based on the speed:
			player.addScore (player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			//at the same time, add that score for a counter to a new life:
			player.setEarnLives(player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			//if the player has earned enough points to gain lives:
			if(player.getEarnLives() >= 100) {
				player.updateLives(-1);
				//reset because you want it only going 100:
				player.resetEarning();
			}
			player.addHit(); //because the ball was hit, add a tick on that variable

			return true;
		}
		//not hit:
		else {

			return false;
		}
	}

    /*reset the ball position to its initial starting location*/
	protected void resetBallPosition()
	{
		pos_x = first_x;
		pos_y = first_y;
		//will be changing the ball's speed:
		x_speed = randSpd();
		//y_speed = randSpd();
	}
	
	/*check if the ball is out of the game borders. if so, game is over!*/ 
	protected boolean isOut ()
	{
		//if the ball hits a border:
		if ((pos_x < gameW.x_leftout) || (pos_x > gameW.x_rightout) || (pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {
			//go back to the initial position
			resetBallPosition();
			outSound.play();
			player.updateLives(1); //ball died, user loses a life
			if (player.getLives() == 0){
				player.gameIsOver();
				return true;
			}
			return true;
		}


		else return false;
	}

	/*draw ball*/
	public void DrawBall (Graphics g)
	{
		g.setColor (color);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
	}

	//create a random function that will return a random number from the negative maxspeed to maxspeed
	public int randSpd(){
		return (-maxspeed + (int) (Math.random() * ((maxspeed - (-maxspeed) + 1))));

	}

}

