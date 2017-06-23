package com.hejia.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

public class BluetoothReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(MainActivity.BLUETOOTH_UPDATE_ACTION)) {
		}

	}

}
