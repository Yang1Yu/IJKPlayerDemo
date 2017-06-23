package com.hejia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hejia.eventbus.EventSerialReadRequest;
import com.hejia.eventbus.EventSystem;
import com.hejia.exception.MyRuntimeException;
import com.hejia.tp_launcher_v3.R;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentVInfo extends Fragment {

	private MainActivity activity;
	private View view;

	private Fragment mContentFragment;
	private FragmentManager managerVehicle;

	private RadioGroup rgVInfo;
	private RadioButton rbVInfoManage;
	private RadioButton rbVInfoTravelRecord;
	private RadioButton rbVInfoTravelInfo;
	private RadioButton rbVInfoErr;

	private FragmentVInfoManage fmVInfoManage;
	private FragmentVInfoTravelRecord fmVInfoTravelRecord;
	private FragmentVInfoTravelInfo fmVInfoTravelInfo;
	private FragmentVInfoErr fmVInfoErr;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_vinfo, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}

	// EventBus
	@Subscribe(threadMode = ThreadMode.MainThread, sticky = true)
	public void initSystem(EventSystem event) {
		if ("INIT".equals(event.getKey())) {
			// 界面初始化
			initFragment();
			initRadioButton();
		}
	}

	private void initFragment() {
		fmVInfoManage = new FragmentVInfoManage();
		fmVInfoTravelRecord = new FragmentVInfoTravelRecord();
		fmVInfoTravelInfo = new FragmentVInfoTravelInfo();
		fmVInfoErr = new FragmentVInfoErr();
	}

	private void initRadioButton() {
		rgVInfo = (RadioGroup) view.findViewById(R.id.rg_vinfo);
		rbVInfoErr = (RadioButton) view.findViewById(R.id.rb_vinfo_err);
		rbVInfoTravelRecord = (RadioButton) view.findViewById(R.id.rb_vinfo_travelrecord);
		rbVInfoManage = (RadioButton) view.findViewById(R.id.rb_vinfo_manage);
		rbVInfoTravelInfo = (RadioButton) view.findViewById(R.id.rb_vinfo_travelinfo);
		managerVehicle = getChildFragmentManager();
		rgVInfo.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch (checkedId) {
					case R.id.rb_vinfo_manage:
						changeMcontentFragment(fmVInfoManage);
						rbVInfoManage.setChecked(true);
						break;
					case R.id.rb_vinfo_travelrecord:
						changeMcontentFragment(fmVInfoTravelRecord);
						rbVInfoTravelRecord.setChecked(true);
						break;
					case R.id.rb_vinfo_travelinfo:
						changeMcontentFragment(fmVInfoTravelInfo);
						rbVInfoTravelInfo.setChecked(true);
						break;
					case R.id.rb_vinfo_err:
						changeMcontentFragment(fmVInfoErr);
						rbVInfoErr.setChecked(true);
						break;
				}
			}
		});
		mContentFragment = fmVInfoTravelInfo;
		managerVehicle.beginTransaction().replace(R.id.fragment_vinfo, fmVInfoTravelInfo).commit();
		rbVInfoTravelInfo.setChecked(true);
	}

	// 碎片替换帮助函数
	private void changeMcontentFragment(Fragment to) {
		rbtnVInfoUnable();
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerVehicle.beginTransaction();
			if (!to.isAdded()) {
				// 初始界面
				transaction.hide(mContentFragment).add(R.id.fragment_vinfo, to).commit();
			} else {
				transaction.hide(mContentFragment).show(to).commit();
			}
			mContentFragment = to;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
	}

	private void rbtnVInfoUnable() {
		rbVInfoTravelInfo.setEnabled(false);
		rbVInfoErr.setEnabled(false);
		rbVInfoTravelRecord.setEnabled(false);
		rbVInfoManage.setEnabled(false);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rbVInfoTravelInfo.setEnabled(true);
		rbVInfoErr.setEnabled(true);
		rbVInfoTravelRecord.setEnabled(true);
		rbVInfoManage.setEnabled(true);
	}

}
