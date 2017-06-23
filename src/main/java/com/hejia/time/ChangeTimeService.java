package com.hejia.time;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.hejia.time.*;
import com.hejia.time.TimerEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChangeTimeService extends Service {
	private Handler netTimeHandler;
	private com.hejia.time.TimeManager manager;
	private boolean flag = true;
	private Intent changeTimeIntent;
	private Intent mIntent;
	private com.hejia.time.TimerEntity entity;
	private SimpleDateFormat format;
	private boolean tag;

	@Override
	public IBinder onBind(Intent intent) {

		return new ChangeTimeBinder();
	}

	public class ChangeTimeBinder extends Binder {
		public ChangeTimeService getChangeTimeService() {
			return ChangeTimeService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		manager = com.hejia.time.TimeManager.instance();
		mIntent = new Intent();
		tag = true;
		mIntent.setAction("NETTIMEBROADCAST");
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while (tag) {
//					if (MainActivity.isNetAvailable(getApplicationContext())) {
//						// TODO 如连接到网络则发送获取网络时间
//						// TODO 停止此线程
//						tag = false;
//					} else {
//						Log.i("chanyeol", "网络不可用");
//					}
//				}
//			}
//		}).start();
		netTimeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					changeTimeIntent = (Intent) msg.obj;
					entity = (TimerEntity) changeTimeIntent.getSerializableExtra("time");
					new Thread(new Runnable() {

						@Override
						public void run() {
							while (flag) {
								entity = manager.running(entity);
								mIntent.putExtra("newTime", entity);
								String time = format.format(Calendar.getInstance().getTime());
								mIntent.putExtra("bendiTime", time);
								sendBroadcast(mIntent);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
					break;

				default:
					break;
				}
			}
		};

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
	}

	public void startGetTime(Intent intent) {
		Message message = netTimeHandler.obtainMessage();
		message.what = 0;
		message.obj = intent;
		netTimeHandler.sendMessage(message);
	}
}
