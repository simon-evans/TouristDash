package org.simonevans.touristdash.library;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class UpgradesActivity extends Activity {
	
	UserData userdata;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		userdata = new UserData(this);
		
		init();
	}
	
	private void init() {
		
		setContentView(R.layout.upgrades);
		
		Button moreAmmoButton = (Button) findViewById(R.id.addAmmoButton);
		moreAmmoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				userdata.buyAmmo();
				updateAmmo();
			}
			
		});
		updateAmmo();
	}
	
	private void updateAmmo() {
		TextView ammoLabel = (TextView) findViewById(R.id.currentAmmoLabel);
		TextView ammoCount = (TextView) findViewById(R.id.ammoLabel);
		ammoCount.setText("Hat Ammo (" + userdata.fetchAmmo() + ")");
		ammoLabel.setText("£" + (userdata.getAmmoCost()));
		
		Button moreAmmoButton = (Button) findViewById(R.id.addAmmoButton);
		if(userdata.fetchMoney() < userdata.getAmmoCost()) {
			moreAmmoButton.setClickable(false);
			ammoLabel.setTextColor(Color.RED);
		} else {
			moreAmmoButton.setClickable(true);
			ammoLabel.setTextColor(Color.parseColor("#228b22"));
		}
	}

}
