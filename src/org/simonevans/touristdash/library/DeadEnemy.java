package org.simonevans.touristdash.library;

public class DeadEnemy extends EnemyType {

	DeadEnemy() {
		enemyImage = R.drawable.dead_enemy;
		height = 40;
		width = 66;
	}
	
	public boolean detectCollision(Game game, Enemy enemy) {
		return false;
	}
	
	
}
