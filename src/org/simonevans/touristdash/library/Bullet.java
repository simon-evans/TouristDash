package org.simonevans.touristdash.library;

public class Bullet {
	
	//int bulletImage;
	
	int xCoord;
	int yCoord;
	
	Bullet(int x) {
		xCoord = x;
		yCoord = 420;
		
		
	}
	
	public void update() {
		
		this.yCoord -= 4;
		
		
	}

}
