package org.simonevans.touristdash.library;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;

public class TouristDashWave extends TouristDash implements OnKeyListener {
	
	int levelNum;
	int enemiesPerRow;
	int rowSpacing;
	int levelLength;
	int reward;
	UserData userdata;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		levelNum = getIntent().getExtras().getInt("levelNum");
		
		levelInfo = new LevelInfoHelper(this);
		readLevelInfo(levelNum);
		
		this.game = new WaveGame(levelLength, rowSpacing, enemiesPerRow, 3);
		gameCanvas.game = this.game;
		
		userdata = new UserData(this);
		
	}
	
	
	LevelInfoHelper levelInfo;

	
	private void readLevelInfo(int levelNum) {
		Cursor values = levelInfo.getLevelInfo(levelNum);
		
		this.enemiesPerRow = values.getInt(0);
		this.rowSpacing = values.getInt(1);
		this.levelLength = values.getInt(2);
		this.reward = values.getInt(3);
		
		values.close();
	}
	
	@Override
	protected void gameOverScreen() {
		
		userdata.addCamerasKilled(this.game.camerasKilled);
		userdata.addFattysKilled(this.game.fattysKilled);
		
		if(((WaveGame)this.game).win) {
			waveComplete();
		} else {
			waveFailed();
		}
		
	}
	
	private void waveComplete() {
		
		setContentView(R.layout.wavecomplete);
		
		
		userdata.addEarnings(reward + game.collectedMoney);
		
		populateText(R.id.moneyEarned, "£".concat(Integer.toString(reward)));
		populateText(R.id.waveCompleteHeading, "Completed Wave ".concat(Integer.toString(levelNum + 1)));
		populateText(R.id.moneyScavenged, "£".concat(Integer.toString(game.collectedMoney)));
		
		Button mainMenu = (Button) findViewById(R.id.mainMenuButton);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goToMainMenu();
			}
		});
		
		Button nextWave = (Button) findViewById(R.id.nextWaveButton);
		if(levelNum >= levelInfo.maxLevel()) {
			nextWave.setVisibility(View.GONE);
		}
		nextWave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.WAVEGAME");
				intent.putExtra("levelNum", levelNum+1);
				startActivity(intent);
			}
		});
		
		Button getUpgrades = (Button) findViewById(R.id.getUpgradesButton);
		getUpgrades.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.UPGRADES");
				startActivity(intent);
			}
		});
		
	}
	
	private void waveFailed() {
		
		setContentView(R.layout.wavefailed);
		Log.w("Foo", "Bar");
		userdata.addEarnings(game.collectedMoney);
		
		populateText(R.id.waveFailedHeading, "Failed Wave ".concat(Integer.toString(levelNum + 1)));
		populateText(R.id.moneyScavenged, "£".concat(Integer.toString(game.collectedMoney)));
		
		Button mainMenu = (Button) findViewById(R.id.mainMenuButton);
		mainMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				goToMainMenu();
			}
		});
		
		Button tryAgain = (Button) findViewById(R.id.tryAgainButton);
		tryAgain.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				game.reset();
				setContentView(gameCanvas);
			}
		});
		
		Button getUpgrades = (Button) findViewById(R.id.getUpgradesButton);
		getUpgrades.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("org.simonevans.touristdash.library.UPGRADES");
				startActivity(intent);
			}
		});
	}
	
	private void populateText(int buttonId, String text) {
		
		TextView tv = (TextView) findViewById(buttonId);
		tv.setText(text);
		
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && Integer.parseInt(android.os.Build.VERSION.SDK) < 5) {
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public void onBackPressed() {
		goToMainMenu();
	}

	private void goToMainMenu() {
		Intent intent = new Intent("org.simonevans.touristdash.library.MAINMENU");
        startActivity(intent);
	}
}
