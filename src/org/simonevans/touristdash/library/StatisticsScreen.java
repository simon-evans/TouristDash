package org.simonevans.touristdash.library;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StatisticsScreen extends Activity {
	
	UserData userdata;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		UserData.initialise(this);
		userdata = UserData.getUserData();
		
		
        setContentView(R.layout.statistics);

        TableLayout table = (TableLayout) findViewById(R.id.statsTable);
        
        int totalEarned = userdata.fetchTotalEarnings();
        int currentMoney = userdata.fetchMoney();
        int totalKilled = userdata.getCamerasKilled() + userdata.getFattysKilled();
        
        
        table.addView(addStat("Current Money", "£" + Integer.toString(currentMoney)));
        table.addView(addStat("Total Money Earned", "£" + Integer.toString(totalEarned)));
        table.addView(addStat("Total Tourists Hit", Integer.toString(totalKilled)));
        
        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               finish();
            }
        });
    }
	
	private TableRow addStat(String label, String data) {
		
		TableRow row = new TableRow(this);
		
		TextView labelView = new TextView(this);
		labelView.setText(label);
		labelView.setTextSize(20);
		labelView.setTextColor(Color.BLACK);
		labelView.setWidth(200);
		
		TextView dataView = new TextView(this);
		dataView.setText(data);
		dataView.setTextSize(20);
		dataView.setTextColor(Color.BLACK);
		
		row.addView(labelView);
		row.addView(dataView);
		
		row.setPadding(10, 5, 0, 0);
		return row;
	}

}
