package com.example.mrnice;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MrNiceApp extends Application {
	
	private String LOGGING_TAG = "MrNice Application";
	private SharedPreferences prefs;
	
	@Override
	public void onCreate() {
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// not guaranteed to be called
		super.onTerminate();
	}
	
	public SharedPreferences getPrefs() {
		return this.prefs;
	}
	
	public void setBooleanValueToSharedPreferences(String key, Boolean bool) {

		Editor editor = prefs.edit();
		editor.putBoolean(key, bool);
		editor.commit();
	}

}
