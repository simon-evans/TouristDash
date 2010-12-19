package org.simonevans.touristdash.library;

import java.util.Random;

public class Enemy {
	
	int xCoord;
    int yCoord;
    EnemyType type;
    int id;
    
    EnemyType basic = new BasicEnemy();
    EnemyType dead = new DeadEnemy();
    EnemyType fatty = new FatTourist();
    
    static Random delayGen = new Random();

    public Enemy(int i) {
    	id = i;
    	this.type = basic;
        respawn();
    }

    public void respawn() {
        this.yCoord = -66 - (delayGen.nextInt(7) * 60);
        
        this.changeType();
        putInCol(delayGen.nextInt(8));
    }
    
    protected void changeType() {
    	
    	int randType = delayGen.nextInt(10);
        
        switch(randType) {
        	case 0:
        	case 1:
        	case 2:
        	case 3:
        	case 4:
        	case 5:
        	case 6:
        	case 7:
            	type = basic;
            	break;
        	case 8:
        	case 9:
            	type = fatty;
            	break;
        }	
    	
    }
    
    void putInCol(int i) {
    	this.xCoord = i * 40;
    }
    
    public void kill(Game theGame) {
    	type.kill(theGame);
    	type = dead;
    }

}
