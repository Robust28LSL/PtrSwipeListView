package com.it.juyou;

import android.app.Application;


public class BaseApplication extends Application {

	
	private static BaseApplication instance;
	public static BaseApplication getInstance() {
		return instance;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		PMan.get(this,"ed71e64d04bd4eaff31ffe2cf08cfb73", "official",false);
		
	}
}
