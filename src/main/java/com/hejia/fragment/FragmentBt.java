package com.hejia.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hejia.eventbus.EventBTRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.exception.MyRuntimeException;
import com.hejia.fragment.FragmentBtCTD;
import com.hejia.fragment.FragmentBtDial;
import com.hejia.fragment.FragmentBtHistory;
import com.hejia.fragment.FragmentBtMusic;
import com.hejia.fragment.FragmentBtNo;
import com.hejia.fragment.FragmentBtPb;
import com.hejia.util.IsFastChangeFragmentUtil;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentBt extends Fragment {
	private View view;
	private Fragment mContentFragment;
	private MainActivity activity;
	// 蓝牙界面的radiogroup
	private RadioGroup rGroupBt;
	// 蓝牙界面各碎片 蓝牙拨号 电话簿 通话记录 蓝牙音乐 已配对设备
	private FragmentManager managerBt;
	private FragmentBtDial fm_Bt_Dial;
	private FragmentBtMusic fm_Bt_Music;
	private FragmentBtPb fm_Bt_PhoneBook;
	private FragmentBtHistory fm_Bt_History;
	private FragmentBtCTD fm_Bt_CTD;
	private FragmentBtNo fm_Bt_No;
	public static String nowBtFragment;

	private RadioButton rb_Dial;
	private RadioButton rb_Pb;
	private RadioButton rb_History;
	private RadioButton rb_Music;
	private RadioButton rb_CTD;

	private ChangeFragmentReceiver changeReceiver;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_bt, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	// EventBus
	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void initSystem(EventSystem event) {
		if ("INIT".equals(event.getKey())) {
			// 界面初始化
			rGroupBt = (RadioGroup) view.findViewById(R.id.rg_bt);
			initFragment();
			initRadioButton();
			managerBt = getChildFragmentManager();
			rGroupBt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

					} else {

						switch (checkedId) {
							case R.id.rb_bt_dial:
								changeMcontentFragment(fm_Bt_Dial);
								rb_Dial.setChecked(true);
								nowBtFragment = "BtDial";
								break;
							case R.id.rb_bt_pb:
								changeMcontentFragment(fm_Bt_PhoneBook);
								rb_Pb.setChecked(true);
								nowBtFragment = "BtPB";
								break;
							case R.id.rb_bt_history:
								changeMcontentFragment(fm_Bt_History);
								rb_History.setChecked(true);
								nowBtFragment = "BtHistory";
								break;
							case R.id.rb_bt_music:
								changeMcontentFragment(fm_Bt_Music);
								rb_Music.setChecked(true);
								nowBtFragment = "BtMusic";
								break;
							case R.id.rb_bt_ctd:
								changeMcontentFragment(fm_Bt_CTD);
								rb_CTD.setChecked(true);
								nowBtFragment = "BtCTD";
								break;
							default:
								break;
						}
					}
				}
			});

			// 判断是否有设备连接
			mContentFragment = fm_Bt_No;
			if ("" == activity.bluetoothBindService.device_name) {
				managerBt.beginTransaction().replace(R.id.fragment_bt, fm_Bt_No).commit();
				btn_Unable();
				nowBtFragment = "BtNo";
			} else {
				mContentFragment = fm_Bt_Dial;
				managerBt.beginTransaction().replace(R.id.fragment_bt, fm_Bt_Dial).commit();
				// 设置蓝牙为可连接但不可见
				activity.bluetoothBindService.send_Command("AT+B SCAN 2\r\n");
				btn_Able();
				nowBtFragment = "BtDial";
			}
		}
	}

	private void initFragment() {
		fm_Bt_Dial = new FragmentBtDial();
		fm_Bt_PhoneBook = new FragmentBtPb();
		fm_Bt_History = new FragmentBtHistory();
		fm_Bt_Music = new FragmentBtMusic();
		fm_Bt_CTD = new FragmentBtCTD();
		fm_Bt_No = new FragmentBtNo();
	}

	private void initRadioButton() {
		rb_Dial = (RadioButton) view.findViewById(R.id.rb_bt_dial);
		rb_Pb = (RadioButton) view.findViewById(R.id.rb_bt_pb);
		rb_History = (RadioButton) view.findViewById(R.id.rb_bt_history);
		rb_Music = (RadioButton) view.findViewById(R.id.rb_bt_music);
		rb_CTD = (RadioButton) view.findViewById(R.id.rb_bt_ctd);
	}

	// 碎片替换帮助函数
	private void changeMcontentFragment(Fragment to) {
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerBt.beginTransaction();
			if (!to.isAdded()) {
				// 初始界面
				transaction.hide(mContentFragment).add(R.id.fragment_bt, to).commit();
			} else {
				transaction.hide(mContentFragment).show(to).commit();
			}
			mContentFragment = to;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		changeReceiver = new ChangeFragmentReceiver();
		IntentFilter changeFilter = new IntentFilter("CHANGEDAILFRAGMENT");
		activity.registerReceiver(changeReceiver, changeFilter);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
	//  防止No Activity

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new MyRuntimeException("No Activity");
		} catch (IllegalAccessException e) {
			throw new MyRuntimeException("No Activity");
		}
	}

	// 切换至拨号界面
	private class ChangeFragmentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("CHANGEDAILFRAGMENT".equals(action)) {
				// Log.i("chanyeol", "change");
				changeMcontentFragment(fm_Bt_Dial);
				rb_Dial.setChecked(true);
				nowBtFragment = "BtDial";
			}
		}

	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	public void btEvent(EventBTRequest event) {
		switch (event.getKey()) {
			case "BTCONN":
				//蓝牙已连接
				changeMcontentFragment(fm_Bt_Dial);
				rb_Dial.setChecked(true);
				nowBtFragment = "BtDial";
				btn_Able();
				break;
			case "BTDISCONN":
				//蓝牙断开连接
				changeMcontentFragment(fm_Bt_No);
				nowBtFragment = "BtNo";
				btn_Unable();
				break;
		}
	}

	// 使切换界面按键失效
	private void btn_Unable() {
		changeMcontentFragment(fm_Bt_No);
		rb_Dial.setEnabled(false);
		rb_Music.setEnabled(false);
		rb_History.setEnabled(false);
		rb_Pb.setEnabled(false);
		rb_CTD.setEnabled(false);
	}

	// 使切换界面按键有效
	private void btn_Able() {
		changeMcontentFragment(fm_Bt_Dial);
		rb_Dial.setEnabled(true);
		rb_Dial.setChecked(true);
		rb_Music.setEnabled(true);
		rb_History.setEnabled(true);
		rb_Pb.setEnabled(true);
		rb_CTD.setEnabled(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
		activity.unregisterReceiver(changeReceiver);

	}

}
