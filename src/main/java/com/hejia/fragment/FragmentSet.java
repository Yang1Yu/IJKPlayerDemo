package com.hejia.fragment;

import android.app.Activity;
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

import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;
import com.hejia.tp_launcher_v3.R;
import com.hejia.exception.MyRuntimeException;
import com.hejia.fragment.FragmentSetAbout;
import com.hejia.fragment.FragmentSetAd;
import com.hejia.fragment.FragmentSetCloud;
import com.hejia.fragment.FragmentSetManage;
import com.hejia.fragment.FragmentSetMoni;
import com.hejia.fragment.FragmentSetRoutine;
import com.hejia.fragment.FragmentSetVol;
import com.hejia.fragment.FragmentSetWifi;

import java.lang.reflect.Field;

public class FragmentSet extends Fragment {
	private View view;

	interface MySetDialogListener {
		public void mySetDialogListener(String myString);
	}

	// 设置界面的子碎片管理
	private FragmentManager managerSet;

	// 设置界面子碎片：常规、音量及音效、Wifi热点、视频监控、运营管理、广告播放、云诊断、关于
	private FragmentSetRoutine fmSetRoutine;
	private FragmentSetVol fmSetVol;
	private FragmentSetWifi fmSetWifi;
	private FragmentSetMoni fmSetMoni;
	private FragmentSetManage fmSetManage;
	private FragmentSetAd fmSetAd;
	private FragmentSetCloud fmSetCloud;
	private FragmentSetAbout fmSetAbout;
//	private FragmentSetOfflineMap fmSetOfflineMap;
	private Fragment mContentFragment;

	private MainActivity activity;

	// 导航栏按键
	private RadioButton rbSetRoutine;
	private RadioButton rbSetVol;
	private RadioButton rbSetWifi;
	private RadioButton rbSetMoni;
	private RadioButton rbSetManage;
	private RadioButton rbSetAd;
	private RadioButton rbSetCloud;
	private RadioButton rbSetAbout;
//	private RadioButton rbSetOfflineMap;

	private RadioGroup rGroupSet;

	public static String nowSetFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_set, container, false);
		managerSet = getChildFragmentManager();
		initFragment();
		rGroupSet = (RadioGroup) view.findViewById(R.id.rg_set);
		initRadioButton();
		mContentFragment = fmSetRoutine;
		managerSet.beginTransaction().replace(R.id.fragment_set, fmSetRoutine).commit();
		nowSetFragment = "SetRoutine";
		rbSetRoutine.setChecked(true);
		rGroupSet.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if ("Set".equals(activity.nowFragment)) {
					switch (checkedId) {
					case R.id.rb_set_routine:
						changeMcontentFragmentSet(fmSetRoutine);
						rbSetRoutine.setChecked(true);
						nowSetFragment = "SetRoutine";
						break;
					case R.id.rb_set_vol:
						changeMcontentFragmentSet(fmSetVol);
						rbSetVol.setChecked(true);
						nowSetFragment = "SetVol";
						break;
					case R.id.rb_set_wifi:
						changeMcontentFragmentSet(fmSetWifi);
						rbSetWifi.setChecked(true);
						nowSetFragment = "SetWifi";
						break;
					case R.id.rb_set_moni:
						changeMcontentFragmentSet(fmSetMoni);
						rbSetMoni.setChecked(true);
						nowSetFragment = "SetMoni";
						break;
					case R.id.rb_set_manage:
						changeMcontentFragmentSet(fmSetManage);
						rbSetManage.setChecked(true);
						nowSetFragment = "SetManage";
						break;
					case R.id.rb_set_ad:
						changeMcontentFragmentSet(fmSetAd);
						rbSetAd.setChecked(true);
						nowSetFragment = "SetAD";
						break;
					case R.id.rb_set_cloud:
						changeMcontentFragmentSet(fmSetCloud);
						rbSetCloud.setChecked(true);
						nowSetFragment = "SetCloud";
						break;
					case R.id.rb_set_about:
						changeMcontentFragmentSet(fmSetAbout);
						rbSetAbout.setChecked(true);
						nowSetFragment = "SetAbout";
						break;
//					case R.id.rb_set_offlinemap:
//						changeMcontentFragmentSet(fmSetOfflineMap);
//						rbSetOfflineMap.setChecked(true);
//						nowSetFragment = "SetMap";
//						break;

					default:
						break;
					}
				}
			}
		});
		return view;
	}

	private void initFragment() {
		fmSetRoutine = new FragmentSetRoutine();
		fmSetVol = new FragmentSetVol();
		fmSetWifi = new FragmentSetWifi();
		fmSetMoni = new FragmentSetMoni();
		fmSetManage = new FragmentSetManage();
		fmSetAd = new FragmentSetAd();
		fmSetCloud = new FragmentSetCloud();
		fmSetAbout = new FragmentSetAbout();
//		fmSetOfflineMap = new FragmentSetOfflineMap();
		mContentFragment = fmSetRoutine;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	private void initRadioButton() {
		rbSetRoutine = (RadioButton) view.findViewById(R.id.rb_set_routine);
		rbSetVol = (RadioButton) view.findViewById(R.id.rb_set_vol);
		rbSetWifi = (RadioButton) view.findViewById(R.id.rb_set_wifi);
		rbSetMoni = (RadioButton) view.findViewById(R.id.rb_set_moni);
		rbSetManage = (RadioButton) view.findViewById(R.id.rb_set_manage);
		rbSetAd = (RadioButton) view.findViewById(R.id.rb_set_ad);
		rbSetCloud = (RadioButton) view.findViewById(R.id.rb_set_cloud);
		rbSetAbout = (RadioButton) view.findViewById(R.id.rb_set_about);
//		rbSetOfflineMap = (RadioButton) view.findViewById(R.id.rb_set_offlinemap);
	}

	private void changeMcontentFragmentSet(Fragment to) {
		rbtnSetUnable();
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerSet.beginTransaction();
			if (!to.isAdded()) {
				fmSetWifi.hideKeyboard();
				transaction.hide(mContentFragment).add(R.id.fragment_set, to).commit();
			} else {
				fmSetWifi.hideKeyboard();
				transaction.hide(mContentFragment).show(to).commit();
			}
			mContentFragment = to;
		}
	}

	private void rbtnSetUnable() {
		rbSetRoutine.setEnabled(false);
		rbSetVol.setEnabled(false);
		rbSetWifi.setEnabled(false);
//		rbSetOfflineMap.setEnabled(false);
		rbSetMoni.setEnabled(false);
		rbSetManage.setEnabled(false);
		rbSetAd.setEnabled(false);
		rbSetCloud.setEnabled(false);
		rbSetAbout.setEnabled(false);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rbSetRoutine.setEnabled(true);
		rbSetVol.setEnabled(true);
		rbSetWifi.setEnabled(true);
//		rbSetOfflineMap.setEnabled(true);
		rbSetMoni.setEnabled(true);
		rbSetManage.setEnabled(true);
		rbSetAd.setEnabled(true);
		rbSetCloud.setEnabled(true);
		rbSetAbout.setEnabled(true);
	}

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
}
