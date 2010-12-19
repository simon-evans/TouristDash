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
	
	public void update(Enemy thisEnemy, Game theGame) {
		thisEnemy.yCoord += (0.2 + theGame.speedIncrease/10) * theGame.timeDifference;
	}
	
	public int detectShot(Game game, Enemy thisEnemy) {
		
		for(Bullet bullet : game.bullets) {
			if(thisEnemy.xCoord - 20 <= bullet.xCoord && bullet.xCoord <= thisEnemy.xCoord + width - 9 && thisEnemy.yCoord >= -5 && thisEnemy.yCoord + height >= bullet.yCoord && thisEnemy.yCoord <= bullet.yCoord) {
				return game.bullets.indexOf(bullet);
			}
		}
		return -1;
		
	}
	
	public void kill(Game theGame) {
		theGame.collectedMoney += UserData.getUserData().getEarnings();
	}
	
}
