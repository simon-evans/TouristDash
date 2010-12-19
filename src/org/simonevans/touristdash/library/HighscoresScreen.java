package org.simonevans.touristdash.library;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HighscoresScreen extends Activity {

	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		setContentView(R.layout.highscores);


        highscoresDb = new HighScoreOpenHelper(this);
		
        TableLayout highscoresContainer = (TableLayout) findViewById(R.id.highscoresTable);
        putHighScoreRows(10, highscoresContainer);

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
             finish();
            }
        });
	}
	
	HighScoreOpenHelper highscoresDb;

	private void putHighScoreRows(int count, TableLayout container) {

        SQLiteDatabase hsdb = highscoresDb.getReadableDatabase();
        String[] columns = {"Name", "Score"};
        Cursor cursor = hsdb.query("highscores", columns, null, null, null, null, "Score DESC", String.valueOf(count));
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TableRow row = new TableRow(this);

            TextView name = new TextView(this);
            name.setText(cursor.getString(0));
            name.setTextSize(20);
            name.setTextColor(Color.BLACK);

            TextView score = new TextView(this);
            score.setText(String.valueOf(cursor.getInt(1)));
            score.setTextSize(22);
            score.setTextColor(Color.RED);
            score.setPadding(20, 0, 0, 0);

            row.addView(name);
            row.addView(score);

            container.addView(row);
            cursor.moveToNext();
        }

        cursor.close();
        
        hsdb.close();

    }
	
}
