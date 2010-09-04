/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.simonevans.touristdash.library;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 *
 * @author Simon
 */
public class TouristDash extends Activity {

    protected static final int GAMEOVER = 0x8421;
    Game game;
    GameCanvas gameCanvas;
    HighScoreOpenHelper highscoresDb;
    String playerName;
    SharedPreferences prefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
        prefs = getPreferences(MODE_PRIVATE);
        
    	this.playerName = prefs.getString("playerName", "");
        
        startupScreen();

        gameCanvas = new GameCanvas(this, messageHandler);

        init();

        game = gameCanvas.game;

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
        
    	this.playerName = prefs.getString("playerName", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        prefs.edit().putString("playerName", playerName);
    }

    private void init() {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(SensorManager.SENSOR_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(SensorManager.SENSOR_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        highscoresDb = new HighScoreOpenHelper(this);

    }

    private void startupScreen() {

        setContentView(R.layout.startup);

        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                setContentView(gameCanvas);
            }
        });

        Button highScoresButton = (Button) findViewById(R.id.highScoresButton);
        highScoresButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                highscoreScreen();
            }
        });

        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                helpScreen();
            }
        });

    }

    private void helpScreen() {
        setContentView(R.layout.howtoplay);

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                startupScreen();
            }
        });
    }

    private void highscoreScreen() {

        setContentView(R.layout.highscores);

        TableLayout highscoresContainer = (TableLayout) findViewById(R.id.highscoresTable);
        putHighScoreRows(10, highscoresContainer);

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                startupScreen();
            }
        });
    }
    SensorManager sensorManager;
    private static final int matrix_size = 16;
    float[] rot = new float[matrix_size];
    float[] I = new float[matrix_size];
    float[] values = new float[3];
    float[] mags;
    float[] accels;
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

    private void gameOverScreen() {
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
                setContentView(gameCanvas);
            }
        });

        Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                startupScreen();
            }
        });

    }
    SensorEventListener sensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent e) {
            if (e.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                return;
            }

            switch (e.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = e.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = e.values.clone();
                    break;
            }

            if (mags != null && accels != null) {
                SensorManager.getRotationMatrix(rot, I, accels, mags);

                SensorManager.getOrientation(rot, values);

                double roll = (180 / Math.PI) * values[2];
                String s = String.valueOf(roll);
                game.userXCoord += roll;
                if (game.userXCoord > 281) {
                    game.userXCoord = 281;
                } else if (game.userXCoord < 0) {
                    game.userXCoord = 0;
                }
            }
        }

        public void onAccuracyChanged(Sensor s, int value) {
        }
    };

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

    }
}
