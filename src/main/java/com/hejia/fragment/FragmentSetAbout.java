package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.*;

import java.io.File;

public class FragmentSetAbout extends Fragment {
	private View view;
	private MainActivity activity;
	private TextView tvUpgrade;
	private TextView tvUpset;
	private PopupWindow window;
	private TimeCount timeCount;

	private ShowUpgradePopReceiver showUpgradePopReceiver;

	// 扫描升级文件
	// private FilePerate filePerate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set_about, container, false);
		tvUpgrade = (TextView) view.findViewById(R.id.tv_set_about_upgrade);
		tvUpset = (TextView) view.findViewById(R.id.tv_set_about_upset);
		timeCount = new TimeCount(60000, 60000);

		tvUpset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAbout".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					MySetAboutUpgradeUpsetDialog dialog = new MySetAboutUpgradeUpsetDialog(activity, "UPSET","null");
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.show();
				}
			}
		});

		tvUpgrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("SetAbout".equals(com.hejia.fragment.FragmentSet.nowSetFragment) && "Set".equals(activity.nowFragment)) {
					scanUpgradeFile();
				}
			}
		});

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showUpgradePopReceiver = new ShowUpgradePopReceiver();
		IntentFilter filter = new IntentFilter("SHOWUPGRADEPOPBROADCAST");
		activity.registerReceiver(showUpgradePopReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.unregisterReceiver(showUpgradePopReceiver);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	// 扫描升级文件
	private void scanUpgradeFile() {
		File fSD = new File("/mnt/media_rw/extsd/sabresd_6dq-ota-imx6q-ldo.zip");
		File fUSB = new File("/mnt/media_rw/udisk/sabresd_6dq-ota-imx6q-ldo.zip");
		if (!fSD.exists() && !fUSB.exists()) {
			// TODO 如不存在升级文件
			MySetAboutUpgradeNoDialog dialog = new MySetAboutUpgradeNoDialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
		} else if (fSD.exists()) {
			// TODO 如存在升级文件
			MySetAboutUpgradeUpsetDialog dialog = new MySetAboutUpgradeUpsetDialog(activity,"UPGRADE",fSD.toString());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			return;
		} else if (fUSB.exists()) {
			// TODO 如存在升级文件
			MySetAboutUpgradeUpsetDialog dialog = new MySetAboutUpgradeUpsetDialog(activity,"UPGRADE",fUSB.toString());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
			return;
		}

	}

	public void popUpgradeScan() {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View m_view_pop = inflater.inflate(R.layout.pop_set_upgrade_anim, null);
		window = new PopupWindow(m_view_pop, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		window.setFocusable(false);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		window.setBackgroundDrawable(dw);
		window.showAtLocation(m_view_pop, Gravity.CENTER, 0, 0);
	}

	private class ShowUpgradePopReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("SHOWUPGRADEPOPBROADCAST".equals(action)) {
				popUpgradeScan();
				timeCount.start();

			}
		}
	}

	// 升级安装计时
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��ʱ�� ��ʱ��ʱ����
		}

		@Override
		public void onFinish() {
			// 计时完毕时触发
			window.dismiss();
			MySetAboutUpgradeErrDialog dialog = new MySetAboutUpgradeErrDialog(activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	}
}
