package org.simonevans.touristdash.library;

public class BasicEnemy extends EnemyType {

	public BasicEnemy() {
		width = 38;
		height = 66;
		enemyImage = R.drawable.camera_man;
	}
	
	public void kill(Game theGame) {
		super.kill(theGame);
		theGame.camerasKilled++;
	}
	
	

}
