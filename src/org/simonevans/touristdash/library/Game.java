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
 */
public class Game {

    float userXCoord;
    float userYCoord;
    int score;
    boolean gameover;
    boolean playing = false;
    int INITIAL_ENEMIES = 5;

    ArrayList<Enemy> enemies;

    Game() {
        reset();
    }


    public void reset() {

        enemies = new ArrayList<Enemy>();

        int i = 0;
        while(i < INITIAL_ENEMIES) {
        		enemies.add(new Enemy(i));
        		i++;
        }

        userXCoord = 140;
        userYCoord = 400;

        gameover = false;

        score = 0;
    }

    public void update() {
        Enemy enemy;
        for(int i = 0; i < enemies.size(); i++) {
        	enemy = enemies.get(i);
            enemy.type.update(enemy);
        	
            if(enemy.yCoord > 375) {
                if(enemy.yCoord < 420 && enemy.type.detectCollision(this, enemy)) {
                    playing = false;
                    gameover = true;
                } else if(enemy.yCoord > 480) {
                    enemy.respawn();
                }
            }
        }
        if((score % 1000) == 0 && score > 0) {
            addEnemy();
        }
        score++;
        

    }

    private void addEnemy() {
        
        enemies.add(new Enemy(enemies.size() + 1));

    }

}