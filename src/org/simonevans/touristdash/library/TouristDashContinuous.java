package org.simonevans.touristdash.library;

import android.database.Cursor;
import android.os.Bundle;

public class TouristDashContinuous extends TouristDash {
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		this.game = new Game();
		gameCanvas.game = this.game;
	}
}
