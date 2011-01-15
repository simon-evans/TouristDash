package org.simonevans.touristdash.library;

import android.os.Bundle;
import android.util.Log;

public class TouristDashContinuous extends TouristDash {
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.w("SIMON", "foo");
		this.game = new Game();
		gameCanvas.game = this.game;
	}
	
	
}
