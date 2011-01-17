package org.simonevans.touristdash.library.menus;

import org.simonevans.touristdash.library.R;
import org.simonevans.touristdash.library.UserData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class OptionsActivity extends Activity {

	static final int MAX_SENSITIVITY = 9;
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		
		setContentView(R.layout.options);
		
		/*
		 * Useful when introducing new upgrades. Should not be available in normal game.
		 *  Button addCashButton = (Button) findViewById(R.id.addCashButton);
		
		addCashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				UserData.getUserData().addEarnings(500);
				
			}
		
		});
		*/
		/**
		 * 
		 * To stop seek bar getting set to 0 (which would mean it player would never
		 * move), we store an incremented value and then adjust for this when displaying
		 * back to user. Values therefore in range 1-10.
		 * 
		 */
		
		SeekBar sensitivity = (SeekBar) findViewById(R.id.sensitivitySlider);
		sensitivity.setMax(MAX_SENSITIVITY);
		int curSensitivity = UserData.getUserData().getSensitivity();
		sensitivity.setProgress(curSensitivity--);
		
		sensitivity.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int newSensitivity = seekBar.getProgress() + 1;
				UserData.getUserData().setSensitivity(newSensitivity);
			}
			
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }


			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
		});
		
	}

}
