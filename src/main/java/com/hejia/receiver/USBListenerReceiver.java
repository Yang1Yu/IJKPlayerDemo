package com.hejia.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hejia.eventbus.EventMediaRequest;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import de.greenrobot.event.EventBus;

public class USBListenerReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String path = intent.getData().getPath();
		if (!TextUtils.isEmpty(path)) {
			if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
					EventBus.getDefault().post(new EventMediaRequest("USB", "UNMOUNTED", "default"));

			}
			if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
				EventBus.getDefault().post(new EventMediaRequest("USB", "MOUNTED", "default"));
			}
		}
	}

}
