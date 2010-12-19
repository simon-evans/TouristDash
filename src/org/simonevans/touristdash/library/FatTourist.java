package org.simonevans.touristdash.library;

public class FatTourist extends EnemyType {
	
	public FatTourist() {
		width = 80;
		height = 66;
		enemyImage = R.drawable.fat_tourist;
	}
	
	public void kill(Game theGame) {
		super.kill(theGame);
		theGame.fattysKilled++;
	}
}
