package com.hejia.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.hejia.bean.User;
import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SysSetService extends Service {

	private MyRunnable myRunnable;

	// 设置升级
	private Intent popUpgradeIntent;
	public static boolean startUpgrade = false;


	// 与主界面连接绑定的接口
	@Override
	public IBinder onBind(Intent arg0) {

		return new SysSetBinder();
	}

	public class SysSetBinder extends Binder {
		public SysSetService getSysSetService() {
			return SysSetService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		myRunnable = new MyRunnable();
		new Thread(myRunnable).start();
		// 设置升级
		popUpgradeIntent = new Intent();

	}
	public void initMain(){
		EventBus.getDefault().postSticky(new EventSystem("INITMAIN"));
	}

	// 发送设置升级广播
	public void sendPopUpgrade() {
		popUpgradeIntent.setAction("SHOWUPGRADEPOPBROADCAST");
		sendBroadcast(popUpgradeIntent);
	}

	private class MyRunnable implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					// 每隔100ms执行一次
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
				}
				// 设置升级
				if (startUpgrade) {
					sendPopUpgrade();
					startUpgrade = false;
				}
			}
		}
	}

}
