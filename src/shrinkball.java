import java.applet.AudioClip;
import java.awt.*;

public class shrinkball extends Ball {
    public int initRad;
    public shrinkball(int bRadius, int bInitXpos, int bInitYpos, int bSpeedX, int bSpeedY, int bMaxBallSpeed, Color bColor, AudioClip bOutSound, Player player, GameWindow gameW)
    {
        super(bRadius,bInitXpos,bInitYpos,bSpeedX,bSpeedY,bMaxBallSpeed,bColor,bOutSound,player,gameW);
        initRad = bRadius;
    }

    @Override
    public void ballWasHit() {
        int tooSmall = (int) (initRad*.3);
        if(radius <= tooSmall) {
            radius = initRad;
            resetBallPosition();
        }
        else {
            radius = (int) (radius * 0.7);
        }
    }
}
