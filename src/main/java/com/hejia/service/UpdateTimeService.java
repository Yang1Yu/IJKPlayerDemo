package com.hejia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.hejia.eventbus.EventSetRequest;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class UpdateTimeService extends Service {
	private Timer timer = null;
	private SimpleDateFormat ymd = null;
	private SimpleDateFormat hm = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//                sendTimeChangedBroadcast();
//			}
//		}, 1000, 20);
	}

	private void init() {
		timer = new Timer();
		ymd = new SimpleDateFormat("yyyy年MM月dd日");
		hm = new SimpleDateFormat("hh:mm");
	}

	private String getTimeYearMonthDay() {
		return ymd.format(new Date());
	}

	private String getTimeHoutMinute() {
		return hm.format(new Date());
	}

	private void sendTimeChangedBroadcast() {
		EventBus.getDefault().post(new EventSetRequest("UPDATE", getTimeHoutMinute(), "default"));
	}

}
