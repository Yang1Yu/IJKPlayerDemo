package com.hejia.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hejia.eventbus.EventSystem;
import com.hejia.tp_launcher_v3.R;
import com.hejia.fragment.FragmentCloudInfoDCACAirPump;
import com.hejia.fragment.FragmentCloudInfoDCACDCDC;
import com.hejia.fragment.FragmentCloudInfoDCACOilPump;
import com.hejia.util.IsFastChangeFragmentUtil;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class FragmentCloudInfoDCAC extends Fragment {
	private RadioGroup rgCloudInfoDCAC;
	private RadioButton rbCloudInfoDCAC01;
	private RadioButton rbCloudInfoDCAC02;
	private RadioButton rbCloudInfoDCAC03;
	private LinearLayout llCloudInfoDCAC;

	private FragmentCloudInfoDCACDCDC fmCloudInfoDCAC01;
	private FragmentCloudInfoDCACOilPump fmCloudInfoDCAC02;
	private FragmentCloudInfoDCACAirPump fmCloudInfoDCAC03;

	private FragmentManager managerCloudInfoDCAC;
	private Fragment mContentFragment;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fm_cloud_info_dcac, container, false);
		// 注册EventBus
		EventBus.getDefault().register(this);
		return view;
	}
	// EventBus
	@Subscribe(threadMode= ThreadMode.MainThread,sticky = true)
	public void initSystem(EventSystem event){
		if ("INIT".equals(event.getKey())){
			// 界面初始化
			managerCloudInfoDCAC = getChildFragmentManager();
			initFragment();
			initView();
			initListener();
			mContentFragment = fmCloudInfoDCAC01;
			managerCloudInfoDCAC.beginTransaction().replace(R.id.ll_cloud_info_dcac, fmCloudInfoDCAC01).commit();
		}
	}
	private void initView() {
		rgCloudInfoDCAC = (RadioGroup) view.findViewById(R.id.rg_cloud_info_dcac);
		rbCloudInfoDCAC01 = (RadioButton) view.findViewById(R.id.rb_cloud_info_dcac_01);
		rbCloudInfoDCAC02 = (RadioButton) view.findViewById(R.id.rb_cloud_info_dcac_02);
		rbCloudInfoDCAC03 = (RadioButton) view.findViewById(R.id.rb_cloud_info_dcac_03);
		llCloudInfoDCAC = (LinearLayout) view.findViewById(R.id.ll_cloud_info_dcac);
		rbCloudInfoDCAC01.setChecked(true);

	}


	private void initFragment() {
		fmCloudInfoDCAC01 = new FragmentCloudInfoDCACDCDC();
		fmCloudInfoDCAC02 = new FragmentCloudInfoDCACOilPump();
		fmCloudInfoDCAC03 = new FragmentCloudInfoDCACAirPump();

	}

	private void initListener() {
		rgCloudInfoDCAC.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (IsFastChangeFragmentUtil.isFastChangeFragment()) {

				} else {

					switch (checkedId) {
						case R.id.rb_cloud_info_dcac_01:
							rbCloudInfoDCAC01.setChecked(true);
							changeFragmentCloudInfo(fmCloudInfoDCAC01);
							break;
						case R.id.rb_cloud_info_dcac_02:
							rbCloudInfoDCAC02.setChecked(true);
							changeFragmentCloudInfo(fmCloudInfoDCAC02);
							break;
						case R.id.rb_cloud_info_dcac_03:
							rbCloudInfoDCAC03.setChecked(true);
							changeFragmentCloudInfo(fmCloudInfoDCAC03);
							break;

						default:
							break;
					}
				}
			}
		});
	}

	private synchronized void changeFragmentCloudInfo(Fragment to) {
		rbtnUnable();
		if (mContentFragment != null && mContentFragment != to) {
			FragmentTransaction transaction = managerCloudInfoDCAC.beginTransaction();
			if (!to.isAdded()) {
				transaction.hide(mContentFragment).add(R.id.ll_cloud_info_dcac, to).commit();
			} else {
				transaction.hide(mContentFragment).show(to).commit();
			}
			mContentFragment = to;
		}
	}

	private void rbtnUnable() {
		rbCloudInfoDCAC01.setEnabled(false);
		rbCloudInfoDCAC02.setEnabled(false);
		rbCloudInfoDCAC03.setEnabled(false);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rbCloudInfoDCAC01.setEnabled(true);
		rbCloudInfoDCAC02.setEnabled(true);
		rbCloudInfoDCAC03.setEnabled(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 解除EventBus
		EventBus.getDefault().unregister(this);
	}
}
