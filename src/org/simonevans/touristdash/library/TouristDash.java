/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simonevans.touristdash.library;

import org.simonevans.touristdash.library.sensor.RollListener;
import org.simonevans.touristdash.library.sensor.RollReader;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 *
 * @author Simon
 */
public class TouristDash extends Activity implements OnClickListener, RollListener {

    protected static final int GAMEOVER = 0x8421;
    private static Context CONTEXT;
    static final double PIUNDER180 = 180.0 / Math.PI;
	private static final float ROLL_SENSITIVITY = 20;
    Game game;
    GameCanvas gameCanvas;
    HighScoreOpenHelper highscoresDb;
    String playerName;
    SharedPreferences prefs;
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
    	
    	
        super.onCreate(bundle);

        init();
        
    	this.playerName = prefs.getString("playerName", "");
    	
        gameCanvas = new GameCanvas(this, messageHandler);
        gameCanvas.setOnClickListener(this);

        CONTEXT = this;
        setContentView(gameCanvas);
    }

    @Override
    public void onPause() {
        super.onPause();
        gameCanvas.canvasThread.run = false;
        
        Editor editor = prefs.edit();
        
        editor.putString("playerName", playerName);
        editor.commit();
        
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    	this.playerName = prefs.getString("playerName", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        prefs.edit().putString("playerName", playerName);
    }

    protected void init() {

        prefs = getPreferences(MODE_PRIVATE);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new UserData(this);

        highscoresDb = new HighScoreOpenHelper(this);
        Log.e("SIMON", "Called init");
        if(RollReader.isSupported()) {
    		RollReader.startListening(this);
    	} else {
    		/**
    		 * SWITCH TO A VIEW INFORMING USER THAT GAME WILL NOT WORK & INVITE THEM TO SUBMIT COMMENT DETAILING THEIR PHONE MAKE/MODEL
    		 * Possible mention which sensor is missing
    		 * 
    		 * It's possible this can be avoided by looking for a gyroscope?
    		 */
    	}
    }
    
    Handler messageHandler = new Handler() {

        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case TouristDash.GAMEOVER:
                    gameOverScreen();
                    break;
            }

        }
    };

    protected void gameOverScreen() {
    	RollReader.stopListening();
        setContentView(R.layout.gameover);
        TextView finalScore = (TextView) findViewById(R.id.finalScore);
        final String score = String.valueOf(game.score);
        finalScore.setText(score);

        
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.namedialog);
        dialog.setTitle(R.string.namePrompt);
		EditText name = (EditText) dialog.findViewById(R.id.nameInput);
		name.getEditableText().clear();
		name.getEditableText().append(playerName);
        
        Button hideButton = (Button) dialog.findViewById(R.id.saveButton);
        
        hideButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		EditText name = (EditText) dialog.findViewById(R.id.nameInput);
        		playerName = name.getText().toString();      		
        		
                SQLiteDatabase db = highscoresDb.getWritableDatabase();
                ContentValues data = new ContentValues();
                data.put("Name", playerName);
                data.put("Score", score);
                db.insert("highscores", null, data);
                
                TableLayout smallHighScores = (TableLayout) findViewById(R.id.smallHighscoresTable);
                smallHighScores.removeAllViews();
                putHighScoreRows(3, smallHighScores);
                
        		dialog.dismiss();  
        	}
        	
        });
        
        Button cancelButton = (Button) dialog.findViewById(R.id.closeButton);
        
        cancelButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		dialog.dismiss();
        	}
        	
        });
        
        dialog.show();
        
        game.reset();
        
        TableLayout smallHighScores = (TableLayout) findViewById(R.id.smallHighscoresTable);
        putHighScoreRows(3, smallHighScores);
        
        Button restartButton = (Button) findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	RollReader.startListening(TouristDash.this);
                setContentView(gameCanvas);
            }
        });

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent("org.simonevans.touristdash.library.MAINMENU");
                startActivity(intent);
            }
        });

    }
    
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

    public static Context getContext() {
    	return CONTEXT;
    }
    

	@Override
	public void onClick(View view) {
		game.shoot();
	}

	@Override
	public void onRollChanged(float roll) {
        game.userXCoord += (roll * ROLL_SENSITIVITY);
        if (game.userXCoord > 281) {
            game.userXCoord = 281;
        } else if (game.userXCoord < 0) {
            game.userXCoord = 0;
        }
	}
}
