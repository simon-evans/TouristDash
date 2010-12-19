package org.simonevans.touristdash.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LevelInfoHelper extends SQLiteOpenHelper {
	
	private static final String TABLE_NAME = "level_definitions";
	private static final int DATABASE_VERSION = 6;
	private static final String LEVEL_DEFINITIONS = "CREATE TABLE " + TABLE_NAME + " (LevelNum INT PRIMARY KEY, EnemiesPerRow INT NOT NULL, RowSpacing INT NOT NULL, LevelLength INT NOT NULL, Reward INT NOT NULL);";

	private Context context;
	
	public LevelInfoHelper(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
		
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(LEVEL_DEFINITIONS);
		
		BufferedReader insertData = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.inserts)));
		try {
			
			try {
				String line = null;
				while((line = insertData.readLine()) != null) {
					db.execSQL("INSERT INTO " + TABLE_NAME + " (LevelNum, EnemiesPerRow, RowSpacing, LevelLength, Reward) VALUES (" + line + ");");
				}
			}
			finally {
				insertData.close();
			}
		} 
		catch (IOException e){
			e.printStackTrace();
		}
			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
		db.execSQL(LEVEL_DEFINITIONS);
		
		BufferedReader insertData = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.inserts)));
		
		try {
			try {
				String line = null;
				while((line = insertData.readLine()) != null) {
					db.execSQL("INSERT INTO " + TABLE_NAME + " (LevelNum, EnemiesPerRow, RowSpacing, LevelLength, Reward) VALUES (" + line + ");");
				}
			}
			finally {
				insertData.close();
			}
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	protected Cursor getLevelInfo(int levelNumber) {
		
		SQLiteDatabase db = getReadableDatabase();
		
		String[] columns = {"EnemiesPerRow", "RowSpacing", "LevelLength", "Reward"};
		String whereClase = "LevelNum = " + levelNumber;
		Cursor values = db.query(TABLE_NAME, columns, whereClase, null, null, null, null); 
		
		if(values != null) {
			values.moveToFirst();
		}
		
		db.close();
		
		return values;
		
	}
	
	protected int getLevelCount() {
		SQLiteDatabase db = getReadableDatabase();
		
		//String query = "SELECT COUNT(*) FROM " + TABLE_NAME + ";";
		
		Cursor cursor = db.query(TABLE_NAME, new String[] {"COUNT(*)"}, null, null, null, null, null);
		
		if(cursor != null) {
			cursor.moveToFirst();
		}
		
		int returnVal = cursor.getInt(0);
		db.close();
		return returnVal;
	}
	
	protected int maxLevel() {
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, new String[] {"MAX(LevelNum)"}, null, null, null, null, null);
		
		if(cursor!=null) {
			cursor.moveToFirst();
			int returnVal = cursor.getInt(0);
			cursor.close();
			db.close();
			return returnVal;
		}

		cursor.close();
		db.close();
		return -1;
	}

}
