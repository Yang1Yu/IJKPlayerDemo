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

import com.hejia.eventbus.EventSystem;
import com.hejia.exception.MyRuntimeException;
import com.hejia.tp_launcher_v3.R;
import com.hejia.tp_launcher_v3.android.tp_launcher_v3.MainActivity;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentVInfoManage extends Fragment {

	private MainActivity activity;
	private View view;

	private Fragment mContentFragment;
	private FragmentManager managerVehicleErr;

	private RadioGroup rgVInfoErr;
	private RadioButton rbVInfoErrRegister;
	private RadioButton rbVInfoErrCommunication;
	private RadioButton rbVInfoErrFirstServer;
	private RadioButton rbVInfoErrSecondServer;
	private RadioButton rbVInfoErrLocation;

	private FragmentVInfoManageRegister fmVInfoErrRegister;
	private FragmentVInfoManageCommunication fmVInfoErrCommunication;
	private FragmentVInfoManageFirstServer fmVInfoErrFirstServer;
	private FragmentVInfoManageSecondServer fmVInfoErrSecondServer;
	private FragmentVInfoManageLocation fmVInfoErrLocation;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_vinfo_manage, container, false);
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
		fmVInfoErrRegister = new FragmentVInfoManageRegister();
		fmVInfoErrCommunication = new FragmentVInfoManageCommunication();
		fmVInfoErrFirstServer = new FragmentVInfoManageFirstServer();
		fmVInfoErrSecondServer = new FragmentVInfoManageSecondServer();
		fmVInfoErrLocation = new FragmentVInfoManageLocation();
	}

	private void initRadioButton() {
		managerVehicleErr = getChildFragmentManager();
		rgVInfoErr = (RadioGroup) view.findViewById(R.id.rg_vehicleinfo_err);
		rbVInfoErrRegister = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_err_register);
		rbVInfoErrCommunication = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_err_communication);
		rbVInfoErrFirstServer = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_err_firstserver);
		rbVInfoErrSecondServer = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_err_secondserver);
		rbVInfoErrLocation = (RadioButton) view.findViewById(R.id.rb_vehicleinfo_err_location);
		rgVInfoErr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch (checkedId) {
					case R.id.rb_vehicleinfo_err_register:
						changeMcontentFragment(fmVInfoErrRegister);
						rbVInfoErrRegister.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_err_communication:
						changeMcontentFragment(fmVInfoErrCommunication);
						rbVInfoErrCommunication.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_err_firstserver:
						changeMcontentFragment(fmVInfoErrFirstServer);
						rbVInfoErrFirstServer.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_err_secondserver:
						changeMcontentFragment(fmVInfoErrSecondServer);
						rbVInfoErrSecondServer.setChecked(true);
						break;
					case R.id.rb_vehicleinfo_err_location:
						changeMcontentFragment(fmVInfoErrLocation);
						rbVInfoErrLocation.setChecked(true);
						break;
				}
			}
		});
		mContentFragment = fmVInfoErrRegister;
		managerVehicleErr.beginTransaction().replace(R.id.fragment_vehicleinfo_err, fmVInfoErrRegister).commit();
		rbVInfoErrRegister.setChecked(true);
	}

	// 碎片替换帮助函数
	private void changeMcontentFragment(Fragment to) {
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerVehicleErr.beginTransaction();
			if (!to.isAdded()) {
				// 初始界面
				transaction.hide(mContentFragment).add(R.id.fragment_vehicleinfo_err, to).commit();
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
