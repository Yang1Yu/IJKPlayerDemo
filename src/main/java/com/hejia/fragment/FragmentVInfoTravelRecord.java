package com.hejia.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hejia.eventbus.EventSystem;
import com.hejia.exception.MyRuntimeException;
import com.hejia.tp_launcher_v3.R;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentVInfoTravelRecord extends Fragment {

	private MainActivity activity;
	private View view;

	private Fragment mContentFragment;
	private FragmentManager managerVehicleTravel;

	private RadioGroup rgVehicleInfoTravel;
	private RadioButton rbVehicleInfoTravelBadness;
	private RadioButton rbVehicleInfoTravelOvertime;
	private RadioButton rbVehicleInfoTravelOverspeed;
	private RadioButton rbVehicleInfoTravelDoubt;
	private RadioButton rbVehicleInfoTravelElectricity;

	private FragmentVInfoTravelBadness fmVInfoTravelBadness;
	private FragmentVInfoTravelOvertime fmVInfoTravelOvertime;
	private FragmentVInfoTravelOverspeed fmVInfoTravelOverspeed;
	private FragmentVInfoTravelDoubt fmVInfoTravelDoubt;
	private FragmentVInfoTravelElectricity fmVInfoTravelElectricity;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_vinfo_travel, container, false);
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
		fmVInfoTravelBadness = new FragmentVInfoTravelBadness();
		fmVInfoTravelOvertime = new FragmentVInfoTravelOvertime();
		fmVInfoTravelOverspeed = new FragmentVInfoTravelOverspeed();
		fmVInfoTravelDoubt = new FragmentVInfoTravelDoubt();
		fmVInfoTravelElectricity = new FragmentVInfoTravelElectricity();
	}

	private void initRadioButton() {
		managerVehicleTravel = getChildFragmentManager();
		rgVehicleInfoTravel = (RadioGroup) view.findViewById(R.id.rg_vehicleinfo_travel);
		rbVehicleInfoTravelBadness = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_travel_badness);
		rbVehicleInfoTravelOvertime = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_travel_overtime);
		rbVehicleInfoTravelOverspeed = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_travel_overspeed);
		rbVehicleInfoTravelDoubt = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_travel_doubt);
		rbVehicleInfoTravelElectricity = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_travel_electricity);
		rgVehicleInfoTravel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch (checkedId) {
					case R.id.rb_vehicleinfo_travel_badness:
						changeMcontentFragment(fmVInfoTravelBadness);
						rbVehicleInfoTravelBadness.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_travel_overtime:
						changeMcontentFragment(fmVInfoTravelOvertime);
						rbVehicleInfoTravelOvertime.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_travel_overspeed:
						changeMcontentFragment(fmVInfoTravelOverspeed);
						rbVehicleInfoTravelOverspeed.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_travel_doubt:
						changeMcontentFragment(fmVInfoTravelDoubt);
						rbVehicleInfoTravelDoubt.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_travel_electricity:
						changeMcontentFragment(fmVInfoTravelElectricity);
						rbVehicleInfoTravelElectricity.setChecked(true);
						break;
				}
			}
		});
		mContentFragment = fmVInfoTravelBadness;
		managerVehicleTravel.beginTransaction().replace(R.id.fragment_vehicleinfo_travel, fmVInfoTravelBadness).commit();
		rbVehicleInfoTravelBadness.setChecked(true);
	}

	// 碎片替换帮助函数
	private void changeMcontentFragment(Fragment to) {
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerVehicleTravel.beginTransaction();
			if (!to.isAdded()) {
				// 初始界面
				transaction.hide(mContentFragment).add(R.id.fragment_vehicleinfo_travel, to).commit();
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

}
