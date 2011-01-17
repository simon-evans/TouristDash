/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.simonevans.touristdash.library;

import java.util.ArrayList;

import android.util.Log;


/**
 *
 * @author Simon
 * 
 * Handles most of the game logic with  regard updating.
 * Eventually this should all be moved into an interface.
 * 
 */
public class Game {

    int userXCoord;
    int userYCoord;
    int score;
    boolean gameover;
    boolean playing = false;
    int INITIAL_ENEMIES = 5;
    ArrayList<Bullet> bullets;
    long lastShot = 0;
    int speedIncrease;
    int counter;
    int ammo;
    int collectedMoney;
    UserData userData;
    
    int camerasKilled = 0;
    int fattysKilled = 0;
    
    long lastFrameTime = 0;
    long timeDifference = 0;
    long newTime = 0;

    ArrayList<Enemy> enemies;

    Game() {
    	
    	userData = UserData.getUserData();
        reset();
    }


    public void reset() {

        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<Bullet>();

        int i = 0;
        while(i < INITIAL_ENEMIES) {
        		enemies.add(new Enemy(i));
        		i++;
        }

        userXCoord = 140;
        userYCoord = 400;

        gameover = false;

        lastFrameTime = 0;
        newTime = 0;
        score = 0;
        counter = 0;
        speedIncrease = 1;
        ammo = UserData.getUserData().fetchAmmo();
        collectedMoney = 0;
    }

    public void update() {
    	
    	newTime = System.currentTimeMillis();
    	timeDifference = newTime - lastFrameTime;
    	if(timeDifference == newTime) {
    		timeDifference = 1;
    	}
    	lastFrameTime = newTime;
    	
    	
    	counter++;
    	updateBullets();
    	updateEnemies();
    	
        standardCheckScore();
        specialCheckScore();
        
        
    }
    
    /**
     * 
     * This is used for every game and changes game parameters when the counter hits 
     * particular values.
     * 
     */
    
    protected void standardCheckScore() {
    	if((counter % 1000) == 0) {
            addEnemy();
        }
        
        if((counter % 3) == 0) {
        	score += speedIncrease;
        }
    }
    
    /**
     * 
     * New types of games may override this method with their own way to change as
     * the game progresses.
     * 
     */
    
    protected void specialCheckScore() {
        if((counter % 2000) == 0) {
        	speedIncrease += 1;
        }
    }
    
    /**
     * 
     * Updates the location of bullets on screen and removes any that have gone 
     * above the top of the screen.
     * 
     * Using the for(Bullet bullet : bullets) syntax caused concurrency issues.
     * Even synchronizing it did not fix this.
     * 
     */
    private void updateBullets() {
    	Bullet bullet;
		for(int i = 0; i < bullets.size(); i++) {
    		bullet = bullets.get(i);
			bullet.update();
    		    		
    		if(bullet.yCoord < 0) {
    			bullets.remove(bullet);
    		}
    	}
    }
    
    protected void updateEnemies() {
    	int index;
        for(Enemy enemy : enemies) {
        	
        	enemy.type.update(enemy,this);
            if((index = enemy.type.detectShot(this, enemy)) >= 0) {
            	enemy.kill(this);
            	synchronized(this) {
            		bullets.remove(index);
            	}
            }
            
            if(enemy.yCoord > 375) {
                if(enemy.yCoord < 420 && enemy.type.detectCollision(this, enemy)) {
                    playing = false;
                    gameover = true;
                    
                    userData.addFattysKilled(fattysKilled);
                    userData.addCamerasKilled(camerasKilled);
                    userData.addEarnings(collectedMoney);
                    
                } else if(enemy.yCoord > 480) {
                    enemy.respawn();
                }
            }
            
        }    	
    }

    private void addEnemy() {
        
        enemies.add(new Enemy(enemies.size() + 1));

    }
    
    public void shoot() {
    	if(System.currentTimeMillis() - lastShot > 1000 && ammo > 0) {
    		synchronized(this) {
    			bullets.add(new Bullet(userXCoord));
    		}
    		ammo--;
    		lastShot = System.currentTimeMillis();
    	}
    	
    }

}