package org.simonevans.touristdash.library;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OptionsActivity extends Activity {
	
	public void onCreate(Bundle b){
		super.onCreate(b);
		
		setContentView(R.layout.options);
		
		Button addCashButton = (Button) findViewById(R.id.addCashButton);
		
		addCashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				UserData.getUserData().addEarnings(500);
				
			}
		
		});
		
	}

}
