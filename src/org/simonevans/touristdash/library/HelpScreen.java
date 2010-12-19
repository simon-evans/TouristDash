package org.simonevans.touristdash.library;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpScreen extends Activity {
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
        setContentView(R.layout.howtoplay);

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               finish();
            }
        });
    }

}
