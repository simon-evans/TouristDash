package org.simonevans.touristdash.library;

import java.util.Random;

public class WaveGame extends Game {
	
	private int scoreLimit;
	public boolean win;
	private int waveDistance;
	private int enemiesPerWave;
	private int waveHeight;
	
	public WaveGame(int scoreLimit, int waveDistance, int enemiesPWave, int waveHeight) {
		this.scoreLimit = scoreLimit;
		this.waveDistance = waveDistance;
		this.enemiesPerWave = enemiesPWave;
		this.waveHeight = waveHeight;
		win = false;
	}
	
	@Override
	protected void specialCheckScore() {
		if(score >= scoreLimit) {
			playing = false;
            gameover = true;
            win = true;
		}
		
		if(this.counter % waveDistance == 0 && counter > 0 && this.score + (2 * waveHeight) < scoreLimit) {
			addWave();
		}
	}
	
	@Override
	protected void updateEnemies() {
    	int index;
    	Enemy enemy;
        for(int i = 0; i < enemies.size(); i++) {
        	
        	enemy = enemies.get(i);
            enemy.type.update(enemy,this);
            if((index = enemy.type.detectShot(this, enemy)) >= 0) {
            	enemy.kill(this);
            	bullets.remove(index);
            }
            
            if(enemy.yCoord > 375) {
                if(enemy.yCoord < 420 && enemy.type.detectCollision(this, enemy)) {
                    playing = false;
                    gameover = true;
                } else if(enemy.yCoord > 480) {
                    enemies.remove(enemy);
                }
            }
            
        }    	
    }
	
	Random random = new Random(System.currentTimeMillis());
	
	private void addWave() {
		
		int height;
		int column;
		
		boolean[] waveLayout = new boolean[8*waveHeight];
		
		for(int i = 0; i < enemiesPerWave; i++) {
			do {
				height = random.nextInt(waveHeight);
				column = random.nextInt(8);
			} while(waveLayout[(height*8)+column] == true);
			waveLayout[(height*8)+column] = true;
			Enemy enemy = new Enemy(i);
			enemy.putInCol(column);
			enemy.changeType();
			enemy.yCoord =  -66 - (height * 60);
			
			enemies.add(enemy);
		}
		
	}
	
	

}
