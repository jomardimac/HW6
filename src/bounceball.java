import java.applet.AudioClip;
import java.awt.*;

public class bounceball extends Ball {
    private int bounceCount;
    //this will all be parsed in by the xml and is the constructor for bounceball:

    public bounceball(int bRadius, int bInitXpos, int bInitYpos, int bSpeedX, int bSpeedY, int bMaxBallSpeed,  Color bColor, int bCount, AudioClip bOutSound, Player player, GameWindow gameW)
    {
        super(bRadius,bInitXpos,bInitYpos,bSpeedX,bSpeedY,bMaxBallSpeed,bColor,bOutSound,player,gameW);
        setBounceCount(bCount);
    }

    public int getBounceCount () {return bounceCount;}

    public void setBounceCount(int num) {bounceCount = num;}

    public void updateBounceCount() {bounceCount -= 1;}
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
}
