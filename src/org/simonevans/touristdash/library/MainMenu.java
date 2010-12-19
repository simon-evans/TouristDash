package org.simonevans.touristdash.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;


public class MainMenu extends Activity implements OnKeyListener {
	
	boolean playMenu;
	int chosenWave;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mainMenu();

	}
	
	public void onActivityResult() {
		
	}
	
	private void mainMenu() {
		
		setContentView(R.layout.startup);
		
		playMenu = false;

        Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //open menu to choose game type
            	playMenu();
            }
        });

        Button highScoresButton = (Button) findViewById(R.id.highScoresButton);
        highScoresButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //start highscore activity.
            	Intent intent = new Intent("org.simonevans.touristdash.library.HIGHSCORES");
            	startActivity(intent);
            }
        });

        Button helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	//show help screen activity
                Intent intent = new Intent("org.simonevans.touristdash.library.HELPSCREEN");
                
                startActivity(intent);
            	
            }
        });
        
        Button statsButton = (Button) findViewById(R.id.statisticsButton);
        statsButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		//show options screen
        		Intent intent = new Intent("org.simonevans.touristdash.library.STATISTICS");
        		startActivity(intent);
        	}
        });
        
        Button upgradesButton = (Button) findViewById(R.id.upgradesButton);
		upgradesButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.UPGRADES");
				startActivity(intent);
			}
			
		});
		
		Button optionsButton = (Button) findViewById(R.id.optionsButton);
		optionsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.OPTIONS");
				startActivity(intent);
			}
		});
		
		UserData.initialise(this);
        
	}
	
	private void playMenu() {
		playMenu = true;
		setContentView(R.layout.playmenu);
		chosenWave = 0;
		
		Button continuousButton = (Button) findViewById(R.id.ContinuousButton);
		
		continuousButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.CONT_GAME");
				startActivity(intent);
			}
		});
		
		Button decreaseButton = (Button) findViewById(R.id.decreaseLevelButton);
		decreaseButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chosenWave--;
				updateButtons();
			}
		});
		
		Button increaseButton = (Button) findViewById(R.id.increaseLevelButton);
		increaseButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				chosenWave++;
				updateButtons();
			}
		});
		
		Button playWaveButton = (Button) findViewById(R.id.playWaveButton);
		playWaveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.WAVEGAME");
				intent.putExtra("levelNum", chosenWave);
				startActivity(intent);
			}
		});
		
		updateButtons();
		
	}
	
	LevelInfoHelper levelInfo = null;
	
	private void updateButtons() {
		if(levelInfo == null) {
			levelInfo = new LevelInfoHelper(this);
		}
		Button decreaseButton = (Button) findViewById(R.id.decreaseLevelButton);
		if(chosenWave == 0) {
			decreaseButton.setClickable(false);
		} else {
			decreaseButton.setClickable(true);
		}
		
		Button increaseButton = (Button) findViewById(R.id.increaseLevelButton);
		
		int levelCount = levelInfo.maxLevel();
		if(chosenWave >= levelCount) {
			increaseButton.setClickable(false);
		} else {
			increaseButton.setClickable(true);
		}
		
		Button playWaveButton = (Button) findViewById(R.id.playWaveButton);
		String buttonText = "Play wave " + Integer.toString(chosenWave);
		playWaveButton.setText(buttonText);
		
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if((playMenu = true) && (keyCode == KeyEvent.KEYCODE_BACK) && (Integer.parseInt(android.os.Build.VERSION.SDK) < 5)) {
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public void onBackPressed() {
		if(playMenu) {
			mainMenu();
			playMenu = false;
		} else {
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			startActivity(intent);
		}
	}
	

}
