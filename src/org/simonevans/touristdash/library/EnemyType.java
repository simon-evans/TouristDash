package org.simonevans.touristdash.library;

public abstract class EnemyType {

	int enemyImage;
	int height;
	int width;
	
	public boolean detectCollision(Game game, Enemy thisEnemy) {
		if((game.userXCoord - width) < thisEnemy.xCoord && thisEnemy.xCoord < (game.userXCoord + 39)) { // the 39 is the player's width. Really should save this somewhere...
			return true;
		}
		return false;
	}
	
	public void update(Enemy thisEnemy) {
		thisEnemy.yCoord += 5;
	}
	
}
