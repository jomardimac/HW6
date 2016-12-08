import java.applet.AudioClip;
import java.awt.*;

public class bounceball extends Ball {
    private int bounceCount;
    public int tempCount;
    //this will all be parsed in by the xml and is the constructor for bounceball:

    public bounceball(int bRadius, int bInitXpos, int bInitYpos, int bSpeedX, int bSpeedY, int bMaxBallSpeed,  Color bColor, int bCount, AudioClip bOutSound, Player player, GameWindow gameW)
    {
        super(bRadius,bInitXpos,bInitYpos,bSpeedX,bSpeedY,bMaxBallSpeed,bColor,bOutSound,player,gameW);
        setBounceCount(bCount);
        tempCount = bCount;
    }

    public int getBounceCount () {return bounceCount;}

    public void setBounceCount(int num) {bounceCount = num;}

    public void updateBounceCount() {bounceCount -= 1;}

    @Override
    protected boolean isOut() {
        //no more bounces left:
        if(getBounceCount() == 0){
            resetBallPosition();
            outSound.play();
            player.updateLives(1);
            if (player.getLives() == 0) {
                player.gameIsOver();
                return true;
            }
            else{
                setBounceCount(tempCount); //this should be coming from the xml
                return true;
            }
        }
        if ((pos_x < gameW.x_leftout) || (pos_x >gameW.x_rightout)){
            x_speed += -2*x_speed;
            updateBounceCount();
            return false;
        }
        if ((pos_y < gameW.y_upout) || (pos_y > gameW.y_downout)) {
            y_speed+= -2*y_speed;
            updateBounceCount();
            return false;
        }


        else return false;
    }

    /*check whether the player hit the ball. If so, update the player score based on the current ball speed. */
    public boolean userHit (int mouse_x, int mouse_y)
    {

        double x = mouse_x - pos_x; //will be used to check if it is hit
        double y = mouse_y - pos_y; //will be used y coordinate to check if ball is hit

        double distance = Math.sqrt ((x*x) + (y*y)); //algorithm that mouse did hit the ball

        //if hit:
        if (distance-this.radius < (int)(player.scoreConstant)) {
            setBounceCount(tempCount);
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
}
