package com.hejia.caught;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


public class ProjectApplication extends MultiDexApplication {
	private static ProjectApplication mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

	}

	public static ProjectApplication getInstance() {
		return mInstance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}
}
