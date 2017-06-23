package com.hejia.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;

import com.hejia.service.BluetoothService;
import com.hejia.tp_launcher_v3.R;


public class MySetAboutUpgradeUpsetDialog extends Dialog {
	private Context context = null;
	private Button btnAboutYes;
	private Button btnAboutNo;
	private PopupWindow window;
	private String key;
	private String path;

	static {
		System.loadLibrary("recovery_jni");
	}

	private static native int RecoveryUpdata(String path);

	private static native int RecoveryRecovery();

	public MySetAboutUpgradeUpsetDialog(Context context, String key, String path) {// key=UPSET/UPGRADE
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.key = key;
		this.path = path;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (key) {
			case "UPSET":
				this.setContentView(R.layout.dialog_set_about_upset);
				break;
			case "UPGRADE":
				this.setContentView(R.layout.dialog_set_about_upgrade_yes);
				break;
		}
		initView();
		initClickListener();
	}

	private void initClickListener() {
		switch (key) {
			case "UPSET":
				btnAboutNo.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});
				btnAboutYes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 恢复出厂设置
						int i = RecoveryRecovery();
						dismiss();

					}
				});
				break;
			case "UPGRADE":
				btnAboutNo.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();

					}
				});
				btnAboutYes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 开始升级
						RecoveryUpdata(path);
						dismiss();
						BluetoothService.startUpgrade = true;
					}
				});
				break;
		}

	}

	private void initView() {
		switch (key) {
			case "UPSET":
				btnAboutNo = (Button) findViewById(R.id.btn_dialog_set_about_upset_no);
				btnAboutYes = (Button) findViewById(R.id.btn_dialog_set_about_upset_yes);
				break;
			case "UPGRADE":
				btnAboutNo = (Button) findViewById(R.id.btn_dialog_set_about_upgrade_yes_no);
				btnAboutYes = (Button) findViewById(R.id.btn_dialog_set_about_upgrade_yes_yes);
				break;
		}

	}

}
