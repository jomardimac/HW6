public class Player
{
	
	private int score;			   //player score
	private int playerLives;
	private int earnLives;
	private int clickHit;
	private int totalClicks;
	private double percentHit;
	private double percentMiss;
	private boolean gameover=false;	
	public int scoreConstant = 10; //This constant value is used in score calculation. You don't need to change this. 		

	
	public Player()
	{
		score = 0; //initialize the score to 0
		playerLives = 2; //gonna parse xml later
		earnLives = 0; //another parse
	}

	/* get player score*/
	public int getScore ()
	{
		return score;
	}

	// get player's lives:
	public int getLives() { return playerLives;}
	//getter for earning a life:
	public int getEarnLives() { return earnLives;}

	//setter for earning a life:
	public void setEarnLives(int scorer){
		earnLives += scorer;
	}
	//resetting the earning, will be used every 100 points
	public void resetEarning() {
		earnLives = 0;
	}



	//getting for clickhit:
	public int getHit(){
		return clickHit;
	}
	//adds a tick when it hits the ball
	public void addHit(){
		clickHit += 1;
	}
	//getter for clickMiss;
	public int getTotalClicks(){
		return totalClicks;
	}
	//adds a tick whenever the mouse clicks the screen:
	public void addClicks(){
		totalClicks += 1;
	}
	public double getPercentHit() {
		percentHit = (double) clickHit / totalClicks * 100;
		percentHit = Math.round(percentHit);
		return percentHit;
	}
	public double getPercentMiss(){
		percentMiss = Math.round((double) (totalClicks-clickHit) / totalClicks * 100);
		percentMiss = Math.round(percentMiss);
		return percentMiss;
	}



	/*check if the game is over*/
	public boolean isGameOver ()
	{
		return gameover;
	}

	/*update player score*/
	public void addScore (int plus)
	{
		score += plus;
	}

	//update player lives:
	public void updateLives (int died) {
		playerLives -= died;
	}
	/*update "game over" status*/
	public void gameIsOver ()
	{
		gameover = true;
	}

}