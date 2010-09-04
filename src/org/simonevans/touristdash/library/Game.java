/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.simonevans.touristdash.library;

import java.util.ArrayList;
import java.util.Random;

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
    int INITIAL_ENEMIES = 6;

    ArrayList<Enemy> enemies;

    Game() {
        reset();
    }

    Random delayGen = new Random(System.currentTimeMillis());

    public void reset() {

        enemies = new ArrayList<Enemy>();

        int i=0;
        while(i < INITIAL_ENEMIES) {
            enemies.add(new Enemy());
            i++;
        }

        userXCoord = 140;
        userYCoord = 400;

        gameover = false;

        score = 0;
    }

    public void update() {
        Enemy enemy;
        score++;
        for(int i = 0; i < enemies.size(); i++) {
             enemy = enemies.get(i);
             enemy.yCoord += 5;

            if(enemy.yCoord > 375) {
                if(userXCoord - 39 < enemy.xCoord && enemy.xCoord < userXCoord + 39) {
                    playing = false;
                    gameover = true;
                } else if(enemy.yCoord > 440) {
                    enemy.respawn();
                }
            }
        }
        if((score % 1000) == 0) {
            addEnemy();
        }

    }

    private void addEnemy() {
        
        enemies.add(new Enemy());

    }

    class Enemy {

        float xCoord;
        float yCoord;

        Enemy() {
            respawn();
        }

        private void respawn() {
            yCoord = -66 - (delayGen.nextInt(8) * 80);
            xCoord = delayGen.nextInt(8) * 40;
        }
    }

}