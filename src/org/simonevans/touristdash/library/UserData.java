package org.simonevans.touristdash.library;

import java.util.Random;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

public class UserData {
	
	SharedPreferences prefs;
	private static UserData userdata = null;
	
	static int AMMOCOST = 100;
	
	/**
	 * @TODO: Currently must ensure that class has been initialised before calling this.
	 * In theory it is initialised when game first boots up. This can be fixed using
	 * static reference to context now saved in TouristDash class. 
	 * 
	 * @return instance of UserData class which provides access to most (@TODO: move rest here)
	 * of data stored for user.
	 */
	public static UserData getUserData() {
		return userdata;
	}
	
	static void initialise(Context c) {
		if(userdata == null) {
			userdata = new UserData(c);
		}
	}
	
	public UserData(Context context) {
		prefs = context.getSharedPreferences("TouristDashUserData", Context.MODE_PRIVATE);
		prefEditor = prefs.edit();
	}

	public int fetchMoney() {
		return prefs.getInt("currentMoney", 0);
	}
	
	public int fetchTotalEarnings() {
		return prefs.getInt("totalEarnings", 0);
	}
	

	Editor prefEditor;
	private final String curMoneyName = "currentMoney";
	private final String totalEarningsName = "totalEarnings";
	
	public void addEarnings(int earnings) {
		int curMoney = fetchMoney();
		int totalEarnings = fetchTotalEarnings();
		
		
		prefEditor.putInt(curMoneyName, curMoney + earnings);
		prefEditor.putInt(totalEarningsName, totalEarnings + earnings); 
		
		prefEditor.commit();
	}
	
	public void spendMoney(int price) {
		int curMoney = fetchMoney();
		
		prefEditor.putInt("currentMoney", curMoney - price);
		
		prefEditor.commit();
	}
	
	public int fetchAmmo() {
		return prefs.getInt("ammo", 5);
	}
	
	public void addAmmo(int increment) {
		int curAmmo = fetchAmmo();
		prefEditor.putInt("ammo", curAmmo + increment);
		
		prefEditor.commit();
	}
	
	public void buyAmmo() {
		if(this.fetchMoney() >= getAmmoCost()) {
			spendMoney(getAmmoCost());
			addAmmo(1);
		}
	}
	
	public int getAmmoCost() {
		return AMMOCOST * this.fetchAmmo();
	}
	
	Random earningsRand = new Random(System.currentTimeMillis());
	
	public int getEarnings() {
		
		return earningsRand.nextInt(6) + 2; 
	}
	
	public void addCamerasKilled(int num) {
		int prev = prefs.getInt("camerasKilled", 0);
		prefEditor.putInt("camerasKilled", prev + num).commit();
	}
	
	public void addFattysKilled(int num) {
		int prev = prefs.getInt("fattysKilled", 0);
		prefEditor.putInt("fattysKilled", prev + num).commit();
	}
	
	public int getCamerasKilled() {
		return prefs.getInt("camerasKilled", 0);
	}
	
	public int getFattysKilled() {
		return prefs.getInt("fattysKilled", 0);
	}
	
	public int getSensitivity() {
		return prefs.getInt("sensitivity", 5);
	}
	
	public void setSensitivity(int newSensitivity) {
		prefEditor.putInt("sensitivity", newSensitivity).commit();
	}
}
